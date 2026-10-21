package com.tecsup.cafecampus.ui.cliente

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.ProductoIcono
import com.tecsup.cafecampus.viewmodel.SesionViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(vm: SesionViewModel, onIr: (Rol?) -> Unit) {
    var mostrarBotones by remember { mutableStateOf(false) }
    val escala = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        escala.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }
    LaunchedEffect(Unit) {
        delay(1200)
        val rol = vm.restaurarSesion()
        // Si ya hay perfil guardado salta el login; si no, muestra los botones
        if (rol != null) onIr(rol) else mostrarBotones = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            Modifier
                .graphicsLayer {
                    scaleX = escala.value
                    scaleY = escala.value
                }
                .size(96.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.LocalCafe, contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(52.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Text("CafeCampus", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Pide sin hacer cola", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(32.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ProductoIcono("cafe", tamano = 72.dp)
            ProductoIcono("plato", tamano = 72.dp)
            ProductoIcono("jugo", tamano = 72.dp)
        }
        Spacer(Modifier.weight(1f))
        if (mostrarBotones) {
            CafeButton("Comenzar", { onIr(null) }, Modifier.fillMaxWidth())
            TextButton(onClick = { onIr(null) }) {
                Text("Ya tengo una cuenta", color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Spacer(Modifier.height(100.dp))
        }
    }
}
