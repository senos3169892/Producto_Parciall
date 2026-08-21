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
import com.example.ctma.ambienteValido
import com.example.ctma.duracionValida
import com.example.ctma.model.Equipo
import com.example.ctma.model.EstadoSolicitud
import com.example.ctma.model.SolicitudPrestamo
import com.example.ctma.propositoValido

@Composable
fun SolicitudScreen(
    equipo: Equipo,
    onSolicitudCreada: (SolicitudPrestamo) -> Result<Unit>
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

        Text(
            text = "${proposito.length}/180 caracteres",
            style = MaterialTheme.typography.bodySmall
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
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {

                val horas = duracionTexto.toIntOrNull()

                when {

                    !ambienteValido(ambienteDestino) -> {
                        error = "El ambiente de destino es obligatorio"
                    }

                    !propositoValido(proposito) -> {
                        error = when {
                            proposito.trim().length < 10 ->
                                "El propósito debe tener mínimo 10 caracteres"

                            else ->
                                "El propósito no puede superar 180 caracteres"
                        }
                    }

                    horas == null -> {
                        error = "Ingresa una duración válida"
                    }

                    !duracionValida(horas) -> {
                        error = when {
                            horas < 1 ->
                                "La duración mínima es de 1 hora"

                            else ->
                                "La duración máxima es de 8 horas"
                        }
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

                        val resultado = onSolicitudCreada(solicitud)

                        if (resultado.isFailure) {
                            error = resultado.exceptionOrNull()?.message
                                ?: "No se pudo crear la solicitud"
                        }
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