> Modificar Pom

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

Se importan librerias como Parent para evitar que se generen multiples logos de la misma app en la barra de tareas.

```
package avi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        Parent raiz = FXMLLoader.load(getClass().getResource("ui/InterfazPrincipal.fxml")); // "ui/home-view.fxml"
        escenarioPrincipal.setTitle("AVI - Agente Virtual Inteligente");
        escenarioPrincipal.setScene(new Scene(raiz, 400, 640));
        escenarioPrincipal.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
```

# MVC

- Model: Otorga la estructura
- View: Otorga el diseño
- Controller: Otorga la funcionalidad

## Ejemplo Nascar

Model: Estructura - Debe ser siempre el mismo para todos, como la carroceria
1. Peso = 1TON
2. Largo = 3MT
3. Engranaje = 16
4. Motor = V25
5. Dimensiones = 25x16x124

Se define solamente lo básico

View: Diseño - Debe tener datos que los diferencien de cada uno, como la pintura
1. Patrocinadores
2. Numero
3. Llantas

Controller: Funcionalidad - involucra todas las funciones que tiene este vehículo:
1. Freno
2. Acelerador
3. Distribución Combustible
4. Cambio Marchas
5. Freno de mano









