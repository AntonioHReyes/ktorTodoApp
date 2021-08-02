package todo.back.app.tonyinc.com.repositories

class UserRepositoryImpl : UserRepository {

    private val credentialsToUsers = mapOf<String, UserRepository.User>(
        "max:1234" to UserRepository.User(1, "Max", "James"),
        "fer:demo" to UserRepository.User(2, "Fernando", "Flores"),
        "lisa:demo1234" to UserRepository.User(3, "Lisa", "Pink")
    )

    override fun getUser(userName: String, password: String): UserRepository.User? {
        return credentialsToUsers["$userName:$password"]
    }
}