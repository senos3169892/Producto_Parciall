package com.example.ctma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ctma.model.Equipo
import com.example.ctma.model.SolicitudPrestamo
import com.example.ctma.repository.InMemoryPrestamoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PrestamoUiState(
    val equipos: List<Equipo> = emptyList(),
    val solicitudes: List<SolicitudPrestamo> = emptyList(),
    val mensaje: String? = null
)

class PrestamoViewModel : ViewModel() {

    private val repository = InMemoryPrestamoRepository()

    private val _uiState = MutableStateFlow(
        PrestamoUiState(
            equipos = repository.obtenerEquipos(),
            solicitudes = repository.obtenerSolicitudes()
        )
    )

    val uiState: StateFlow<PrestamoUiState> = _uiState.asStateFlow()

    fun obtenerEquipos(): List<Equipo> {
        return repository.obtenerEquipos()
    }

    fun obtenerSolicitudes(): List<SolicitudPrestamo> {
        return repository.obtenerSolicitudes()
    }

    fun crearSolicitud(
        solicitud: SolicitudPrestamo
    ): Result<Unit> {

        val resultado = repository.crearSolicitud(solicitud)

        actualizarEstado()

        return resultado
    }

    fun cancelarSolicitud(
        id: Int
    ): Result<Unit> {

        val resultado = repository.cancelarSolicitud(id)

        actualizarEstado()

        return resultado
    }

    private fun actualizarEstado() {

        _uiState.value = PrestamoUiState(
            equipos = repository.obtenerEquipos(),
            solicitudes = repository.obtenerSolicitudes()
        )
    }
}