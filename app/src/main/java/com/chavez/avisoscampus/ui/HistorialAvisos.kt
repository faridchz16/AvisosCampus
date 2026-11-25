package com.chavez.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chavez.avisoscampus.model.Aviso
import com.chavez.avisoscampus.model.Origen

private val ColorBoton = Color(0xFF1E88E5)
private val ColorRecordatorio = Color(0xFFFFB300)
private val ColorModoAvion = Color(0xFF7E57C2)

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    TarjetaSeccion("Historial") {
        if (avisos.isEmpty()) {
            Text(
                text = "Sin avisos todavía",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            avisos.take(20).forEach { aviso ->
                FilaAviso(aviso)
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EtiquetaOrigen(aviso.origen)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aviso.titulo,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = aviso.texto,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = aviso.hora,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EtiquetaOrigen(origen: Origen) {
    val (fondo, texto) = when (origen) {
        Origen.BOTON -> ColorBoton to Color.White
        Origen.RECORDATORIO -> ColorRecordatorio to Color.Black
        Origen.MODO_AVION -> ColorModoAvion to Color.White
    }
    Surface(color = fondo, shape = RoundedCornerShape(50)) {
        Text(
            text = origen.texto,
            color = texto,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
