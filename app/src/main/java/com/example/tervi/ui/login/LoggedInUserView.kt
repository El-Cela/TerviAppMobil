package com.example.tervi.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val userId: String,
    val email: String? = null,
    val healthStatus: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastNameP: String? = null,
    val lastNameM: String? = null,
    val gender: String? = null
)