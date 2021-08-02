package todo.back.app.tonyinc.com.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.*
import io.ktor.auth.jwt.*

class JwtConfig(jwtSecret: String) {

    companion object Constants {
        // jwt config
        private const val jwtIssuer = "com.tonyinc.back"
        private const val jwtRealm = "com.tonyinc.back.app.todo"

        // claims
        private const val CLAIM_USERID = "userId"
        private const val CLAIM_USERNAME = "userName"
        private const val CLAIM_ROLES = "roles"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    /**
     * Generate a token for a authenticated user
     */
    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_USERID, user.userId)
        .withClaim(CLAIM_USERNAME, user.userName)
        .withClaim(CLAIM_ROLES, user.roles.toList())
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(config: JWTAuthenticationProvider.Configuration) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate {
            val userId = it.payload.getClaim(CLAIM_USERID).asInt()
            val userName = it.payload.getClaim(CLAIM_USERNAME).asString()
            val roles = it.payload.getClaim(CLAIM_ROLES).asArray(String::class.java).toSet()

            if (userId != null && userName != null) {
                JwtUser(userId, userName, roles)
            } else {
                null
            }
        }
    }

    /**
     * POKO, that contains information of an authenticated user that is authenticated via jwt
     */
    data class JwtUser(
        val userId: Int,
        val userName: String,
        val roles: Set<String>
        ): Principal


}