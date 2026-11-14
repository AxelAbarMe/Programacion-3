# Resumen Parcial 2 (Prof. José Sánchez): Java, JDBC, Hilos y Sockets

---

## PARTE I. FUNDAMENTOS DEL LENGUAJE JAVA

### 1. Java como plataforma

| Concepto | Definición |
|---|---|
| **Java** | Lenguaje **orientado a objetos**. El compilador no genera lenguaje de máquina sino un lenguaje intermedio llamado **bytecode** |
| **Bytecode** | Lenguaje intermedio que debe ser interpretado por la JVM |
| **JVM** (*Java Virtual Machine*) | Programa que interpreta el bytecode |
| **JRE** (*Java Runtime Environment*) | Ambiente de ejecución: **JVM + biblioteca de clases** |

* Fue desarrollado por **James Gosling** (Sun Microsystems) y publicado en **1995**; **Oracle compró Sun en 2010**.
* Está diseñado para ser **independiente de la plataforma** (hardware + sistema operativo): se compila una vez y se ejecuta en distintas plataformas **sin recompilar**.
* Su sintaxis se basa en **C y C++**.

### 2. Clases y objetos

* **Clase:** contiene miembros (**atributos** y **métodos**); funciona como un **molde** para fabricar objetos.
* **Objeto:** **instancia** de una clase; se le envían mensajes (invocaciones) para que ejecute sus métodos. **Solo se manipulan por referencias** (punteros implícitos).

```java
Employee e1 = new Employee("Juan", "Perez", "111");
System.out.println(e1.toString() + " --- C.C.: " + e1.iniTials());
```

### 3. Constructores

* **Inicializan** los objetos (no los construyen). Se invocan con `new` y se nombran **igual que la clase**.
* Puede haber **varios** con distintos parámetros (sobrecarga). Uno puede llamar a otro con **`this(parámetros)`**.
* `new` **retorna una referencia** al objeto creado.

```java
public Employee(String first, String last) {
    this(first, last, "");   // llama al otro constructor
}
```

### 4. La palabra `this`

* Hace referencia al **objeto actual** sobre el que se ejecuta el método: `this.atributo`, `this.metodo()`, `this`.
* Se asume por omisión: `atributo ≈ this.atributo`.
* Puede **pasarse como parámetro** (`objeto.metodo(this)`) o **retornarse** (`return this;`), lo que permite encadenar llamadas: `new Clase(..).metodo(..).otroMetodo(..)`.

### 5. Referencias

* Dada `Employee e;`, `e` es una **referencia, no un objeto**; inicialmente contiene **`null`**.
* **Dereferenciar** una referencia nula (`e.metodo()`) genera **`NullPointerException`**.
* `new` crea el objeto y retorna su referencia.
* La **asignación copia la referencia, no el objeto** (`e1 = e`). A los métodos se les pasan **referencias**.

### 6. `==` vs. `equals`

| Operador / método | Qué compara |
|---|---|
| `==` | **Valores**. Con objetos compara el valor de las **referencias** (punteros), no el contenido |
| `equals` | Método heredado de `Object` que se puede **sobre-escribir** para comparar **contenidos** |

```java
Employee e1 = new Employee("Juan","Perez","111");
Employee e2 = new Employee("Juan","Perez","111");
e1 == e2;        // false (distintas referencias)
e1.equals(e2);   // true  (si equals compara el número de seguro social)
```

### 7. Miembros estáticos (`static`)

* Existe **una sola copia**, asociada a la **clase** (no una por instancia). Se accede por `Clase.miembro` o por cualquier instancia.
* Los **métodos estáticos** solo pueden acceder a miembros **estáticos**.
* Las **variables estáticas** se inicializan al **cargar la clase**, cuando se usa por primera vez.
* Un **bloque estático** (`static { ... }`) se ejecuta al cargar la clase.

```java
public class Model {
    static HashMap<String,Employee> employees = new HashMap();
    public static Employee buscar(String ssn) { return employees.get(ssn); }
    static { /* se ejecuta al cargar la clase */ }
}
```

### 8. Tipos elementales

| Tipo | Clase de valor | Memoria | Rango |
|---|---|---|---|
| `byte` | Entero | 1 byte | -128 a 127 |
| `short` | Entero | 2 bytes | -32,768 a 32,767 |
| `int` | Entero | 4 bytes | -2,147,483,648 a 2,147,483,647 |
| `long` | Entero | 8 bytes | ±9.22×10¹⁸ aprox. |
| `float` | Punto flotante | 4 bytes | ±3.4×10³⁸ a ±1.4×10⁻⁴⁵ |
| `double` | Punto flotante | 8 bytes | ±1.8×10³⁰⁸ a ±4.9×10⁻³²⁴ |
| `char` | Carácter Unicode | 2 bytes | 0 a 65,535 |
| `boolean` | Lógico | 1 bit | `true` / `false` |

### 9. Envolturas (*wrappers*), autoboxing y unboxing

* Las colecciones solo aceptan **objetos**, no tipos elementales. Por eso existen las **clases envoltorio** (`Integer`, `Boolean`, etc.).
* El compilador realiza las conversiones **automáticamente**:
  * **Autoboxing:** de `int` a `Integer`.
  * **Unboxing:** de `Integer` a `int`.

```java
// List<int> l = new ArrayList<int>();   // NO permitido
List<Integer> l = new ArrayList<Integer>();
l.add(3);          // autoboxing
int i = l.get(0);  // unboxing
```

| Método de `Integer` | Función |
|---|---|
| `intValue()` | Retorna el `int` |
| `parseInt(String)` | Convierte texto a `int` |
| `valueOf(String)` / `valueOf(int)` | Retorna un `Integer` |
| `toString()` / `toString(int)` | Retorna el texto |

---

## PARTE II. HERENCIA, POLIMORFISMO, INTERFACES Y PAQUETES

### 10. Herencia

* Java soporta **herencia simple** entre clases (una sola superclase). La relación se lee **"es un / es una"**.
* Las subclases pueden **agregar** atributos o métodos y **sobre-escribir** métodos.
* La superclase puede declarar **métodos abstractos** que implementan las subclases.
* Se establece con **`extends`**. El constructor de la subclase invoca el de la superclase con **`super(...)`**. Los métodos sobre-escritos de la superclase se invocan con **`super.metodo(...)`**.

```java
public abstract class Employee {
    public abstract double earnings();   // sin implementación
}

public class SalariedEmployee extends Employee {
    private double weeklySalary;
    public SalariedEmployee(String first, String last, String ssn, double salary) {
        super(first, last, ssn);         // constructor de la superclase
        setWeeklySalary(salary);
    }
    public double earnings() { return getWeeklySalary(); }
}
```

### 11. Polimorfismo

* Una **variable** del tipo de la superclase puede referirse a un objeto de la superclase, de cualquiera de sus subclases o de cualquier descendiente.
* Un **método** invocado con esa variable toma **distintas formas** según el tipo del objeto concreto referido.
* Un método con **parámetro** de tipo superclase acepta objetos de las subclases; un método con **tipo de retorno** de la superclase puede retornar objetos de las subclases.

```java
Employee e;
e = new SalariedEmployee("Juan","Perez","111",1000);
e = new HourlyEmployee("Maria","Mata","222",10,30);
for (Employee emp : Model.employees.values()) {
    total += emp.earnings();   // cada objeto ejecuta SU versión de earnings()
}
```

### 12. Interfaces

* Declaran la **funcionalidad (métodos)** que debe proveer cada clase que las cumpla. Establecen un **tipo** (para variables, parámetros, retornos).
* Se parecen a las clases abstractas, pero **una clase puede implementar múltiples interfaces** (mientras que solo hereda de una clase).
* Se implementan con **`implements`**.

```java
public interface Payable { double getPaymentAmount(); }

public class Invoice implements Payable {
    public double getPaymentAmount() { return getQuantity() * getPricePerItem(); }
}
public abstract class Employee implements Payable {
    public double getPaymentAmount() { return earnings(); }
}
```

### 13. Programación con interfaces

* El **cliente** (`Listing`) solo usa la **interface** (`Payable`); los **proveedores** (`Invoice`, `Employee`, etc.) solo la **implementan**.
* Así, el cliente trabaja con objetos de clases distintas sin conocerlas.

```java
public class Listing {
    public List<Payable> payables;
    public void list() {
        for (Payable pay : payables) {
            total += pay.getPaymentAmount();
        }
    }
}
```

### 14. Paquetes

* Agrupan las clases formando una **jerarquía** (`package`). **Ordenan** las clases y **evitan colisiones de nombres**.
* Para usar una clase de otro paquete se debe **importar** (el paquete completo con `*` o clase por clase).

```java
package payroll;
import payroll.logic.*;
```

| Concepto | Resumen |
|---|---|
| **Herencia** | `extends`, simple, relación "es un" |
| **Clase abstracta** | Puede tener métodos abstractos; se hereda |
| **Interface** | Contrato de métodos; `implements`, múltiples |
| **Polimorfismo** | Misma llamada, distinto comportamiento según el objeto real |
| **Paquete** | Agrupa clases y evita colisiones de nombres |

---

## PARTE III. ACCESO A BASES DE DATOS CON JDBC Y DAO

### 15. JDBC

* **JDBC** es un **API** (*Application Programming Interface*) que define **cómo una aplicación Java puede acceder a una base de datos**.
* Cada proveedor de DBMS debe suplir **su propia implementación** de JDBC.
* **Esquema:** la aplicación Java (clientes) se comunica por **LAN o WAN** con el **servidor (DBMS, ej. MySQL)**; envía una **solicitud SQL** (*SQL request*) y recibe los **datos seleccionados** (*selected data*).

#### Arquitectura JDBC

* La aplicación usa la capa **JDBC**, compuesta por interfaces: **`Driver`, `Connection`, `Statement`, `ResultSet`**.
* Un **driver** (MySQL, ODBC, Oracle, SQL Server) es un componente que **implementa las interfaces definidas por JDBC**, contenidas en el **paquete `sql`**.
* Existen drivers de proveedor directo (ej. "SqlServer JDBC") y el **puente ODBC** ("Odbc Bridge JDBC") que comunica con ODBC.

| Elemento | Función |
|---|---|
| `Driver` | Implementación del proveedor |
| `Connection` | Conexión con la base de datos |
| `Statement` / `PreparedStatement` | Sentencia SQL a ejecutar |
| `ResultSet` | Filas devueltas por una consulta |

### 16. Patrón DAO (*Data Access Object*)

* Un **DAO** encapsula el acceso a datos de una entidad con las operaciones **CRUD** más búsquedas y mapeo.

```java
public class PersonaDao {
    Database db;
    public PersonaDao() { db = Database.instance(); }
    public void create(Persona p) throws Exception { ... }
    public Persona read(String id) throws Exception { ... }
    public void update(Persona p) throws Exception { ... }
    public void delete(Persona o) throws Exception { ... }
    public List<Persona> findByNombre(Persona filtro) { ... }
    private Persona from(ResultSet rs) { ... }
}
```

| Método | Función |
|---|---|
| `create` | **INSERT**; si no se afectó ninguna fila lanza "Persona ya existe" |
| `read` | **SELECT** por identificador; si no hay resultado lanza "Persona no existe" |
| `update` / `delete` | **UPDATE** / **DELETE** |
| `findByNombre` | Búsqueda con `LIKE ?` y `"%" + nombre + "%"` |
| `from(rs)` | **Mapea cada fila del `ResultSet` a un objeto** |

#### Mapeo fila → objeto

* `from(rs)` convierte cada fila en un objeto (`rs.getString("columna")`, `rs.getBoolean("columna")`), de modo que el resultado es una **colección de objetos** (`List<EstadoCivil>`, `List<Persona>`).

```java
public EstadoCivil from(ResultSet rs) {
    EstadoCivil ec = new EstadoCivil();
    ec.setCodigo(rs.getString("codigo"));
    ec.setDescripcion(rs.getString("descripcion"));
    return ec;
}
```

### 17. Sentencias preparadas y ejecución

| Elemento | Función |
|---|---|
| `?` | Marcador de parámetro de `PreparedStatement` |
| `setString(1, valor)`, `setBoolean(5, valor)` | Asignan el valor al marcador; la numeración **comienza en 1** |
| `executeUpdate()` | Para **INSERT / UPDATE / DELETE**; retorna las **filas afectadas** |
| `executeQuery()` | Para **SELECT**; retorna un **`ResultSet`** |
| `rs.next()` | Avanza a la siguiente fila; retorna **`false`** al final |
| `inner join ... on` | Une tablas relacionadas dentro de la consulta |

### 18. Clase `Database` (Singleton y conexión)

* **Patrón Singleton:** garantiza **una sola instancia** (y una sola conexión) compartida por todos los DAO, mediante `Database.instance()`.
* La **configuración** (driver, servidor, puerto, usuario, clave, nombre de BD) se lee desde un archivo **`.properties`**, evitando escribirla en el código.
* `Class.forName(driver)` **carga el driver**; `DriverManager.getConnection(url)` **abre la conexión**.
* **URL:** `jdbc:mysql://servidor:puerto/basedatos?user=...&password=...&serverTimezone=UTC`.

```java
public class Database {
    private static Database theInstance;
    public static Database instance() {
        if (theInstance == null) { theInstance = new Database(); }
        return theInstance;
    }
}
```

### 19. Script de base de datos (SQL)

```sql
CREATE DATABASE Personas;
use Personas;
create table EstadoCivil (
    codigo      varchar(10) not null,
    descripcion varchar(30) not null,
    Primary Key (codigo)
);
create table Persona (
    id varchar(10) not null,
    nombre varchar(30) not null,
    sexo char(1) not null,
    estadoCivil varchar(10) not null,
    Primary Key (id)
);
ALTER TABLE Persona ADD Foreign Key (estadoCivil) REFERENCES EstadoCivil(codigo);
```

* `Primary Key` identifica cada fila; `Foreign Key ... REFERENCES` relaciona `Persona` con `EstadoCivil`.

---

## PARTE IV. PROCESOS E HILOS

### 20. Procesos

* El **sistema operativo (SO)** administra los trabajos en ejecución, llamados **procesos**.
* Cada proceso tiene **recursos propios**: datos, código, archivos, etc.
* Al iniciar un trabajo, el SO: **crea el proceso**, **le asigna recursos**, **crea su hilo principal (`main`)** y **calendariza** dicho hilo.

### 21. Hilos

* El hilo principal puede solicitar al SO la creación de **otros hilos dentro del mismo proceso**.
* Un **hilo** es un **único flujo de control** dentro de un programa: una secuencia de instrucciones relacionadas que se ejecuta **de forma independiente** de otras secuencias.

| Aspecto | Descripción |
|---|---|
| **Cada hilo tiene** | Su propio **contador del programa**, **registros del CPU** y **pila (stack)** |
| **Los hilos comparten** | El **código** y los **datos** del proceso |
| **Para el SO** | Son **unidades de ejecución** o **unidades de calendarización** |

* El SO **alterna los núcleos (procesadores) entre los hilos**; cada hilo se ejecuta en el contexto de su proceso.

### 22. Hilos en Java

| Elemento | Función |
|---|---|
| **`Runnable`** | Interface con el método **`void run()`**, que representa la **tarea** a ejecutar. Puede ser una secuencia que se ejecuta una vez o un **ciclo** hasta que se cumpla una condición |
| **`Thread`** | Clase que **representa un hilo**; la tarea puede pasarse como un `Runnable` al construirlo |
| **`start()`** | **Inicia** la ejecución de la tarea (`run`) |

* **El hilo termina cuando su tarea (`run`) termina.**

### 23. Ejemplo: hilo con ciclo controlado por bandera

* El `run` repite: **esperar un tiempo** y **actualizar estadísticas**, hasta que la **bandera** se pone en `false`.

```java
private Thread hilo;
private boolean condition = false;

public void start() {
    Runnable tarea = new Runnable() {
        public void run() {
            while (condition) {
                try { Thread.sleep(2000); }
                catch (InterruptedException ex) {}
                actualizarEstadisticas();
            }
        }
    };
    hilo = new Thread(tarea);
    condition = true;
    hilo.start();
}
public void stop() { condition = false; }
```

* `Thread.sleep(ms)` pausa el hilo; `stop()` solo cambia la **bandera** para que el ciclo termine de forma controlada.

### 24. Hilos e interfaz gráfica (Swing)

* El hilo principal (`main`) **crea la interfaz** y sigue su ejecución hasta terminar. Un hilo aparte, el **EDT (*Event Dispatch Thread*)**, **procesa los eventos** de la interfaz (mouse, teclado, etc.).
* **Flujo:** la interfaz encola los eventos en la **Event Queue** → el EDT los toma → notifica a los **Event Listeners** → estos actualizan la interfaz.
* **Swing es uni-hilo (*single threaded*):** sus elementos gráficos **solo deben ser accedidos por el EDT**.
* Si otro hilo necesita actualizar la interfaz, debe colocar una **tarea (`Runnable`) en la cola del EDT**.

```java
private void actualizarEstadisticas() {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            controller.actualizarEstadisticas();
        }
    });
}
```

| Plataforma | Hilo de la interfaz | Mecanismo para actualizar desde otro hilo |
|---|---|---|
| **Swing** | **EDT** (*Event Dispatch Thread*) | `SwingUtilities.invokeLater(Runnable)` |
| **JavaFX** | Hilo de aplicación de JavaFX | `Platform.runLater(() -> ...)` |

---

## PARTE V. SOCKETS

### 25. Concepto

* Un **socket** es una **interface entre la aplicación y la red**. La aplicación lo crea y, una vez configurado, **le pasa datos para transmitirlos** por la red y **recibe datos** transmitidos desde otro computador.
* Ejemplo típico: la comunicación **cliente-servidor**.

### 26. Pila TCP/IP

* Los sockets se basan en el **stack TCP/IP, provisto por el sistema operativo**, y se ubican en la **capa de transporte**.
* Cada extremo del enlace se identifica por **una IP y un puerto**.

| Capa | Protocolos |
|---|---|
| **Aplicación** | HTTP, FTP, TLS/SSL, SMTP, POP, IMAP |
| **Transporte** | **TCP, UDP** (aquí se ubican los sockets) |
| **Internet** | IP, ICMP, IGMP |
| **Enlace** | ARP, DSL, ISDN, OSPF, Ethernet |

### 27. Protocolos de aplicación

* Los sockets permiten enviar y recibir **"chorros" de bytes, sin ninguna interpretación** de ellos.
* La **capa de aplicación** establece las reglas sobre el **formato** de los datos enviados y recibidos (HTTP, FTP, etc.).

### 28. Canales de entrada y salida

* Cada socket provee un **canal de salida (`OutputStream`)** para enviar datos a su contraparte y un **canal de entrada (`InputStream`)** para recibir datos de ella.
* **Lo que se envía por el canal de salida llega a la contraparte por su canal de entrada**, y viceversa.

### 29. `Socket` y `ServerSocket` (handshake)

* El **enganche (*handshake*)** se hace mediante un **`ServerSocket`**.
* El **cliente** envía la solicitud de conexión **al crear un `Socket`**.
* El **`ServerSocket`** del servidor **acepta la petición (`accept`) y crea el `Socket` contraparte**.
* Luego, el socket del cliente y el socket del servidor **interactúan** intercambiando bytes.
* En el esquema: *welcoming socket* (el `ServerSocket`) vs. *connection socket* (el creado por `accept`).

### 30. Ejemplo cliente-servidor

```java
// SERVIDOR
ServerSocket serverSocket = new ServerSocket(20000);
Socket socket = serverSocket.accept();           // bloquea hasta que llegue un cliente
InputStream is = socket.getInputStream();
OutputStream os = socket.getOutputStream();
byte[] buffer = new byte[1024];
int len = is.read(buffer);
String msg = new String(buffer, 0, len);
os.write("Hello Client".getBytes());
socket.close();

// CLIENTE
Socket socket = new Socket("127.0.0.1", 20000);
OutputStream os = socket.getOutputStream();
InputStream is = socket.getInputStream();
os.write("Hello Server".getBytes());
int len = is.read(buffer);
```

| Elemento | Función |
|---|---|
| `new ServerSocket(20000)` | El servidor escucha en el puerto **20000** |
| `accept()` | **Bloqueante**: espera un cliente y retorna el `Socket` de la conexión |
| `new Socket("127.0.0.1", 20000)` | El cliente se conecta; `127.0.0.1` es **localhost** |
| `read(buffer)` | Lee bytes del canal de entrada y retorna la **cantidad leída** |
| `write(bytes)` | Escribe bytes en el canal de salida |
| `close()` | Cierra la conexión |

### 31. Ejemplo: cliente HTTP con sockets

```java
Socket s = new Socket("www.apache.org", 80);
PrintWriter pw = new PrintWriter(s.getOutputStream());
pw.print("GET / HTTP/1.0\r\n\r\n");
pw.flush();
BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
String t;
while ((t = br.readLine()) != null) System.out.println(t);
```

* Se conecta al puerto **80** (HTTP), envía una petición **`GET`** (protocolo de la capa de aplicación sobre el socket) y lee la respuesta (HTML).
* `flush()` fuerza el envío de los datos almacenados en el búfer.

---

## PARTE VI. ARQUITECTURA BACKEND/FRONTEND CON SOCKETS SINCRÓNICOS Y ASINCRÓNICOS

### 32. Idea general

* Cada **Frontend** abre **dos conexiones** al **Backend**:

| Conexión | Función |
|---|---|
| **SYNC (sincrónica)** | Sus **peticiones** (petición → respuesta) |
| **ASYNC (asincrónica)** | Recibir **notificaciones** que el servidor envía **sin que el cliente las pida** |

* Ambos sockets son administrados luego por un **Worker** del Backend (**un worker por frontend**).

### 33. Componentes del Backend

#### `Worker`

* Administra **dos sockets**: el **sincrónico** (atención de peticiones de su Frontend) y el **asincrónico** (envío de notificaciones).
* Almacena su **id único de sesión** (`sid`, un `String`).
* Su socket asincrónico lo recibe **después**, mediante **`setAs`**.
* Envía **notificaciones asincrónicas** (`deliver_message`) y atiende operaciones (`listen`).
* El envío asincrónico puede ser un **efecto colateral** de una operación sincrónica: el worker que atiende la operación llama a `srv.deliver_message`, y el servidor pide a **cada otro worker** que envíe la notificación.

```java
case Protocol.PRODUCTO_CREATE:
    service.create((Producto) is.readObject());
    os.writeInt(Protocol.ERROR_NO_ERROR);
    srv.deliver_message(this, "Producto creado");   // notifica a los demás
    break;
```

#### `Server`

* Recibe de cada Frontend **dos solicitudes de conexión**:
  1. **SYNC:** se crea el `Worker`, se establece el **id de sesión** (`sid`) y se **envía de vuelta** al Frontend.
  2. **ASYNC:** el Frontend **adjunta el `sid`**; el servidor busca el worker correspondiente (**`join`**) y le asigna el socket asincrónico.
* **`deliver_message(from, mensaje)`:** instruye a **cada worker, menos al que originó la operación**, para que envíe la notificación.

```java
public void deliver_message(Worker from, String message) {
    for (Worker w : workers) {
        if (w != from) w.deliver_message(message);
    }
}
```

### 34. Componentes del Frontend

| Pieza | Rol |
|---|---|
| **`Service` (Proxy)** | Establece la conexión **SYNC**, guarda el `sid` recibido e **implementa `IService`**. Es un **Singleton** |
| **`SocketListener`** | Usa un **`Thread`** para **escuchar** las notificaciones del socket **ASYNC** y entregarlas al `ThreadListener` |
| **`ThreadListener`** | **Interface** con los métodos que atienden las notificaciones asincrónicas |
| **`Controller`** | Implementa `ThreadListener`; al recibir una notificación **refresca su búsqueda** para mostrar el nuevo dato |

* `SocketListener` usa **`invokeLater`** para que la notificación la atienda el **EDT** y no el hilo del listener, **respetando el diseño mono-hilo de Swing**.
* `SocketListener` **delega** la atención en el `ThreadListener` (usualmente el Controller, que actualiza su MVC).

```java
private void deliver(final String message) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() { listener.deliver_message(message); }
    });
}
```

### 35. Protocolo propio y envío de objetos

* Clase **`Protocol`:** constantes compartidas por ambos lados (`SYNC`, `ASYNC`, `DELIVER_MESSAGE`, `PRODUCTO_CREATE`, `ERROR_NO_ERROR`, `ERROR_ERROR`, `SERVER`, `PORT`).
* La comunicación usa **`ObjectOutputStream` / `ObjectInputStream`** (`writeInt`, `writeObject`, `readInt`, `readObject`, `flush`).
* **Regla:** primero se envía un **entero** (tipo de operación) y luego el **objeto**; el **orden de lectura debe coincidir con el de escritura**.

| Pieza | Lado | Rol |
|---|---|---|
| `Server` | Backend | Acepta conexiones, crea `Worker` o asigna el socket asincrónico (`join`), reparte notificaciones |
| `Worker` | Backend | Un hilo por frontend; atiende peticiones sincrónicas y envía notificaciones |
| `Service` (Proxy) | Frontend | Conexión SYNC; guarda el `sid` |
| `SocketListener` | Frontend | Hilo que escucha el socket ASYNC |
| `ThreadListener` | Frontend | Interface implementada por el Controller |
| `Protocol` | Ambos | Constantes del protocolo |

---

## PARTE VII. CONCEPTOS CLAVE PARA EL EXAMEN

| Pregunta típica | Respuesta clave |
|---|---|
| ¿Qué genera el compilador de Java? | **Bytecode**, interpretado por la **JVM** |
| ¿Qué es el JRE? | **JVM + biblioteca de clases** |
| ¿Qué contiene una variable de objeto sin inicializar? | **`null`**; dereferenciarla produce **`NullPointerException`** |
| ¿Qué hace `==` con objetos? | Compara **referencias**, no contenido |
| ¿Cómo comparar contenido? | Sobre-escribir **`equals`** |
| ¿Qué es un miembro `static`? | **Una sola copia** asociada a la clase |
| ¿Qué es autoboxing? | Conversión automática de `int` a `Integer` |
| ¿Cómo llama un constructor a otro? | **`this(...)`** |
| ¿Cómo llama una subclase al constructor de su superclase? | **`super(...)`** |
| ¿Cuántas superclases / interfaces puede tener una clase? | **Una** superclase, **varias** interfaces |
| ¿Qué es polimorfismo? | Una variable de la superclase refiere a subclases y el método toma la **forma del objeto concreto** |
| ¿Qué es JDBC? | **API** que define cómo una aplicación Java accede a una base de datos |
| ¿Qué es un driver? | Componente que **implementa las interfaces JDBC** (paquete `sql`) |
| ¿Qué hace `from(rs)`? | **Mapea una fila** del `ResultSet` a un objeto |
| `executeUpdate` vs. `executeQuery` | INSERT/UPDATE/DELETE (filas afectadas) vs. SELECT (`ResultSet`) |
| ¿Qué garantiza el Singleton `Database`? | **Una sola instancia/conexión** compartida |
| ¿Qué comparten los hilos de un proceso? | **Código y datos** |
| ¿Qué tiene cada hilo de forma propia? | **Contador de programa, registros y pila** |
| ¿Qué define `Runnable`? | El método **`run()`** (la tarea) |
| ¿Qué hace `start()`? | **Inicia** la ejecución de `run` |
| ¿Cuándo termina un hilo? | Cuando **termina su `run`** |
| ¿Qué es el EDT? | Hilo que **procesa los eventos** de la interfaz Swing |
| ¿Cómo actualiza la interfaz otro hilo en Swing? | **`SwingUtilities.invokeLater`** (JavaFX: `Platform.runLater`) |
| ¿Qué es un socket? | **Interface entre la aplicación y la red** |
| ¿En qué capa se ubican los sockets? | **Transporte** |
| ¿Qué identifica a cada extremo? | **IP y puerto** |
| ¿Qué envían los sockets? | **Chorros de bytes** sin interpretación |
| ¿Quién hace el handshake? | **`ServerSocket`** (`accept` crea el socket contraparte) |
| ¿Qué es `127.0.0.1`? | **localhost** |
| ¿Para qué sirven las dos conexiones SYNC/ASYNC? | Peticiones (sincrónica) y **notificaciones no solicitadas** (asincrónica) |
| ¿Qué es el `sid`? | **Id de sesión** que une el socket SYNC con el ASYNC |
| ¿Quién escucha las notificaciones en el Frontend? | **`SocketListener`** (hilo), que delega en **`ThreadListener`** |

---

## PARTE VIII. RESUMEN GENERAL

* **Java** compila a **bytecode** que ejecuta la **JVM**, lo que lo hace independiente de la plataforma; el **JRE** incluye la JVM y la biblioteca de clases.
* Los objetos se manipulan por **referencias** (`null` por defecto); `==` compara referencias y `equals` compara contenido. Los miembros `static` tienen una única copia por clase.
* Los **constructores** inicializan objetos (`this(...)`, `super(...)`); los **wrappers** permiten usar tipos elementales en colecciones mediante **autoboxing/unboxing**.
* La **herencia** (`extends`, simple) y las **interfaces** (`implements`, múltiples) habilitan el **polimorfismo**; la programación con interfaces desacopla clientes y proveedores; los **paquetes** organizan las clases y evitan colisiones.
* **JDBC** es el API de acceso a bases de datos, con interfaces (`Driver`, `Connection`, `Statement`, `ResultSet`) implementadas por cada proveedor. El patrón **DAO** encapsula el CRUD, `from(rs)` mapea filas a objetos y `Database` (**Singleton**) administra la conexión leyendo su configuración de un `.properties`.
* Un **proceso** tiene código, datos y recursos propios; sus **hilos** comparten código y datos pero tienen cada uno su **pila, contador y registros**. En Java se crean con `Runnable` y `Thread`, se inician con `start()` y terminan cuando acaba su `run`.
* En interfaces gráficas, solo el **hilo de la interfaz** (EDT en Swing; hilo de aplicación en JavaFX) puede tocar los componentes; otros hilos usan **`invokeLater` / `Platform.runLater`**.
* Un **socket** es la interface entre la aplicación y la red (capa de **transporte**, identificado por **IP y puerto**), transmite **bytes sin interpretación** y se complementa con protocolos de la capa de aplicación. El **handshake** lo realiza `ServerSocket.accept()`, y cada socket ofrece un `InputStream` y un `OutputStream` cruzados con los de su contraparte.
* La arquitectura **Backend/Frontend** usa **dos sockets por cliente** (**SYNC** para peticiones y **ASYNC** para notificaciones), un **`Worker`** por frontend en el servidor, un **id de sesión** que enlaza ambos sockets, un **`SocketListener`** con hilo propio y un **`ThreadListener`** (Controller) que refresca la vista, comunicándose con un **protocolo propio** sobre `ObjectOutputStream`/`ObjectInputStream`.