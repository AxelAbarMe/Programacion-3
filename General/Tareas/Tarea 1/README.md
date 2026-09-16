# Manual — Semana 2: Cómo integrar Gemini a un proyecto Java, paso a paso

**UNIVERSIDAD NACIONAL · Escuela de Informática**
**EIF206 · Programación 3 · II Ciclo 2026**
**Prof. Deivert Guiltrichs Cordero**

## 1. Objetivo

Este manual muestra, con código completo y explicado, cómo conectar una aplicación Java a la API de Gemini: cómo se arma la petición, cómo se lee la respuesta, y cómo hacer que el programa le haga una pregunta escrita por el usuario y muestre la respuesta en la consola. Al final hay un reto para entregar: ampliar este mismo proyecto agregando herencia.

Siga los pasos en orden. Todo el código que aparece aquí es funcional — cópielo tal cual y ejecútelo para comprobar que funciona antes de continuar.

## 2. Preparativos

### 2.1 Obtener la clave de API

1. Vaya a https://aistudio.google.com/apikey y genere una clave de API gratuita (necesita una cuenta de Google).
2. Verifique que tiene instalado JDK 17 o superior y Maven. En IntelliJ IDEA puede comprobar la versión de JDK en `File > Project Structure`.

### 2.2 Configurar la clave como variable de entorno

La clave nunca se escribe directamente en el código. Hay dos formas de configurarla; para el trabajo de este curso se recomienda la **Opción B**, porque queda guardada dentro del propio proyecto y no depende de la terminal ni del sistema operativo.

#### Opción A — Variable de entorno del sistema operativo

Sirve para cualquier terminal y cualquier programa de su computadora, pero hay que configurarla por fuera de IntelliJ.

- **Linux/macOS (terminal), temporal** — se pierde al cerrar la terminal:
  ```
  export GEMINI_API_KEY="su_clave_aqui"
  ```
- **Windows (PowerShell), temporal:**
  ```
  $env:GEMINI_API_KEY="su_clave_aqui"
  ```
- **Permanente en macOS/Linux:** agregue la línea `export GEMINI_API_KEY="su_clave_aqui"` al final de `~/.zshrc` (o `~/.bashrc`) y reinicie la terminal.
- **Permanente en Windows:** busque "Variables de entorno" en el menú de inicio > Editar las variables de entorno del sistema > Variables de entorno > en "Variables de usuario", Nueva > Nombre: `GEMINI_API_KEY`, Valor: su clave.

> **Importante:** si usa esta opción, debe reiniciar IntelliJ IDEA después de configurarla — el IDE solo hereda las variables de entorno que existían al momento de abrirse.

#### Opción B — Solo dentro de IntelliJ, para este proyecto (recomendada)

Esta opción configura la clave únicamente para las ejecuciones de este proyecto en particular, sin tocar el sistema operativo. Los pasos dependen de cómo vaya a ejecutar la aplicación:

**Si ejecuta con el botón ▶ (Run/Debug Configuration de tipo Application):**

1. Ejecute el programa una vez (aunque falle) para que IntelliJ genere la configuración, o vaya directamente a `Run > Edit Configurations…`
2. Seleccione la configuración de Main (o créela con el botón `+ > Application`).
3. Busque el campo **Environment variables** (si no aparece, haga clic en "Modify options" y actívelo).
4. Haga clic en el ícono de carpeta/lápiz al final del campo > `+` > Nombre: `GEMINI_API_KEY`, Valor: su clave.
5. Apply > OK, y ejecute de nuevo con ▶.

**Si ejecuta con Maven (`mvn compile exec:java`, como se usa en este manual):**

1. Abra el panel de Maven (`View > Tool Windows > Maven`).
2. Navegue a `Plugins > exec > exec:java`, clic derecho > Create 'avi-semana02 [exec:java]'…
3. En esa configuración también aparece el campo **Environment variables** — agregue `GEMINI_API_KEY=su_clave_aqui` igual que en la opción anterior.
4. Guarde y ejecute esa configuración desde el panel de Maven o con ▶.

Esta clave queda guardada en el archivo de configuración de esa Run Configuration (dentro de la carpeta `.idea/` del proyecto), nunca en el código fuente. Esa carpeta normalmente ya está excluida por el `.gitignore` por defecto de IntelliJ, pero revíselo antes de subir el proyecto a un repositorio.

> **Nota:** esta configuración solo aplica dentro del IDE. Si más adelante ejecuta `mvn compile exec:java` desde una terminal externa a IntelliJ, ahí sí necesita la variable de entorno del sistema operativo (Opción A).

## 3. Crear el proyecto de consola en IntelliJ IDEA

1. Abra IntelliJ IDEA. En la pantalla de bienvenida haga clic en **New Project** (si ya tiene otro proyecto abierto, use `File > New > Project`).
2. En el panel izquierdo del asistente, seleccione **Java** (no Maven Archetype ni ninguna plantilla especial — un proyecto Java común).
3. Complete los campos:
   - **Name:** `avi-semana02`
   - **Location:** la carpeta donde quiere guardar el proyecto
   - **Build system:** Maven
   - **JDK:** seleccione la versión 17 o superior (si no aparece en la lista, use `Add JDK…` y ubique la carpeta donde tiene instalado el JDK)
4. Antes de hacer clic en Create, revise que las casillas **Add sample code** (o similar) queden desmarcadas — vamos a escribir el código nosotros mismos, en lugar de partir de un ejemplo genérico.
5. Haga clic en **Create**. IntelliJ va a crear la estructura del proyecto y descargar Maven; espere a que el ícono de sincronización de la esquina inferior derecha termine de girar antes de continuar.
6. En el panel Project (usualmente a la izquierda), confirme que existe la ruta `src > main > java`. Ahí es donde va a vivir todo el código de este proyecto.
7. Haga clic derecho sobre la carpeta `java` > `New > Package`, y escriba `una.eif206.avi`. Esto crea el paquete raíz del proyecto (las carpetas `una/eif206/avi` se generan automáticamente).
8. Reemplace todo el contenido de `pom.xml` (el archivo que está en la raíz del proyecto, no dentro de `src`) por este:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
 <modelVersion>4.0.0</modelVersion>
 <groupId>una.eif206</groupId>
 <artifactId>avi-semana02</artifactId>
 <version>1.0.0</version>
 <packaging>jar</packaging>
 <properties>
 <maven.compiler.source>17</maven.compiler.source>
 <maven.compiler.target>17</maven.compiler.target>
 <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
 </properties>
 <dependencies>
 <dependency>
 <groupId>org.json</groupId>
 <artifactId>json</artifactId>
 <version>20240303</version>
 </dependency>
 </dependencies>
 <build>
 <plugins>
 <plugin>
 <groupId>org.codehaus.mojo</groupId>
 <artifactId>exec-maven-plugin</artifactId>
 <version>3.2.0</version>
 <configuration>
 <mainClass>una.eif206.avi.Main</mainClass>
 </configuration>
 </plugin>
 </plugins>
 </build>
</project>
```

Esto le agrega al proyecto dos cosas: la librería `org.json` (para construir y leer JSON sin tener que hacerlo a mano con Strings) y el plugin `exec-maven-plugin` (para poder ejecutar la aplicación de consola con un solo comando). Después de guardar el archivo, IntelliJ va a mostrar un ícono de "recargar" Maven (una M con una flecha) en la esquina superior derecha del editor — haga clic ahí para que descargue la nueva dependencia.

## 4. Construir la conexión con Gemini (GeminiService)

Dentro del paquete `una.eif206.avi` que ya creó, haga clic derecho > `New > Package`, y escriba `service` (esto crea `una.eif206.avi.service`). Luego, clic derecho sobre ese nuevo paquete > `New > Java Class`, escriba `GeminiService`, y reemplace el contenido del archivo generado por este código completo:

```java
package una.eif206.avi.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiService {

 private static final String MODELO = "gemini-2.5-flash";
 private static final String ENDPOINT_BASE =
 "https://generativelanguage.googleapis.com/v1beta/models/";

 private final String apiKey;
 private final HttpClient httpClient;

 public GeminiService() {
 this.apiKey = System.getenv("GEMINI_API_KEY");
 if (apiKey == null || apiKey.isBlank()) {
 throw new IllegalStateException(
 "No se encontro la variable de entorno GEMINI_API_KEY.");
 }
 this.httpClient = HttpClient.newHttpClient();
 }

 public String enviarMensaje(String textoUsuario)
 throws IOException, InterruptedException {

 String url = ENDPOINT_BASE + MODELO + ":generateContent";

 JSONObject parte = new JSONObject().put("text", textoUsuario);
 JSONObject contenido = new JSONObject()
 .put("parts", new JSONArray().put(parte));
 JSONObject cuerpo = new JSONObject()
 .put("contents", new JSONArray().put(contenido));

 HttpRequest request = HttpRequest.newBuilder()
 .uri(URI.create(url))
 .header("Content-Type", "application/json")
 .header("x-goog-api-key", apiKey)
 .POST(HttpRequest.BodyPublishers.ofString(cuerpo.toString()))
 .build();

 HttpResponse<String> response = httpClient.send(
 request, HttpResponse.BodyHandlers.ofString());

 if (response.statusCode() != 200) {
 throw new IOException("Error de la API (HTTP "
 + response.statusCode() + "): " + response.body());
 }

 return extraerTexto(response.body());
 }

 private String extraerTexto(String jsonRespuesta) {
 JSONObject raiz = new JSONObject(jsonRespuesta);
 JSONArray candidatos = raiz.getJSONArray("candidates");
 JSONObject primerCandidato = candidatos.getJSONObject(0);
 JSONObject contenido = primerCandidato.getJSONObject("content");
 JSONArray partes = contenido.getJSONArray("parts");
 return partes.getJSONObject(0).getString("text");
 }
}
```

### 4.1 Qué hace cada parte

- **El constructor:** lee la clave desde la variable de entorno con `System.getenv("GEMINI_API_KEY")`. Si no existe o está vacía, lanza una excepción de inmediato — así el error aparece apenas se arranca el programa, y no a mitad de una consulta.
- **Construcción del JSON:** la API de Gemini espera un cuerpo con esta forma exacta: `{ "contents": [ { "parts": [ { "text": "..." } ] } ] }`. Las clases `JSONObject` y `JSONArray` de `org.json` arman esa estructura sin que tengamos que escribir el JSON a mano como texto (lo cual sería propenso a errores de sintaxis).
- **El HttpRequest:** es una petición POST con dos headers importantes: `Content-Type` (le dice al servidor que el cuerpo es JSON) y `x-goog-api-key` (la forma en que Gemini recibe la clave de autenticación).
- **httpClient.send(...):** envía la petición y espera la respuesta de forma síncrona (bloqueante) — el programa se detiene en esa línea hasta que Gemini responde.
- **Manejo de errores:** si el código de estado HTTP no es 200 (éxito), se lanza una `IOException` con el detalle del error. Esto es lo que le permite al programa mostrar un mensaje útil en vez de simplemente fallar sin explicación.
- **extraerTexto(...):** la respuesta de Gemini es un JSON anidado (`candidates -> content -> parts -> text`). Este método navega esa estructura y devuelve solamente el texto final, que es lo único que nos interesa mostrar.

## 5. Escribir una pregunta y leer la respuesta desde consola

Haga clic derecho sobre el paquete `una.eif206.avi` (un nivel arriba de `service`) > `New > Java Class`, escriba `Main`, y reemplace el contenido del archivo generado por este:

```java
package una.eif206.avi;

import una.eif206.avi.service.GeminiService;

import java.io.IOException;
import java.util.Scanner;

public class Main {
 public static void main(String[] args) {
 GeminiService geminiService = new GeminiService();
 Scanner lector = new Scanner(System.in);

 System.out.println("=== AVI - Agente Virtual Inteligente (consola) ===");
 System.out.println("Escriba su pregunta (o 'salir' para terminar).");
 System.out.print("\n> ");

 String pregunta = lector.nextLine();

 while (!pregunta.equalsIgnoreCase("salir")) {
 try {
 String respuesta = geminiService.enviarMensaje(pregunta);
 System.out.println("\nAVI responde:");
 System.out.println(respuesta);
 } catch (IOException | InterruptedException e) {
 System.out.println("\nOcurrio un error: " + e.getMessage());
 }
 System.out.print("\n> ");
 pregunta = lector.nextLine();
 }

 System.out.println("\nHasta luego!");
 lector.close();
 }
}
```

El programa usa un `Scanner` para leer lo que el usuario escribe en la consola (`lector.nextLine()`), se lo pasa a `geminiService.enviarMensaje(...)`, e imprime la respuesta con `System.out.println(...)`. El ciclo `while` se repite hasta que la persona escribe "salir".

## 6. Ejecutar y probar

Desde la terminal, ubicado en la raíz del proyecto, ejecute:

```
mvn compile exec:java
```

Debe ver algo similar a esto (el texto exacto de la respuesta va a variar):

```
=== AVI - Agente Virtual Inteligente (consola) ===
Escriba su pregunta (o 'salir' para terminar).

> ¿Que es la programacion orientada a objetos?

AVI responde:
La programacion orientada a objetos es un paradigma que organiza
el codigo en base a "objetos", que combinan datos (atributos) y
comportamiento (metodos)...

> salir

Hasta luego!
```

Si en vez de esto ve un error, revise primero que la variable de entorno `GEMINI_API_KEY` esté bien configurada en la misma terminal desde la que ejecuta el comando.

## 7. El reto de esta semana (entregable)

Con el proyecto ya funcionando, deben ampliarlo agregando herencia: crear una jerarquía de clases que modele los temas teóricos de esta semana (historia de Java, características de Java, comparación con C++), y usarla para consultarle esos tres temas al AVI automáticamente.

### 7.1 Recordatorio de sintaxis: clases abstractas

Un repaso rápido de cómo se declara una clase abstracta con un método abstracto, y una subclase que la extiende (este ejemplo es genérico, no es el que deben construir):

```java
public abstract class Animal {
 private final String nombre;

 protected Animal(String nombre) {
 this.nombre = nombre;
 }

 public String getNombre() { return nombre; }

 // Metodo abstracto: cada subclase decide como lo implementa.
 public abstract String hacerSonido();
}

public class Perro extends Animal {
 public Perro() { super("Perro"); }

 @Override
 public String hacerSonido() { return "Guau"; }
}
```

### 7.2 Lo que deben construir

1. Una clase abstracta `ConsultaTema` (paquete `una.eif206.avi.model`) con un atributo `nombre`, su constructor, un `getNombre()`, y un método abstracto `getPrompt()` que retorne un `String`.
2. Tres subclases concretas que extiendan `ConsultaTema`, una por cada tema teórico de la semana: historia de Java, características de Java, y comparación con C++. Cada una implementa `getPrompt()` retornando la pregunta correspondiente a su tema (puede reutilizar las preguntas de la sección 4 de este manual, o redactar las suyas propias con más detalle).
3. Modificar `Main.java` para que, en vez de leer una sola pregunta con `Scanner`, construya una `List<ConsultaTema>` con instancias de las tres subclases, y recorra esa lista con un for-each, llamando a `geminiService.enviarMensaje(tema.getPrompt())` para cada una e imprimiendo el nombre del tema y la respuesta obtenida.
4. El recorrido debe ser polimórfico: el código del for-each debe trabajar únicamente con el tipo `ConsultaTema` (la clase abstracta), sin necesitar preguntar de qué subclase se trata en cada caso (nada de `if (tema instanceof ...)`).
5. Manejar errores igual que antes: si una consulta falla, el programa debe continuar con la siguiente en vez de detenerse.

---

### Cómo se califica (100 pts totales)

| Criterio | Puntos |
|---|---|
| Proyecto base funcionando | 20 |
| Jerarquía `ConsultaTema` (clase abstracta + 3 subclases) | 25 |
| Los tres temas teóricos cubiertos | 20 |
| Uso de colección genérica y polimorfismo real | 15 |
| Evidencia entregada | 10 |
| Reflexión escrita | 10 |

