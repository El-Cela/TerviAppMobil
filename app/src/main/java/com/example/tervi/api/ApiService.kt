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

    // Obtener historial completo (actividades y avances)
    @GET("actions/get_historial.php")
    suspend fun getHistorial(
        @Query("usuario_id") userId: String,
        @Query("nombre_usuario") userName: String,
        @Query("_") timestamp: Long = System.currentTimeMillis()
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
    val repeticiones_hechas: Int?,
    val fecha_completado: String?
)

data class UserData(
    val id_usuario: Int,
    val nombre_usu: String,
    val apellidoP_usu: String,
    val apellidoM_usu: String,
    val edad_usu: Int? = 0,
    val sexo_usu: String? = "N/A",
    val peso_usu: Double? = null,
    val altura_usu: Double? = null,
    val correo_usu: String? = null,
    val usuario_usu: String,
    val recetas_vistas: Int? = 0,
    val estado_salud: String? = "N/A",
    val causa_rehabilitacion: String? = "N/A"
)

data class DataModel(
    val id: Int,
    val titulo: String,
    val descripcion: String
)