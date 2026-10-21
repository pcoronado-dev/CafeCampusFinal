@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecsup.cafecampus.ui.cliente

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.AppTopBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.EstadoVacio
import com.tecsup.cafecampus.ui.components.PedidoCard
import com.tecsup.cafecampus.ui.components.SelectorSegmentado
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@Composable
fun MisPedidosScreen(
    vm: PedidosClienteViewModel,
    onPedido: (Int) -> Unit,
    onRepetir: (PedidoConDetalle) -> Unit
) {
    val pedidos by vm.misPedidos.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(0) }

    // Activos = RECIBIDO, PREPARANDO, LISTO
    val activos = pedidos.filter { EstadoPedido.desde(it.pedido.estado).activo }
    val historial = pedidos.filter { !EstadoPedido.desde(it.pedido.estado).activo }

    Scaffold(topBar = { AppTopBar("Mis pedidos") }) { padding ->
        Column(Modifier.padding(padding)) {
            SelectorSegmentado(
                opciones = listOf("Activos", "Historial"),
                seleccion = pestana,
                onSeleccion = { pestana = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            // Transición suave al cambiar entre Activos e Historial
            Crossfade(targetState = pestana, label = "pestanas") { tab ->
                val lista = if (tab == 0) activos else historial
                if (lista.isEmpty()) {
                    EstadoVacio(
                        icono = Icons.Filled.Receipt,
                        titulo = if (tab == 0) "No hay pedidos activos" else "No hay pedidos en tu historial",
                        mensaje = if (tab == 0) "Cuando hagas un pedido podrás seguirlo aquí."
                        else "Tus pedidos entregados o cancelados aparecerán aquí.",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(lista, key = { it.pedido.id }) { p ->
                            PedidoCard(p, onClick = { onPedido(p.pedido.id) }) {
                                if (tab == 1) {
                                    CafeButton(
                                        texto = "Repetir pedido",
                                        onClick = { onRepetir(p) },
                                        outlined = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
