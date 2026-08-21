package com.example.ctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ctma.model.Equipo
import com.example.ctma.ui.catalogo.CatalogoScreen
import com.example.ctma.ui.catalogo.solicitud.SolicitudScreen
import com.example.ctma.ui.catalogo.solicitudes.SolicitudesScreen
import com.example.ctma.ui.theme.PrestamoLabCTMAATheme
import com.example.ctma.viewmodel.PrestamoViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PrestamoLabCTMAATheme {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val prestamoViewModel: PrestamoViewModel = viewModel()

                    PrestamoApp(
                        viewModel = prestamoViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun PrestamoApp(
    viewModel: PrestamoViewModel
) {

    /*
     * 0 = Catálogo
     * 1 = Solicitudes
     * 2 = Crear solicitud
     */

    var pantallaActual by remember {
        mutableIntStateOf(0)
    }

    var equipoSeleccionado by remember {
        mutableStateOf<Equipo?>(null)
    }

    /*
     * Observamos el estado del ViewModel.
     * Cuando se crea o cancela una solicitud,
     * el StateFlow se actualiza automáticamente.
     */
    val uiState by viewModel.uiState.collectAsState()

    when (pantallaActual) {

        // ==========================================
        // CATÁLOGO
        // ==========================================

        0 -> {

            CatalogoScreen(
                equipos = uiState.equipos,

                onEquipoSeleccionado = { equipo ->

                    equipoSeleccionado = equipo
                    pantallaActual = 2
                }
            )
        }

        // ==========================================
        // SOLICITUDES
        // ==========================================

        1 -> {

            SolicitudesScreen(
                solicitudes = uiState.solicitudes,

                onCancelarSolicitud = { id ->

                    viewModel.cancelarSolicitud(id)
                }
            )
        }

        // ==========================================
        // CREAR SOLICITUD
        // ==========================================

        2 -> {

            val equipo = equipoSeleccionado

            if (equipo != null) {

                SolicitudScreen(
                    equipo = equipo,

                    onSolicitudCreada = { solicitud ->

                        val resultado = viewModel.crearSolicitud(solicitud)

                        if (resultado.isSuccess) {

                            equipoSeleccionado = null

                            pantallaActual = 1
                        }
                    }
                )

            } else {

                pantallaActual = 0
            }
        }
    }
}