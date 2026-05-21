package com.example.tervi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Toast

class EscanerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_escaner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Simulación de inicio de escaneo
        leerQR()
    }

    private fun leerQR() {
        // Esta función es representativa.
        // En una implementación real, aquí se iniciaría la cámara o la librería de escaneo (ej. ZXing o ML Kit)
        Toast.makeText(this, "Iniciando escáner...", Toast.LENGTH_SHORT).show()
        
        // Simulamos que después de un tiempo "lee" algo
        findViewById<android.view.View>(R.id.main).postDelayed({
            Toast.makeText(this, "Código QR detectado (Simulación)", Toast.LENGTH_LONG).show()
        }, 2000)
    }
}