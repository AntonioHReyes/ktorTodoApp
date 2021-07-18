package todo.back.app.tonyinc.com.database

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.text

object DbTodoTable: Table<DbTodoEntity>("todos"){

    val id = int("id").primaryKey().bindTo { it.id }
    val title = text("title").bindTo { it.title }
    val description = text("description").bindTo { it.description }
    val done = boolean("done").bindTo { it.done }
}

interface DbTodoEntity: Entity<DbTodoEntity>{

    companion object : Entity.Factory<DbTodoEntity>()

    val id: Int
    val title: String
    val description: String
    val done: Boolean

}