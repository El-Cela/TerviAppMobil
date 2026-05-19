package com.example.tervi.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val email: String? = null,
    val healthStatus: String? = null
)