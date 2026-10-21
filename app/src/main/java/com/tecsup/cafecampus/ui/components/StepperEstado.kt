package com.tecsup.cafecampus.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido

private val PASOS = listOf(
    EstadoPedido.RECIBIDO, EstadoPedido.PREPARANDO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO
)

private fun descripcion(e: EstadoPedido) = when (e) {
    EstadoPedido.RECIBIDO -> "Tu pedido llegó a la cocina"
    EstadoPedido.PREPARANDO -> "Estamos cocinando tu pedido"
    EstadoPedido.LISTO -> "Puedes pasar a recogerlo"
    else -> "¡Buen provecho!"
}

private val Verde = Color(0xFF16A34A)

/**
 * Stepper vertical animado:
 *  · la línea naranja se "llena" al avanzar de estado,
 *  · el check aparece con un pequeño rebote,
 *  · el paso en curso tiene un halo que late.
 */
@Composable
fun StepperEstado(estado: EstadoPedido, modifier: Modifier = Modifier) {
    if (estado == EstadoPedido.CANCELADO) {
        EstadoChip(estado, modifier)
        return
    }
    val actual = PASOS.indexOf(estado)
    val primario = MaterialTheme.colorScheme.primary
    val gris = MaterialTheme.colorScheme.outlineVariant

    val pulso by rememberInfiniteTransition(label = "pulso").animateFloat(
        initialValue = 0.15f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "halo"
    )

    Column(modifier) {
        PASOS.forEachIndexed { i, paso ->
            val hecho = i < actual || estado == EstadoPedido.ENTREGADO
            val enCurso = i == actual && estado != EstadoPedido.ENTREGADO
            val pendiente = !hecho && !enCurso

            val progresoLinea by animateFloatAsState(
                targetValue = if (i < actual) 1f else 0f,
                animationSpec = tween(600),
                label = "linea"
            )
            // El check "salta" al aparecer (escala con rebote)
            val escalaCheck by animateFloatAsState(
                targetValue = if (hecho) 1f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "check"
            )
            val colorCirculo by animateColorAsState(
                when {
                    hecho -> Verde
                    enCurso -> primario
                    else -> MaterialTheme.colorScheme.surfaceVariant
                },
                animationSpec = tween(400),
                label = "circulo"
            )

            Row {
                Column(Modifier.width(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (enCurso) primario.copy(alpha = pulso) else Color.Transparent)
                            .then(if (pendiente) Modifier.border(2.dp, gris, CircleShape) else Modifier),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            Modifier
                                .size(if (enCurso) 16.dp else if (pendiente) 24.dp else 28.dp)
                                .clip(CircleShape)
                                .background(colorCirculo),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Check, null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(16.dp)
                                    .graphicsLayer {
                                        scaleX = escalaCheck
                                        scaleY = escalaCheck
                                        alpha = escalaCheck.coerceIn(0f, 1f)
                                    }
                            )
                        }
                    }
                    if (i != PASOS.lastIndex) {
                        Box(
                            Modifier
                                .width(3.dp)
                                .height(44.dp)
                                .background(gris)
                        ) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(progresoLinea)
                                    .background(primario)
                            )
                        }
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.padding(top = 3.dp)) {
                    Text(
                        paso.etiqueta,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (pendiente) FontWeight.SemiBold else FontWeight.Bold,
                        color = if (pendiente) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                        else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        descripcion(paso),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (pendiente) 0.5f else 1f)
                    )
                }
            }
        }
    }
}
