# AVI — Inicio de sesión con JSON y XML

Proyecto JavaFX adaptado para practicar persistencia en dos formatos y separación por capas.

## Credenciales de prueba

- Usuario: `admin`
- Contraseña: `1234`
- Fuente: `JSON` o `XML`

Los datos están en:

- `data/usuarios.json`
- `data/usuarios.xml`

Si alguno de esos archivos no existe, la aplicación lo crea automáticamente con el usuario de prueba.

## Capas utilizadas

- `dto/UsuarioDTO.java`: transporta los datos del usuario entre capas.
- `datos/UsuarioJsonDatos.java`: lee y escribe JSON.
- `datos/UsuarioXmlDatos.java`: lee y escribe XML.
- `service/UsuarioService.java`: comunica la lógica con la fuente seleccionada.
- `logica/LoginLogica.java`: valida reglas del inicio de sesión.
- `controllers/LoginViewController.java`: recibe los eventos de la interfaz y llama a la lógica.
- `ui/login-view.fxml`: agrega el selector JSON/XML.

## Ejecutar

Requiere JDK 17+ y Maven.

```bash
mvn javafx:run
```

La variable `GEMINI_API_KEY` continúa siendo necesaria únicamente para usar el chat contra Gemini.

> Nota académica: las contraseñas se guardan en texto plano porque el ejercicio solicita practicar lectura y escritura de JSON/XML. En una aplicación real deberían almacenarse mediante hash seguro, no como texto plano.
