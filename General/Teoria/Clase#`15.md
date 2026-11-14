# Servidor de Expertos con Sockets e Hilos en Java (Proyecto AVI): Resumen Académico

---

## 1. Arquitectura de la solución

* La aplicación **AVI** (chat con IA) se extiende con un **chat experto**: un humano que **valida o corrige** la respuesta de la IA.
* Se separan dos aplicaciones que se comunican por **sockets TCP**:

| Componente | Rol | Clase principal |
|---|---|---|
| **Cliente AVI** | Envía la pregunta del usuario y la respuesta de la IA al experto, y espera la respuesta | `ExpertoSocketService` |
| **Servidor del experto** | Escucha consultas, las muestra al experto y devuelve su respuesta | `ServidorExpertoService` |
| **Interfaz del experto** | Aplicación JavaFX independiente donde el experto escribe | `ExpertoApp` / `ExpertoViewController` |
| **DTO** | Transporta los datos de la consulta entre capas | `ConsultaExpertoDTO` |

* La clase `ServidorExpertoService` funciona como **reemplazo de un protocolo propio**: gracias a `ServerSocket`, que ya implementa **TCP/IP**, no es necesario programar una capa adicional.
* **Flujo completo:**
  1. El usuario pregunta y la IA responde.
  2. El usuario pulsa **"Consultar experto"**.
  3. El cliente se conecta al servidor (puerto **5000**) y envía la **pregunta** y la **respuesta de la IA** (para que el experto tenga contexto).
  4. El servidor las muestra en la interfaz del experto.
  5. El experto escribe su respuesta y la envía.
  6. El servidor la escribe en el socket y **cierra la conexión**.
  7. El cliente muestra la respuesta del experto en el historial.

---

## 2. Clases de Java utilizadas

| Clase | Función |
|---|---|
| `ServerSocket` | Abre un puerto y espera conexiones; ya incluye el protocolo TCP/IP |
| `Socket` | Representa la conexión con cada cliente |
| `DataInputStream` | Define el **espacio de entrada** de datos (lectura de mensajes recibidos) |
| `DataOutputStream` | Define el **espacio de salida** de datos (envío de respuestas) |
| `IOException` | Maneja los errores de comunicación; necesaria porque se conectan recursos **remotos** que no se pueden controlar |
| `readUTF()` / `writeUTF()` | Leen y escriben cadenas de texto de forma simétrica en el flujo |
| `flush()` | Fuerza el envío inmediato de los datos almacenados en el búfer |

---

## 3. Servidor: `ServidorExpertoService`

* Inicia un `ServerSocket` en un **puerto específico** (5000), recibe clientes (del usuario final o experto), y administra su **entrada** y **salida**.
* Mantiene como atributos la conexión **actual**: `clienteActual`, `entradaActual` y `salidaActual`.

```java
public class ServidorExpertoService {

    private final int puerto;
    private ServerSocket servidor;
    private Socket clienteActual;
    private DataInputStream entradaActual;
    private DataOutputStream salidaActual;

    public ServidorExpertoService(int puerto) {
        this.puerto = puerto;
    }

    public void iniciar() throws IOException {
        servidor = new ServerSocket(puerto);
    }

    public synchronized ConsultaExpertoDTO esperarConsulta() throws IOException {
        clienteActual = servidor.accept();
        entradaActual = new DataInputStream(clienteActual.getInputStream());
        salidaActual  = new DataOutputStream(clienteActual.getOutputStream());

        String pregunta     = entradaActual.readUTF();
        String respuestaAVI = entradaActual.readUTF();

        return new ConsultaExpertoDTO(pregunta, respuestaAVI);
    }

    public synchronized void enviarRespuesta(String respuesta) throws IOException {
        if (salidaActual == null) {
            throw new IOException("No existe ninguna consulta activa. Conexión rechazada.");
        }
        salidaActual.writeUTF(respuesta);
        salidaActual.flush();
        cerrarClienteActual();
    }

    private void cerrarClienteActual() {
        try { if (entradaActual != null) entradaActual.close(); } catch (Exception ignorado) {}
        try { if (salidaActual  != null) salidaActual.close();  } catch (Exception ignorado) {}
        try { if (clienteActual != null) clienteActual.close(); } catch (Exception ignorado) {}

        entradaActual = null;
        salidaActual  = null;
        clienteActual = null;
    }
}
```

### Método por método

| Método | Responsabilidad |
|---|---|
| `iniciar()` | Crea una nueva instancia de `ServerSocket` en el puerto definido |
| `esperarConsulta()` | **Sincronizado.** Espera con `accept()` a que llegue un cliente, abre sus flujos y lee la **última pregunta** del usuario y la **última respuesta** de la IA, empaquetándolas en un DTO que se entrega a la interfaz del experto |
| `enviarRespuesta()` | **Sincronizado.** Valida que exista una consulta activa, escribe la respuesta del experto en la salida, hace `flush()` y **libera la conexión** |
| `cerrarClienteActual()` | Cierra entrada, salida y socket (si tienen contenido) y deja todo en `null` |

### Conceptos clave

| Concepto | Significado |
|---|---|
| **Método `synchronized`** | Solo **un hilo a la vez** puede ejecutarlo; los demás esperan su turno. Protege el estado compartido (`clienteActual`, `entradaActual`, `salidaActual`) |
| **`accept()`** | Método **bloqueante**: detiene el hilo hasta que un cliente se conecte |
| **Cierre y liberación de recursos** | Se cierra cada recurso y se asigna `null` para dejar la memoria libre de nuevo |
| **Round Robin** | Algoritmo de planificación en sistemas operativos en el que los procesos o hilos se atienden por **turnos**, en orden de llegada (analogía: el orden de turnos de bateo en béisbol) |

### ¿Por qué se cierra la conexión después de cada consulta?

* **Seguridad:** evita dejar conexiones abiertas innecesariamente.
* **Memoria:** libera los objetos que ya no se usan.
* **Carga del puerto:** impide que el puerto se **sobrecargue** con demasiadas conexiones y luego no pueda responder.

---

## 4. Cliente: `ExpertoSocketService`

* Se conecta al servidor, envía las dos cadenas y **espera** la respuesta del experto.

```java
public class ExpertoSocketService {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public String consultarExperto(String pregunta, String respuestaAVI) throws IOException {
        try (
            Socket socket = new Socket(HOST, PUERTO);
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream())
        ) {
            salida.writeUTF(pregunta);      // Pregunta original
            salida.writeUTF(respuestaAVI);  // Respuesta de la IA a validar
            salida.flush();

            return entrada.readUTF();       // Bloquea hasta que responde el experto
        }
    }
}
```

> **Regla de simetría:** el **orden** de los `writeUTF` del cliente debe coincidir con el de los `readUTF` del servidor, y ambos lados deben usar el mismo tipo de flujo (`Data*Stream`). Si un lado usa `PrintWriter`/`BufferedReader` y el otro `DataInputStream`, la comunicación falla.

---

## 5. Hilos en las interfaces gráficas

* Las operaciones de red **bloquean** (`accept()`, `readUTF()`), por lo que **nunca** deben ejecutarse en el hilo de JavaFX: la ventana se congelaría.
* Cada tarea bloqueante se ejecuta en un **hilo de fondo** (daemon) y el resultado se muestra con `Platform.runLater()`.

### 5.1 Lado del cliente (`ChatViewController`)

```java
private void consultarExperto() {
    String pregunta = ultimaPregunta;
    String respuestaAVI = ultimaRespuestaAVI;

    botonConsultarExperto.setDisable(true);
    etiquetaEstadoExperto.setText("Esperando respuesta del experto...");

    Thread hiloExperto = new Thread(() -> {
        try {
            String respuestaExperto =
                    expertoSocketService.consultarExperto(pregunta, respuestaAVI);

            Platform.runLater(() -> {
                areaHistorial.appendText("\nEXPERTO: " + respuestaExperto + "\n\n");
                etiquetaEstadoExperto.setText("Validación recibida.");
                botonConsultarExperto.setDisable(false);
            });

        } catch (Exception e) {
            Platform.runLater(() -> {
                etiquetaEstadoExperto.setText("Experto no disponible.");
                botonConsultarExperto.setDisable(false);
            });
        }
    }, "Hilo-Consulta-Experto");

    hiloExperto.setDaemon(true);
    hiloExperto.start();
}
```

### 5.2 Lado del servidor (`ExpertoViewController`)

| Hilo | Tarea |
|---|---|
| `Hilo-Servidor-Experto` | Inicia el `ServerSocket` y actualiza la etiqueta de estado ("Servidor activo - Puerto 5000") |
| `Hilo-Espera-Consulta` | Llama a `esperarConsulta()` y, al recibir la consulta, muestra pregunta y respuesta de la IA, y habilita el botón de enviar |
| `Hilo-Respuesta-Experto` | Llama a `enviarRespuesta()`, limpia la pantalla y **vuelve a lanzar** `esperarSiguienteConsulta()` para atender la siguiente consulta |

```java
private void esperarSiguienteConsulta() {
    Thread hiloEspera = new Thread(() -> {
        try {
            ConsultaExpertoDTO consulta = servidor.esperarConsulta();

            Platform.runLater(() -> {
                areaPregunta.setText(consulta.getPregunta());
                areaRespuestaAVI.setText(consulta.getRespuestaAVI());
                areaRespuestaExperto.clear();
                botonEnviarRespuesta.setDisable(false);
            });
        } catch (Exception e) {
            Platform.runLater(() ->
                etiquetaEstadoExperto.setText("Error esperando consulta."));
        }
    }, "Hilo-Espera-Consulta");

    hiloEspera.setDaemon(true);
    hiloEspera.start();
}
```

* El servidor es **reactivo**: siempre está escuchando; recibe una consulta, reacciona, devuelve la respuesta y vuelve a esperar.

---

## 6. Variante: servidor de consola

* Versión inicial del servidor, sin interfaz gráfica: un `while (true)` que acepta un cliente a la vez, lee pregunta y respuesta de la IA, y obtiene la respuesta del experto con un `Scanner`.

```java
try (ServerSocket servidor = new ServerSocket(5000);
     Scanner scanner = new Scanner(System.in)) {

    while (true) {
        try (Socket cliente = servidor.accept();
             DataInputStream entrada = new DataInputStream(cliente.getInputStream());
             DataOutputStream salida = new DataOutputStream(cliente.getOutputStream())) {

            String pregunta = entrada.readUTF();
            String respuestaAVI = entrada.readUTF();

            System.out.println("Pregunta: " + pregunta);
            System.out.println("Respuesta del AVI: " + respuestaAVI);
            System.out.print("Respuesta del experto: ");

            salida.writeUTF(scanner.nextLine());
            salida.flush();
        }
    }
}
```

* Es una versión **secuencial**: atiende un cliente a la vez. La versión con interfaz gráfica y hilos mejora la experiencia de uso.

---

## 7. Servicio HTTP para el inicio de sesión

* Además de los sockets, el proyecto expone el **inicio de sesión como un servicio HTTP** que reutiliza `UsuarioService`.
* **HTTP** es un protocolo de nivel superior construido sobre TCP, basado en **solicitudes y respuestas**.

| Elemento | Función |
|---|---|
| `HttpServer` (`com.sun.net.httpserver`) | Servidor HTTP incluido en el JDK |
| `createContext("/usuario/login", handler)` | Asocia una **ruta** con un manejador |
| `HttpHandler` | Procesa cada solicitud (`handle`) |
| `Executors.newFixedThreadPool(4)` | **Grupo de 4 hilos** para atender solicitudes simultáneas |
| `POST` | Método HTTP usado para enviar credenciales en el cuerpo (no en la URL) |
| Código `200` / `400` / `405` / `500` | Éxito / solicitud inválida / método no permitido / error interno |
| **JSON** | Formato de la respuesta: `{"autenticado": true}` |

```java
servidor = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
servidor.createContext("/usuario/login", new LoginHandler());
servidor.setExecutor(Executors.newFixedThreadPool(4));
servidor.start();
```

### Cliente HTTP

```java
HttpClient cliente = HttpClient.newHttpClient();

HttpRequest solicitud = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/usuario/login"))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString(datos))
        .build();

HttpResponse<String> respuesta =
        cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
```

* Los datos viajan como `usuario=...&contrasena=...`, codificados con `URLEncoder` y decodificados en el servidor con `URLDecoder`.

---

## 8. Conclusiones

* Un **servidor con `ServerSocket`** reemplaza la necesidad de programar un protocolo propio, porque TCP/IP ya está incorporado.
* Los métodos **`synchronized`** aseguran que solo un hilo manipule la conexión actual a la vez.
* Es indispensable **cerrar y liberar** entradas, salidas y sockets tras cada consulta.
* Las tareas bloqueantes siempre se ejecutan en **hilos de fondo**, y la interfaz se actualiza con `Platform.runLater()`.
* La arquitectura es **escalable** (se pueden agregar más usuarios, servidores o servicios) y **reactiva**.

---

## 9. Resumen general del tema

* El proyecto conecta dos aplicaciones (cliente AVI y consola del experto) mediante **sockets TCP** en el puerto **5000**.
* El cliente envía la **pregunta** y la **respuesta de la IA**; el servidor devuelve la **respuesta del experto**.
* Se usan `ServerSocket`, `Socket`, `DataInputStream`, `DataOutputStream` e `IOException`.
* `esperarConsulta()` y `enviarRespuesta()` son métodos **sincronizados**; `cerrarClienteActual()` libera todos los recursos.
* Cada operación bloqueante corre en un **hilo daemon**, y los resultados se muestran con `Platform.runLater()`.
* El inicio de sesión también se ofrece como **servicio HTTP** (`HttpServer`, puerto 8080, grupo de 4 hilos, respuesta en JSON).
