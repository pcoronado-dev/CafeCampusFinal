# CafeCampus

App de pedidos para la cafetería del campus (TECSUP · Programación en Móviles · Lab MVVM + Room).
Dos perfiles en el mismo dispositivo: **Estudiante** (pide y sigue su pedido) y **Administrador** (gestiona pedidos y menú).
Ambos comparten la misma base de datos Room, así que el estudiante ve en tiempo real los cambios de estado.

Kotlin · Jetpack Compose · Material 3 · Room 2.6.1 · DataStore · Navigation Compose · MVVM + Repository (interface)

**Credenciales de prueba (datos ficticios):** `juan@campus.test / 1234` (Estudiante) · `admin@cafe.test / admin` (Administrador)

## Cómo ejecutar
1. Android Studio (Koala o superior, JDK 17) → *Open* → carpeta `CafeCampus`.
2. Esperar el *Gradle Sync* (necesita internet la primera vez).
3. Ejecutar en un emulador o celular (minSdk 26).

## Árbol de paquetes
```
com.tecsup.cafecampus
├── MainActivity.kt
├── CafeApp.kt                  (crea base, repository y siembra datos)
├── data
│   ├── local
│   │   ├── entity   Producto · Usuario · Pedido · DetallePedido
│   │   ├── dao      ProductoDao · UsuarioDao · PedidoDao
│   │   ├── relation PedidoConDetalle · PedidoPorHora
│   │   ├── CafeDatabase.kt · DatosIniciales.kt
│   ├── repository   CafeRepository (interface) · CafeRepositoryRoom
│   └── prefs        PreferenciasStore (DataStore)
├── model            Rol · EstadoPedido · ItemCarrito
├── viewmodel        Sesion · Menu · PedidosCliente · PanelAdmin · PedidosAdmin · MenuAdmin · Factory
└── ui
    ├── theme · components · navigation · util
    ├── cliente      Splash · Login · Menu · DetalleProducto · ConfirmarPedido · Seguimiento · MisPedidos · Perfil
    └── admin        Panel · PedidosAdmin · DetallePedidoAdmin · MenuAdmin · FormularioProducto · Ajustes
```

## Diagrama MVVM
```
VIEW (Composable)
  │ eventos (clics) ▲ collectAsState()
  ▼                 │
VIEWMODEL (StateFlow)
  │ llama           ▲ Flow / resultados
  ▼                 │
CafeRepository (interface)
  │ la implementa
  ▼
CafeRepositoryRoom ──► DAO ──► SQLite (cafecampus.db)
DataStore (solo preferencias) ──► ViewModel
```

## Capturas
(Agrega aquí 3 capturas del perfil Estudiante y 3 del Administrador)

| Estudiante | | |
|---|---|---|
| ![](capturas/cliente1.png) | ![](capturas/cliente2.png) | ![](capturas/cliente3.png) |

| Administrador | | |
|---|---|---|
| ![](capturas/admin1.png) | ![](capturas/admin2.png) | ![](capturas/admin3.png) |

## Para pensar
- **¿Por qué el Repository es una interface?** Para que los ViewModels dependan de un contrato y no de Room; así se puede cambiar la fuente de datos (servidor, nube) sin tocarlos.
- **¿Por qué `crearPedido` es suspend y `observarPedidosCliente` es Flow?** Crear es una acción única que termina; observar debe avisar a la pantalla cada vez que los datos cambian.
- **¿Y si dos perfiles usaran bases distintas?** Cada uno vería datos diferentes: el estudiante nunca vería el cambio de estado hecho por el administrador.
- **¿Qué cambiaría si los datos viven en un servidor?** Solo una nueva implementación del Repository (p. ej. `CafeRepositoryRemoto`); ViewModels y pantallas quedan igual.
