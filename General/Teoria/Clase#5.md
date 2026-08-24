# MVC — Arquitectura en Capas

## Base de datos

La parte más profunda de una aplicación es definir la base de datos.

> **Función:** Almacenamiento de los datos únicamente. La base de datos no debe contener lógica de negocio; su responsabilidad se limita a guardar, leer, actualizar y eliminar información.
>
> **Trigger:** A partir de una acción realizada sobre una base de datos (INSERT, UPDATE, DELETE), se provoca automáticamente otro cambio dentro de la misma base de datos, sin intervención del programador.

* Los triggers ya no forman parte del flujo de datos manejado por el programador; ahora ese tipo de automatización ocurre únicamente dentro del flujo interno de la base de datos, y no del flujo de software (la aplicación en sí).
* Los **procedimientos almacenados** (stored procedures) también se mantienen exclusivamente para tareas de almacenamiento; son pequeños bloques de código que corren *dentro* del motor de base de datos, fuera del flujo de código de la aplicación (Java, en este caso).

> **Nota adicional:** aunque los triggers y procedimientos almacenados son útiles para mantener integridad de datos (por ejemplo, actualizar automáticamente un campo de auditoría), abusar de ellos dificulta la trazabilidad del sistema, ya que parte de la lógica queda "escondida" dentro de la base de datos en lugar de estar en el código de la aplicación.

---

## Backend

El backend se organiza típicamente en varias capas, cada una con una única responsabilidad, de modo que un cambio en una capa no obligue a modificar las demás. Este es el mismo principio de responsabilidad única aplicado a nivel de arquitectura.

---

## Capa de Datos

Se encarga de comunicar el código fuente con la base de datos; funciona como un intérprete que permite traer los datos desde la base.

> **Función:** Intercambiar datos con la base de datos, únicamente: insertar, eliminar, buscar. Su única preocupación es que exista conexión y que la operación se ejecute correctamente.

* La capa de Datos **no maneja nada** (no valida, no calcula, no aplica reglas de negocio); solo se comunica con la base de datos.
* Es la única capa que debería conocer detalles técnicos de la base de datos (cadena de conexión, tipo de motor, tablas, columnas).

---

## Lógica

Es el "cerebro" de la aplicación: calcula, valida datos y prepara la información relacionándola con las reglas del negocio.

* **Función:** preparar la información obtenida (o que se va a enviar) y aplicarle las reglas del negocio correspondientes.
* **Ejemplo:** en un AVI (Agente Virtual Inteligente) de tipo médico, la capa de Lógica sería la encargada de validar que el usuario no sea menor de edad antes de continuar con una consulta.
* Es también la capa encargada de realizar el **mapeo** entre lo que llega de la base de datos (el *dataset*) y los objetos DTO que usará el resto de la aplicación.

---

## Servicios

Es una capa **opcional** dentro de la arquitectura.

* Permite comunicar la aplicación con cualquier tipo de interfaz: podría ser Perl, React, una app móvil de iOS, etc.
* Una aplicación debe poder comunicarse con cualquier tipo de interfaz; al implementar Servicios, esto se vuelve posible: se puede cambiar de una interfaz hecha en Scene Builder a una hecha en PHP o HTML sin tocar la Lógica ni los Datos.
* Los Servicios indican **a dónde ir**, sin importar quién preguntó (es decir, no le interesa si la petición viene de una app de escritorio, una web o un móvil).
* Es la capa **más rápida** de todas, precisamente porque **no valida nada** dentro de ella; toda la validación ya ocurrió en la capa de Lógica.

> **Nota adicional:** un ejemplo típico de esta capa son las **API REST**: un mismo servicio backend puede exponer sus datos a través de endpoints HTTP, y tanto una app de JavaFX como una página web o una app móvil pueden consumir exactamente el mismo servicio sin duplicar lógica.

---

## DTO (Data Transfer Object / Model - Entity)

Sirve únicamente para almacenar datos de forma lógica; es un conjunto de clases cuyos elementos solo tienen propiedades. Las entidades DTO no implementan métodos de lógica de negocio, únicamente `set`, `get` o constructor.

* Una **lista** está definida por los datos que contiene: si se debe devolver una lista de cédulas que inicien en 4, lo que se devuelve es una `List<PersonaDTO>`.
* **Regla de mapeo:** todo lo que existe en la base de datos debe poder representarse en el DTO, pero no todo lo que existe en el DTO tiene que estar necesariamente en la base de datos (pueden existir campos calculados, combinados o derivados que no son columnas reales).
* El **dataset** de la base de datos representa los datos tal cual vienen; lo que "mueve" ese dataset hacia el resto de la aplicación son los DTO. Se mapea el dataset proveniente de la base de datos hacia DTO por razones de **seguridad** y para desacoplar la estructura interna de la base de datos del resto del sistema (eliminar la dependencia directa a los "estándares" internos de la base). La capa de **Lógica** es la encargada de dicho mapeo.
* Todos los DTO **desaparecen** al cerrar la aplicación (viven únicamente en memoria durante la ejecución); el cierre y la persistencia real de la información deben hacerse en la capa de **Datos**.
* Las capas de **Lógica**, **Servicios** y los **DTO** no deben de conocer que existe una base de datos; solo la capa de Datos sabe que esta existe.

> **Nota adicional:** este desacoplamiento es lo que permite, por ejemplo, cambiar de una base de datos MySQL a PostgreSQL, o de una base de datos SQL a una NoSQL, sin necesidad de modificar la Lógica, los Servicios ni las Vistas de la aplicación — únicamente se reescribe la capa de Datos.

---

# Implementación en Java

## Manipular el `Parent` para cambiar de pantalla

Como se vio anteriormente, `Parent` representa el nodo raíz de una pantalla cargada desde un archivo FXML. Para cambiar de pantalla, existen dos enfoques comunes:

**1. Reemplazar el `Scene` completo** (crea una escena nueva):

```java
Parent raiz = FXMLLoader.load(getClass().getResource("otraPantalla.fxml"));
Stage stage = (Stage) boton.getScene().getWindow();
stage.setScene(new Scene(raiz));
```

**2. Reemplazar solo el nodo raíz** (`setRoot`), manteniendo el mismo `Scene` (más eficiente, conserva tamaño y posición de la ventana):

```java
Parent raiz = FXMLLoader.load(getClass().getResource("otraPantalla.fxml"));
Stage stage = (Stage) boton.getScene().getWindow();
stage.getScene().setRoot(raiz);
```

> El `Stage` es la ventana; el `Scene` es el contenido gráfico completo que se le asigna al `Stage`; `Parent` es el nodo raíz dentro de ese `Scene`. Cambiar de "pantalla" en realidad significa reemplazar el `Parent` (y opcionalmente el `Scene`) dentro del mismo `Stage`.

## Enviar información entre pantallas (Controllers)

Cuando se hace `FXMLLoader.load(...)` de forma directa, no se tiene acceso al Controller de la pantalla destino. Para enviar datos entre pantallas, se debe usar el `FXMLLoader` como objeto, obtener su Controller, y llamar un método propio de ese Controller antes de mostrar la pantalla:

```java
FXMLLoader loader = new FXMLLoader(getClass().getResource("pantallaDestino.fxml"));
Parent raiz = loader.load();

// Se obtiene el Controller de la nueva pantalla
PantallaDestinoController controller = loader.getController();

// Se le "inyecta" la información necesaria
controller.recibirDatos(datoAEnviar);

Stage stage = (Stage) boton.getScene().getWindow();
stage.getScene().setRoot(raiz);
```

Dentro del Controller destino, se define un método público para recibir esos datos:

```java
public class PantallaDestinoController {

    private String datoRecibido;

    public void recibirDatos(String dato) {
        this.datoRecibido = dato;
        // aquí se podría, por ejemplo, actualizar un Label con el dato recibido
    }
}
```

> Este patrón es, en esencia, cómo la capa de **Lógica** o de **Servicios** entregaría un DTO a la Vista: el Controller de destino expone un método público que actúa como "punto de entrada" para recibir datos ya procesados.

## AnchorPane Constraints

Cuando un nodo se coloca dentro de un `AnchorPane`, se le pueden asignar restricciones de anclaje a los cuatro bordes del panel (`topAnchor`, `bottomAnchor`, `leftAnchor`, `rightAnchor`). Esto se puede definir directamente en el FXML (normalmente generado por Scene Builder) o por código:

```xml
<AnchorPane>
   <children>
      <Button text="Enviar" AnchorPane.bottomAnchor="10.0" AnchorPane.rightAnchor="10.0" />
   </children>
</AnchorPane>
```

Equivalente por código Java:

```java
AnchorPane.setBottomAnchor(boton, 10.0);
AnchorPane.setRightAnchor(boton, 10.0);
```

> Fijar un nodo a los cuatro bordes a la vez (`top`, `bottom`, `left`, `right`) hace que este crezca proporcionalmente cuando la ventana se redimensiona, ya que el `AnchorPane` mantiene la distancia especificada respecto a cada borde en todo momento.

## Pantalla completa y modo ventana

El `Stage` permite alternar entre modo ventana normal y pantalla completa mediante la propiedad `fullScreen`:

```java
Stage stage = (Stage) boton.getScene().getWindow();
stage.setFullScreen(true);   // Activa pantalla completa
stage.setFullScreen(false);  // Regresa a modo ventana
```

También se pueden configurar otros comportamientos relevantes de la ventana:

```java
stage.setResizable(false);                 // Impide redimensionar la ventana
stage.setFullScreenExitHint("");           // Oculta el mensaje de "presione ESC para salir"
stage.setMaximized(true);                  // Maximiza la ventana (no es lo mismo que pantalla completa)
```

> **Diferencia clave:** `setMaximized(true)` mantiene la barra de título y los bordes del sistema operativo; `setFullScreen(true)` oculta la barra de título y ocupa toda la pantalla, similar a un modo *kiosko*.

## Eventos de botones y otros eventos del sistema

Además del clásico `onAction` de un `Button`, JavaFX permite escuchar múltiples tipos de eventos del sistema directamente desde el Controller. Algunos ejemplos comunes:

**Evento de cierre de ventana** (por ejemplo, para confirmar antes de salir):

```java
stage.setOnCloseRequest(evento -> {
    System.out.println("El usuario intenta cerrar la ventana");
    // evento.consume(); // Descomentar para impedir el cierre
});
```

**Evento de tecla presionada** (a nivel de escena completa):

```java
scene.setOnKeyPressed(evento -> {
    if (evento.getCode() == KeyCode.ENTER) {
        enviarMensaje();
    }
});
```

**Evento de cambio de tamaño de ventana:**

```java
stage.widthProperty().addListener((obs, anchoAnterior, anchoNuevo) -> {
    System.out.println("Nuevo ancho: " + anchoNuevo);
});
```

> Todos estos eventos siguen el mismo modelo dirigido por eventos visto anteriormente: el sistema captura el evento (tecla presionada, ventana redimensionada, intento de cierre), lo despacha al *listener* correspondiente registrado en el Controller, y este ejecuta la respuesta definida — sin que el programador tenga que implementar manualmente un ciclo de espera (*event loop*), ya que JavaFX lo gestiona internamente.
