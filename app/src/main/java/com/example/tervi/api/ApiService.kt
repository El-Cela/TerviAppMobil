package com.example.tervi.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Ejemplo de un POST para login (suspend para usar con corrutinas)
    @FormUrlEncoded
    @POST("auth/login.php")
    suspend fun login(
        @Field("correo") user: String,
        @Field("password") pass: String
    ): ApiResponse

    // Obtener actividades (ejercicios, repeticiones programadas y hechas)
    @GET("actions/get_actividades.php")
    suspend fun getActividades(
        @Query("usuario_id") userId: String,
        @Query("nombre_usuario") userName: String,
        @Query("_") timestamp: Long = System.currentTimeMillis() // Parámetro para evitar caché
    ): ApiResponseActividades
}

// Clases de modelo para las respuestas JSON
data class ApiResponse(
    val status: String,
    val message: String?,
    val user: UserData?
)

data class ApiResponseActividades(
    val status: String?,
    val message: String?,
    val data: List<ActividadData>?,
    val avances: List<AvanceData>?,
    val completados_count: Int? = 0 // Nueva propiedad para el contador
)

data class AvanceData(
    val id_avance: Int?,
    val ejercicio_nombre: String?,
    val puntos: Int?,
    val fecha_registro: String?,
    val tipo_entorno: String?
)

data class ActividadData(
    val ejercicio: String?,
    val repeticiones_programadas: Int?,
    val repeticiones_hechas: Int?
)

data class UserData(
    val id_usuario: Int,
    val nombre_usu: String,
    val apellidoP_usu: String,
    val apellidoM_usu: String,
    val edad_usu: Int,
    val sexo_usu: String,
    val peso_usu: Double?,
    val altura_usu: Double?,
    val correo_usu: String,
    val usuario_usu: String,
    val recetas_vistas: Int,
    val estado_salud: String
)

data class DataModel(
    val id: Int,
    val titulo: String,
    val descripcion: String
)