# Tarea clase asíncrona

## Inicio de sesión utilizando JSON y XML

Modifique el módulo de inicio de sesión del Agente Virtual Inteligente desarrollado en Java para que los datos de los usuarios puedan almacenarse y consultarse desde dos fuentes diferentes: **JSON** y **XML**.

### La aplicación deberá:

1. Guardar la información de usuario y contraseña tanto en un archivo JSON como en un archivo XML.

2. Mostrar en la pantalla de inicio de sesión una opción que permita seleccionar la fuente de datos que se desea utilizar:
   - JSON
   - XML

3. Al presionar el botón **Iniciar sesión**, validar las credenciales consultando únicamente la fuente seleccionada.

4. Mostrar un mensaje indicando si el inicio de sesión fue exitoso o si las credenciales son incorrectas.

5. Implementar una **bitácora de inicios de sesión**. Cada vez que un usuario intente iniciar sesión, la aplicación deberá registrar como mínimo:
   - Nombre de usuario.
   - Fecha del intento de inicio de sesión.
   - Hora del intento de inicio de sesión.
   - Fuente de datos utilizada (JSON o XML).
   - Resultado del intento (exitoso o fallido).

   > La bitácora deberá almacenarse en un archivo independiente y conservar los registros anteriores, de manera que cada nuevo intento se agregue al historial existente.

6. Implementar un **control de intentos fallidos** de inicio de sesión. La aplicación deberá:
   - Contabilizar los intentos consecutivos de inicio de sesión incorrectos.
   - Mostrar al usuario la cantidad de intentos fallidos realizados.
   - Bloquear temporalmente nuevos intentos de inicio de sesión cuando se alcancen 3 intentos fallidos consecutivos.
   - Reiniciar el contador cuando se realice un inicio de sesión exitoso.

---

**Objetivo:** practicar en Java la lectura, escritura y manejo de información utilizando diferentes formatos de almacenamiento de datos.
