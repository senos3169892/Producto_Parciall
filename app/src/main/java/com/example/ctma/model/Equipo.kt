package com.example.ctma.model

data class Equipo(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val estado: EstadoEquipo
)