package com.example.tervi.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("TerviPrefs", Context.MODE_PRIVATE)

    fun saveUser(
        id: String,
        name: String,
        email: String? = null,
        health: String? = null,
        username: String? = null,
        firstName: String? = null,
        lastNameP: String? = null,
        lastNameM: String? = null,
        gender: String? = null
    ) {
        prefs.edit().apply {
            putString("user_id", id)
            putString("user_name", name)
            email?.let { putString("user_email", it) }
            health?.let { putString("user_health", it) }
            username?.let { putString("user_username", it) }
            firstName?.let { putString("user_firstname", it) }
            lastNameP?.let { putString("user_lastnamep", it) }
            lastNameM?.let { putString("user_lastnamem", it) }
            gender?.let { putString("user_gender", it) }
            apply()
        }
    }

    fun getUserName(): String? = prefs.getString("user_name", null)
    fun getUserId(): String? = prefs.getString("user_id", null)
    fun getUserEmail(): String? = prefs.getString("user_email", null)
    fun getUserHealth(): String? = prefs.getString("user_health", null)
    fun getUserUsername(): String? = prefs.getString("user_username", null)
    fun getUserFirstName(): String? = prefs.getString("user_firstname", null)
    fun getUserLastNameP(): String? = prefs.getString("user_lastnamep", null)
    fun getUserLastNameM(): String? = prefs.getString("user_lastnamem", null)
    fun getUserGender(): String? = prefs.getString("user_gender", null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}