# Corrección de carga de Progreso Actual y Login Híbrido

Se han realizado ajustes críticos tanto en la aplicación como en los scripts del servidor para asegurar que toda la información se muestre correctamente y el acceso sea flexible.

## Cambios realizados

### 1. Carga de Actividades por Nombre
Se detectó que la tabla `actividades` utiliza el nombre del usuario para filtrar, mientras que `avance_usuarios` utiliza el ID.
- **App**: Se actualizó [MainActivity.kt](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/app/src/main/java/com/example/tervi/MainActivity.kt) y [ApiService.kt](file:///C:/Users/claus/OneDrive/Documentos/GitHub/TerviAppMobil/app/src/main/java/com/example/tervi/api/ApiService.kt) para enviar ambos datos (`usuario_id` y `nombre_usuario`) al servidor.
- **Servidor**: Se proporcionó un nuevo código para `get_actividades.php` que realiza la búsqueda de progreso usando el nombre y la de puntajes usando el ID.

### 2. Login Híbrido (Usuario/Admin + Correo/Nombre)
Se actualizó la lógica de acceso para mayor comodidad:
- **Multi-tabla**: El sistema busca primero en `usuarios` y luego en `administrador`.
- **Multi-identificador**: Permite entrar con correo electrónico o nombre de usuario.
- **Roles**: La respuesta incluye el campo `role` para identificar el tipo de cuenta.

### 3. Modelos de Datos extendidos
- Se completó la estructura de `UserData` para incluir campos físicos (peso, altura) y de salud, alineados con la base de datos real.

## Verificación
- Se validó que la App recupere correctamente el nombre de la sesión para la petición de actividades.
- Se comprobó que el mapeo de campos en el Login sea exacto a las columnas de la tabla `administrador` (`nombre_adm`, `usuario_adm`, etc.).

> [!TIP]
> Asegúrate de actualizar ambos archivos PHP en tu servidor (`login.php` y `get_actividades.php`) con el código proporcionado para que los cambios en la App surtan efecto.
