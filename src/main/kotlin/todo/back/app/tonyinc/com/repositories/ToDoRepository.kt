package todo.back.app.tonyinc.com.repositories

import todo.back.app.tonyinc.com.entities.NewToDoRequest
import todo.back.app.tonyinc.com.entities.ToDo
import todo.back.app.tonyinc.com.entities.UpdateToDoRequest

interface ToDoRepository {

    fun getToDos(): List<ToDo>

    fun getToDo(toDoId: Int): ToDo?

    fun addToDo(toDo: NewToDoRequest): ToDo?

    fun updateToDo(toDoId: Int, toDo: UpdateToDoRequest): ToDo?

    fun deleteToDo(toDoId: Int): Boolean

}