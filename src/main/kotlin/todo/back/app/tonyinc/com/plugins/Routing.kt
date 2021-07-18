package todo.back.app.tonyinc.com.plugins

import com.google.gson.stream.MalformedJsonException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import todo.back.app.tonyinc.com.entities.NewToDoRequest
import todo.back.app.tonyinc.com.entities.UpdateToDoRequest
import todo.back.app.tonyinc.com.repositories.MySqlTodoRepository
import todo.back.app.tonyinc.com.repositories.ToDoRepositoryImpl
import java.lang.Exception

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

    routing {

        val repository = MySqlTodoRepository()

        get("/") {
            call.respondText("Servidor iniciado en ${env}")
        }

        get("/todos"){
            call.respond(repository.getToDos())
        }

        get("/todos/{id}"){
            val idOfTodo = call.parameters["id"]?.toIntOrNull()

            if(idOfTodo == null){
                call.respond(HttpStatusCode.BadRequest, "Es necesario enviar un id")

                return@get
            }else{

                val todo = repository.getToDo(idOfTodo)

                if(todo == null){
                    call.respond(HttpStatusCode.NotFound, "No existe información para el id proporcionado")
                    return@get
                }else{
                    call.respond(todo)
                }
            }
        }

        post("/todos"){
            try {
                val todoRequest = call.receive<NewToDoRequest>()
                val newTodo = repository.addToDo(todoRequest) ?: call.respond(HttpStatusCode.NotFound, "No se pudo realizar la operación")

                call.respond(newTodo)
            }catch (e: MalformedJsonException){
             call.respond(HttpStatusCode.BadRequest, "Verifique su objeto")
            }
        }

        put("/todos/{id}"){
            val toDoId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "No se especifico un id")

            try {
                val todoUpdated = repository.updateToDo(toDoId.toInt(), call.receive())
                if(todoUpdated == null){
                    call.respond(HttpStatusCode.NotFound, "No existe el id proporcionado")
                }else{
                    call.respond(todoUpdated)
                }
            }catch (e: MalformedJsonException){
                call.respond(HttpStatusCode.BadRequest, "Verifique su objeto")
            }
        }

        delete ("/todos/{id}"){
            val todoId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "No se encontro el id")

            val todoRemoved = repository.deleteToDo(todoId.toInt())

            if(todoRemoved){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.NotFound, "No existe el id proporcionado")
            }
        }
    }

}
