package com.example.ctma

fun propositoValido(texto: String): Boolean {
    return texto.trim().length in 10..180
}

fun duracionValida(horas: Int): Boolean {
    return horas in 1..8
}

fun ambienteValido(ambiente: String): Boolean {
    return ambiente.trim().isNotEmpty()
}