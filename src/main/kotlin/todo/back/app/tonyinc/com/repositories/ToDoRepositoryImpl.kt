package todo.back.app.tonyinc.com.repositories

import todo.back.app.tonyinc.com.entities.NewToDoRequest
import todo.back.app.tonyinc.com.entities.ToDo
import todo.back.app.tonyinc.com.entities.UpdateToDoRequest

class ToDoRepositoryImpl : ToDoRepository {

    private val todos = mutableListOf(
        ToDo(1, "Comprar leche", "Ir a comprar leche", false),
    )

    override fun getToDos(): List<ToDo> = todos
    override fun getToDo(toDoId: Int): ToDo? = todos.firstOrNull { it.id == toDoId }
    override fun addToDo(toDo: NewToDoRequest): ToDo {
        val newToDo = ToDo(
            id = todos.size + 1,
            title = toDo.title ?: "",
            description = toDo.description ?: "",
            done = toDo.done ?: false
        )

        todos.add(newToDo)

        return newToDo
    }

    override fun updateToDo(toDoId: Int, toDo: UpdateToDoRequest): ToDo? {
        val todo = todos.firstOrNull { it.id == toDoId } ?: return null

        todo.title = toDo.title ?: todo.title
        todo.description = toDo.description ?: todo.description
        todo.done = toDo.done ?: todo.done

        return todo
    }

    override fun deleteToDo(toDoId: Int): Boolean {
        return todos.removeIf { it.id == toDoId }
    }
}