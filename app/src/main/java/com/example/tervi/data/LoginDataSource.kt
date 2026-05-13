package com.example.tervi.data

import com.example.tervi.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // Usuarios hardcodeados para validación local
            val hardcodedUsers = mapOf(
                "admin@tervi.com" to "123456",
                "user@test.com" to "password"
            )

            if (hardcodedUsers[username] == password) {
                val displayName = username.substringBefore("@")
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), displayName)
                Result.Success(fakeUser)
            } else {
                Result.Error(IOException("Usuario o contraseña incorrectos"))
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}