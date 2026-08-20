package com.example.ctma.ui.catalogo.solicitud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ctma.model.Equipo
import com.example.ctma.model.EstadoEquipo
import com.example.ctma.model.EstadoSolicitud
import com.example.ctma.model.SolicitudPrestamo

@Composable
fun SolicitudScreen(
    equipo: Equipo,
    onSolicitudCreada: (SolicitudPrestamo) -> Unit
) {

    var ambienteDestino by remember {
        mutableStateOf("")
    }

    var proposito by remember {
        mutableStateOf("")
    }

    var duracionTexto by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Solicitar préstamo",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Equipo: ${equipo.nombre}",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Categoría: ${equipo.categoria}"
        )

        Text(
            text = "Estado: ${equipo.estado}"
        )

        if (equipo.estado != EstadoEquipo.DISPONIBLE) {

            Text(
                text = "Este equipo no está disponible para préstamo.",
                color = MaterialTheme.colorScheme.error
            )

        } else {

            OutlinedTextField(
                value = ambienteDestino,
                onValueChange = {
                    ambienteDestino = it
                    error = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Ambiente de destino")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = proposito,
                onValueChange = {
                    proposito = it
                    error = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Propósito del préstamo")
                },
                minLines = 3
            )

            OutlinedTextField(
                value = duracionTexto,
                onValueChange = {
                    duracionTexto = it
                    error = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Duración en horas")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            if (error.isNotEmpty()) {

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {

                    val horas = duracionTexto.toIntOrNull()

                    when {

                        ambienteDestino.isBlank() -> {
                            error = "Ingresa el ambiente de destino"
                        }

                        proposito.isBlank() -> {
                            error = "Ingresa el propósito del préstamo"
                        }

                        horas == null || horas <= 0 -> {
                            error = "Ingresa una duración válida"
                        }

                        else -> {

                            val solicitud = SolicitudPrestamo(
                                id = 0,
                                equipoId = equipo.id,
                                ambienteDestino = ambienteDestino.trim(),
                                proposito = proposito.trim(),
                                duracionHoras = horas,
                                estado = EstadoSolicitud.SOLICITADA
                            )

                            onSolicitudCreada(solicitud)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Crear solicitud"
                )
            }
        }
    }
}