package com.example.tervi

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tervi.api.RetrofitClient
import com.example.tervi.data.SessionManager
import kotlinx.coroutines.launch

class ResumenActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var txtEjerciciosCompl: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resumen)

        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()
        val userName = sessionManager.getUserName()

        val btnRegresar = findViewById<Button>(R.id.btn_regresar_resumen)
        txtEjerciciosCompl = findViewById(R.id.ejercicios_compl)

        btnRegresar.setOnClickListener {
            finish()
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            if (userId != null && userName != null) {
                cargarResumen(userId, userName)
            } else {
                swipeRefreshLayout.isRefreshing = false
            }
        }

        if (userId != null && userName != null) {
            cargarResumen(userId, userName)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarResumen(userId: String, userName: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getActividades(userId, userName, System.currentTimeMillis())
                if (response.status == "success") {
                    val count = response.completados_count ?: 0
                    txtEjerciciosCompl.text = "Ejercicios completados: $count"
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_APP", "Error al cargar resumen", e)
                Toast.makeText(this@ResumenActivity, "Error al actualizar datos", Toast.LENGTH_SHORT).show()
            } finally {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}