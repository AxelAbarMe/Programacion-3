
# 1

Una tabla CONTACTO guarda varios números telefónicos separados por comas dentro de la columna telefonos. ¿Qué modificación se ajusta mejor a la primera forma normal (1FN)?

* a. Ordenar alfabéticamente los teléfonos dentro de la misma celda.
* b. Guardar cada número como valor individual en una tabla relacionada con CONTACTO.
* c. Sustituir la clave primaria por la columna telefonos.
* d. Almacenar todos los teléfonos en un único campo de texto más grande.

# 2

La tabla MATRICULA tiene como clave primaria compuesta (id_estudiante, id_curso). También almacena nombre_curso, que depende únicamente de id_curso. ¿Qué problema de normalización se observa?

* a. Una dependencia transitiva propia exclusivamente de la tercera forma normal.
* b. Un problema de tipos de datos causado por la clave primaria compuesta.
* c. Una dependencia parcial que impide cumplir la segunda forma normal.
* d. Una violación de integridad referencial por una clave foránea inexistente.

---

# 3

En una tabla PEDIDO se almacenan id_pedido, id_ciudad y nombre_ciudad. Cada id_ciudad identifica un único nombre_ciudad. ¿Qué rediseño permite eliminar la dependencia transitiva?

* a. Duplicar nombre_ciudad en una segunda columna del mismo PEDIDO.
* b. Crear una tabla CIUDAD con id_ciudad y nombre_ciudad, y conservar id_ciudad como referencia en PEDIDO.
* c. Convertir nombre_ciudad en la clave primaria de PEDIDO.
* d. Eliminar la columna id_ciudad y copiar nombre_ciudad en todos los pedidos.

---

# 4

Una aplicación requiere que el correo electrónico de cada cuenta no se repita, aunque el identificador numérico ya sea clave primaria. ¿Qué restricción es la más adecuada para correo?

* a. AUTO_INCREMENT.
* b. DEFAULT.
* c. UNIQUE.
* d. FOREIGN KEY sin una tabla referenciada.

---

# 5

¿Qué representa el resultado de la siguiente consulta SQL?
`SELECT COUNT(*) FROM empleado WHERE activo = 1;`

* a. El número de columnas que tiene la tabla empleado.
* b. La suma de los identificadores de los empleados activos.
* c. Una lista con todas las columnas de los empleados activos.
* d. La cantidad de filas de empleado cuyo campo activo vale 1.

---

# 6

¿Para qué se incluye la siguiente instrucción al inicio de una clase Java que trabaja con SQL?
`import java.sql.PreparedStatement;`

* a. Para convertir cada consulta SQL en un procedimiento almacenado.
* b. Para poder referirse al tipo PreparedStatement por su nombre simple en el archivo Java.
* c. Para abrir automáticamente una conexión con MySQL.
* d. Para crear las tablas de la base de datos al iniciar la clase.

---

# 7

En una consulta JDBC que contiene dos marcadores de posición ?, ¿qué hace la siguiente instrucción?
`sentencia.setString(1, correo);`

* a. Crea una nueva columna llamada correo de tipo VARCHAR.
* b. Sustituye el nombre de la primera tabla incluida en la consulta.
* c. Asigna el correo a la primera fila obtenida en el ResultSet.
* d. Asocia el valor de correo con el primer marcador ? de la sentencia preparada.

---

# 8

¿Cuál es la principal ventaja de usar try-with-resources al trabajar con una conexión JDBC?
`try (Connection conexion = abrirConexion()) { /* operaciones */ }`

* a. Convierte la conexión en una transacción confirmada de forma automática.
* b. Permite compartir la conexión sin restricciones entre todas las aplicaciones.
* c. La conexión se cierra automáticamente al salir del bloque, incluso ante una excepción.
* d. Evita que la consulta SQL pueda devolver cero registros.

---

# 9

Un servidor web atiende numerosas consultas a una base de datos y el establecimiento de cada conexión resulta costoso. ¿Qué estrategia ayuda a reducir ese costo?

* a. Abrir una conexión nueva para cada campo recuperado.
* b. Eliminar las restricciones de integridad para acelerar la autenticación.
* c. Guardar todas las consultas SQL como archivos de texto.
* d. Mantener un pool de conexiones reutilizables.

---

# 10

Una tarea se ejecuta en un hilo de fondo y necesita cambiar el texto de una etiqueta JavaFX. ¿Qué logra el fragmento siguiente?
`Platform.runLater(() -> etiqueta.setText("Listo"));`

* a. Programa la actualización para que se ejecute en el hilo de aplicación de JavaFX.
* b. Ejecuta obligatoriamente la actualización dentro del mismo hilo de fondo.
* c. Crea un proceso independiente del sistema operativo para dibujar la etiqueta.
* d. Detiene la aplicación hasta que el usuario presione la etiqueta.

---

# 11

Si un archivo Java contiene una llamada a Platform.runLater(...), ¿qué import hace accesible la clase Platform de JavaFX por su nombre simple?

* a. import java.lang.Thread;
* b. import java.net.Socket;
* c. import javafx.application.Platform;
* d. import java.sql.Connection;

---

# 12

Se configura un hilo mediante la instrucción hilo.setDaemon(true) antes de iniciarlo. ¿Qué implica esta configuración?
`hilo.setDaemon(true);`

* a. Ese hilo no mantiene la JVM en ejecución por sí solo cuando ya no quedan hilos no daemon.
* b. El hilo tendrá prioridad máxima garantizada.
* c. El hilo queda protegido automáticamente frente a condiciones de carrera.
* d. El hilo se ejecutará siempre antes que el hilo principal.

---

# 13

Una aplicación gráfica descarga un archivo grande. ¿Por qué conviene ejecutar la descarga en segundo plano y mantener la interfaz en su hilo correspondiente?

* a. Para convertir una descarga de red en una consulta SQL.
* b. Para permitir que la interfaz siga atendiendo acciones del usuario mientras continúa la descarga.
* c. Para garantizar que la red transmita datos sin errores.
* d. Para evitar por completo el uso de memoria RAM.

---

# 14

¿Qué efecto tiene declarar el método de instancia mostrado como synchronized cuando varios hilos usan el mismo objeto?
`public synchronized void guardar() { /* modifica estado */ }`

* a. Impide que se ejecuten otros métodos en objetos distintos de la misma clase.
* b. Hace que la ejecución del método ocurra exclusivamente en el hilo de JavaFX.
* c. Impide que dos hilos ejecuten simultáneamente ese método sincronizado sobre la misma instancia.
* d. Garantiza que el método nunca lance excepciones.

---

# 15

En Java, ¿qué papel cumple la expresión lambda en el siguiente fragmento?
`Thread trabajador = new Thread(() -> generarReporte());`

* a. Proporciona el comportamiento de una tarea Runnable que ejecutará el hilo.
* b. Convierte el hilo en un socket TCP automáticamente.
* c. Indica el nombre de la tabla SQL que usará el hilo.
* d. Ejecuta generarReporte en el constructor como un método estático.

---

# 16

Dos hilos incrementan muchas veces una variable entera compartida usando contador++. Al finalizar, el valor es inferior al esperado. ¿Cuál es la explicación más probable?

* a. Todos los incrementos se convierten automáticamente en decrementos.
* b. El tipo int no admite incrementos dentro de un hilo.
* c. La operación de incremento involucra pasos que pueden intercalarse entre hilos y perder actualizaciones.
* d. Cada hilo posee obligatoriamente una copia independiente de toda variable compartida.

---

# 17

Un servidor utiliza el siguiente executor. Si recibe 10 tareas que tardan en finalizar, ¿qué comportamiento es el esperado?
`Executors.newFixedThreadPool(4);`

* a. Hasta cuatro tareas se ejecutan simultáneamente en el pool y las demás esperan su turno.
* b. Se crean exactamente diez hilos nuevos de forma simultánea.
* c. Solo se puede ejecutar una tarea durante toda la vida del servidor.
* d. Se descartan inmediatamente todas las tareas después de la cuarta.

---

# 18

Un sistema debe transferir un documento íntegro, respetando el orden de los datos. ¿Qué protocolo se ajusta mejor a ese requisito?

* a. TCP, porque ofrece un flujo confiable y ordenado.
* b. DNS, porque establece una conexión para cada byte enviado.
* c. ICMP, porque almacena documentos en una base de datos.
* d. UDP, porque garantiza que todos los datagramas lleguen en el mismo orden.

---

# 19

Una aplicación de audio en tiempo real prioriza una latencia baja y puede tolerar la pérdida ocasional de paquetes. ¿Qué opción suele ser apropiada?

* a. Usar exclusivamente un sistema de archivos local sin comunicación por red.
* b. Usar solo JDBC porque transporta audio en tiempo real.
* c. Usar DNS como protocolo de streaming continuo.
* d. Usar UDP y gestionar, si se requiere, la recuperación o tolerancia a pérdidas en la aplicación.

---

# 20

Un servidor TCP funciona en su propia computadora, pero los clientes de otra red no consiguen conectarse al puerto configurado. ¿Cuál es una revisión de infraestructura pertinente?

* a. Comprobar las reglas del firewall y la accesibilidad de red hacia ese puerto.
* b. Agregar un import de JDBC a cada clase cliente.
* c. Reemplazar todos los hilos Java por variables locales.
* d. Cambiar la clave primaria de todas las tablas de la base de datos.

---

# 21

Un programa envía una cadena por un socket usando DataOutputStream.writeUTF(texto). ¿Qué operación es la contraparte compatible para leerla desde el otro extremo?
`salida.writeUTF(texto);`

* a. BufferedReader.readLine() sin ninguna conversión ni acuerdo adicional.
* b. DataInputStream.readUTF().
* c. ServerSocket.accept() para recuperar directamente el texto.
* d. ObjectInputStream.readObject() para cualquier String enviado mediante writeUTF.

---

# 22

El cliente envía primero un identificador y luego una descripción, ambos con writeUTF. ¿Cómo debe leerlos el servidor para interpretar correctamente el protocolo acordado?
`salida.writeUTF(identificador); salida.writeUTF(descripcion);`

* a. Usar una única llamada a readUTF y asumir que combina todos los envíos.
* b. Realizar dos llamadas a readUTF en el mismo orden: primero identificador y luego descripción.
* c. Invocar accept después de cada cadena para recibir el segundo valor.
* d. Leer la descripción primero porque los sockets invierten automáticamente el orden.

---

# 23

Si un cliente Java utiliza la dirección localhost al conectarse a un servicio TCP, ¿a qué equipo apunta esa dirección?
`new Socket("localhost", 5000);`

* a. Al mismo equipo donde se está ejecutando ese cliente.
* b. Siempre al servidor público de la universidad.
* c. Al router de cualquier red Wi-Fi disponible.
* d. A la última computadora que aceptó una conexión TCP.

---

# 24

En una solicitud HTTP creada con HttpRequest, ¿cuál es la función de BodyPublishers.ofString(datos) cuando se usa en el método POST?
`.POST(HttpRequest.BodyPublishers.ofString(datos))`

* a. Escribir directamente datos en un control JavaFX.
* b. Aportar la cadena datos como cuerpo de la petición HTTP.
* c. Convertir la petición HTTP en un ServerSocket.
* d. Abrir una conexión JDBC utilizando los valores de datos.

---

# 25

Un servicio HTTP acepta exclusivamente solicitudes POST para un recurso. ¿Qué indica normalmente el código de estado HTTP 405 cuando recibe una solicitud GET a ese recurso?

* a. Que el método HTTP usado no está permitido para ese recurso.
* b. Que el servidor ha redirigido permanentemente el recurso.
* c. Que el cliente escribió una contraseña incorrecta, independientemente del servicio.
* d. Que la respuesta fue exitosa y contiene el recurso solicitado.