package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.local.relation.PedidoPorHora
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import com.tecsup.cafecampus.model.Rol
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(db: CafeDatabase) : CafeRepository {

    private val productoDao = db.productoDao()
    private val usuarioDao = db.usuarioDao()
    private val pedidoDao = db.pedidoDao()

    // ---------- Usuarios ----------
    override suspend fun login(correo: String, clave: String): Usuario? =
        usuarioDao.login(correo, clave)

    override suspend fun usuarioPorRol(rol: Rol): Usuario? =
        usuarioDao.primeroPorRol(rol.name)

    override suspend fun registrarEstudiante(nombre: String, correo: String, clave: String): Usuario? {
        val correoLimpio = correo.trim().lowercase()
        if (usuarioDao.contarPorCorreo(correoLimpio) > 0) return null
        val nuevo = Usuario(
            nombre = nombre.trim(),
            correo = correoLimpio,
            clave = clave,
            rol = Rol.CLIENTE.name // nunca se registra un administrador
        )
        val id = usuarioDao.insertar(nuevo)
        return nuevo.copy(id = id.toInt())
    }

    // ---------- Productos ----------
    override fun observarProductos(): Flow<List<Producto>> = productoDao.observarTodos()

    override suspend fun guardarProducto(p: Producto) {
        productoDao.insertar(p) // REPLACE: sirve para nuevo y para editar
    }

    override suspend fun eliminarProducto(p: Producto) = productoDao.eliminar(p)

    override suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean) =
        productoDao.cambiarDisponibilidad(id, disponible)

    // ---------- Pedidos ----------
    override suspend fun crearPedido(
        clienteId: Int, items: List<ItemCarrito>,
        horaRecojo: String, pago: String, nota: String
    ): Long {
        val total = items.sumOf { it.producto.precio * it.cantidad }
        val pedido = Pedido(
            clienteId = clienteId,
            estado = EstadoPedido.RECIBIDO.name,
            horaRecojo = horaRecojo,
            pago = pago,
            nota = nota,
            total = total
        )
        // "Foto" del producto: copiamos nombre y precio del momento
        val detalles = items.map {
            DetallePedido(
                pedidoId = 0, // el DAO asigna el id real
                productoId = it.producto.id,
                nombreProducto = it.producto.nombre,
                cantidad = it.cantidad,
                precioUnitario = it.producto.precio
            )
        }
        return pedidoDao.crearPedido(pedido, detalles)
    }

    override fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>> =
        pedidoDao.observarPorCliente(clienteId)

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> =
        pedidoDao.observarPorEstado(estado.name)

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> = pedidoDao.observarUno(id)

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) =
        pedidoDao.cambiarEstado(pedidoId, estado.name)

    // ---------- Panel ----------
    override fun observarVentasHoy(inicioDia: Long): Flow<Double> = pedidoDao.ventasDesde(inicioDia)

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> =
        pedidoDao.cantidadPorEstado(estado.name)

    override fun observarPedidosHoy(inicioDia: Long): Flow<Int> = pedidoDao.contarDesde(inicioDia)

    override fun observarPedidosPorHora(inicioDia: Long): Flow<List<PedidoPorHora>> =
        pedidoDao.pedidosPorHora(inicioDia)
}
