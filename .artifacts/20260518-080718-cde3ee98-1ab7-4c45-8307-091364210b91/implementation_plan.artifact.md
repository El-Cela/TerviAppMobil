# Actualización de Carga de Actividades por Nombre de Usuario

Se modificará el flujo de carga de actividades para filtrar por `nombre_usuario` (columna en la tabla `actividades`) en lugar de `id_usuario`.

## Proposed Changes

### Backend / PHP

#### [get_actividades.php](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/server/get_actividades.php)

- Cambiar el parámetro de entrada de `usuario_id` a `nombre_usuario`.
- Actualizar la consulta a la tabla `actividades` para filtrar por `nombre_usuario = ?`.
- Mantener la consulta a `avance_usuarios` por `id_usuario` (ya que esa tabla sí usa IDs numéricos).

```php
// Nuevo parámetro
$nombre_usuario = isset($_GET['nombre_usuario']) ? $_GET['nombre_usuario'] : null;
$usuario_id = isset($_GET['usuario_id']) ? $_GET['usuario_id'] : null;

// Consulta actividades usando nombre_usuario
$sql_actividades = "SELECT ejercicio, repeticiones_programadas, repeticiones_hechas FROM actividades WHERE nombre_usuario = ? LIMIT 1";
$stmt1 = $conn->prepare($sql_actividades);
$stmt1->bind_param("s", $nombre_usuario);
// ...
```

### UI / Android

#### [ApiService.kt](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/app/src/main/java/com/example/tervi/api/ApiService.kt)

- Modificar `getActividades` para que acepte tanto `usuario_id` como `nombre_usuario`.

```kotlin
@GET("get_actividades.php")
suspend fun getActividades(
    @Query("usuario_id") userId: String,
    @Query("nombre_usuario") userName: String
): ApiResponseActividades
```

#### [MainActivity.kt](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/app/src/main/java/com/example/tervi/MainActivity.kt)

- Recuperar el nombre de usuario desde `SessionManager`.
- Pasar el nombre de usuario a `cargarActividades`.
- Actualizar la llamada a la API para incluir ambos parámetros.

## Verification Plan

### Manual Verification
1.  **Login**: Iniciar sesión.
2.  **Carga**: Verificar que los campos de ejercicio y repeticiones se llenan con datos reales.
3.  **Logs**: Comprobar que la URL generada sea similar a `get_actividades.php?usuario_id=1&nombre_usuario=Juan`.
