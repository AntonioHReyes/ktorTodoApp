package todo.back.app.tonyinc.com.entities

data class NewToDoRequest(
    val title: String?,
    val description: String?,
    val done: Boolean?
)

data class UpdateToDoRequest(
    val title: String?,
    val description: String?,
    val done: Boolean?
)
