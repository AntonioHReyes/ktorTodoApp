package todo.back.app.tonyinc.com.entities

data class ToDo(
    var id: Int,
    var title: String,
    var description : String,
    var done: Boolean
)
