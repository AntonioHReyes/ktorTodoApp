package todo.back.app.tonyinc.com.plugins

import com.google.gson.stream.MalformedJsonException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import todo.back.app.tonyinc.com.authentication.JwtConfig
import todo.back.app.tonyinc.com.authentication.RoleBasedAuthorization
import todo.back.app.tonyinc.com.authentication.withRole
import todo.back.app.tonyinc.com.entities.LoginBody
import todo.back.app.tonyinc.com.entities.NewToDoRequest
import todo.back.app.tonyinc.com.repositories.ToDoRepositoryImpl
import todo.back.app.tonyinc.com.repositories.UserRepositoryImpl

val jwtConfig = JwtConfig(System.getenv("KTOR_TODO_LIST_JWT_SECRET"));

fun Application.configureRouting() {
    // Starting point for a Ktor app:

    val env = environment.config.propertyOrNull("ktor.environment")?.getString() ?: "UNDEFINED"

    //Permite mostrar el stackTrace de los metodos
    install(CallLogging)
    install(ContentNegotiation){
        gson{
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }
    install(RoleBasedAuthorization) {
        getRoles { (it as JwtConfig.JwtUser).roles }
    }

    routing {

        val repository = ToDoRepositoryImpl()
        val userRepository = UserRepositoryImpl()

        get("/") {
            call.respondText("Servidor iniciado en ${env}")
        }

        post("/login") {
            val loginBody = call.receive<LoginBody>()

            val user = userRepository.getUser(loginBody.userName, loginBody.password)

            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }

            val token = jwtConfig.generateToken(JwtConfig.JwtUser(user.userId, user.name, setOf("EDIT_ME")))

            call.respond(token)
        }

        get("/me/queryParams"){


            call.respond(call.request.queryString())
        }

        authenticate {

            withRole("EDIT_ME"){
                get("/me/edit"){
                    val user = call.authentication.principal as JwtConfig.JwtUser
                    call.respond(user)
                }
            }

            get("/me"){
                val user = call.authentication.principal as JwtConfig.JwtUser
                call.respond(user)
            }

            get("/todos") {
                call.respond(repository.getToDos())
            }

            get("/todos/{id}") {
                val idOfTodo = call.parameters["id"]?.toIntOrNull()

                if (idOfTodo == null) {
                    call.respond(HttpStatusCode.BadRequest, "Es necesario enviar un id")

                    return@get
                } else {

                    val todo = repository.getToDo(idOfTodo)

                    if (todo == null) {
                        call.respond(HttpStatusCode.NotFound, "No existe información para el id proporcionado")
                        return@get
                    } else {
                        call.respond(todo)
                    }
                }
            }

            post("/todos") {
                try {
                    val todoRequest = call.receive<NewToDoRequest>()
                    val newTodo = repository.addToDo(todoRequest) ?: call.respond(
                        HttpStatusCode.NotFound,
                        "No se pudo realizar la operación"
                    )

                    call.respond(newTodo)
                } catch (e: MalformedJsonException) {
                    call.respond(HttpStatusCode.BadRequest, "Verifique su objeto")
                }
            }

            put("/todos/{id}") {
                val toDoId = call.parameters["id"] ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    "No se especifico un id"
                )

                try {
                    val todoUpdated = repository.updateToDo(toDoId.toInt(), call.receive())
                    if (todoUpdated == null) {
                        call.respond(HttpStatusCode.NotFound, "No existe el id proporcionado")
                    } else {
                        call.respond(todoUpdated)
                    }
                } catch (e: MalformedJsonException) {
                    call.respond(HttpStatusCode.BadRequest, "Verifique su objeto")
                }
            }

            delete("/todos/{id}") {
                val todoId = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    "No se encontro el id"
                )

                val todoRemoved = repository.deleteToDo(todoId.toInt())

                if (todoRemoved) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "No existe el id proporcionado")
                }
            }
        }
    }
}
