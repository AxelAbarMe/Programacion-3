# Resumen Programación 3

## 1. Patrón de Arquitectura MVC

> MVC es una forma de programar, no una herramienta; es un patrón de organización del código que pertenece al **frontend**.

| Capa | Responsabilidad | No debe hacer |
|:--|:--|:--|
| **Modelo (Model)** | Otorga la **estructura** de los datos (define las reglas y forma de los objetos) | No transfiere datos, no valida reglas de negocio |
| **Vista (View)** | Otorga el **diseño** visual (`.fxml`) | No contiene lógica ni código fuente |
| **Controlador (Controller)** | Otorga la **funcionalidad**: intermediario entre Vista y Modelo, procesa eventos del usuario | No debe manejar bases de datos directamente (vulnerabilidad) |

* **Código espagueti:** mezclar lenguajes/lógica en un mismo lugar (ej. SQL dentro de HTML). Se evita comunicando capas por **referencias**, nunca mezclando código.
* **Frameworks que lo implementan:** Spring MVC, Django, ASP.NET, Ruby on Rails.
* **Ventaja clave:** permite trabajo en equipo (Modelo, Vista y Controlador simultáneamente) y sistemas escalables/mantenibles.

### Ejemplo conceptual (analogía NASCAR)

| Model (Estructura) | View (Diseño) | Controller (Funcionalidad) |
|:--|:--|:--|
| Peso, largo, motor, engranaje (igual para todos) | Patrocinadores, número, llantas (diferencia a cada uno) | Freno, acelerador, cambio de marchas |

---

## 2. Arquitectura en Capas (Backend completo)

```
┌─────────────────────────────┐
│   Interfaz de Usuario (MVC) │  Vista + Controller
├─────────────────────────────┤
│          Servicios          │  (opcional) enruta, no valida
├─────────────────────────────┤
│            Lógica           │  reglas de negocio, mapea a DTO
├─────────────────────────────┤
│             Datos           │  conecta con BD, valida solo conexión
├─────────────────────────────┤
│         Base de Datos       │  almacenamiento puro
└─────────────────────────────┘
```

| Capa | Función | Tipo de validación |
|:--|:--|:--|
| **Base de Datos** | Almacenamiento únicamente. Puede tener **triggers** (disparados automáticamente ante INSERT/UPDATE/DELETE) y **procedimientos almacenados** (código que corre dentro del motor) | Integridad, tipos, llaves únicas |
| **Datos** | Intercambia información con la BD (insertar, buscar, eliminar) y la traslada hacia/desde Lógica | Solo incidentes de conexión |
| **Lógica** | "Cerebro": prepara datos, aplica reglas de negocio, hace el **mapeo** dataset → DTO | Reglas de negocio |
| **Servicios** *(opcional)* | Envía/recibe solicitudes desde el cliente; conecta con **cualquier interfaz** (JavaFX, web, móvil). Es la capa **más rápida** porque no valida nada | Ninguna |
| **UI (Vista/Controller)** | Presenta información y captura solicitudes del usuario | Estructura de los datos |

> **Regla de oro:** solo la capa de **Datos** sabe que existe una base de datos. Lógica, Servicios, DTO y UI la desconocen por completo — esto permite cambiar de MySQL a PostgreSQL sin tocar el resto del sistema.

### DTO (Data Transfer Object)

* Clase que **solo almacena datos**: `get`, `set` y constructor. Sin lógica de negocio.
* **Regla de mapeo:** todo lo que está en la BD debe poder representarse en el DTO, pero **no todo DTO tiene que existir en la BD** (pueden ser campos calculados, ej. `Edad` a partir de `FechaNacimiento`).
* Vive **solo en memoria** durante la ejecución; desaparece al cerrar la app.
* Es el objeto que "viaja" entre capas — la entidad cruda de la BD (`UsuarioEntity`/dataset) **nunca** sale de la capa de Datos/Lógica.

**Ejemplo del dominio médico (PDF de referencia):**

| Entidad BD (`Paciente`) | DTO (`PacienteDTO`) |
|:--|:--|
| `PKPaciente` int autoincremental | `IdPaciente` |
| `NomPaciente`, `NomPrimerApellidoPaciente`, `NomSegundoApellidoPaciente` | `Nombre`, `PrimerApellido`, `SegundoApellido` |
| `IndSexo`, `FecNacimiento`, `IdCedula` | `Sexo`, `FechaNacimiento`, `Identificacion` |
| *(no existe en BD)* | **`Edad`** ← campo calculado, solo en el DTO |

> El DTO de `Cita` referencia a los DTO de `Paciente` y `Doctor` (composición), en vez de repetir sus datos.

---

## 3. Aplicaciones dirigidas por Eventos

* **Event-Driven Programming:** el programa no corre secuencialmente; permanece a la espera ("idle") de estímulos.
* **Daemons:** aplicaciones **dirigidas por servicio**, sin interfaz gráfica, que corren en segundo plano (servidor web, antivirus, servicio de impresión).

### Ciclo del Event Loop

```
 1. Espera (idle) → 2. Captura del evento → 3. Cola de eventos (FIFO + prioridad)
        ↑                                             │
        └──── 6. Retorno a espera ← 5. Ejecución ← 4. Despacho (dispatching)
```

### Eventos vs Pseudoeventos

| Tipo | Definición | Ejemplo |
|:--|:--|:--|
| **Evento real** | Suceso físico/natural del sistema | Clic de mouse, tecla presionada, paquete de red |
| **Pseudoevento** | Generado artificialmente por la app | `Timer`, `fireEvent()` en JavaFX |

### Clasificación por origen

| Origen | Ejemplos |
|:--|:--|
| **Hardware** | Mouse, teclado, sensores, sockets |
| **Sistema** | Memoria agotada, `onShown`, batería baja |
| **Aplicación** | Validación de formulario terminada, `PropertyChangeEvent` |

### Jerarquía / prioridad de atención

* **Fija:** eventos críticos (ej. corte de energía) interrumpen todo.
* **Secuencial (FIFO):** primero en llegar, primero en atenderse.
* **Por tipo de origen:** prioridad según de dónde viene el evento.

---

## 4. Características de Java (fundamentos relevantes)

| Concepto | Definición breve |
|:--|:--|
| Encapsulamiento | Ocultar detalles internos, exponer solo lo necesario |
| Abstracción | Representar entidades por sus características esenciales |
| Sin punteros | No se manipulan direcciones de memoria directamente |
| Todo son referencias | Variables objeto = referencia a la ubicación en memoria |
| Garbage Collector | Libera memoria automáticamente |
| Herencia simple | `extends` (una sola superclase) |
| Interfaces | `implements` (múltiples comportamientos) |
| Polimorfismo | `@Override` de métodos |
| Genéricos | `Map<K,V>` — reutilización con seguridad de tipos |
| Lambdas | Funciones concisas sin estado propio |
| Streams | Procesamiento declarativo de colecciones |

---

## 5. JavaFX, Scene Builder y FXML

* **FXML:** formato XML declarativo que separa la Vista (diseño) del Controller (lógica). Requiere `xmlns:fx="http://javafx.com/fxml"` y `fx:controller="paquete.Clase"`.
* **Scene Builder:** herramienta visual (drag & drop) que genera FXML automáticamente.
* **Stage → Scene → Parent:** el `Stage` es la ventana; el `Scene` es el contenido gráfico completo; `Parent` es el nodo raíz dentro del `Scene`. "Cambiar de pantalla" = reemplazar el `Parent` (y opcionalmente el `Scene`) dentro del mismo `Stage`.
* **`setRoot()` vs `new Scene()`:** `setRoot` conserva tamaño/posición de la ventana; crear un `Scene` nuevo no.
* **Contenedores más usados:** `AnchorPane` (anclas a bordes), `VBox`/`HBox` (apilado vertical/horizontal), `BorderPane` (5 regiones fijas), `GridPane` (filas/columnas), `Pane` (coordenadas absolutas `layoutX`/`layoutY`).
* **Controles comunes:** `Button`, `TextField`, `PasswordField`, `ComboBox`, `ChoiceBox`, `TableView`, `ListView`, `Alert`, `DatePicker`.

---

## 6. Regex (Expresiones Regulares)

* Usadas para **validar** entradas, **buscar/reemplazar** texto y **parsear** datos no estructurados.
* **Regex101:** herramienta web para probar patrones en tiempo real, con selector de "flavor" (Java, JS, Python, etc.) y generador de código.

| Elemento | Significado |
|:--:|:--|
| `\d` `\w` `\s` `.` | dígito / palabra / espacio / cualquier carácter |
| `*` `+` `?` `{n,m}` | 0+, 1+, 0 o 1, rango de repeticiones |
| `^` `$` `\b` | inicio, fin, límite de palabra |
| `(...)` `(?:...)` | grupo de captura / sin captura |
| `\|` | alternancia (o) |
| `[abc]` `[^abc]` | clase de caracteres / negación |
| `(?=...)` `(?!...)` | lookahead positivo / negativo |

* **En Java:** `java.util.regex.Pattern` + `Matcher` (`matches()`, `find()`, `group()`, `replaceAll()`).
* **En JavaFX:** `TextFormatter` permite restringir en tiempo real lo que el usuario escribe (ej. campo solo numérico).

---

## 7. Consumo de API e Integración con IA

* Se usa `java.net.http.HttpClient` (Java 11+) para peticiones `POST`/`GET` sin librerías externas.
* Las credenciales (`API_KEY`) se leen de **variables de entorno** (`System.getenv(...)`), nunca escritas en el código.
* La respuesta JSON se navega con `org.json` (`JSONObject`/`JSONArray`), extrayendo solo el texto útil.
* Esta lógica de conexión pertenece a la capa de **Servicio** (o `Lógica`), nunca al Controller, respetando responsabilidad única.

---

# Parte Práctica

## 1. Estructura de carpetas (proyecto MVC + Maven + JavaFX)

Basado en los proyectos analizados (`avi-semana04-simple`, `Proyecto1Progra3`, `P3Prueba1`):

```
mi-proyecto/
├── pom.xml
├── data/                              ← "base de datos" en archivos
│   ├── usuarios.json
│   ├── usuarios.xml
│   └── bitacora.json
└── src/main/
    ├── java/
    │   └── una/eif206/avi/
    │       ├── App.java               ← extends Application (punto de entrada JavaFX)
    │       ├── Launcher.java          ← punto de entrada real (evita bug del módulo JavaFX)
    │       ├── dto/                   ← DTOs (estructura de datos)
    │       │   ├── UsuarioDTO.java
    │       │   └── LoginDTO.java
    │       ├── datos/                 ← Capa de Datos (JSON / XML)
    │       │   ├── UsuarioJsonDatos.java
    │       │   └── UsuarioXmlDatos.java
    │       ├── logica/                ← Capa de Lógica (reglas de negocio)
    │       │   └── LoginLogica.java
    │       ├── service/               ← Capa de Servicios
    │       │   └── UsuarioService.java
    │       └── controllers/           ← Controladores (UI)
    │           ├── LoginViewController.java
    │           └── ChatViewController.java
    └── resources/
        └── una/eif206/avi/ui/
            ├── home-view.fxml
            ├── login-view.fxml
            └── chat-view.fxml
```

> **Flujo de dependencia entre capas:** `UI → Service → Lógica → Datos → (archivo/BD)`. Cada capa solo conoce a la inmediatamente inferior.

---

## 2. Maven: `pom.xml` explicado línea por línea

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>

    <!-- Identificación del proyecto -->
    <groupId>una.eif206</groupId>       <!-- "paquete" organizacional -->
    <artifactId>avi-semana04</artifactId> <!-- nombre del proyecto -->
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <javafx.version>21.0.2</javafx.version> <!-- variable reutilizable -->
    </properties>

    <!-- Dependencies: librerías externas que el proyecto necesita -->
    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>  <!-- Botones, TextField, etc. -->
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>       <!-- Carga de archivos .fxml -->
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>              <!-- JSONObject / JSONArray -->
            <version>20240303</version>
        </dependency>
    </dependencies>

    <!-- Build: cómo se compila y empaqueta -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId> <!-- une JavaFX con Maven -->
                <version>0.0.8</version>
                <configuration>
                    <mainClass>una.eif206.avi.App</mainClass> <!-- clase con main() -->
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

* **`<dependencies>`:** librerías que se descargan de un repositorio (Maven Central) y quedan disponibles como `import`.
* **`<plugin>`:** extensiones que se ejecutan **durante** el build (compilar, empaquetar, correr). El `javafx-maven-plugin` permite ejecutar `mvn javafx:run`.
* **`<mainClass>`:** necesaria porque JavaFX exige que el módulo de la app declare su clase de arranque.

### `Launcher.java` — por qué existe

```java
package una.eif206.avi;

public class Launcher {
    public static void main(String[] args) {
        App.main(args);   // delega en la clase Application real
    }
}
```

> Java exige que la clase con `main()` **no** extienda directamente de `Application` cuando se genera un `.jar` ejecutable sin módulos (`module-info.java`), porque el *classloader* de JavaFX falla al buscar el módulo. `Launcher` es una clase "puente" sin esa herencia que simplemente llama a `App.main()`.

### `App.java` — punto de entrada JavaFX

```java
package una.eif206.avi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        Parent raiz = FXMLLoader.load(getClass().getResource("/una/eif206/avi/ui/home-view.fxml"));
        escenarioPrincipal.setTitle("AVI - Agente Virtual Inteligente");
        escenarioPrincipal.setScene(new Scene(raiz, 480, 640));
        escenarioPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args); // arranca el ciclo de vida de JavaFX (init → start → stop)
    }
}
```

* `package` agrupa clases relacionadas; `import` trae clases de otros paquetes/librerías.
* `getClass().getResource("/ruta")` busca el archivo **dentro del classpath** (por eso el FXML vive en `src/main/resources`, que Maven copia tal cual al `.jar`).

---

## 3. Capa DTO

```java
package una.eif206.avi.dto;

public class UsuarioDTO {
    private String usuario;
    private String contrasena;

    public UsuarioDTO() { }  // constructor vacío: requerido para mapear JSON/XML

    public UsuarioDTO(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
```

> Solo propiedades + `get`/`set` + constructores. **Cero lógica de negocio.**

---

## 4. Capa de Datos — JSON como "base de datos"

```java
package una.eif206.avi.datos;

import org.json.JSONArray;
import org.json.JSONObject;
import una.eif206.avi.dto.UsuarioDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioJsonDatos {

    private final Path rutaArchivo;

    public UsuarioJsonDatos(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public boolean existeArchivo() {
        return Files.exists(rutaArchivo);
    }

    // --- GUARDAR (Create/Update masivo) ---
    public void guardarUsuarios(List<UsuarioDTO> usuarios) throws IOException {
        crearDirectorioSiEsNecesario();

        JSONArray arreglo = new JSONArray();
        for (UsuarioDTO usuario : usuarios) {
            JSONObject objeto = new JSONObject();
            objeto.put("usuario", usuario.getUsuario());
            objeto.put("contrasena", usuario.getContrasena());
            arreglo.put(objeto);
        }

        JSONObject raiz = new JSONObject();
        raiz.put("usuarios", arreglo);

        Files.writeString(rutaArchivo, raiz.toString(4), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // --- LEER (Read) ---
    public List<UsuarioDTO> leerUsuarios() throws IOException {
        List<UsuarioDTO> usuarios = new ArrayList<>();
        if (!Files.exists(rutaArchivo)) return usuarios;

        String contenido = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
        JSONObject raiz = new JSONObject(contenido);
        JSONArray arreglo = raiz.optJSONArray("usuarios");
        if (arreglo == null) return usuarios;

        for (int i = 0; i < arreglo.length(); i++) {
            JSONObject objeto = arreglo.getJSONObject(i);
            usuarios.add(new UsuarioDTO(
                    objeto.optString("usuario", ""),
                    objeto.optString("contrasena", "")
            ));
        }
        return usuarios;
    }

    // --- Validación puntual (usa lambda dentro de un stream) ---
    public boolean validarCredenciales(String usuario, String contrasena) throws IOException {
        return leerUsuarios().stream()
                .anyMatch(u -> u.getUsuario().equals(usuario)
                        && u.getContrasena().equals(contrasena));
    }

    private void crearDirectorioSiEsNecesario() throws IOException {
        Path directorio = rutaArchivo.getParent();
        if (directorio != null) Files.createDirectories(directorio);
    }
}
```

**Ejemplo del archivo `usuarios.json` resultante:**

```json
{
    "usuarios": [
        { "usuario": "admin", "contrasena": "1234" }
    ]
}
```

## 5. Capa de Datos — XML como "base de datos"

```java
package una.eif206.avi.datos;

import org.w3c.dom.*;
import una.eif206.avi.dto.UsuarioDTO;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioXmlDatos {

    private final Path rutaArchivo;

    public UsuarioXmlDatos(Path rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public void guardarUsuarios(List<UsuarioDTO> usuarios) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.newDocument();

        Element raiz = documento.createElement("usuarios");
        documento.appendChild(raiz);

        for (UsuarioDTO usuario : usuarios) {
            Element nodoUsuario = documento.createElement("usuario");

            Element nombre = documento.createElement("nombre");
            nombre.setTextContent(usuario.getUsuario());
            nodoUsuario.appendChild(nombre);

            Element contrasena = documento.createElement("contrasena");
            contrasena.setTextContent(usuario.getContrasena());
            nodoUsuario.appendChild(contrasena);

            raiz.appendChild(nodoUsuario);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(documento), new StreamResult(rutaArchivo.toFile()));
    }

    public List<UsuarioDTO> leerUsuarios() throws Exception {
        List<UsuarioDTO> usuarios = new ArrayList<>();
        if (!Files.exists(rutaArchivo)) return usuarios;

        Document documento = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(rutaArchivo.toFile());
        documento.getDocumentElement().normalize();

        NodeList nodosUsuario = documento.getElementsByTagName("usuario");
        for (int i = 0; i < nodosUsuario.getLength(); i++) {
            Element elemento = (Element) nodosUsuario.item(i);
            usuarios.add(new UsuarioDTO(
                    obtenerTexto(elemento, "nombre"),
                    obtenerTexto(elemento, "contrasena")
            ));
        }
        return usuarios;
    }

    // Método privado dentro de la clase, invocado por leerUsuarios()
    private String obtenerTexto(Element elemento, String etiqueta) {
        NodeList nodos = elemento.getElementsByTagName(etiqueta);
        return nodos.getLength() > 0 ? nodos.item(0).getTextContent() : "";
    }
}
```

**Ejemplo del archivo `usuarios.xml` resultante:**

```xml
<usuarios>
    <usuario>
        <nombre>admin</nombre>
        <contrasena>1234</contrasena>
    </usuario>
</usuarios>
```

---

## 6. Capa de Lógica (reglas de negocio + orquestación de Datos)

```java
package una.eif206.avi.logica;

import una.eif206.avi.datos.UsuarioJsonDatos;
import una.eif206.avi.datos.UsuarioXmlDatos;
import una.eif206.avi.datos.LoginJsonDatos;
import una.eif206.avi.dto.LoginDTO;
import una.eif206.avi.dto.UsuarioDTO;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoginLogica {

    public static final String FUENTE_JSON = "JSON";
    public static final String FUENTE_XML = "XML";

    // Instancias NO estáticas: cada LoginLogica maneja sus propios objetos de Datos
    private final UsuarioJsonDatos jsonDatos;
    private final UsuarioXmlDatos xmlDatos;
    private final LoginJsonDatos bitacoraDatos;

    public LoginLogica() {
        this.jsonDatos = new UsuarioJsonDatos(Path.of("data", "usuarios.json"));
        this.xmlDatos = new UsuarioXmlDatos(Path.of("data", "usuarios.xml"));
        this.bitacoraDatos = new LoginJsonDatos(Path.of("data", "bitacora.json"));
    }

    public boolean iniciarSesion(String usuario, String contrasena, String fuente) throws Exception {
        // Validación de reglas de negocio (aquí, no en el Controller)
        if (usuario == null || usuario.isBlank())
            throw new IllegalArgumentException("Debe ingresar el usuario.");
        if (contrasena == null || contrasena.isBlank())
            throw new IllegalArgumentException("Debe ingresar la contraseña.");

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.trim(), contrasena);
        boolean credencialesValidas = validarCredenciales(usuarioDTO, fuente);
        registrarIntento(usuarioDTO.getUsuario(), fuente, credencialesValidas);
        return credencialesValidas;
    }

    private boolean validarCredenciales(UsuarioDTO usuario, String fuente) throws Exception {
        if (FUENTE_JSON.equalsIgnoreCase(fuente)) {
            return jsonDatos.validarCredenciales(usuario.getUsuario(), usuario.getContrasena());
        }
        if (FUENTE_XML.equalsIgnoreCase(fuente)) {
            return xmlDatos.validarCredenciales(usuario.getUsuario(), usuario.getContrasena());
        }
        throw new IllegalArgumentException("Fuente de datos no válida.");
    }

    private void registrarIntento(String usuario, String fuente, boolean exitoso) throws Exception {
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora = ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String resultado = exitoso ? "Exitoso" : "Fallido";
        bitacoraDatos.registrarIntento(new LoginDTO(usuario, fecha, hora, fuente, resultado));
    }
}
```

> Nótese el mapeo dataset→DTO (`objeto.optString(...)` → `UsuarioDTO`) y cómo la Lógica decide **cuál** fuente de Datos usar (JSON o XML) según la regla de negocio, sin que el Controller lo sepa.

---

## 7. Capa de Servicios (puente entre UI y Lógica)

```java
package una.eif206.avi.service;

import una.eif206.avi.dto.UsuarioDTO;
import una.eif206.avi.logica.LoginLogica;
import java.util.List;

public class UsuarioService {

    // Instancia propia de Lógica: NO estática, se crea al instanciar el Service
    private final LoginLogica logica;

    public UsuarioService() {
        this.logica = new LoginLogica();
    }

    public boolean iniciarSesion(String usuario, String contrasena, String fuente) throws Exception {
        return logica.iniciarSesion(usuario, contrasena, fuente); // solo reenvía
    }

    public void guardarUsuariosEnAmbasFuentes(List<UsuarioDTO> usuarios) throws Exception {
        logica.guardarUsuariosEnAmbasFuentes(usuarios);
    }
}
```

> El Service **no valida nada**, solo enruta. Esa es su definición teórica: la capa "más rápida".

---

## 8. Capa UI — Controller completo (listeners, alertas, threads, lambdas)

```java
package una.eif206.avi.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import una.eif206.avi.service.UsuarioService;

public class LoginViewController {

    private static final int MAX_INTENTOS_FALLIDOS = 3;
    private static final int SEGUNDOS_BLOQUEO = 30;

    // Referencias a los nodos del FXML: el fx:id debe coincidir exactamente
    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoContrasena;
    @FXML private ComboBox<String> comboFuenteDatos;
    @FXML private Button botonIngresar;
    @FXML private Button botonCancelar;
    @FXML private Label etiquetaError;

    // Instancia propia del Service (manejo NO estático)
    private final UsuarioService usuarioService = new UsuarioService();
    private int intentosFallidos = 0;
    private PauseTransition pausaBloqueo;

    // Se ejecuta automáticamente al cargar el FXML
    @FXML
    private void initialize() {
        comboFuenteDatos.getItems().setAll("JSON", "XML");
        comboFuenteDatos.getSelectionModel().selectFirst();

        // setOnAction con referencia a método (method reference)
        botonIngresar.setOnAction(this::intentarIngresar);

        // setOnAction con lambda (función anónima inline)
        botonCancelar.setOnAction(evento -> {
            detenerBloqueo();
            cambiarPantalla(evento, "home-view.fxml");
        });
    }

    private void intentarIngresar(ActionEvent evento) {
        etiquetaError.setText("");
        String usuario = campoUsuario.getText();
        String contrasena = campoContrasena.getText();
        String fuente = comboFuenteDatos.getValue();

        try {
            boolean credencialesValidas = usuarioService.iniciarSesion(usuario, contrasena, fuente);

            if (credencialesValidas) {
                intentosFallidos = 0;
                mostrarMensaje(Alert.AlertType.INFORMATION, "Inicio de sesión",
                        "Inicio de sesión exitoso usando " + fuente + ".");
                cambiarPantalla(evento, "chat-list-view.fxml");
            } else {
                intentosFallidos++;
                String mensaje = "Usuario o contraseña incorrectos. Intentos: "
                        + intentosFallidos + " de " + MAX_INTENTOS_FALLIDOS + ".";
                etiquetaError.setText(mensaje);
                mostrarMensaje(Alert.AlertType.ERROR, "Inicio de sesión", mensaje);

                if (intentosFallidos >= MAX_INTENTOS_FALLIDOS) {
                    bloquearTemporalmente();
                }
            }
        } catch (IllegalArgumentException e) {
            etiquetaError.setText(e.getMessage());
        } catch (Exception e) {
            etiquetaError.setText("Ocurrió un error al consultar la fuente de datos.");
        }
    }

    // Bloqueo temporal usando PauseTransition (hilo NO bloqueante del UI thread)
    private void bloquearTemporalmente() {
        campoUsuario.setDisable(true);
        botonIngresar.setDisable(true);
        etiquetaError.setText("Demasiados intentos. Espere " + SEGUNDOS_BLOQUEO + " segundos.");

        pausaBloqueo = new PauseTransition(Duration.seconds(SEGUNDOS_BLOQUEO));
        // lambda ejecutada cuando termina la pausa
        pausaBloqueo.setOnFinished(finalizado -> {
            intentosFallidos = 0;
            campoUsuario.setDisable(false);
            botonIngresar.setDisable(false);
            etiquetaError.setText("");
        });
        pausaBloqueo.play();
    }

    private void detenerBloqueo() {
        if (pausaBloqueo != null) pausaBloqueo.stop();
    }

    // Pop-up de alerta reutilizable (método dentro de la clase, invocado varias veces)
    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Cambio de pantalla reutilizable
    private void cambiarPantalla(ActionEvent evento, String archivoFxml) {
        try {
            Parent raiz = FXMLLoader.load(getClass().getResource("/una/eif206/avi/ui/" + archivoFxml));
            Stage stage = (Stage) ((Node) evento.getSource()).getScene().getWindow();
            stage.getScene().setRoot(raiz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

**Conceptos ilustrados en este controller:**
* `setOnAction(this::metodo)` vs `setOnAction(evento -> { ... })` — referencia a método vs lambda.
* `Alert` como **pop-up** (`INFORMATION`, `ERROR`, `WARNING`) con `showAndWait()`.
* `PauseTransition` como **hilo no bloqueante** del hilo de UI (equivalente ligero a un `Thread` con `sleep`, sin congelar la interfaz).
* Métodos privados reutilizables **dentro** de la misma clase (`mostrarMensaje`, `cambiarPantalla`).
* Manejo de instancias (`new UsuarioService()`), **no estático** — cada `Controller` crea su propio `Service`.

---

## 9. Ejemplo CRUD con `TableView` (patrón visto en los proyectos de examen)

```java
public class GestionProyectoController {

    @FXML private TableView<Project> tbl_project;
    @FXML private TextField txt_description_project;
    @FXML private Button btn_create_project;

    private final ProjectService projectService = new ProjectService();
    private final ObservableList<Project> masterProjects = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureProjectTable();
        reloadProjectsTable();
        btn_create_project.setOnAction(this::crearProyecto);

        // Doble clic en fila = selección/edición (lambda + comprobación de botón)
        tbl_project.setOnMouseClicked(evento -> {
            if (evento.getButton() == MouseButton.PRIMARY && evento.getClickCount() == 2) {
                Project seleccionado = tbl_project.getSelectionModel().getSelectedItem();
                if (seleccionado != null) abrirEdicion(seleccionado);
            }
        });
    }

    private void configureProjectTable() {
        TableColumn<Project, String> colDesc =
                (TableColumn<Project, String>) tbl_project.getColumns().get(0);
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        // SortedList: permite ordenar la tabla sin alterar la lista real
        SortedList<Project> ordenados = new SortedList<>(masterProjects);
        ordenados.comparatorProperty().bind(tbl_project.comparatorProperty());
        tbl_project.setItems(ordenados);
    }

    private void reloadProjectsTable() {
        try {
            masterProjects.setAll(projectService.getAllProjects()); // Read
        } catch (Exception e) {
            Utilities.showAlert("Error", "No se pudieron cargar los proyectos", Alert.AlertType.ERROR);
        }
    }

    private void crearProyecto(ActionEvent evento) {
        try {
            projectService.create(txt_description_project.getText()); // Create
            reloadProjectsTable();
            txt_description_project.clear();
        } catch (Exception e) {
            Utilities.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void abrirEdicion(Project proyecto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editar-proyecto.fxml"));
            Parent raiz = loader.load();
            EditarProyectoController controller = loader.getController();
            controller.recibirDatos(proyecto);   // paso de DTO entre pantallas

            Stage ventana = new Stage();
            ventana.initModality(Modality.APPLICATION_MODAL); // pop-up modal
            ventana.setScene(new Scene(raiz));
            ventana.showAndWait();

            reloadProjectsTable(); // refrescar tras editar/eliminar
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

**Clase utilitaria de alertas reutilizable (`Utilities.java`):**

```java
public class Utilities {
    public static void showAlert(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
```

---

## 10. Resumen del flujo completo (de un clic a un archivo JSON/XML)

```
Usuario hace clic en "Ingresar"
        │
        ▼
Controller.intentarIngresar()  ──► captura texto de TextField/PasswordField
        │
        ▼
UsuarioService.iniciarSesion() ──► solo reenvía (capa Servicios)
        │
        ▼
LoginLogica.iniciarSesion()    ──► valida reglas de negocio (campos vacíos, etc.)
        │
        ▼
UsuarioJsonDatos / UsuarioXmlDatos ──► lee/escribe el archivo (capa Datos)
        │
        ▼
Resultado boolean regresa por la misma cadena hasta el Controller
        │
        ▼
Controller muestra Alert (pop-up) y cambia de pantalla con FXMLLoader
```

---

## Ampliación — API/IA, Threads, XML avanzado, Print (PDF) y JUnit (Surefire/Failsafe)

### 1. Servicio de API con IA — `GeminiService` (versión estructurada)

A diferencia de la versión simple, esta versión del proyecto **le exige a la IA que responda en un JSON con estructura fija**, usando *prompt engineering* directamente en el código con un **text block** de Java (`"""..."""`):

```java
package resourcemanager.service;

import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class GeminiService {
    private static final String MODELO = "gemini-3.6-flash";
    private static final String ENDPOINT_BASE =
            "https://generativelanguage.googleapis.com/v1beta/models/";

    private final String apiKey;
    private final HttpClient httpClient;

    public GeminiService() {
        // La clave NUNCA se escribe en el código: se lee de una variable de entorno
        this.apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("No se encontro la variable de entorno GEMINI_API_KEY.");
        }
        this.httpClient = HttpClient.newHttpClient();
    }

    public String requestJSON(String prompt, JsonArray availableCategories) throws Exception {
        String hoy = LocalDate.now().toString(); // ISO-8601 (YYYY-MM-DD)

        // Text block (Java 15+): instrucción de "prompt engineering" que obliga
        // a la IA a responder SOLO con un JSON de forma predecible
        String instruccion = """

    Answer exclusively with a JSON following this format... use "null" if missing.
    {
      "description": "string",
      "date": "YYYY-MM-DD",
      "startHour": int,
      "endHour": int,
      "categories": ["cat1","cat2"...]
    }
    Do not wrap it in markdown code fences. Today's date is """ + hoy + """
    . The user requests: """ + prompt + ". Available categories: " + availableCategories;

        String url = ENDPOINT_BASE + MODELO + ":generateContent";
        JSONObject parte = new JSONObject().put("text", instruccion);
        JSONObject contenido = new JSONObject().put("parts", new JSONArray().put(parte));
        JSONObject cuerpo = new JSONObject().put("contents", new JSONArray().put(contenido));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(cuerpo.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new java.io.IOException("Error de la API (HTTP " + response.statusCode() + "): " + response.body());
        }
        return extraerTexto(response.body());
    }

    private String extraerTexto(String jsonRespuesta) {
        JSONObject raiz = new JSONObject(jsonRespuesta);
        JSONArray candidatos = raiz.getJSONArray("candidates");
        JSONObject contenido = candidatos.getJSONObject(0).getJSONObject("content");
        return contenido.getJSONArray("parts").getJSONObject(0).getString("text");
    }
}
```

**Puntos clave de la API:**
* El **prompt** describe explícitamente el formato de salida esperado (JSON), la fecha actual y las restricciones de negocio (no cruzar medianoche, no usar categorías inventadas) — esto se llama **prompt engineering estructurado**.
* `httpClient.send(...)` es una llamada **síncrona/bloqueante**: si se ejecuta directamente en el hilo de UI, la ventana se congela. Por eso siempre se envuelve en un `Thread` (ver siguiente sección).
* El código de estado HTTP (`response.statusCode()`) se valida antes de intentar leer el cuerpo, lanzando una excepción con contexto en vez de dejar que falle silenciosamente.

---

### 2. Threads: IA en segundo plano sin congelar la UI

Este es el patrón central de "Aplicaciones dirigidas por eventos" aplicado con IA. La **Lógica** expone un método con **callbacks (`Consumer`)** en vez de retornar un valor directamente, porque la respuesta llega en otro hilo:

```java
public class ReservationLogic {

    public void promptAI(String prompt, Consumer<GeneratedReservationDTO> onSuccess, Consumer<Exception> onError) {
        if (prompt == null || prompt.isBlank()) {
            onError.accept(new InvalidParameterException("Debe de describir la reserva"));
            return; // no se puede contestar una pregunta vacía
        }

        // Se crea un hilo nuevo para que Gemini trabaje EN PARALELO
        // sin bloquear el hilo principal de JavaFX
        Thread hiloGemini = new Thread(() -> {
            try {
                ArrayList<Category> categories = categoryLogic.findFreeCategories();
                if (categories.isEmpty()) throw new RuntimeException("No hay categorías con recursos libres");

                Gson gson = new GsonBuilder().create();
                JsonArray categoryJson = (JsonArray) gson.toJsonTree(categories,
                        new TypeToken<ArrayList<Category>>() {}.getType());

                GeminiService geminiService = new GeminiService();
                String jsonString = geminiService.requestJSON(prompt, categoryJson);
                GeneratedReservationDTO parsed = parseAI(jsonString, categories);

                // Regresar al hilo de JavaFX para tocar la UI de forma segura
                Platform.runLater(() -> onSuccess.accept(parsed));

            } catch (Exception e) {
                Platform.runLater(() -> onError.accept(e));
            }
        });

        hiloGemini.setDaemon(true); // el hilo no bloquea el cierre de la aplicación
        hiloGemini.start();
    }
}
```

**Consumo desde el Controller (UI):**

```java
btn_reserve_ai.setOnAction(event -> {
    String prompt = txt_reserve_prompt.getText().trim();
    if (prompt.isEmpty()) {
        Utilities.showAlert("Error", "Debe de describir la reserva", Alert.AlertType.ERROR);
        return;
    }

    btn_reserve_ai.setDisable(true);
    btn_reserve_ai.setText("Extrayendo...");

    reservationService.promptAI(
        prompt,
        generated -> {                       // onSuccess (lambda como Consumer<T>)
            btn_reserve_ai.setDisable(false);
            btn_reserve_ai.setText("Extraer con IA");
            applyAiSuggestion(generated);
        },
        error -> {                           // onError (lambda como Consumer<Exception>)
            btn_reserve_ai.setDisable(false);
            btn_reserve_ai.setText("Extraer con IA");
            String mensaje = error.getMessage() != null ? error.getMessage() : "Error inesperado";
            Utilities.showAlert("Error", mensaje, Alert.AlertType.ERROR);
        }
    );
});
```

| Concepto | Explicación |
|:--|:--|
| `Thread hiloGemini = new Thread(() -> {...})` | Ejecuta una tarea en paralelo al hilo principal (`() -> {}` = `Runnable` como lambda) |
| `.setDaemon(true)` | El hilo muere automáticamente al cerrar la app (no "detiene" el proceso principal) |
| `Platform.runLater(() -> ...)` | **Obligatorio** en JavaFX: cualquier cambio a un nodo de la UI debe ocurrir en el hilo de JavaFX, nunca desde un `Thread` externo |
| `Consumer<T>` | Interfaz funcional (`java.util.function`) usada como **callback**: permite que la Lógica "avise" al Controller cuando termina, sin que la Lógica conozca la UI |
| Botón deshabilitado + texto cambiado | Patrón de UX para indicar carga (feedback visual) mientras se espera la red |

---

### 3. Capa de Datos XML avanzada (Jackson `XmlMapper` + StAX)

En vez de construir el XML manualmente con `Document`/`Element` (como en el ejemplo simple), este proyecto usa **Jackson (`XmlMapper`)** para mapear objetos Java directamente a XML, combinado con **StAX** (`XMLStreamWriter`/`XMLStreamReader`) para poder guardar/leer **listas** con un tag raíz genérico.

**`MapperSingleton` — una única instancia configurada del mapper:**

```java
public class MapperSingleton {
    private static final XmlMapper mapper = new XmlMapper();
    static {
        mapper.registerModule(new JavaTimeModule()); // para que entienda LocalDate
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // fechas como texto, no como arreglo de números
    }

    private MapperSingleton() {} // constructor privado: nadie puede instanciarla

    public static XmlMapper getInstance() { return mapper; }
}
```

> Patrón **Singleton**: constructor privado + método estático `getInstance()`. Se usa porque crear un `XmlMapper` es costoso y su configuración debe ser **la misma** en toda la app.

**`DataPaths` — centraliza las rutas y permite cambiarlas para pruebas:**

```java
public class DataPaths {
    static final String USERS_PATH = "/resourcemanager/data/users.xml";

    public static File getUsersFile() throws Exception {
        return pathToFile(USERS_PATH);
    }

    private static File pathToFile(String classpathPath) throws Exception {
        // Permite que las pruebas de integración usen otra carpeta sin tocar los datos reales
        String baseDir = System.getProperty("resourcemanager.dataDir", "src/main/resources");
        return new File(baseDir + classpathPath);
    }
}
```

**`SaveToXML` — escritura por streaming + archivo temporal (operación atómica):**

```java
public <T> void saveList(File file, String rootTag, String itemTag, ArrayList<T> items) throws Exception {
    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

    // Se escribe primero en un archivo TEMPORAL para no corromper el original si algo falla
    File tempFile = new File(file.getAbsolutePath() + ".tmp");

    try (Writer fileWriter = new FileWriter(tempFile)) {
        XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(fileWriter);
        xmlStreamWriter.writeStartDocument();
        xmlStreamWriter.writeStartElement(rootTag);

        XmlFactory xmlFactory = (XmlFactory) mapper.getFactory();
        ToXmlGenerator generator = xmlFactory.createGenerator(xmlStreamWriter);
        ObjectWriter itemWriter = mapper.writer().withRootName(itemTag);

        for (T item : items) {
            generator.setNextName(new QName(itemTag)); // tag para cada objeto
            itemWriter.writeValue(generator, item);     // Jackson serializa el objeto Java
        }

        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
        xmlStreamWriter.close();
    } catch (Exception e) {
        tempFile.delete(); // se descarta el intento fallido; el original queda intacto
        throw e;
    }

    // Solo si TODO salió bien, se reemplaza el archivo real (operación atómica)
    Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
}

// Métodos de conveniencia que reutilizan saveList()
public void overwriteUsers(ArrayList<User> items) throws Exception {
    saveList(DataPaths.getUsersFile(), "users", "user", items);
}
```

**Actualizar un elemento dentro de la lista (patrón "cargar todo → modificar → sobreescribir todo"):**

```java
public boolean updateUser(User updatedUser) throws Exception {
    ArrayList<User> users = loadFromXML.loadUsers(); // 1. cargar TODO el XML

    for (int i = 0; i < users.size(); i++) {
        if (users.get(i).getId().equals(updatedUser.getId())) {
            users.set(i, updatedUser);   // 2. reemplazar el elemento en memoria
            overwriteUsers(users);       // 3. sobreescribir el archivo completo
            return true;
        }
    }
    return false; // no se encontró
}
```

> **Por qué "cargar todo y sobreescribir todo":** un archivo XML no permite editar un solo registro sin reescribir su estructura completa (a diferencia de una BD relacional con `UPDATE`). Por eso el patrón estándar en archivos planos es: leer la lista completa → modificarla en memoria (con el `id` como llave) → volver a escribir el archivo entero.

**`LoadFromXML` — lectura con `XMLStreamReader` (evento por evento, más liviano que cargar el DOM completo):**

```java
private <T> ArrayList<T> loadList(File file, String itemTagName, Class<T> itemClass) throws Exception {
    ArrayList<T> results = new ArrayList<>();
    if (!file.exists()) return results;

    try (InputStream is = new FileInputStream(file)) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(is);

        while (reader.hasNext()) {
            int event = reader.next();
            // Solo cuando encuentra la etiqueta de apertura del item buscado
            if (event == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals(itemTagName)) {
                T item = mapper.readValue(reader, itemClass); // Jackson lo convierte a objeto Java
                results.add(item);
            }
        }
    }
    return results;
}

public ArrayList<User> loadUsers() throws Exception {
    return loadList(DataPaths.getUsersFile(), "user", User.class);
}
```

**Archivo `users.xml` resultante:**

```xml
<?xml version="1.0" ?>
<users>
    <user>
        <id>100000000</id>
        <name>Juan Perez</name>
        <password>100000000</password>
        <phoneNumber>1111-2222</phoneNumber>
        <isAdmin>false</isAdmin>
    </user>
</users>
```

| Herramienta | Rol |
|:--|:--|
| `XmlMapper` (Jackson) | Convierte objetos Java ↔ XML automáticamente (como un ORM, pero para XML) |
| `XMLStreamWriter`/`Reader` (StAX) | Escribe/lee el XML como un **flujo de eventos**, permitiendo controlar manualmente el tag raíz y procesar archivos grandes sin cargarlos completos en memoria |
| Archivo `.tmp` + `Files.move(..., REPLACE_EXISTING)` | Evita que el archivo quede corrupto si el programa se cierra a medias mientras escribe |

---

### 4. "Print" del proyecto — dos significados distintos

#### a) `System.out.println` — impresión de depuración (debug/logging)

```java
// Launcher.java — imprime la ruta del FXML cargado para verificar que se encuentra
java.lang.System.out.println(getClass().getResource("ui/login.fxml"));
Parent raiz = FXMLLoader.load(getClass().getResource("ui/login.fxml"));
```

```java
// ReservationLogic.java — imprime cada id mientras busca una reserva (debug de recorrido)
for (Reservation r : allReservations) {
    System.out.println(r.getId());
    if (r.getId().equals(id)) return r;
}
```

> Uso típico durante desarrollo para verificar rutas de recursos o el contenido real de una lista antes de confirmar que la lógica funciona. **No es una buena práctica dejarlo en producción** (se reemplazaría por un `Logger`), pero es común encontrarlo en proyectos académicos.

También se ajusta el nivel de log de librerías externas para no llenar la consola de warnings:

```java
// Evitar que PDFBox llene la consola de warnings sobre fuentes al generar PDFs
Logger.getLogger("org.apache.fontbox").setLevel(Level.SEVERE);
Logger.getLogger("org.apache.pdfbox").setLevel(Level.SEVERE);
```

#### b) "Print" como generación de PDF (impresión real de reportes)

`PrintLogic` genera un PDF **genérico** a partir de cualquier lista de objetos, usando **reflexión** (`Field[]`) para leer los atributos sin conocer la clase de antemano:

```java
public <T> File generatePdf(List<T> items, Class<T> classType, String fileName) throws Exception {
    PDDocument document = new PDDocument();

    // Reflexión: obtiene los atributos de la clase para usarlos como columnas
    Field[] fields = classType.getDeclaredFields();
    for (Field f : fields) f.setAccessible(true);

    // Decide orientación según cuántas columnas caben en la hoja
    boolean landscape = (fields.length * 150) + 100 > PDRectangle.A4.getWidth();
    PDPage page = new PDPage(landscape
            ? new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())
            : PDRectangle.A4);
    document.addPage(page);

    PDPageContentStream content = new PDPageContentStream(document, page);
    // ... escribe encabezados con el nombre de cada Field ...
    for (T item : items) {
        for (Field f : fields) {
            Object value = f.get(item);              // lee el valor por reflexión
            String text = value != null ? value.toString() : "";
            content.showText(text);
        }
    }
    content.close();

    File file = new File(fileName);
    document.save(file);
    document.close();
    return file;
}

public void openPdf(File file) throws Exception {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(file); // abre el PDF con el visor predeterminado del SO
    }
}
```

**`PrintService` (capa Servicios) simplemente reenvía a la Lógica:**

```java
public class PrintService {
    private PrintLogic printLogic = new PrintLogic();

    public <T> File generatePdf(List<T> items, Class<T> classType, String fileName) throws Exception {
        return printLogic.generatePdf(items, classType, fileName);
    }

    public void openPdf(File file) throws Exception {
        printLogic.openPdf(file);
    }
}
```

**Uso desde el Controller:**

```java
btn_reserve_print.setOnAction(event -> {
    try {
        User currentUser = userService.getLoggedUser();
        userService.printUserReservations(currentUser); // genera y abre el PDF
    } catch (Exception e) {
        Utilities.showAlert("Error", "Error al generar el PDF: " + e.getMessage(), Alert.AlertType.ERROR);
    }
});
```

---

### 5. Pruebas con JUnit 5, Surefire y Failsafe

#### Configuración en `pom.xml`

```xml
<properties>
    <junit.version>5.11.3</junit.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>  <!-- solo disponible al compilar/correr pruebas, no en producción -->
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- SUREFIRE: corre las pruebas UNITARIAS rápidas (fase "test") -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.5.2</version>
            <configuration>
                <excludes>
                    <exclude>**/*IT.java</exclude> <!-- excluye las pruebas de integración -->
                </excludes>
            </configuration>
        </plugin>

        <!-- FAILSAFE: corre las pruebas de INTEGRACIÓN (fase "integration-test") -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-failsafe-plugin</artifactId>
            <version>3.5.2</version>
            <executions>
                <execution>
                    <goals>
                        <goal>integration-test</goal>
                        <goal>verify</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <includes>
                    <include>**/*IT.java</include> <!-- solo corre las que terminan en "IT" -->
                </includes>
                <systemPropertyVariables>
                    <!-- redirige los archivos XML a una carpeta de prueba, para no tocar los datos reales -->
                    <resourcemanager.dataDir>${project.build.directory}/test-data</resourcemanager.dataDir>
                </systemPropertyVariables>
            </configuration>
        </plugin>
    </plugins>
</build>
```

| Plugin | Fase de Maven | Qué corre | Convención de nombre |
|:--|:--|:--|:--|
| **Surefire** | `test` | Pruebas **unitarias** (rápidas, sin tocar archivos/red reales) | `*Test.java` |
| **Failsafe** | `integration-test` / `verify` | Pruebas de **integración** (leen/escriben archivos XML reales, llaman APIs) | `*IT.java` |

> **Por qué separarlas:** Surefire corre siempre (incluso en cada `mvn test`), mientras que Failsafe corre en la fase `verify`, después de haber empaquetado la app — así las pruebas lentas (que tocan disco o red) no frenan el ciclo normal de desarrollo.

#### Prueba unitaria (`*Test.java`) — no toca archivos ni red

```java
class AuthLogicTest {

    private final AuthLogic authLogic = new AuthLogic();

    // @ParameterizedTest: corre el mismo test con varios valores distintos
    @ParameterizedTest
    @ValueSource(strings = {"Abcdefg1!", "Xy9$zzzz", "Cl4ve#Segura"})
    void satisfiesPolicy_validPassword_returnsTrue(String password) {
        assertTrue(authLogic.satisfiesPolicy(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "abcdefgh", "12345678"})
    void satisfiesPolicy_invalidPassword_returnsFalse(String password) {
        assertFalse(authLogic.satisfiesPolicy(password));
    }

    @Test
    void satisfiesPolicy_nullPassword_returnsFalse() {
        assertFalse(authLogic.satisfiesPolicy(null));
    }

    @Test
    void verifyPhone_matchingPhoneNumber_returnsTrue() {
        User user = new User("111111111", "Juan Perez", "x", false);
        user.setPhoneNumber("2222-3333");
        assertTrue(authLogic.verifyPhone(user, "2222-3333"));
    }
}
```

* **Convención de nombres:** `metodo_escenario_resultadoEsperado` (ej. `satisfiesPolicy_nullPassword_returnsFalse`) — hace autoexplicativo qué se prueba.
* `@ParameterizedTest` + `@ValueSource` evita repetir el mismo `@Test` con distintos valores.
* `assertTrue`, `assertFalse`, `assertEquals`, `assertThrows`, `assertDoesNotThrow`, `assertNotNull` — aserciones estándar de JUnit 5 (`org.junit.jupiter.api.Assertions`).

#### Prueba de integración (`*IT.java`) — sí toca archivos XML reales (redirigidos a `target/test-data`)

```java
class UserLogicIT {

    private final UserLogic userLogic = new UserLogic();

    // Se ejecuta ANTES de cada @Test: limpia los XML para que las pruebas no se contaminen entre sí
    @BeforeEach
    void resetXml() throws Exception {
        TestDataSupport.resetXmlFiles();
    }

    @Test
    void addUser_validData_persists() throws Exception {
        User created = userLogic.addUser("100000000", "Juan Perez", "1111-2222");

        assertEquals("100000000", created.getId());
        assertEquals("100000000", created.getPassword()); // clave inicial = id
        assertFalse(created.getIsAdmin());
    }

    @Test
    void addUser_duplicateId_throwsException() throws Exception {
        userLogic.addUser("100000003", "Juan Perez", "1111-2222");
        assertThrows(Exception.class, () -> userLogic.addUser("100000003", "Otro Nombre", "3333-4444"));
    }

    @Test
    void updateUserInfo_validData_updatesNameAndPhone() throws Exception {
        userLogic.addUser("100000006", "Juan Perez", "1111-2222");
        assertTrue(userLogic.updateUserInfo("100000006", "Juan Actualizado", "5555-6666"));

        User found = userLogic.findUserById("100000006");
        assertEquals("Juan Actualizado", found.getName());
    }
}
```

**Clase de apoyo para las pruebas (`TestDataSupport`):**

```java
public final class TestDataSupport {
    public static final Path DATA_DIR = Path.of("target/test-data/resourcemanager/data");

    private TestDataSupport() {} // clase utilitaria: constructor privado, solo métodos estáticos

    public static void resetXmlFiles() throws Exception {
        Files.createDirectories(DATA_DIR);
        writeEmptyRoot("categories");
        writeEmptyRoot("resources");
        writeEmptyRoot("reservations");
        writeEmptyRoot("users");
    }

    private static void writeEmptyRoot(String rootTag) throws Exception {
        String xmlContent = "<?xml version=\"1.0\"?><" + rootTag + "></" + rootTag + ">";
        Files.writeString(DATA_DIR.resolve(rootTag + ".xml"), xmlContent);
    }
}
```

**Prueba de integración condicional (solo corre si hay clave de API disponible):**

```java
@EnabledIfEnvironmentVariable(named = "GEMINI_API_KEY", matches = ".+")
class GeminiServiceIT {

    @Test
    void requestJSON_simplePrompt_returnsNonEmptyText() throws Exception {
        GeminiService geminiService = new GeminiService();
        JsonArray availableCategories = new JsonArray();
        availableCategories.add("cat1");

        String result = geminiService.requestJSON("Necesito una sala mañana de 9am a 10am", availableCategories);

        assertNotNull(result);
        assertFalse(result.isBlank());
    }
}
```

**Prueba de integración que genera archivos reales (PDF):**

```java
class PrintLogicIT {

    private final PrintLogic printLogic = new PrintLogic();

    @BeforeAll // se ejecuta UNA sola vez, antes de todos los @Test de la clase
    static void createOutputDir() throws Exception {
        Files.createDirectories(Path.of("target/test-output"));
    }

    @Test
    void generatePdf_withItems_createsNonEmptyFile() throws Exception {
        ArrayList<CategoryCountDTO> rows = new ArrayList<>();
        rows.add(new CategoryCountDTO("Sala de Juntas", 3));

        File pdf = printLogic.generatePdf(rows, CategoryCountDTO.class, "target/test-output/prueba.pdf");

        assertTrue(pdf.exists());
        assertTrue(pdf.length() > 0);
    }

    @Test
    void generatePdf_manyItems_createsMultiplePagesWithoutError() throws Exception {
        ArrayList<CategoryCountDTO> rows = new ArrayList<>();
        for (int i = 0; i < 60; i++) rows.add(new CategoryCountDTO("Categoria " + i, i));

        File pdf = printLogic.generatePdf(rows, CategoryCountDTO.class, "target/test-output/prueba_muchas.pdf");
        assertTrue(pdf.exists());
    }
}
```

| Anotación | Cuándo se ejecuta |
|:--|:--|
| `@BeforeEach` | Antes de **cada** `@Test` (limpia estado, ej. reiniciar XML) |
| `@BeforeAll` | **Una sola vez**, antes de todos los tests de la clase (debe ser `static`) — ej. crear carpetas |
| `@Test` | El caso de prueba individual |
| `@ParameterizedTest` + `@ValueSource` | Repite el mismo test con múltiples entradas |
| `@EnabledIfEnvironmentVariable` | Corre el test solo si existe una variable de entorno (evita fallar en CI sin clave de API) |
| `assertThrows(Exception.class, () -> ...)` | Verifica que un bloque de código lance una excepción |

---

### 6. Otros elementos de contexto (Singletons de aplicación)

```java
// Guarda el Stage principal para que cualquier clase pueda abrir ventanas nuevas sobre él
public class AppContext {
    private static Stage primaryStage;
    private AppContext() {}

    public static void setPrimaryStage(Stage stage) { primaryStage = stage; }
    public static Stage getPrimaryStage() { return primaryStage; }
}

// Guarda quién inició sesión, accesible desde cualquier Controller sin pasarlo manualmente
public class CurrentSession {
    private static CurrentSession instance;
    private User loggedUser;

    private CurrentSession() {}

    public static CurrentSession getInstance() {
        if (instance == null) instance = new CurrentSession();
        return instance;
    }

    public User getLoggedUser() { return loggedUser; }
    public void setLoggedUser(User user) { this.loggedUser = user; }
    public boolean isLoggedIn() { return loggedUser != null; }
    public void logout() { loggedUser = null; }
}
```
