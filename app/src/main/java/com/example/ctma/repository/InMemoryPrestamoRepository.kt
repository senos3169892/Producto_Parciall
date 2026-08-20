package com.example.ctma.repository

import com.example.ctma.model.Equipo
import com.example.ctma.model.EstadoEquipo
import com.example.ctma.model.EstadoSolicitud
import com.example.ctma.model.SolicitudPrestamo

class InMemoryPrestamoRepository : PrestamoRepository {

    private val equipos = mutableListOf(
        Equipo(
            id = 1,
            nombre = "Multímetro Digital",
            categoria = "Electrónica",
            estado = EstadoEquipo.DISPONIBLE
        ),
        Equipo(
            id = 2,
            nombre = "Osciloscopio",
            categoria = "Electrónica",
            estado = EstadoEquipo.DISPONIBLE
        ),
        Equipo(
            id = 3,
            nombre = "Fuente de Alimentación",
            categoria = "Electrónica",
            estado = EstadoEquipo.PRESTADO
        )
    )

    private val solicitudes = mutableListOf<SolicitudPrestamo>()

    override fun obtenerEquipos(): List<Equipo> {
        return equipos.toList()
    }

    override fun obtenerEquipo(id: Int): Equipo? {
        return equipos.find { it.id == id }
    }

    override fun obtenerSolicitudes(): List<SolicitudPrestamo> {
        return solicitudes.toList()
    }

    override fun obtenerSolicitud(id: Int): SolicitudPrestamo? {
        return solicitudes.find { it.id == id }
    }

    override fun crearSolicitud(
        solicitud: SolicitudPrestamo
    ): Result<Unit> {

        val equipo = obtenerEquipo(solicitud.equipoId)

        if (equipo == null) {
            return Result.failure(
                IllegalArgumentException("El equipo no existe")
            )
        }

        if (equipo.estado != EstadoEquipo.DISPONIBLE) {
            return Result.failure(
                IllegalStateException("El equipo no está disponible")
            )
        }

        solicitudes.add(solicitud)

        return Result.success(Unit)
    }

    override fun cancelarSolicitud(id: Int): Result<Unit> {

        val solicitud = obtenerSolicitud(id)

        if (solicitud == null) {
            return Result.failure(
                IllegalArgumentException("La solicitud no existe")
            )
        }

        solicitudes.remove(solicitud)

        return Result.success(Unit)
    }
}