package com.example.tervi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    //Variables globales
    private lateinit var btn_retos_comp: Button

    private lateinit var btn_resumen : Button

    private lateinit var btn_escaner : Button

    private lateinit var btn_perfil: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //Se crean los objetos
        btn_retos_comp = findViewById<Button>(R.id.btn_retos_comp)
        btn_resumen = findViewById<Button>(R.id.btn_resumen)
        btn_escaner = findViewById<Button>(R.id.btn_escaner)
        btn_perfil = findViewById<Button>(R.id.btn_perfil)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        //cambiar a la ui del resumen
        btn_resumen.setOnClickListener {
            val intent = Intent(this, ResumenActivity::class.java)
            startActivity(intent)
        }

        //cambiar a la ui del escaner
        btn_escaner.setOnClickListener {
            val intent = Intent(this, EscanerActivity::class.java)
            startActivity(intent)
        }

        //cambiar a la ui del perfil
        btn_perfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity ::class.java)
            startActivity(intent)
        }

        //Desplegable retos
        btn_retos_comp.setOnClickListener {

            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.desplegable_retos,null)

            dialog.setContentView(view)

            // Referencias
            val btnCompletar = view.findViewById<Button>(R.id.btn_completar)
            val txtReto = view.findViewById<TextView>(R.id.reto01)

            // Acción del botón
            btnCompletar.setOnClickListener {
                txtReto.text = "Completado"
                btnCompletar.isEnabled = false

                Toast.makeText(this, "Reto completado", Toast.LENGTH_SHORT).show()
            }

            dialog.show()
        }


    }
}