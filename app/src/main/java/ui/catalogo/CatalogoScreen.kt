package com.example.ctma.ui.catalogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ctma.model.Equipo

@Composable
fun CatalogoScreen(
    equipos: List<Equipo>,
    onEquipoSeleccionado: (Equipo) -> Unit,
    onMisSolicitudes: () -> Unit
) {

    var equipoSeleccionado by remember {
        mutableStateOf<Equipo?>(null)
    }

    if (equipoSeleccionado == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Catálogo de equipos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // ==============================
            // BOTÓN MIS SOLICITUDES
            // ==============================

            Button(
                onClick = {
                    onMisSolicitudes()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Mis solicitudes")
            }

            // ==============================
            // LISTA DE EQUIPOS
            // ==============================

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(equipos) { equipo ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = equipo.nombre,
                                style = MaterialTheme.typography.titleLarge
                            )

                            Text(
                                text = "Categoría: ${equipo.categoria}"
                            )

                            Text(
                                text = "Estado: ${equipo.estado}"
                            )

                            Button(
                                onClick = {
                                    equipoSeleccionado = equipo
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text("Ver detalle")
                            }
                        }
                    }
                }
            }
        }

    } else {

        val equipo = equipoSeleccionado!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Detalle del equipo",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = equipo.nombre,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "ID: ${equipo.id}",
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    Text(
                        text = "Categoría: ${equipo.categoria}",
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Estado: ${equipo.estado}",
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        TextButton(
                            onClick = {
                                equipoSeleccionado = null
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Volver")
                        }

                        Button(
                            onClick = {
                                onEquipoSeleccionado(equipo)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Solicitar préstamo")
                        }
                    }
                }
            }
        }
    }
}