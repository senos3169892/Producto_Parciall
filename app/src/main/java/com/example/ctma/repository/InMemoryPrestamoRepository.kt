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

        val solicitudActiva = solicitudes.any {
            it.equipoId == solicitud.equipoId &&
                    it.estado == EstadoSolicitud.SOLICITADA
        }

        if (solicitudActiva) {
            return Result.failure(
                IllegalStateException(
                    "El equipo ya tiene una solicitud activa"
                )
            )
        }

        val nuevoId = if (solicitudes.isEmpty()) {
            1
        } else {
            solicitudes.maxOf { it.id } + 1
        }

        val nuevaSolicitud = solicitud.copy(
            id = nuevoId,
            estado = EstadoSolicitud.SOLICITADA
        )

        solicitudes.add(nuevaSolicitud)

        val indiceEquipo = equipos.indexOfFirst {
            it.id == equipo.id
        }

        if (indiceEquipo != -1) {
            equipos[indiceEquipo] = equipo.copy(
                estado = EstadoEquipo.PRESTADO
            )
        }

        return Result.success(Unit)
    }

    override fun cancelarSolicitud(id: Int): Result<Unit> {

        val solicitud = obtenerSolicitud(id)

        if (solicitud == null) {
            return Result.failure(
                IllegalArgumentException("La solicitud no existe")
            )
        }

        if (solicitud.estado != EstadoSolicitud.SOLICITADA) {
            return Result.failure(
                IllegalStateException(
                    "Solo se pueden cancelar solicitudes solicitadas"
                )
            )
        }

        val indiceSolicitud = solicitudes.indexOfFirst {
            it.id == id
        }

        if (indiceSolicitud == -1) {
            return Result.failure(
                IllegalArgumentException("La solicitud no existe")
            )
        }

        solicitudes[indiceSolicitud] = solicitud.copy(
            estado = EstadoSolicitud.CANCELADA
        )

        val equipo = obtenerEquipo(solicitud.equipoId)

        if (equipo != null) {

            val indiceEquipo = equipos.indexOfFirst {
                it.id == equipo.id
            }

            if (indiceEquipo != -1) {
                equipos[indiceEquipo] = equipo.copy(
                    estado = EstadoEquipo.DISPONIBLE
                )
            }
        }

        return Result.success(Unit)
    }
}