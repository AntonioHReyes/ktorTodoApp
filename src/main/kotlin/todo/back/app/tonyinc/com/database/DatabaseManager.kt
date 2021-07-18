package todo.back.app.tonyinc.com.database

import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.server.engine.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import todo.back.app.tonyinc.com.entities.NewToDoRequest
import todo.back.app.tonyinc.com.entities.UpdateToDoRequest
import kotlin.Exception

class DatabaseManager {

    private val hostName = HoconApplicationConfig(ConfigFactory.load()).property("ktor.db_hostname").getString()
    private val databaseName = HoconApplicationConfig(ConfigFactory.load()).property("ktor.db_name").getString()
    private val userName = HoconApplicationConfig(ConfigFactory.load()).property("ktor.db_username").getString()
    private val password = HoconApplicationConfig(ConfigFactory.load()).property("ktor.db_password").getString()

    //database
    private val kTormDatabase: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostName:3306/$databaseName?user=$userName&password=$password&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false"
        kTormDatabase = Database.connect(jdbcUrl)
    }

    fun getAllTodos(): List<DbTodoEntity> = kTormDatabase.sequenceOf(DbTodoTable).toList()
    fun getTodo(toDoId: Int): DbTodoEntity? = kTormDatabase.sequenceOf(DbTodoTable).firstOrNull { it.id eq toDoId}
    fun addTodo(toDo: NewToDoRequest): DbTodoEntity?{
        try {
            kTormDatabase.useTransaction {
                val insertedId = kTormDatabase.insertAndGenerateKey(DbTodoTable){
                    set(DbTodoTable.title, toDo.title)
                    set(DbTodoTable.description, toDo.description)
                    set(DbTodoTable.done, toDo.done)
                } as Int

                return getTodo(insertedId)
            }
        }catch (e: Exception){
            return null
        }
    }

    fun updateTodo(toDoId: Int, toDo: UpdateToDoRequest): DbTodoEntity?{
        try {
            kTormDatabase.useTransaction {
                kTormDatabase.update(DbTodoTable){

                    toDo.title?.let { set(DbTodoTable.title, toDo.title) }
                    toDo.description?.let { set(DbTodoTable.description, toDo.description) }
                    toDo.done?.let { set(DbTodoTable.done, toDo.done) }

                    where {
                        it.id eq toDoId
                    }
                }

                return getTodo(toDoId)
            }
        }catch (e: Exception){
            return null
        }
    }

    fun deleteTodo(toDoId: Int): Boolean{
        try{
            kTormDatabase.useTransaction {
                val deletedRows = kTormDatabase.delete(DbTodoTable){
                    it.id eq toDoId
                }

                return deletedRows > 0
            }
        }catch (e: Exception){
            return false
        }
    }
}