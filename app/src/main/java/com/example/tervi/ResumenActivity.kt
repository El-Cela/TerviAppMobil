package com.example.tervi

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tervi.api.ApiResponseActividades
import com.example.tervi.api.RetrofitClient
import com.example.tervi.data.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class ResumenActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var txtEjerciciosCompl: TextView
    private var lastResponse: ApiResponseActividades? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resumen)

        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()
        val userUsername = sessionManager.getUserUsername()

        val btnRegresar = findViewById<Button>(R.id.btn_regresar_resumen)
        val btnHistorialActividades = findViewById<Button>(R.id.btn_historial_actividades)
        val btnHistorialEjercicios = findViewById<Button>(R.id.btn_historial_ejercicios)
        
        txtEjerciciosCompl = findViewById(R.id.ejercicios_compl)

        btnRegresar.setOnClickListener {
            finish()
        }

        btnHistorialActividades.setOnClickListener {
            if (userId != null && userUsername != null) {
                setupDynamicPanel(R.layout.desplegable_actividades, true, userId, userUsername)
            }
        }

        btnHistorialEjercicios.setOnClickListener {
            if (userId != null && userUsername != null) {
                setupDynamicPanel(R.layout.desplegable_ejercicios, false, userId, userUsername)
            }
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            if (userId != null && userUsername != null) {
                cargarResumen(userId, userUsername)
            } else {
                swipeRefreshLayout.isRefreshing = false
            }
        }

        if (userId != null && userUsername != null) {
            cargarResumen(userId, userUsername)
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
                lastResponse = response
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

    private fun setupDynamicPanel(layoutId: Int, isActivities: Boolean, userId: String, userName: String) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(layoutId, null)
        val container = view.findViewById<LinearLayout>(if (isActivities) R.id.container_actividades else R.id.container_ejercicios)

        // Función interna para poblar el contenedor con los datos
        fun populateContainer(response: ApiResponseActividades) {
            container.removeAllViews() // Limpiar por si acaso
            if (isActivities) {
                val actividades = response.data
                if (!actividades.isNullOrEmpty()) {
                    for (actividad in actividades) {
                        val itemView = layoutInflater.inflate(R.layout.item_historial, container, false)
                        itemView.findViewById<TextView>(R.id.text_main).text = actividad.ejercicio ?: "N/A"
                        itemView.findViewById<TextView>(R.id.text_secondary).text = "Reps: ${actividad.repeticiones_hechas ?: 0} / ${actividad.repeticiones_programadas ?: 0}"
                        itemView.findViewById<TextView>(R.id.text_extra).text = if (!actividad.fecha_completado.isNullOrEmpty()) {
                            actividad.fecha_completado.split(" ")[0]
                        } else {
                            "Pendiente"
                        }
                        container.addView(itemView)
                    }
                } else {
                    mostrarMensajeVacio(container, "No hay actividades registradas")
                }
            } else {
                val avances = response.avances
                if (!avances.isNullOrEmpty()) {
                    for (avance in avances) {
                        val itemView = layoutInflater.inflate(R.layout.item_historial, container, false)
                        itemView.findViewById<TextView>(R.id.text_main).text = avance.ejercicio_nombre ?: "N/A"
                        itemView.findViewById<TextView>(R.id.text_secondary).text = "${avance.puntos ?: 0} pts - ${avance.tipo_entorno ?: "N/A"}"
                        itemView.findViewById<TextView>(R.id.text_extra).text = if (!avance.fecha_registro.isNullOrEmpty()) {
                            avance.fecha_registro.split(" ")[0]
                        } else {
                            "N/A"
                        }
                        container.addView(itemView)
                    }
                } else {
                    mostrarMensajeVacio(container, "No hay ejercicios registrados")
                }
            }
        }

        // Si ya tenemos los datos cargados del resumen, los usamos directamente
        if (lastResponse != null && lastResponse?.status == "success") {
            populateContainer(lastResponse!!)
        } else {
            // Si no hay datos, intentamos cargarlos usando getActividades (más estable que getHistorial)
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.getActividades(userId, userName, System.currentTimeMillis())
                    if (response.status == "success") {
                        lastResponse = response
                        populateContainer(response)
                    } else {
                        Toast.makeText(this@ResumenActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    android.util.Log.e("DEBUG_APP", "Error al cargar historial", e)
                    Toast.makeText(this@ResumenActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun mostrarMensajeVacio(container: LinearLayout, mensaje: String) {
        val emptyText = TextView(this)
        emptyText.text = mensaje
        emptyText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        emptyText.setPadding(0, 50, 0, 0)
        container.addView(emptyText)
    }
}