# Fotos de los platillos y del perfil

## 1) Fotos incluidas en la app (las 6 del menú inicial)
Copia tus fotos (JPG o PNG, ~800×600 px, menos de 300 KB cada una) en:

    app/src/main/res/drawable-nodpi/

con EXACTAMENTE estos nombres (minúsculas, sin espacios):

| Producto            | Archivo                    |
|---------------------|----------------------------|
| Café americano      | prod_cafe_americano.jpg    |
| Sándwich de pollo   | prod_sandwich_pollo.jpg    |
| Menú del día        | prod_menu_del_dia.jpg      |
| Jugo de naranja     | prod_jugo_naranja.jpg      |
| Empanada            | prod_empanada.jpg          |
| Chicha morada       | prod_chicha_morada.jpg     |

En Android Studio: clic derecho en `res` → New → Android Resource Directory →
tipo *drawable* y calificador *nodpi* (o simplemente crea la carpeta `drawable-nodpi`)
y arrastra las fotos. Si falta alguna foto, la app muestra el ícono del producto.

Fuentes gratuitas con licencia de uso libre: Unsplash, Pexels, Pixabay.
Revisa la licencia y cita la fuente en tu README si lo piden.

## 2) Fotos desde la galería (sin tocar el código)
- Administrador → Menú → editar (lápiz) o "+" → toca la tarjeta amarilla → elige una foto.
- Perfil (Estudiante) / Ajustes (Administrador) → toca la foto de perfil → elige una foto.

Las fotos de la galería se copian al almacenamiento interno de la app y se conservan al cerrarla.
