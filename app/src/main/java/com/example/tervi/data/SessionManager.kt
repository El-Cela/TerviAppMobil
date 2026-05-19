package com.example.tervi.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("TerviPrefs", Context.MODE_PRIVATE)

    fun saveUser(id: String, name: String, email: String? = null, health: String? = null) {
        prefs.edit().apply {
            putString("user_id", id)
            putString("user_name", name)
            email?.let { putString("user_email", it) }
            health?.let { putString("user_health", it) }
            apply()
        }
    }

    fun getUserName(): String? = prefs.getString("user_name", null)
    fun getUserId(): String? = prefs.getString("user_id", null)
    fun getUserEmail(): String? = prefs.getString("user_email", null)
    fun getUserHealth(): String? = prefs.getString("user_health", null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}