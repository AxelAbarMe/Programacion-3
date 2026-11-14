# Resumen Parcial 2: Bases de Datos, Hilos, Sockets y HTTP en Java

---

## PARTE I. BASES DE DATOS RELACIONALES

### 1. Modelo relacional de Codd

* Una **base de datos relacional** es un conjunto organizado de datos representados mediante **relaciones (tablas)** que pueden vincularse entre sí usando **claves**.
* Organiza la información en filas y columnas, define relaciones explícitas, aplica **reglas de integridad** y permite consultar y modificar datos mediante **SQL**.

| Concepto | Definición |
|---|---|
| **Relación** | Una tabla que representa un conjunto de datos del mismo tipo |
| **Tupla** | Una fila de la relación: una ocurrencia o registro |
| **Atributo** | Una columna que describe una propiedad del dato |
| **Dominio** | Conjunto de valores válidos que puede tomar un atributo (tipo y restricciones) |

* La **posición** de filas y columnas **no define el significado**: lo hacen los nombres, dominios y relaciones.
* Los datos de las tablas no siempre se repiten; las tablas deben **relacionarse entre sí**. Una tabla que no se relaciona con ninguna otra sobra.

### 2. Claves

| Clave | Sigla | Función | Ejemplo |
|---|---|---|---|
| **Clave primaria** | PK | Identifica de forma **única** cada fila; no admite nulos ni repetidos | `id_usuario` |
| **Clave foránea** | FK | Relaciona una tabla con otra; debe apuntar a una fila **existente** | `id_usuario` en `conversaciones` |
| **Clave única** | UQ | Evita valores **duplicados** en un atributo que no es la PK | `nombre_usuario`, `correo` |

* En una relación **1:N**, la clave foránea siempre va en el lado **N**.
* Una tabla puede tener una PK numérica autoincremental y además un atributo `UNIQUE` (por ejemplo, un correo que no debe repetirse).

### 3. Reglas de integridad

* Su responsabilidad es garantizar la **consistencia** del dato: un dato no debería cambiar salvo mediante cambios controlados.

| Integridad | Regla |
|---|---|
| **De entidad** | La clave primaria no puede ser nula |
| **Referencial** | Toda FK debe apuntar a una fila existente |
| **De dominio** | Cada atributo respeta su tipo y restricciones |

### 4. Relaciones entre tablas

| Tipo | Descripción | Ejemplo |
|---|---|---|
| **1:1** | Un registro se relaciona con uno solo | `USUARIO` – `PERFIL` |
| **1:N** | Un registro se relaciona con muchos | `USUARIO` – `CONVERSACIÓN` |
| **N:M** | Muchos registros se relacionan con muchos | `ESTUDIANTE` – `CURSO` |

* Las relaciones se implementan mediante **claves foráneas**.
* Una relación **N:M** requiere una **tabla intermedia** con las FK de ambas tablas (ejemplo: `NOMBRAMIENTO` entre `FUNCIONARIO` y `PUESTO`).
* **Modelo del proyecto AVI:** `USUARIOS` (1:N) `CONVERSACIONES` (1:N) `MENSAJES`. Un usuario tiene muchas conversaciones y cada conversación contiene muchos mensajes.

### 5. Modelo de objetos vs. modelo relacional

| Aspecto | Modelo de objetos | Modelo relacional |
|---|---|---|
| **Unidad principal** | Objeto / instancia | Tupla dentro de una relación |
| **Estructura** | Atributos + métodos | Atributos definidos por columnas |
| **Identidad** | Referencia / identidad del objeto | Clave primaria |
| **Relaciones** | Referencias entre objetos | Claves foráneas |
| **Herencia** | Natural en POO | No forma parte directa del modelo |
| **Colecciones** | `List`, `Set`, `Map` | Filas relacionadas en otras tablas |
| **Comportamiento** | Incluido en clases y métodos | Los datos no incorporan comportamiento |

* **Desajuste objeto-relacional:** Java y el modelo relacional representan la información de forma distinta; integrarlos exige **transformar estructuras** (**mapeo**). **JDBC** es el puente inicial entre ambos modelos.

### 6. Normalización

* Es un **proceso de diseño** que organiza los datos para **reducir redundancia** y evitar **inconsistencias**.
* **Objetivos:** evitar almacenar el mismo dato innecesariamente, reducir anomalías, representar dependencias con claridad y facilitar el mantenimiento y la integridad.

#### Anomalías de una tabla sin normalizar

| Anomalía | Descripción |
|---|---|
| **De actualización** | Si cambia un dato (ej. nombre del usuario), hay que modificar **varias filas** |
| **De inserción** | Puede ser difícil registrar un dato sin tener otro (ej. una conversación sin mensaje) |
| **De eliminación** | Al borrar un registro se puede **perder información** (ej. al borrar el último mensaje se pierde la conversación) |

#### Primera Forma Normal (1FN)

* Cada atributo contiene **valores atómicos** (indivisibles) y **no existen grupos repetitivos**.
* **Incumple 1FN:** una columna `telefonos` con `8888-1111, 8777-2222`.
* **Solución:** guardar cada teléfono como un valor individual en una **tabla relacionada** (una fila por teléfono, enlazada por `id_usuario`).
* Ordenar los valores, ampliar el campo de texto o cambiar la PK **no** resuelven el problema.

#### Segunda Forma Normal (2FN)

* Requiere estar en 1FN y que **cada atributo no clave dependa de la clave primaria completa** (aplica a **claves compuestas**).
* **Dependencia parcial:** un atributo depende solo de **una parte** de la clave compuesta.
* **Ejemplo:** `MATRICULA(id_estudiante, id_curso, nombre_estudiante, nombre_curso, nota)`: `nombre_estudiante` depende solo de `id_estudiante` y `nombre_curso` depende solo de `id_curso`.
* **Solución:** separar `ESTUDIANTES` y `CURSOS`; la tabla de matrícula conserva solo los atributos que dependen de **ambos** IDs (como `nota`).

#### Tercera Forma Normal (3FN)

* Requiere estar en 2FN y **eliminar dependencias transitivas** entre atributos no clave.
* **Dependencia transitiva:** `id_usuario → id_tipo → tipo_usuario`; `tipo_usuario` depende de `id_tipo`, no directamente de la PK.
* **Ejemplo:** `PEDIDO(id_pedido, id_ciudad, nombre_ciudad)`.
* **Solución:** crear una tabla `CIUDAD(id_ciudad, nombre_ciudad)` y conservar solo `id_ciudad` como **referencia (FK)** en `PEDIDO`.

| Forma normal | Problema que elimina | Se corrige con |
|---|---|---|
| **1FN** | Valores múltiples / grupos repetitivos en una celda | Tabla relacionada, un valor por fila |
| **2FN** | Dependencia **parcial** de la clave compuesta | Separar en tablas por cada parte de la clave |
| **3FN** | Dependencia **transitiva** entre atributos no clave | Tabla aparte + clave foránea |

#### Normalizar con criterio

| Beneficios | Costo práctico |
|---|---|
| Reduce duplicación | Más tablas |
| Facilita actualizaciones | Más `JOIN` |
| Protege la integridad | Consultas más complejas |
| Aclara dependencias | Puede requerir optimización |

* Normalizar **no** significa dividir cada dato en una tabla distinta. **Regla del curso:** diseñar correctamente hasta **3FN** y desnormalizar solo con una razón técnica medible.
* **Mal diseño:** tablas sobrecargadas que mezclan entidades distintas (ubicación, contacto, historia dentro de `Equipos`). **Buen diseño:** una entidad por tabla, con PK y FK.

---

## PARTE II. SQL

### 7. Tipos de sentencias

| Tipo | Función | Ejemplos |
|---|---|---|
| **DDL** (*Data Definition Language*) | Define estructuras | `CREATE TABLE`, `DROP TABLE` |
| **DML** (*Data Manipulation Language*) | Manipula los datos | `SELECT`, `INSERT`, `UPDATE`, `DELETE` |

### 8. Creación de tablas y restricciones

```sql
CREATE TABLE usuario (
    pk_usuario INT AUTO_INCREMENT,
    nombre_usuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT id_usuario PRIMARY KEY (pk_usuario),
    CONSTRAINT uq_usuario UNIQUE (nombre_usuario)
);
```

| Restricción / elemento | Significado |
|---|---|
| `AUTO_INCREMENT` | El motor genera automáticamente el siguiente número; no se envía en el `INSERT` |
| `NOT NULL` | La columna no puede quedar vacía |
| `DEFAULT` | Valor asignado cuando no se especifica uno al insertar |
| `UNIQUE` | Impide valores repetidos (adecuado para un correo o nombre de usuario) |
| `PRIMARY KEY` | Identifica de forma única cada fila; sin nulos ni duplicados |
| `FOREIGN KEY` | Relaciona con la PK de otra tabla (la tabla referenciada debe existir) |
| `CONSTRAINT nombre` | Da un **nombre** a la regla (convención: `pk_`, `uq_`, `fk_`) |
| `CURRENT_TIMESTAMP` | Devuelve la fecha y hora actuales del servidor |
| `VARCHAR(n)` | Texto de longitud variable con un máximo de `n` caracteres |
| `BOOLEAN` | Alias de `TINYINT(1)`: `TRUE` = 1, `FALSE` = 0 |

* Una contraseña debe almacenarse con **hash** (no en texto plano) y la columna necesita **más de 15 caracteres** (por eso `VARCHAR(255)`).

### 9. Operaciones CRUD

| Operación | Sentencia | Ejemplo |
|---|---|---|
| **C**reate | `INSERT INTO` | `INSERT INTO usuario (nombre_usuario, contrasena) VALUES ('admin','1234');` |
| **R**ead | `SELECT` | `SELECT * FROM usuario WHERE activo = TRUE;` |
| **U**pdate | `UPDATE` | `UPDATE usuario SET activo = TRUE WHERE pk_usuario = 1;` |
| **D**elete | `DELETE` | `DELETE FROM usuario WHERE pk_usuario = 3;` |

* `INSERT`: solo se indican las columnas obligatorias sin valor por defecto; las demás se llenan solas. Un valor repetido en una columna `UNIQUE` produce el **Error 1062 (Duplicate entry)**.
* `SELECT *` trae **todas las columnas**; es preferible pedir solo las necesarias. `WHERE` filtra filas.
* **`UPDATE` y `DELETE` siempre deben llevar `WHERE`**; sin él afectan **todas** las filas. `DELETE` borra filas, no la tabla (`DROP TABLE` elimina la tabla).
* **Safe Updates Mode** de Workbench bloquea `UPDATE`/`DELETE` sin `WHERE` sobre una clave (**Error 1175**).

#### Funciones de agregación

```sql
SELECT COUNT(*) FROM empleado WHERE activo = 1;
```

* `COUNT(*)` devuelve la **cantidad de filas** que cumplen la condición (no la suma de IDs, ni el número de columnas, ni la lista de datos).

### 10. Unión de tablas

```sql
SELECT u.id_usuario, u.nombre, c.titulo, m.emisor, m.contenido
FROM usuario u, conversacion c, mensaje m
WHERE u.id_usuario = c.id_usuario
  AND c.id_conversacion = m.id_conversacion;
```

* Los **alias** (`u`, `c`, `m`) son nombres cortos para las tablas.
* La condición de unión compara la **FK de una tabla con la PK de otra** (con un solo `=`).
* `AND` agrega filtros (por ejemplo, un usuario específico).

### 11. Procedimientos almacenados

* Bloque de código SQL guardado en la base de datos; equivale a un **método** en Java y **evita repetir código**. Se nombra con prefijo **`SP_`**.

| Elemento | Función |
|---|---|
| `PROCEDURE` | Palabra reservada para crearlo |
| `IN` | Parámetro de **entrada** |
| `OUT` | Parámetro de **salida** |
| `BEGIN` ... `END` | Equivalen a las llaves `{ }` |
| `DELIMITER //` | Cambia el delimitador para enviar el bloque completo; se restablece con `DELIMITER ;` |
| `CALL` | Invoca el procedimiento |
| `INTO` | Guarda el resultado de un `SELECT` en una variable o parámetro |
| `@variable` | Variable de sesión que recibe un parámetro `OUT` |

```sql
DELIMITER //
CREATE PROCEDURE SP_VALIDAR_LOGIN(
    IN p_usuario VARCHAR(50),
    IN p_contrasena VARCHAR(255),
    OUT p_valido BOOLEAN
)
BEGIN
    SELECT COUNT(*) > 0 INTO p_valido
    FROM usuarios
    WHERE usuario = p_usuario AND contrasena = p_contrasena AND activo = TRUE;
END //
DELIMITER ;

CALL SP_VALIDAR_LOGIN('admin', '1234', @resultado);
SELECT @resultado AS resultado_login;
```

* **Ventaja:** desde Java se invoca con **una sola línea** (`CALL ...`).

### 12. Funciones (`FUNCTION`)

| Característica | Procedimiento almacenado | Función |
|---|---|---|
| **Retorno** | No necesariamente | **Siempre retorna un valor** |
| **Uso** | Manipular o insertar datos existentes | Calcular un valor derivado (ej. edad a partir de la fecha de nacimiento) |
| **Se invoca con** | `CALL` | Dentro de un `SELECT` |

* Elementos: `RETURNS tipo`, `DETERMINISTIC` (devuelve un único dato), `DECLARE` (variable local), `RETURN`, `AS alias` (nombre de la columna resultado).
* Ejemplos: `FN_TOTAL_MENSAJES(id)` usa `COUNT(*)`; `FN_ULTIMO_MENSAJE(id)` usa `MAX(id_mensaje)` (el ID mayor es el último por ser autoincremental).

### 13. Triggers (disparadores)

* Código que se ejecuta **automáticamente** ante un evento en una tabla (acción-reacción).

| Elemento | Función |
|---|---|
| `BEFORE INSERT` | Se ejecuta **antes** de insertar |
| `AFTER INSERT` | Se ejecuta **después** de insertar |
| `FOR EACH ROW` | Se aplica a **cada fila** afectada (importante en inserciones masivas) |
| `NEW` | Referencia a la fila que se inserta |

* Se ejecutan sin importar si el dato entra por un `INSERT` normal o por un procedimiento almacenado.
* Ejemplos: `BEFORE INSERT` para dejar el usuario en minúsculas sin espacios (`LOWER(TRIM(NEW.usuario))`); `AFTER INSERT` en mensajes para actualizar la fecha de última modificación de la conversación.

### 14. Seguridad en la base de datos

* **Usuario de aplicación** (`avi_app`) distinto de `root`, con `CREATE USER` y `GRANT SELECT, INSERT, UPDATE, DELETE ON avi_db.*`.
* **Principio de mínimo privilegio:** la aplicación recibe solo los permisos necesarios; no puede crear ni eliminar tablas, lo que limita el daño si se filtran las credenciales.
* Las credenciales escritas en el código ("quemadas") son mala práctica.

---

## PARTE III. ACCESO A DATOS DESDE JAVA

### 15. Métodos de acceso a datos

* Permiten que las aplicaciones interactúen con bases de datos mediante **conexiones, controladores, operaciones CRUD y mapeo**.

#### Conectores (drivers)

* Software intermedio que **traduce** las solicitudes de la aplicación a comandos que entiende la base de datos.
* Tipos: **JDBC** (*Java Database Connectivity*) y **ODBC** (*Open Database Connectivity*).
* Garantizan **compatibilidad** entre distintos motores (MySQL, Oracle, SQL Server, PostgreSQL).
* **Dependencias de conexión:** las conexiones de sistema usan configuraciones predefinidas (ej. **DSN** en ODBC); las dependientes de la plataforma requieren drivers específicos. Diferencia clave: **flexibilidad vs. estandarización**.

#### Administración de conexiones

| Estrategia | Descripción |
|---|---|
| **Conexiones individuales** | Se abren y cierran para cada consulta; **costoso** en rendimiento |
| **Pool de conexiones** | Conjunto de conexiones **reutilizables**; mejora el desempeño y reduce la carga |

* Si establecer cada conexión es costoso, la estrategia es **mantener un pool de conexiones reutilizables**.
* Herramientas: servidores de aplicaciones y frameworks ORM (Hibernate, Entity Framework).

#### Mapeo de datos (ORM)

* Transforma datos entre la base de datos y **objetos** del código; reduce la necesidad de escribir SQL manual.
* Ejemplos: **Hibernate** (Java), **Entity Framework** (.NET).
* Ventajas: mayor productividad, código más limpio y mantenible, independencia relativa del motor.

### 16. JDBC en la práctica

| Elemento | Función |
|---|---|
| `import java.sql.PreparedStatement;` | Permite usar el tipo por su **nombre simple** en el archivo (un `import` no abre conexiones ni crea tablas) |
| `DriverManager.getConnection(URL, USER, PASSWORD)` | Crea la conexión |
| `Connection` | Conexión activa con la base de datos |
| `PreparedStatement` | Sentencia SQL precompilada con parámetros `?` |
| `setString(1, valor)` | Asocia el valor con el **primer marcador `?`** (índices desde 1) |
| `executeQuery()` | Ejecuta un `SELECT` y devuelve un `ResultSet` |
| `ResultSet` | Conjunto de filas devuelto |
| `resultado.next()` | `true` si existe al menos una fila (credenciales válidas) |
| `SQLException` | Error de acceso a datos |

* **URL de conexión:** `jdbc:mysql://localhost:3306/avi_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` (protocolo, servidor y puerto 3306, base de datos y parámetros).
* **Ventajas de `PreparedStatement`:** **previene inyección SQL** (los valores se envían como parámetros), permite reutilizar la sentencia y mejora legibilidad.
* **`try-with-resources`:** **cierra automáticamente** los recursos (`Connection`, `PreparedStatement`, `ResultSet`) al salir del bloque, **incluso si ocurre una excepción**.
* **Clase utilitaria** (`ConexionBD`): constructor privado y método estático para impedir su instanciación.

### 17. Arquitectura en capas

| Capa | Responsabilidad |
|---|---|
| **Vista / JavaFX** | Captura datos del usuario |
| **Controller** | Procesa eventos de la interfaz |
| **Servicios** | Coordina solicitudes entre capas |
| **Lógica** | Aplica reglas de negocio |
| **Datos / JDBC** | Ejecuta SQL y transforma el `ResultSet` |
| **MySQL** | Persiste los datos |
| **DTO** | Transporta datos entre capas |

* Cada capa se comunica con la contigua. Migrar de archivos (XML/JSON) a MySQL requirió cambiar solo **una o dos líneas por capa**, lo que demuestra:
  * **Escalabilidad:** ampliar, quitar o modificar funcionalidades sin reescribir grandes partes del código.
  * **Mantenibilidad:** facilidad para corregir o modificar el código con poco esfuerzo.

---

## PARTE IV. HILOS (THREADS)

### 18. Clases activas, procesos e hilos

* **Clase activa:** mantiene su **propia ejecución independiente** y contiene uno o más hilos que corren en paralelo (ejemplo: un servidor que escucha peticiones).

| Concepto | Definición |
|---|---|
| **Proceso** | Programa en ejecución con **su propio espacio de memoria** y recursos |
| **Hilo** | Subtarea dentro de un proceso; **comparte la memoria** del proceso |

### 19. Creación de hilos

| Forma | Características |
|---|---|
| **Extender `Thread`** | Directa, pero **limita la herencia** (solo una clase padre) |
| **Implementar `Runnable`** | **Más flexible y recomendada**; permite heredar de otra clase |

```java
Thread hilo = new Thread(() -> generarReporte());  // la lambda es la tarea Runnable
hilo.start();
```

* La **expresión lambda** proporciona el comportamiento de una tarea **`Runnable`** que ejecutará el hilo.
* El código corre en un hilo nuevo solo con **`start()`**; llamar a `run()` directamente lo ejecuta en el hilo actual.

### 20. Ciclo de vida y métodos

| Estado | Descripción |
|---|---|
| **Nuevo** | Se crea el objeto del hilo |
| **Ejecutable** | Se llamó a `start()` |
| **En ejecución** | `run()` se está ejecutando |
| **Bloqueado / en espera** | Espera recursos o eventos |
| **Terminado** | `run()` finaliza |

| Método | Función |
|---|---|
| `start()` | Inicia el hilo |
| `sleep(ms)` | Pausa el hilo |
| `join()` | Espera a que otro hilo termine |
| `interrupt()` | Solicita la interrupción |
| `isAlive()` | Indica si sigue en ejecución |

### 21. Hilos demonio (daemon)

* Hilo de servicio en segundo plano: **no mantiene la JVM en ejecución por sí solo** cuando ya no quedan hilos no daemon.
* Se configura con `setDaemon(true)` **antes** de `start()`.
* **No** otorga prioridad máxima, **no** protege de condiciones de carrera y **no** garantiza orden de ejecución.

### 22. Sincronización

* **Condición de carrera:** varios hilos acceden al mismo recurso y el resultado depende del orden impredecible de ejecución.
* **Ejemplo:** dos hilos hacen `contador++` muchas veces y el resultado es menor al esperado, porque el incremento involucra **varios pasos (leer, sumar, escribir) que pueden intercalarse** y se **pierden actualizaciones**.
* **Sección crítica:** código que solo un hilo debe ejecutar a la vez.
* **`synchronized`:** impide que **dos hilos ejecuten simultáneamente ese método sobre la misma instancia** (no bloquea otros objetos, no fuerza el hilo de JavaFX y no evita excepciones).

### 23. Comunicación entre hilos

* Mediante **variables comunes** y los métodos `wait()` (suspende), `notify()` (despierta a un hilo) y `notifyAll()` (despierta a todos).
* **Productor-consumidor:** un hilo produce datos en una cola (`BlockingQueue`, `LinkedBlockingQueue`) y otro los consume; `take()` espera si la cola está vacía.
* **Comunicación entre componentes locales:** eventos (`ActionEvent`, `MouseEvent`), colas de mensajes y sockets locales (`localhost`).

### 24. Hilos en JavaFX

* La interfaz corre en el **hilo de aplicación de JavaFX**. Las tareas lentas (descargas, IA, base de datos, red) **no deben ejecutarse allí** porque la ventana se congelaría.
* Se ejecutan en un **hilo de fondo** para que la interfaz **siga atendiendo las acciones del usuario**.

```java
Platform.runLater(() -> etiqueta.setText("Listo"));
```

* **`Platform.runLater()`** programa el código para que se ejecute en el **hilo de aplicación de JavaFX** (el único autorizado para modificar controles visuales).
* Import: **`javafx.application.Platform`**.
* **Manejo de errores con hilos:** informar el error al usuario y registrarlo en la bitácora de la base de datos en paralelo; el usuario espera solo el tiempo del proceso más largo.

### 25. Pool de hilos (`Executors`)

```java
Executors.newFixedThreadPool(4);
```

* Crea un grupo de **4 hilos**: con 10 tareas, **hasta 4 se ejecutan simultáneamente y las demás esperan su turno** (no se crean 10 hilos ni se descartan tareas).

### 26. Consideraciones, ventajas y desventajas

| Ventajas | Desventajas y riesgos |
|---|---|
| Mejor uso de la CPU | Complejidad en la depuración |
| Programas más responsivos | Problemas de sincronización y bloqueos |
| Paralelizar tareas intensivas | Consumo de recursos si se crean muchos hilos |
| Ideal para servidores concurrentes | Dificultad para mantener el código |

* Es esencial **cerrar correctamente** los hilos al terminar.
* **Round Robin:** planificación por turnos en orden de llegada (sistemas operativos).

---

## PARTE V. SOCKETS

### 27. Concepto de socket

* **Socket:** punto final de comunicación entre dos programas, en la misma o en distintas máquinas; es la base de la comunicación **cliente-servidor**.
* Componentes: **IP** (identifica el dispositivo), **puerto** (identifica el servicio o aplicación) y **protocolo** (TCP/UDP). La combinación **(IP, puerto, protocolo)** identifica un socket único.

### 28. TCP vs. UDP

| Aspecto | **TCP** (Stream Socket) | **UDP** (Datagram Socket) |
|---|---|---|
| **Conexión** | Orientada a conexión | Sin conexión |
| **Confiabilidad** | **Confiable**: flujo continuo, **ordenado** y sin pérdidas | **Sin garantía** de entrega ni de orden |
| **Velocidad** | Más lento | **Rápido**, baja latencia |
| **Usos** | Correo, web, **transferir un documento íntegro** | Juegos en línea, videollamadas, **audio en tiempo real** (tolera pérdida ocasional) |

* **DNS** e **ICMP** no son los protocolos adecuados para transferir documentos ni para streaming.

### 29. Flujo TCP

| Servidor | Cliente |
|---|---|
| 1. Crea un `ServerSocket` | 1. Crea un `Socket` |
| 2. Espera conexión con `accept()` | 2. Se conecta al servidor |
| 3. Intercambia datos | 3. Envía y recibe datos |
| 4. Cierra la conexión | 4. Cierra la conexión |

* **`accept()`** es **bloqueante**: espera hasta que un cliente se conecte y devuelve un `Socket`.
* **Orden de ejecución:** primero el **servidor**, luego el cliente.
* **`localhost`** apunta al **mismo equipo** donde se ejecuta el cliente (`new Socket("localhost", 5000)`).
* `ServerSocket` ya incluye el protocolo **TCP/IP**, por lo que no es necesario programar una capa adicional.

### 30. Flujos de datos

| Clase | Función |
|---|---|
| `DataOutputStream` | **Salida** de datos; `writeUTF(texto)` envía una cadena |
| `DataInputStream` | **Entrada** de datos; `readUTF()` es la **contraparte compatible** de `writeUTF` |
| `flush()` | Fuerza el envío inmediato de los datos |
| `BufferedReader` / `PrintWriter` | Alternativa de lectura/escritura de texto (`readLine()` / `println()`) |
| `IOException` | Maneja errores de comunicación con recursos remotos |

* **Regla de simetría (protocolo acordado):** el servidor debe leer con `readUTF` **en el mismo orden** en que el cliente escribió con `writeUTF` (dos `writeUTF` → dos `readUTF`: primero el identificador y luego la descripción). Ambos extremos deben usar el **mismo tipo de flujo**.
* Los sockets **no** invierten el orden ni combinan los envíos en una sola lectura; `accept()` no sirve para recuperar texto.

### 31. Configuración y limitaciones

| Aspecto | Consideración |
|---|---|
| **Puertos recomendados** | **1024–65535** |
| **Firewall** | Debe permitir el tráfico en el puerto usado |
| **Problema típico** | Si clientes de **otra red** no se conectan a un servidor TCP que funciona localmente, se revisan las **reglas del firewall** y la **accesibilidad de red** hacia el puerto |

* **Servidor simple:** se bloquea mientras atiende a un cliente; los demás esperan su turno.
* **Servidor multihilo:** crea un **hilo por cliente** tras cada `accept()`, permitiendo múltiples conexiones simultáneas.

### 32. Ventajas, desventajas y propiedades

| Ventajas | Desventajas |
|---|---|
| Comunicación directa y eficiente | Manejo manual de errores |
| Control total sobre datos y conexión | Configuración de seguridad |
| Permite sistemas distribuidos reales | Complejidad en la sincronización de hilos |
| | **No es escalable** sin arquitecturas adicionales (servidores, enrutadores, firewalls) |

| Propiedad | Significado |
|---|---|
| **Distribuidas** | Componentes en equipos distintos comunicados por red |
| **Interactivas** | Comunicación en tiempo real |
| **Concurrentes** | Varios usuarios conectados al mismo tiempo |
| **Escalables** | Se pueden agregar usuarios, servidores o servicios |
| **Reactivas** | Siempre procesan y responden en el momento |

### 33. Aplicación en el proyecto AVI (chat experto)

* El cliente AVI envía la **pregunta** y la **respuesta de la IA** (`writeUTF` × 2) al servidor del experto (puerto **5000**) y espera la respuesta (`readUTF`).
* `ServidorExpertoService`: `iniciar()` crea el `ServerSocket`; `esperarConsulta()` y `enviarRespuesta()` son **`synchronized`**; `cerrarClienteActual()` cierra entrada, salida y socket y los deja en `null`.
* Se cierra la conexión tras cada consulta por **seguridad, memoria y carga del puerto**.
* Todas las operaciones bloqueantes (`accept()`, `readUTF()`) corren en **hilos de fondo daemon**, y la interfaz se actualiza con `Platform.runLater()`.

---

## PARTE VI. HTTP

### 34. Servicio HTTP

* **HTTP:** protocolo de nivel superior basado en **solicitudes y respuestas**, construido sobre TCP.

| Elemento | Función |
|---|---|
| `HttpServer` | Servidor HTTP incluido en el JDK |
| `createContext("/ruta", handler)` | Asocia una ruta con un manejador (`HttpHandler`) |
| `setExecutor(Executors.newFixedThreadPool(4))` | Grupo de hilos para atender solicitudes simultáneas |
| `HttpClient` | Cliente HTTP de Java |
| `HttpRequest` | Construye la solicitud (URI, cabeceras, método) |
| `POST(BodyPublishers.ofString(datos))` | Aporta la cadena `datos` como **cuerpo de la petición** |
| `URLEncoder` / `URLDecoder` | Codifican/decodifican los datos del formulario |
| **JSON** | Formato de respuesta, ej. `{"autenticado": true}` |

* Los datos viajan como `usuario=...&contrasena=...` (`application/x-www-form-urlencoded`).
* `POST` envía las credenciales en el **cuerpo**, no en la URL.

### 35. Códigos de estado HTTP

| Código | Significado |
|---|---|
| **200** | Éxito: la respuesta contiene el recurso solicitado |
| **400** | Solicitud inválida |
| **405** | **Método no permitido** para ese recurso (ej. un GET a un recurso que solo acepta POST) |
| **500** | Error interno del servidor |

---

## PARTE VII. CONCEPTOS CLAVE PARA EL EXAMEN

| Pregunta típica | Respuesta clave |
|---|---|
| ¿Cómo cumplir 1FN con varios teléfonos en una celda? | Un valor por fila en una **tabla relacionada** |
| Atributo que depende solo de parte de una PK compuesta | **Dependencia parcial** → incumple **2FN** |
| `id_ciudad` determina `nombre_ciudad` en `PEDIDO` | **Dependencia transitiva** → tabla `CIUDAD` + FK (3FN) |
| Evitar que un correo se repita (con PK ya definida) | **`UNIQUE`** |
| `SELECT COUNT(*) ... WHERE activo = 1` | **Cantidad de filas** con `activo = 1` |
| ¿Para qué sirve `import java.sql.PreparedStatement`? | Usar el tipo por su **nombre simple** |
| `setString(1, correo)` | Asocia el valor con el **primer `?`** |
| Ventaja de `try-with-resources` | Cierra el recurso **automáticamente**, incluso con excepción |
| Reducir el costo de abrir conexiones | **Pool de conexiones** reutilizables |
| `Platform.runLater(...)` | Ejecuta la actualización en el **hilo de JavaFX** |
| Import de `Platform` | `javafx.application.Platform` |
| `setDaemon(true)` | El hilo **no mantiene viva la JVM** por sí solo |
| ¿Por qué descargar en segundo plano? | Para que la interfaz **siga atendiendo al usuario** |
| Método `synchronized` | **Un solo hilo a la vez** sobre la misma instancia |
| Lambda en `new Thread(() -> ...)` | Proporciona el **`Runnable`** (la tarea) |
| `contador++` da resultado menor al esperado | Pasos **intercalados** entre hilos → **se pierden actualizaciones** |
| `newFixedThreadPool(4)` con 10 tareas | **4 simultáneas**, las demás **esperan** |
| Documento íntegro y en orden | **TCP** |
| Audio en tiempo real con baja latencia | **UDP** |
| Clientes de otra red no se conectan | Revisar **firewall** y accesibilidad al puerto |
| Contraparte de `writeUTF` | **`readUTF()`** (en el mismo orden) |
| `localhost` | El **mismo equipo** donde corre el cliente |
| `BodyPublishers.ofString(datos)` | Aporta `datos` como **cuerpo** de la petición HTTP |
| Código HTTP **405** | **Método no permitido** para ese recurso |

---

## PARTE VIII. RESUMEN GENERAL

* El **modelo relacional** organiza los datos en tablas (relaciones), filas (tuplas) y columnas (atributos con dominio); las tablas se vinculan con **PK, FK y UQ**, y la integridad (**entidad, referencial, dominio**) garantiza la consistencia.
* La **normalización** (1FN: valores atómicos; 2FN: sin dependencias parciales; 3FN: sin dependencias transitivas) reduce redundancia y anomalías, con el costo de más tablas y `JOIN`.
* **SQL** se divide en **DDL** y **DML**; el **CRUD** se implementa con `INSERT`, `SELECT`, `UPDATE` y `DELETE` (siempre con `WHERE` en los dos últimos).
* Los **procedimientos almacenados** (`CALL`, `IN`/`OUT`), las **funciones** (siempre retornan valor) y los **triggers** (`BEFORE`/`AFTER`, `FOR EACH ROW`) encapsulan lógica en la base de datos.
* **JDBC** conecta Java con la base de datos mediante `Connection`, `PreparedStatement` (previene inyección SQL) y `ResultSet`; `try-with-resources` libera los recursos y un **pool de conexiones** reduce el costo de abrirlas.
* La **arquitectura en capas** (Vista, Controller, Servicios, Lógica, Datos, BD) permite cambios profundos con modificaciones mínimas (**escalabilidad** y **mantenibilidad**).
* Un **hilo** comparte la memoria de su proceso; se crea con `Thread` o `Runnable` (lambda) y se inicia con `start()`. La **sincronización** (`synchronized`) evita condiciones de carrera.
* En **JavaFX**, las tareas lentas van en hilos de fondo (daemon) y la interfaz se actualiza con `Platform.runLater()`; un **pool de hilos** limita la concurrencia.
* Los **sockets** (IP, puerto, protocolo) permiten la comunicación cliente-servidor: **TCP** es confiable y ordenado, **UDP** es rápido sin garantías; el servidor usa `ServerSocket.accept()` y el intercambio con `DataInputStream`/`DataOutputStream` exige el **mismo orden** de lectura y escritura.
* Un **servidor multihilo** evita bloquear a los demás clientes; los sockets no escalan sin arquitectura adicional y requieren configurar **firewall** y puertos (1024–65535).
* **HTTP** envía solicitudes y respuestas sobre TCP; `POST` lleva los datos en el cuerpo y los **códigos de estado** (200, 400, 405, 500) indican el resultado.