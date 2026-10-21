package com.tecsup.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.navigation.NavCafe
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as CafeApp
        val factory = CafeViewModelFactory(app.repository, app.preferencias)

        setContent {
            // ViewModels a nivel de Activity: sobreviven a la rotacion (el carrito no se pierde)
            val sesionVm: SesionViewModel = viewModel(factory = factory)
            val menuVm: MenuViewModel = viewModel(factory = factory)
            val pedidosClienteVm: PedidosClienteViewModel = viewModel(factory = factory)
            val panelVm: PanelAdminViewModel = viewModel(factory = factory)
            val pedidosAdminVm: PedidosAdminViewModel = viewModel(factory = factory)
            val menuAdminVm: MenuAdminViewModel = viewModel(factory = factory)

            val oscuro by sesionVm.modoOscuro.collectAsState()
            val usuario by sesionVm.usuarioActual.collectAsState()
            val rol = if (usuario?.rol == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

            CafeTheme(rol = rol, darkTheme = oscuro) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavCafe(
                        sesionVm = sesionVm,
                        menuVm = menuVm,
                        pedidosClienteVm = pedidosClienteVm,
                        panelVm = panelVm,
                        pedidosAdminVm = pedidosAdminVm,
                        menuAdminVm = menuAdminVm
                    )
                }
            }
        }
    }
}
