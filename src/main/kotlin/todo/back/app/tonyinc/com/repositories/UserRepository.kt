package todo.back.app.tonyinc.com.repositories

interface UserRepository {

    fun getUser(userName: String, password: String): User?

    data class User(
        val userId: Int,
        val name: String,
        val lastName: String
    )
}