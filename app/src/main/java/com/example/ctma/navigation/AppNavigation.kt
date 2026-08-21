package com.example.ctma.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.ctma.model.Equipo
import com.example.ctma.ui.catalogo.CatalogoScreen
import com.example.ctma.ui.catalogo.solicitud.SolicitudScreen
import com.example.ctma.ui.catalogo.solicitudes.SolicitudesScreen
import com.example.ctma.viewmodel.PrestamoViewModel

@Composable
fun AppNavigation(
    viewModel: PrestamoViewModel
) {

    /*
     * 0 = Catálogo
     * 1 = Mis solicitudes
     * 2 = Crear solicitud
     */

    var pantallaActual by remember {
        mutableIntStateOf(0)
    }

    var equipoSeleccionado by remember {
        mutableStateOf<Equipo?>(null)
    }

    /*
     * Observamos el StateFlow del ViewModel.
     * Cuando cambia una solicitud o el estado de un equipo,
     * Compose actualiza automáticamente la interfaz.
     */
    val uiState by viewModel.uiState.collectAsState()

    when (pantallaActual) {

        // ==============================
        // CATÁLOGO
        // ==============================

        0 -> {

            CatalogoScreen(
                equipos = uiState.equipos,

                onEquipoSeleccionado = { equipo ->

                    equipoSeleccionado = equipo
                    pantallaActual = 2
                }
            )
        }

        // ==============================
        // SOLICITUDES
        // ==============================

        1 -> {

            SolicitudesScreen(
                solicitudes = uiState.solicitudes,

                onCancelarSolicitud = { id ->

                    viewModel.cancelarSolicitud(id)
                }
            )
        }

        // ==============================
        // CREAR SOLICITUD
        // ==============================

        2 -> {

            val equipo = equipoSeleccionado

            if (equipo != null) {

                SolicitudScreen(
                    equipo = equipo,

                    onSolicitudCreada = { solicitud ->

                        val resultado =
                            viewModel.crearSolicitud(solicitud)

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