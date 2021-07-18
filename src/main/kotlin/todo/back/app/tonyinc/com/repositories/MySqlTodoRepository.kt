package todo.back.app.tonyinc.com.repositories

import todo.back.app.tonyinc.com.database.DatabaseManager
import todo.back.app.tonyinc.com.entities.NewToDoRequest
import todo.back.app.tonyinc.com.entities.ToDo
import todo.back.app.tonyinc.com.entities.UpdateToDoRequest

class MySqlTodoRepository : ToDoRepository {

    private val database = DatabaseManager()

    override fun getToDos(): List<ToDo> {
        return database.getAllTodos().map { toDo ->
            return@map ToDo(
                id = toDo.id,
                title = toDo.title,
                description = toDo.description,
                done = toDo.done
            )
        }
    }

    override fun getToDo(toDoId: Int): ToDo? {
        return database.getTodo(toDoId)?.let {
            ToDo(
                id = it.id,
                title = it.title,
                description = it.description,
                done = it.done
            )
        }
    }

    override fun addToDo(toDo: NewToDoRequest): ToDo? {
        return database.addTodo(toDo)?.let {
            ToDo(
                id = it.id,
                title = it.title,
                description = it.description,
                done = it.done
            )
        }
    }

    override fun updateToDo(toDoId: Int, toDo: UpdateToDoRequest): ToDo? {
        return database.updateTodo(toDoId, toDo)?.let {
            ToDo(
                id = it.id,
                title = it.title,
                description = it.description,
                done = it.done
            )
        }
    }

    override fun deleteToDo(toDoId: Int): Boolean {
        return database.deleteTodo(toDoId)
    }


}