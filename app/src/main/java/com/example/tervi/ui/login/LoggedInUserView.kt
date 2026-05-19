package com.example.tervi.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val userId: String,
    val email: String? = null,
    val healthStatus: String? = null
)