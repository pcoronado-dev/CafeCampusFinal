package com.tecsup.cafecampus.ui.cliente

import androidx.compose.runtime.Composable
import com.tecsup.cafecampus.ui.admin.AjustesContenido
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(vm: SesionViewModel, onCambiarPerfil: () -> Unit, onCerrarSesion: () -> Unit) {
    AjustesContenido(
        titulo = "Perfil",
        vm = vm,
        rolActivo = "Estudiante",
        destinoCambio = "Administrador",
        mostrarRecibirPedidos = false,
        onCambiarPerfil = onCambiarPerfil,
        onCerrarSesion = onCerrarSesion
    )
}
