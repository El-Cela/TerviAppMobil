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
import com.example.tervi.data.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialog

import androidx.lifecycle.lifecycleScope
import com.example.tervi.api.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    //Variables globales
    private lateinit var btn_retos_comp: Button
    private lateinit var btn_resumen : Button
    private lateinit var btn_escaner : Button
    private lateinit var btn_perfil: Button
    private lateinit var btn_logout: Button

    // TextViews para actividades
    private lateinit var txt_ejercicio: TextView
    private lateinit var txt_rep_programadas: TextView
    private lateinit var txt_rep_hechas: TextView

    // TextViews para puntajes
    private lateinit var txt_puntaje: TextView
    private lateinit var txt_nombre_ejercicio_puntos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()
        val userName = sessionManager.getUserName()

        // Redirigir al login si no hay sesión
        if (userId == null || userName == null) {
            val intent = Intent(this, MainInicio::class.java)
            startActivity(intent)
            finish()
            return
        }
        
        // Inicializar vistas
        btn_retos_comp = findViewById(R.id.btn_retos_comp)
        btn_resumen = findViewById(R.id.btn_resumen)
        btn_escaner = findViewById(R.id.btn_escaner)
        btn_perfil = findViewById(R.id.btn_perfil)
        btn_logout = findViewById(R.id.btn_logout)

        txt_ejercicio = findViewById(R.id.ejercicio)
        txt_rep_programadas = findViewById(R.id.repeticiones_programadas)
        txt_rep_hechas = findViewById(R.id.repeticiones_hechas)

        txt_puntaje = findViewById(R.id.puntaje)
        txt_nombre_ejercicio_puntos = findViewById(R.id.nombrre_ejercicio_puntos)

        cargarActividades(userId, userName)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_logout.setOnClickListener {
            sessionManager.logout()
            val intent = Intent(this, MainInicio::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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
            val btnCompletar2 = view.findViewById<Button>(R.id.btn_completar2)
            val txtReto2 = view.findViewById<TextView>(R.id.reto02)
            val btnCompletar3 = view.findViewById<Button>(R.id.btn_completar3)
            val txtReto3 = view.findViewById<TextView>(R.id.reto03)

            // Acción del botón
            btnCompletar.setOnClickListener {
                txtReto.text = "Completado"
                btnCompletar.isEnabled = false

                Toast.makeText(this, "Reto completado", Toast.LENGTH_SHORT).show()
            }

            // Acción del botón2
            btnCompletar2.setOnClickListener {
                txtReto2.text = "Completado"
                btnCompletar3.isEnabled = false

                Toast.makeText(this, "Reto completado", Toast.LENGTH_SHORT).show()
            }

            // Acción del botón3
            btnCompletar3.setOnClickListener {
                txtReto3.text = "Completado"
                btnCompletar3.isEnabled = false

                Toast.makeText(this, "Reto completado", Toast.LENGTH_SHORT).show()
            }

            dialog.show()
        }
    }

    private fun cargarActividades(userId: String, userName: String) {
        android.util.Log.d("DEBUG_APP", "Cargando actividades para: ID=$userId, Name=$userName")
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getActividades(userId, userName)
                android.util.Log.d("DEBUG_APP", "Respuesta recibida: status=${response.status}, message=${response.message}")

                if (response.status == "success") {
                    // Cargar progreso de actividad
                    if (!response.data.isNullOrEmpty()) {
                        android.util.Log.d("DEBUG_APP", "Datos de actividad encontrados: ${response.data.size} items")
                        val actividad = response.data[0]
                        txt_ejercicio.text = actividad.ejercicio
                        txt_rep_programadas.text = actividad.repeticiones_programadas.toString()
                        txt_rep_hechas.text = actividad.repeticiones_hechas.toString()
                    } else {
                        android.util.Log.w("DEBUG_APP", "La lista de actividades (data) está vacía o es nula")
                    }

                    // Cargar puntajes recientes
                    if (!response.avances.isNullOrEmpty()) {
                        android.util.Log.d("DEBUG_APP", "Datos de avances encontrados: ${response.avances.size} items")
                        val avance = response.avances[0]
                        txt_puntaje.text = avance.puntos.toString()
                        txt_nombre_ejercicio_puntos.text = avance.ejercicio_nombre
                    } else {
                        android.util.Log.w("DEBUG_APP", "La lista de avances está vacía o es nula")
                    }
                } else {
                    android.util.Log.e("DEBUG_APP", "Error en la respuesta: ${response.message}")
                    Toast.makeText(this@MainActivity, "No hay actividades disponibles: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_APP", "Error fatal al cargar actividades", e)
                Toast.makeText(this@MainActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
