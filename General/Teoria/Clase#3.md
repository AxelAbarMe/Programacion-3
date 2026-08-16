> Modificar Pom requerido en exámenes.

# JavaFX

Build se genera para conectar maven con la interfaz gráfica

Sirve para configurar propiedades de la aplicación. Configurar versión de Javafx, generar dependencias para comunicación entre java y la interfaz.

Build genera plugins, es lo que une javafx con Maven.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.example</groupId>
    <artifactId>avi-academico</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <maven.compiler.source>26</maven.compiler.source>
        <maven.compiler.target>26</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <javafx.version>21.0.2</javafx.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>21.0.2</version>
        </dependency>
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20240303</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
            </plugin>
        </plugins>
    </build>
</project>
```

Se importan librerías como `Parent` para evitar que se generen múltiples logos de la misma app en la barra de tareas.

---

## Arquitectura MVC

* **Model:** Otorga la estructura
* **View:** Otorga el diseño
* **Controller:** Otorga la funcionalidad

### Ejemplo Nascar

**Model: Estructura** - Debe ser siempre el mismo para todos, como la carrocería. Se define solamente lo básico:

1. Peso = 1TON
2. Largo = 3MT
3. Engranaje = 16
4. Motor = V25
5. Dimensiones = 25x16x124

**View: Diseño** - Debe tener datos que los diferencien de cada uno, como la pintura:

1. Patrocinadores
2. Número
3. Llantas

**Controller: Funcionalidad** - involucra todas las funciones que tiene este vehículo:

1. Freno
2. Acelerador
3. Distribución Combustible
4. Cambio Marchas
5. Freno de mano

### ActionEvent

Es el que controla los eventos y que se dispare todos los movimientos de la interfaz gráfica.

---

# Proyecto AVI (Agente Virtual Inteligente)

A continuación se presenta un proyecto real de ejemplo que aplica los conceptos anteriores (Maven + JavaFX + MVC). Se trata de un asistente virtual que se conecta a un modelo de lenguaje (Gemini) mediante peticiones HTTP.

## Estructura del proyecto

```
src/
 └── main/
      ├── java/
      │    └── avi/
      │         ├── App.java
      │         ├── service/
      │         │    └── GeminiService.java
      │         └── ui/
      │              ├── InterfazInicioController.java
      │              ├── InterfazChatController.java
      │              └── InterfazHistorialController.java
      └── resources/
           └── ui/
                ├── InterfazInicio.fxml
                ├── InterfazChat.fxml
                └── InterfazHistorial.fxml
```

## Flujo de navegación entre pantallas

1. **InterfazInicio** → pantalla de bienvenida donde el usuario selecciona el modelo de lenguaje (`ComboBox`) y elige entre iniciar un chat nuevo o revisar el historial.
2. **InterfazChat** → pantalla principal de conversación con el asistente (equivalente a la interfaz de una IA tipo chat).
3. **InterfazHistorial** → pantalla que lista las conversaciones anteriores y permite reabrirlas o volver al inicio.

---

## Clase principal (`App.java`)

Es el punto de entrada de la aplicación; carga el primer FXML (la pantalla de inicio) dentro del `Stage` principal.

```java
package avi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        Parent raiz = FXMLLoader.load(getClass().getResource("ui/InterfazInicio.fxml"));
        escenarioPrincipal.setTitle("AVI - Agente Virtual Inteligente");
        escenarioPrincipal.setScene(new Scene(raiz, 400, 640));
        escenarioPrincipal.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
```

---

## Servicio de conexión al modelo de IA (`GeminiService.java`)

Clase encargada de la lógica de negocio (fuera del Controller), responsable de comunicarse con la API externa. Mantener esta lógica separada del Controller respeta el principio de responsabilidad única dentro del patrón MVC: el Controller solo debe orquestar la interacción entre la Vista y el Modelo/Servicio, no construir peticiones HTTP directamente.

**Puntos clave:**

* La `API_KEY` se obtiene desde una **variable de entorno** (`GEMINI_API_KEY`), evitando exponer credenciales directamente en el código fuente.
* Se utiliza `java.net.http.HttpClient` (incluido desde Java 11) para realizar la petición `POST`, sin necesidad de librerías externas de red.
* El cuerpo de la petición se construye como un objeto JSON anidado (`contents` → `parts` → `text`), tal como lo requiere la API de Gemini.
* La respuesta se procesa con la librería `org.json`, navegando su estructura (`candidates` → `content` → `parts` → `text`) para extraer únicamente el texto generado.

```java
package avi.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiService {

    private static final String MODELO = "gemini-3.6-flash";
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

---

## Pantalla 1: Inicio (`InterfazInicio.fxml`)

Vista donde el usuario selecciona el modelo (Model, en el sentido de datos disponibles vía `ComboBox`) y decide si abrir un chat nuevo o el historial.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ComboBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.Pane?>
<?import javafx.scene.text.Font?>

<Pane fx:controller="avi.ui.InterfazInicioController" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="400.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/26" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <Label layoutX="45.0" layoutY="24.0" text="Bienvenido a AVI UNA">
         <font>
            <Font name="System Bold Italic" size="24.0" />
         </font>
      </Label>
      <Label layoutX="170.0" layoutY="79.0" text="Seleccione el modelo de lenguaje ">
         <font>
            <Font name="System Italic" size="18.0" />
         </font>
      </Label>
      <ComboBox fx:id="cbModel" layoutX="170.0" layoutY="123.0" prefHeight="25.0" prefWidth="261.0" promptText="AVI Modelos" />
      <Button fx:id="btnOp2" layoutX="189.0" layoutY="159.0" mnemonicParsing="false" prefHeight="35.0" prefWidth="223.0" text="AVI v3.6" />
      <Button fx:id="btnOp1" layoutX="189.0" layoutY="200.0" mnemonicParsing="false" prefHeight="35.0" prefWidth="223.0" text="AVI - UNA" />
   </children>
</Pane>
```

### Controlador de la Pantalla 1 (`InterfazInicioController.java`)

*(anteriormente `HomeViewController.java`, apuntando a `InterfaceAVI_Model.fxml`/comentarios `home-view.fxml`)*

```java
package avi.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class InterfazInicioController {
    // Vamos a definir los elementos (controles) que vamos a manipular desde Java
    // Los nombres de estos elementos deben ser exactamente iguales a los fx:id del diseño de SceneBuilder

    @FXML private ComboBox<String> cbModel;
    @FXML private Button btnOp1;
    @FXML private Button btnOp2;

    @FXML
    private void initialize(){
        btnOp1.setOnAction(event -> cambiarPantalla(event, "InterfazChat.fxml"));
        btnOp2.setOnAction(event -> cambiarPantalla(event, "InterfazHistorial.fxml"));
    }

    private void cambiarPantalla(ActionEvent event, String archivoFxml){
        try{
            Parent raiz = FXMLLoader.load(getClass().getResource(archivoFxml));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(raiz);
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }
}
```

---

## Pantalla 2: Chat (`InterfazChat.fxml`)

Vista principal de conversación, similar a una interfaz de IA: un área de solo lectura con el historial de respuestas, un campo de texto para escribir y un botón de envío.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.TextArea?>
<?import javafx.scene.layout.Pane?>
<?import javafx.scene.text.Font?>

<Pane fx:controller="avi.ui.InterfazChatController" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="626.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/26" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <Label fx:id="lbTitle" layoutX="79.0" layoutY="23.0" prefHeight="45.0" prefWidth="445.0" text="AVI UNA" textAlignment="CENTER">
         <font>
            <Font size="18.0" />
         </font>
      </Label>
      <Label fx:id="lbsubtiitle" layoutX="169.0" layoutY="74.0" text="Versión 3.6">
         <font>
            <Font name="System Italic" size="12.0" />
         </font>
      </Label>
      <TextArea fx:id="textResp" accessibleText="Historial de AVI UNA" editable="false" layoutX="14.0" layoutY="106.0" prefHeight="408.0" prefWidth="574.0" text="Esperando consulta del usuario">
         <font>
            <Font name="System Bold Italic" size="14.0" />
         </font>
      </TextArea>
      <TextArea fx:id="textoPreg" layoutX="14.0" layoutY="541.0" prefHeight="65.0" prefWidth="498.0" />
      <Button fx:id="btnenviar" layoutX="512.0" layoutY="541.0" mnemonicParsing="false" onAction="#enviarMensaje" prefHeight="65.0" prefWidth="77.0" text="Enviar" textAlignment="CENTER" textOverrun="WORD_ELLIPSIS" underline="true" />
   </children>
</Pane>
```

### Controlador de la Pantalla 2 (`InterfazChatController.java`)

*(anteriormente `InterfazPrincipal.fxml` no tenía Controller asignado; se agrega para conectar la Vista con el `GeminiService`)*

```java
package avi.ui;

import avi.service.GeminiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class InterfazChatController {

    @FXML private TextArea textResp;
    @FXML private TextArea textoPreg;
    @FXML private Button btnenviar;

    private final GeminiService servicioGemini = new GeminiService();

    @FXML
    private void enviarMensaje(ActionEvent event){
        String pregunta = textoPreg.getText().trim();
        if(pregunta.isEmpty()){
            return;
        }

        textResp.appendText("\nUsuario: " + pregunta + "\n");
        textoPreg.clear();

        try {
            String respuesta = servicioGemini.enviarMensaje(pregunta);
            textResp.appendText("AVI: " + respuesta + "\n");
        } catch (Exception error) {
            textResp.appendText("AVI: Ocurrió un error al consultar el modelo.\n");
            error.printStackTrace();
        }
    }
}
```

---

## Pantalla 3: Historial de Chats (`InterfazHistorial.fxml`)

Vista que lista conversaciones previas y permite reabrirlas, iniciar una nueva o regresar al inicio, además de filtros por fecha y modo de apertura.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.CheckBox?>
<?import javafx.scene.control.DatePicker?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.RadioButton?>
<?import javafx.scene.layout.Pane?>
<?import javafx.scene.text.Font?>

<Pane fx:controller="avi.ui.InterfazHistorialController" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="400.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/26" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <Label layoutX="45.0" layoutY="26.0" text="Historial de Chats">
         <font>
            <Font name="System Bold Italic" size="14.0" />
         </font>
      </Label>
      <ListView fx:id="lvwChats" layoutX="-3.0" layoutY="61.0" prefHeight="263.0" prefWidth="600.0" />
      <RadioButton layoutX="22.0" layoutY="334.0" mnemonicParsing="false" text="Abrir con Modelo UNA" />
      <CheckBox layoutX="235.0" layoutY="333.0" mnemonicParsing="false" text=" Modo Oscuro" />
      <CheckBox layoutX="355.0" layoutY="333.0" mnemonicParsing="false" text=" Modo Incognito" />
      <Label layoutX="22.0" layoutY="366.0" text="Buscar por rango de fechas">
         <font>
            <Font name="System Bold" size="14.0" />
         </font>
      </Label>
      <DatePicker layoutX="210.0" layoutY="364.0" promptText="Desde" />
      <DatePicker layoutX="394.0" layoutY="364.0" promptText="Hasta" />
      <Button fx:id="btnVolver" layoutX="14.0" layoutY="14.0" mnemonicParsing="false" text="Volver" />
      <Button fx:id="btnAbrirChat" layoutX="440.0" layoutY="366.0" mnemonicParsing="false" text="Abrir chat" />
      <Button fx:id="btnNuevoDesdeHistorial" layoutX="440.0" layoutY="20.0" mnemonicParsing="false" text="Nuevo chat" />
   </children>
</Pane>
```

### Controlador de la Pantalla 3 (`InterfazHistorialController.java`)

*(anteriormente `ChatListViewController.java`, con comentarios que referenciaban `home-view.fxml` y `chat-view.fxml`)*

```java
package avi.ui;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class InterfazHistorialController {
    // Definir controles que vamos a accionar desde la interfaz gráfica, preparar eventos que vamos a ejecutar
    @FXML private ListView<String> lvwChats;
    @FXML private Button btnVolver;
    @FXML private Button btnAbrirChat;
    @FXML private Button btnNuevoDesdeHistorial;

    @FXML
    private void initialize(){
        btnVolver.setOnAction(event -> cambiarPantalla(event, "InterfazInicio.fxml"));
        btnNuevoDesdeHistorial.setOnAction(event -> cambiarPantalla(event, "InterfazChat.fxml"));
        btnAbrirChat.setOnAction(event -> {
            if(lvwChats.getSelectionModel().getSelectedItem() != null){
                cambiarPantalla(event, "InterfazChat.fxml");
            }
        }); // Función Lambda
    }

    private void cambiarPantalla(ActionEvent event, String archivoFxml){
        try{
            Parent raiz = FXMLLoader.load(getClass().getResource(archivoFxml));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(raiz);
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }
}
```

---

## Relación con MVC en este proyecto

* **Model:** los datos que viajan entre pantallas (texto de la pregunta, respuesta del servicio, ítems del `ComboBox`/`ListView`) y la clase `GeminiService`, que actúa como capa de acceso a datos externos (API).
* **View:** los tres archivos `.fxml` (`InterfazInicio`, `InterfazChat`, `InterfazHistorial`), que definen únicamente el diseño visual, sin lógica.
* **Controller:** las clases `InterfazInicioController`, `InterfazChatController` e `InterfazHistorialController`, que manejan los `ActionEvent` de cada botón y coordinan el cambio de pantallas (`stage.getScene().setRoot(...)`) y la comunicación con el `Model`/`GeminiService`.
