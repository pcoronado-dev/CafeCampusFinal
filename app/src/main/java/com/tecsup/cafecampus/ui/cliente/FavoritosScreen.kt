package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.EstadoVacio
import com.tecsup.cafecampus.ui.components.ProductoCard
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@Composable
fun FavoritosScreen(
    vm: MenuViewModel,
    onProducto: (Int) -> Unit,
    onVerMenu: () -> Unit
) {
    val favoritos by vm.productosFavoritos.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Favoritos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        if (favoritos.isEmpty()) {
            EstadoVacio(
                icono = Icons.Filled.Favorite,
                titulo = "Aún no tienes favoritos",
                mensaje = "Toca el corazón de un producto para guardarlo aquí.",
                accion = { CafeButton("Ver el menú", onVerMenu) }
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favoritos, key = { it.id }) { p ->
                    ProductoCard(
                        p,
                        onAgregar = { vm.agregar(p, 1) },
                        esFavorito = true,
                        onFavorito = { vm.alternarFavorito(p.id) }
                    ) { onProducto(p.id) }
                }
            }
        }
    }
}
