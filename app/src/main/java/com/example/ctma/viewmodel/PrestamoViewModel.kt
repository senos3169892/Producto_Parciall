package com.example.ctma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ctma.model.Equipo
import com.example.ctma.model.SolicitudPrestamo
import com.example.ctma.repository.InMemoryPrestamoRepository

class PrestamoViewModel : ViewModel() {

    private val repository = InMemoryPrestamoRepository()

    fun obtenerEquipos(): List<Equipo> {
        return repository.obtenerEquipos()
    }

    fun obtenerSolicitudes(): List<SolicitudPrestamo> {
        return repository.obtenerSolicitudes()
    }

    fun crearSolicitud(
        solicitud: SolicitudPrestamo
    ): Result<Unit> {
        return repository.crearSolicitud(solicitud)
    }

    fun cancelarSolicitud(
        id: Int
    ): Result<Unit> {
        return repository.cancelarSolicitud(id)
    }
}