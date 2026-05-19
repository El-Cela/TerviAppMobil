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
                    healthStatus = userData.estado_salud
                )
                Result.Success(user)
            } else {
                android.util.Log.w("LOGIN_DEBUG", "Login rechazado por servidor: ${response.message}")
                Result.Error(IOException(response.message ?: "Error en el servidor"))
            }
        } catch (e: Exception) {
            // Log detallado para identificar el tipo de error
            android.util.Log.e("LOGIN_DEBUG", "ERROR EN LLAMADA REMOTA", e)
            
            when (e) {
                is java.net.ConnectException -> {
                    android.util.Log.e("LOGIN_DEBUG", "No se pudo conectar al servidor. Revisa IP y red WiFi.")
                }
                is java.net.SocketTimeoutException -> {
                    android.util.Log.e("LOGIN_DEBUG", "Tiempo de espera agotado. El servidor es lento o inaccesible.")
                }
                else -> {
                    android.util.Log.e("LOGIN_DEBUG", "Error inesperado: ${e.message}")
                }
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
                Result.Error(IOException("Error de conexión y credenciales incorrectas", e))
            }
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}