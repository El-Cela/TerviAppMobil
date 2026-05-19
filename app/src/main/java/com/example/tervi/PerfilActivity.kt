package com.example.tervi

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tervi.data.SessionManager

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        val sessionManager = SessionManager(this)
        val textUsuario = findViewById<Button>(R.id.text_usuario)
        val textId = findViewById<Button>(R.id.id)
        val btnRegresar = findViewById<Button>(R.id.btn_qr)

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