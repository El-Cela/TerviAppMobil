package com.example.tervi.data

import com.example.tervi.api.RetrofitClient
import com.example.tervi.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        android.util.Log.d("LOGIN_DEBUG", "Iniciando intento de login para: $username")
        return try {
            // 1. Intentar Login Remoto vía Retrofit
            val response = RetrofitClient.instance.login(username, password)
            android.util.Log.d("LOGIN_DEBUG", "Respuesta recibida: ${response.status}")
            
            if (response.status == "success" && response.user != null) {
                val userData = response.user
                val displayName = "${userData.nombre_usu} ${userData.apellidoP_usu}"
                val user = LoggedInUser(
                    userId = userData.id_usuario.toString(),
                    displayName = displayName,
                    email = userData.correo_usu,
                    healthStatus = userData.estado_salud,
                    username = userData.usuario_usu,
                    firstName = userData.nombre_usu,
                    lastNameP = userData.apellidoP_usu,
                    lastNameM = userData.apellidoM_usu,
                    gender = userData.sexo_usu
                )
                Result.Success(user)
            } else {
                android.util.Log.w("LOGIN_DEBUG", "Login rechazado por servidor: ${response.message}")
                Result.Error(IOException(response.message ?: "Error en el servidor"))
            }
        } catch (e: Exception) {
            // Log detallado para identificar el tipo de error
            android.util.Log.e("LOGIN_DEBUG", "ERROR EN LLAMADA REMOTA", e)
            
            val connectionError = when (e) {
                is java.net.ConnectException -> "No se pudo conectar al servidor. Revisa la IP y que el servidor esté activo."
                is java.net.SocketTimeoutException -> "Tiempo de espera agotado. El servidor no responde."
                is com.google.gson.JsonSyntaxException -> "Error en la respuesta del servidor (JSON inválido). Es posible que el servidor haya enviado un error PHP en lugar de datos."
                else -> "Error de conexión: ${e.message}"
            }

            // 2. Fallback: Solo si falla la conexión, intentamos locales
            val hardcodedUsers = mapOf(
                "admin@tervi.com" to "123456",
                "user@test.com" to "password"
            )

            if (hardcodedUsers[username] == password) {
                android.util.Log.i("LOGIN_DEBUG", "Login exitoso usando credenciales locales (Fallback)")
                val displayName = username.substringBefore("@")
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), displayName)
                Result.Success(fakeUser)
            } else {
                android.util.Log.w("LOGIN_DEBUG", "Credenciales locales también fallaron.")
                Result.Error(IOException("$connectionError Además, las credenciales locales no coinciden.", e))
            }
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}