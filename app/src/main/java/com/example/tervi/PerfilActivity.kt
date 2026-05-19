package com.example.tervi

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tervi.data.SessionManager

class PerfilActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        val sessionManager = SessionManager(this)
        val textUsuario = findViewById<Button>(R.id.text_usuario)
        val textId = findViewById<Button>(R.id.id)
        val btnRegresar = findViewById<Button>(R.id.btn_qr)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // En Perfil, como los datos son de sesión, solo simulamos el refresco
            // o podrías volver a leer de SharedPreferences
            textUsuario.text = sessionManager.getUserName() ?: "Invitado"
            textId.text = "ID: ${sessionManager.getUserId() ?: "000"}"
            
            swipeRefreshLayout.isRefreshing = false
        }

        // Mostrar datos de sesión
        textUsuario.text = sessionManager.getUserName() ?: "Invitado"
        textId.text = "ID: ${sessionManager.getUserId() ?: "000"}"

        btnRegresar.setOnClickListener {
            finish() // Volver atrás
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}