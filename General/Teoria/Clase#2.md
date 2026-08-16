# Documentación de Componentes — JavaFX Scene Builder

> JavaFX Scene Builder is a visual layout tool that lets users quickly design JavaFX application user interfaces, without coding. Users can drag and drop UI components to a work area, modify their properties, apply style sheets, and the FXML code for the layout that they are creating is automatically generated in the background. The result is an FXML file that can then be combined with a Java project by binding the UI to the application’s logic.

## Contenedores (Layout Containers)

### Accordion
Contenedor que agrupa múltiples paneles `TitledPane` en una pila vertical, donde solo un panel puede estar expandido a la vez. Se usa para organizar contenido en secciones colapsables, ahorrando espacio en la interfaz. Su propiedad principal es `expandedPane`, que define qué panel está visible. Es útil en formularios largos o menús de configuración segmentados. Genera en FXML la etiqueta `<Accordion>` conteniendo varios `<TitledPane>`.

### AnchorPane
Contenedor que posiciona sus hijos mediante anclas fijas a los bordes (top, bottom, left, right) del panel. Permite crear layouts que se adaptan proporcionalmente al redimensionar la ventana. Cada nodo hijo recibe restricciones tipo `AnchorPane.topAnchor`, `AnchorPane.leftAnchor`, etc. Es uno de los contenedores más usados como raíz de una escena por su flexibilidad de anclaje. No maneja alineación automática de espaciado, solo anclas absolutas.

### BorderPane
Divide el área en cinco regiones fijas: `top`, `bottom`, `left`, `right` y `center`. Cada región admite un solo nodo (o contenedor) hijo. Es ideal para layouts tipo aplicación de escritorio clásica (menú superior, barra lateral, contenido central, pie de página). El centro se expande automáticamente para ocupar el espacio restante. Configuraciones clave: márgenes (`BorderPane.margin`) y alineación por región.

### ButtonBar
Contenedor especializado en organizar botones de acción (Aceptar, Cancelar, Aplicar) siguiendo las convenciones de UI del sistema operativo. Usa la propiedad `buttonOrder` con códigos de letra (L, R, H, E, Y, N, X, B, I, A, C, O, U) para definir el orden y tipo de botón. Cada botón hijo requiere `ButtonBar.buttonData` para clasificarse (OK_DONE, CANCEL_CLOSE, etc.). Común en diálogos modales.

### DialogPane
Panel raíz utilizado dentro de un `Dialog` o `Alert`, estructurado en encabezado, contenido y área de botones. Contiene propiedades como `headerText`, `contentText` y `expandableContent` para mensajes detallados opcionales. Se integra con `ButtonType` para definir las respuestas del usuario. Se usa típicamente al construir alertas o formularios modales personalizados en Java.

### FlowPane
Organiza los nodos hijos en filas o columnas, ajustando el flujo automáticamente al espacio disponible (similar a texto que hace "wrap"). Configurable mediante `orientation` (HORIZONTAL/VERTICAL), `hgap`, `vgap` y `alignment`. Útil para galerías de imágenes, etiquetas (tags) o botones cuya cantidad es dinámica. No requiere definir filas/columnas manualmente como `GridPane`.

### GridPane
Contenedor de cuadrícula que posiciona los nodos en filas y columnas mediante índices (`GridPane.rowIndex`, `GridPane.columnIndex`). Permite definir restricciones de tamaño por fila/columna (`RowConstraints`, `ColumnConstraints`), así como `rowSpan`/`columnSpan` para fusionar celdas. Es el contenedor más flexible para formularios estructurados y layouts tabulares complejos.

### HBox
Alinea sus nodos hijos horizontalmente en una sola fila. Configurable con `spacing` (espacio entre elementos) y `alignment`. Cada hijo puede tener `HBox.hgrow` para definir cómo crece al expandirse el contenedor. Es uno de los contenedores más simples y usados para barras de herramientas, formularios en línea o grupos de botones.

### Pane
Contenedor base sin política de layout automática (a diferencia de `AnchorPane` o `VBox`); los hijos se posicionan mediante coordenadas absolutas `layoutX`/`layoutY`. Útil para diseños libres, prototipos rápidos o superposición manual de elementos. No soporta redimensionamiento proporcional automático de los hijos.

### ScrollPane
Proporciona barras de desplazamiento (horizontal y/o vertical) para contenido que excede el espacio visible. Su propiedad `content` define el nodo a desplazar; `fitToWidth` y `fitToHeight` controlan el ajuste automático. Configurable también `hbarPolicy`/`vbarPolicy` (ALWAYS, NEVER, AS_NEEDED). Esencial para listas largas, formularios extensos o vistas de imágenes grandes.

### SplitPane
Divide el espacio en dos o más paneles ajustables por el usuario mediante un divisor arrastrable.

#### Vertical
Los paneles se apilan uno sobre otro; el divisor se arrastra en sentido vertical.

#### Horizontal
Los paneles se ubican lado a lado; el divisor se arrastra en sentido horizontal.

Propiedad `dividerPositions` define la proporción inicial. Común en editores de código, exploradores de archivos y paneles maestro-detalle.

### StackPane
Superpone todos sus nodos hijos en capas, uno encima de otro, alineados por defecto al centro. Útil para efectos de superposición (overlays), fondos con contenido encima, o tarjetas con íconos superpuestos. La propiedad `alignment` controla la posición de cada capa dentro del área total.

### Tab
Representa una pestaña individual dentro de un `TabPane`. Contiene un `text` (título), un `graphic` opcional (ícono) y un `content` (nodo mostrado al seleccionarla). Configurable como `closable` para permitir o no su cierre por el usuario. No funciona de manera independiente; siempre debe estar contenido en un `TabPane`.

### TabPane
Contenedor que agrupa múltiples objetos `Tab`, mostrando su contenido según la pestaña activa. Configuraciones clave: `tabClosingPolicy`, `side` (posición de las pestañas: TOP, BOTTOM, LEFT, RIGHT) y `tabMinWidth`/`tabMaxWidth`. Ideal para organizar vistas relacionadas dentro de una misma ventana sin saturar la interfaz.

### TextFlow
Contenedor especializado en organizar fragmentos de texto (`Text`) y otros nodos en un flujo continuo, respetando saltos de línea automáticos. Permite combinar estilos distintos (negrita, color, tamaño) dentro de un mismo párrafo. Se usa para texto enriquecido simple sin necesidad de un editor HTML completo.

### TilePane
Distribuye los hijos en una cuadrícula de celdas de igual tamaño ("tiles"), ajustando automáticamente cuántas caben por fila/columna según el espacio disponible. Configurable mediante `prefColumns`, `prefRows`, `hgap`, `vgap` y `tileAlignment`. Común en galerías de íconos o miniaturas.

### TitledPane
Panel colapsable con un título visible en la parte superior, que al hacer clic expande o contrae su contenido (`content`). Propiedades clave: `expanded`, `collapsible` y `animated`. Se usa individualmente o agrupado dentro de un `Accordion`.

### ToolBar
Contenedor horizontal o vertical diseñado para alojar botones, separadores y controles de acceso rápido, similar a las barras de herramientas de aplicaciones de escritorio. Soporta overflow automático (menú desplegable) cuando el espacio es insuficiente. Configurable con `orientation`.

### VBox
Alinea sus nodos hijos verticalmente en una sola columna. Configuraciones equivalentes a `HBox`: `spacing`, `alignment` y `VBox.vgrow` por hijo. Ampliamente usado como contenedor raíz o para agrupar formularios y listas de controles.

---

## Controles

### Button
Control interactivo estándar que ejecuta una acción al hacer clic, vinculada mediante el atributo `onAction` en FXML. Propiedades relevantes: `text`, `graphic`, `defaultButton` (activa con Enter) y `cancelButton` (activa con Esc). Es el control más común para disparar eventos en la lógica de la aplicación.

### CheckBox
Control de selección booleana (marcado/desmarcado), con soporte opcional para estado indeterminado (`indeterminate`) cuando `allowIndeterminate` es `true`. Su valor se obtiene mediante la propiedad `selected`. Usado en formularios de opciones múltiples independientes entre sí.

### ChoiceBox
Control desplegable simple para seleccionar un valor único de una lista predefinida (`items`). Más ligero que `ComboBox`, sin soporte de edición ni autocompletado. Ideal para listas cortas de opciones fijas.

### ColorPicker
Control que permite seleccionar un color mediante una paleta desplegable o un selector personalizado. La propiedad `value` almacena el `Color` seleccionado. Soporta modo estándar o de "botón" (`ColorPicker.STYLE_CLASS_BUTTON`).

### ComboBox
Control desplegable editable o no editable que combina un campo de texto con una lista de selección (`items`). Soporta `editable`, `promptText` y `converter` para personalizar la representación de los objetos. Más versátil que `ChoiceBox`, admite autocompletado con lógica adicional.

### DatePicker
Control especializado para seleccionar fechas mediante un calendario emergente o entrada manual de texto. Propiedad principal `value` (tipo `LocalDate`). Configurable con `dayCellFactory` para personalizar la apariencia/restricciones de días específicos.

### HTMLEditor
Editor de texto enriquecido tipo WYSIWYG con barra de herramientas (negrita, cursiva, listas, alineación, color). Su contenido se obtiene/asigna como `htmlText`. Pesado en recursos; usado para entradas de texto formateado tipo correo o notas.

### Hyperlink
Control similar a un `Button` pero con apariencia de enlace de texto (subrayado al pasar el mouse). Dispara `onAction` al hacer clic. Propiedad `visited` permite cambiar su estilo tras ser usado, replicando el comportamiento de enlaces web.

### ImageView
Nodo (no control interactivo per se) que muestra una imagen (`Image`) cargada desde archivo o URL. Configurable con `fitWidth`, `fitHeight`, `preserveRatio` y `smooth`. No es un contenedor; solo renderiza gráficos rasterizados.

### Label
Control de solo texto no editable, usado para etiquetas descriptivas, títulos de campos o mensajes estáticos. Soporta `graphic` para íconos junto al texto y `labelFor` para asociarlo a otro control (mejora accesibilidad).

### ListView
Muestra una lista desplazable de elementos (`items`), con selección simple o múltiple (`SelectionMode`). Soporta `cellFactory` para personalizar la renderización de cada ítem. Ideal para listas dinámicas de datos.

### MediaView
Nodo que renderiza contenido de video a partir de un `MediaPlayer`. Se configura junto a un objeto `Media`/`MediaPlayer` en el código Java, ya que Scene Builder solo define la vista, no la lógica de reproducción.

### MenuBar
Barra de menús superior típica de aplicaciones de escritorio, que contiene objetos `Menu` con sus respectivos `MenuItem`. Configurable `useSystemMenuBar` para integrarse con la barra nativa del sistema operativo (macOS).

### MenuButton
Botón que al presionarse despliega una lista de `MenuItem`, similar a un menú contextual pero anclado a un botón visible. Se diferencia de `SplitMenuButton` en que todo el botón despliega el menú.

### Pagination
Control de navegación por páginas, útil para dividir contenido extenso (listas, resultados) en secciones. Configurable con `pageCount` y `pageFactory` (define el contenido de cada página vía código).

### PasswordField
Variante de `TextField` que oculta el texto ingresado con caracteres de máscara. Comparte propiedades como `promptText` y `text`, pero por seguridad su contenido no debe manipularse como texto plano en producción.

### ProgressBar
Indicador visual de progreso determinado, con `progress` en rango 0.0–1.0 (o -1 para modo indeterminado/animado). Usado en cargas, descargas o procesos con avance medible.

### ProgressIndicator
Similar a `ProgressBar` pero de forma circular. Comparte la propiedad `progress`; en modo indeterminado (`-1`) muestra una animación de "spinner" continua.

### RadioButton
Control de selección exclusiva dentro de un grupo (`ToggleGroup`); solo uno puede estar seleccionado a la vez. Requiere asignar manualmente el mismo `ToggleGroup` a todos los botones relacionados en Scene Builder.

### ScrollBar
Barra de desplazamiento independiente (no asociada automáticamente a un `ScrollPane`).

#### Vertical
Desplazamiento en sentido vertical.

#### Horizontal
Desplazamiento en sentido horizontal.

Rara vez se usa sola; normalmente es generada internamente por otros controles.

### Separator
Línea divisoria visual entre elementos.

#### Vertical
Línea divisoria orientada verticalmente.

#### Horizontal
Línea divisoria orientada horizontalmente.

Usado en `ToolBar`, menús y formularios para agrupar visualmente secciones relacionadas.

### Slider
Control deslizante para seleccionar un valor numérico dentro de un rango (`min`, `max`, `value`).

#### Vertical
El deslizador se mueve en sentido vertical.

#### Horizontal
El deslizador se mueve en sentido horizontal.

Soporta marcas (`showTickMarks`, `showTickLabels`) y ajuste por pasos (`majorTickUnit`, `minorTickCount`).

### Spinner
Control numérico o de lista con botones de incremento/decremento. Requiere un `SpinnerValueFactory` (configurado en código) para definir el rango o conjunto de valores. Soporta edición manual del texto si `editable=true`.

### SplitMenuButton
Combina un botón de acción principal con una flecha desplegable que muestra un menú de opciones adicionales (`MenuItem`). Se diferencia de `MenuButton` en que la acción principal es independiente del menú.

### TableColumn
Define una columna dentro de un `TableView` o `TreeTableView`, especificando su `text` (encabezado), `cellValueFactory` (fuente del dato) y ancho preferido. No funciona de forma independiente.

### TableView
Control tabular que muestra datos estructurados en filas y columnas (`TableColumn`), con soporte de ordenamiento, selección y edición de celdas. Requiere vincularse a una `ObservableList` en el código Java.

### TextArea
Campo de texto multilínea editable, con salto de línea automático (`wrapText`) y barras de desplazamiento internas. Propiedades clave: `text`, `promptText`, `prefRowCount`.

### TextField
Campo de texto de una sola línea para entrada de datos simples. Propiedades comunes: `text`, `promptText`, `editable`. Es el control de entrada más básico y utilizado en formularios.

### ToggleButton
Botón con estado persistente (presionado/no presionado), similar a `CheckBox` pero con apariencia de botón. Puede agruparse mediante `ToggleGroup` para comportarse como selección exclusiva.

### TreeTableColumn
Columna especializada usada dentro de un `TreeTableView`, similar a `TableColumn` pero adaptada a estructuras jerárquicas de datos.

### TreeTableView
Combina la estructura jerárquica de un `TreeView` con la organización tabular de un `TableView`. Requiere un `TreeItem` raíz y columnas `TreeTableColumn` vinculadas a propiedades del modelo.

### TreeView
Muestra datos organizados jerárquicamente mediante nodos expandibles (`TreeItem`). Soporta selección simple/múltiple y `cellFactory` personalizado para la representación visual de cada nodo.

### WebView
Componente que embebe un motor de renderizado web (WebKit) dentro de la aplicación JavaFX, permitiendo mostrar contenido HTML/CSS/JS. Se controla mediante un `WebEngine` asociado en el código Java.

---

## Gluon (componentes móviles/Glisten)

> Nota: estos componentes requieren la biblioteca **Gluon Mobile/Glisten** en el classpath del proyecto; no forman parte del SDK estándar de JavaFX.

### AppBar
Barra superior de navegación estándar en aplicaciones Gluon, que agrupa botón de menú, título y acciones contextuales. Equivalente móvil de una `ToolBar`/`MenuBar` combinada.

### AutoCompleteTextField
Campo de texto con sugerencias de autocompletado desplegadas dinámicamente mientras el usuario escribe, basadas en una fuente de datos configurada en código.

### Avatar
Componente visual circular (o con máscara personalizada) para mostrar imágenes de perfil de usuario, con soporte de bordes y tamaños configurables.

### BottomNavigation
Barra de navegación inferior típica de apps móviles, que aloja múltiples `BottomNavigationButton` para cambiar entre vistas principales.

### BottomNavigationButton
Botón individual usado dentro de un `BottomNavigation`, combinando ícono y texto para representar una sección de la app.

### CardPane
Contenedor con estilo de "tarjeta" (bordes redondeados, sombra), usado para agrupar contenido visualmente destacado, común en interfaces tipo Material Design.

### CharmListView
Lista optimizada para dispositivos móviles con soporte de "pull to refresh" y celdas personalizables, parte del conjunto de controles Charm de Gluon.

### Chip
Elemento compacto tipo "etiqueta" con texto e ícono opcional, usado para representar filtros, tags o selecciones múltiples de forma visual.

### CollapsedPanel
Estado colapsado (contraído) de un `ExpansionPanel`, mostrando solo el resumen/encabezado del contenido.

### DropdownButton
Botón que despliega un menú contextual de opciones, similar a `MenuButton` pero con estilo Gluon/Material.

### ExpandedPanel
Estado expandido de un `ExpansionPanel`, mostrando el contenido completo asociado.

### ExpansionPanel
Panel colapsable con dos estados (`CollapsedPanel`/`ExpandedPanel`), similar a `TitledPane` pero con estética Material Design.

### ExpansionPanelContainer
Contenedor que agrupa múltiples `ExpansionPanel`, gestionando su comportamiento de expansión conjunta.

### Icon
Componente que renderiza íconos vectoriales (basados en fuentes de íconos), configurable en tamaño y color mediante estilos CSS.

### Layer
Capa flotante superpuesta a la vista principal, usada para menús, notificaciones o paneles emergentes contextuales.

### NavigationDrawer
Menú lateral deslizante ("hamburger menu") típico de apps móviles, que se despliega desde el borde de la pantalla.

### ProgressBar (Gluon)
Variante de la barra de progreso estándar adaptada al estilo visual Charm/Material de Gluon.

### ProgressIndicator (Gluon)
Variante circular de indicador de progreso con la estética visual de Gluon Charm.

### SettingsPane
Panel prediseñado para mostrar opciones de configuración de la aplicación en formato de lista con controles asociados.

### SplashView
Vista de pantalla de bienvenida/carga inicial ("splash screen") mostrada al arrancar la aplicación.

### TextField (Gluon)
Variante estilizada del campo de texto estándar, adaptada visualmente al diseño Charm de Gluon.

### ToggleButtonGroup
Agrupa múltiples `ToggleButton` bajo un comportamiento de selección exclusiva o múltiple, con estilo Material.

### View
Contenedor raíz de una pantalla/vista dentro de la arquitectura de navegación de Gluon Mobile, análogo a una `Scene` individual dentro del flujo de la app.

---

## Menú

### CheckMenuItem
Ítem de menú con estado booleano (marcado/desmarcado), similar a un `CheckBox` pero dentro de un menú desplegable.

### ContextMenu
Menú emergente contextual que aparece típicamente al hacer clic derecho sobre un nodo, conteniendo objetos `MenuItem`.

### CustomMenuItem
Ítem de menú que permite embeber un nodo personalizado (cualquier control) en lugar de solo texto, manteniendo el comportamiento de selección de menú.

### Menu
Contenedor de ítems (`MenuItem`) dentro de una `MenuBar` o como submenú anidado, mostrado como una etiqueta desplegable.

### MenuItem
Elemento de acción individual dentro de un `Menu`, que dispara `onAction` al seleccionarse. Es el ítem de menú más básico.

### RadioMenuItem
Ítem de menú de selección exclusiva dentro de un grupo (`ToggleGroup`), similar a `RadioButton` pero en formato de menú.

### SeparatorMenuItem
Línea divisoria usada para agrupar visualmente secciones dentro de un menú.

---

## Miscelánea

### Canvas
Superficie de dibujo de bajo nivel donde se renderizan gráficos mediante `GraphicsContext` en código Java (líneas, formas, imágenes, texto pixelado). No tiene representación declarativa rica en FXML más allá de sus dimensiones.

### Group
Nodo contenedor simple que agrupa otros nodos para aplicarles transformaciones o efectos conjuntos, sin gestionar layout automático (los hijos usan coordenadas absolutas).

### Region
Clase base abstracta de la mayoría de contenedores JavaFX; rara vez se usa directamente, pero expone propiedades comunes como `prefWidth`, `prefHeight`, `padding` y fondo (`background`) vía CSS.

### Scene
Representa el contenido gráfico completo mostrado dentro de un `Stage` (ventana). Define el nodo raíz, dimensiones y hoja de estilos CSS asociada. No es un nodo visual en sí, sino el contenedor de más alto nivel.

### Stage
Representa la ventana del sistema operativo en JavaFX (equivalente a un `JFrame` en Swing). Contiene una `Scene` y expone propiedades como título, ícono, redimensionable y modalidad.

### SubScene
Permite embeber una escena secundaria (potencialmente 3D) dentro del árbol de nodos de la escena principal, útil para combinar contenido 2D y 3D o renderizados independientes.

### SwingNode
Puente de integración que permite embeber componentes de Swing (`JComponent`) dentro de una interfaz JavaFX, facilitando la migración gradual de aplicaciones legadas.

### Tooltip
Texto emergente informativo que aparece al posicionar el cursor sobre un control durante un tiempo determinado. Configurable con `text`, `showDelay` y `hideDelay`.

---

## Formas (Shapes)

### Arc
Dibuja un arco (porción de elipse) definido por `startAngle`, `length` (ángulo) y tipo (`OPEN`, `CHORD`, `ROUND`).

### ArcTo
Instrucción de trazado usada dentro de un `Path` para dibujar un segmento de arco entre dos puntos.

### Box
Figura 3D rectangular (caja), definida por `width`, `height` y `depth`.

### Circle
Figura geométrica circular definida por `radius` y centro (`centerX`, `centerY`).

### ClosePath
Instrucción de trazado que cierra la figura actual dentro de un `Path`, conectando el punto final con el inicial.

### CubicCurve
Curva de Bézier cúbica definida por dos puntos de control y dos puntos extremos.

### CubicCurveTo
Instrucción de trazado dentro de un `Path` para dibujar un segmento de curva cúbica.

### Cylinder
Figura 3D cilíndrica definida por `radius` y `height`.

### Ellipse
Figura geométrica elíptica definida por radios `radiusX` y `radiusY`.

### HLineTo
Instrucción de trazado que dibuja una línea horizontal hasta una coordenada X específica dentro de un `Path`.

### Line
Figura geométrica simple que dibuja una línea recta entre dos puntos (`startX/Y`, `endX/Y`).

### LineTo
Instrucción de trazado que dibuja una línea recta hasta un punto específico dentro de un `Path`.

### MeshView
Nodo 3D que renderiza una malla poligonal personalizada (`TriangleMesh`), usado para modelos 3D complejos.

### MoveTo
Instrucción de trazado que mueve el "cursor" de dibujo a una nueva posición sin dibujar línea, dentro de un `Path`.

### Path
Figura compuesta por una secuencia de instrucciones de trazado (`MoveTo`, `LineTo`, `ArcTo`, etc.), permitiendo dibujar formas complejas y personalizadas.

### Polygon
Figura geométrica cerrada definida por una lista de puntos (`points`) conectados entre sí.

### Polyline
Similar a `Polygon` pero sin cerrar automáticamente la figura entre el último y primer punto.

### QuadCurve
Curva de Bézier cuadrática definida por un punto de control y dos extremos.

### QuadCurveTo
Instrucción de trazado dentro de un `Path` para dibujar un segmento de curva cuadrática.

### Rectangle
Figura rectangular definida por `width`, `height` y posición; soporta esquinas redondeadas (`arcWidth`, `arcHeight`).

### SVGPath
Figura definida mediante una cadena de datos SVG estándar (`content`), permitiendo importar formas vectoriales complejas directamente.

### Sphere
Figura 3D esférica definida por `radius`.

### Text
Nodo que renderiza texto vectorial con propiedades de fuente, tamaño y alineación, distinto de `Label` en que no tiene fondo ni comportamiento de control.

### VLineTo
Instrucción de trazado que dibuja una línea vertical hasta una coordenada Y específica dentro de un `Path`.

---

## Gráficos (Charts)

### AreaChart
Gráfico de área que representa datos continuos con relleno bajo la curva, útil para visualizar tendencias acumuladas.

### BarChart
Gráfico de barras (verticales u horizontales) para comparar valores discretos entre categorías.

### BubbleChart
Gráfico de dispersión donde cada punto ("burbuja") tiene un tercer valor representado por su tamaño (radio).

### LineChart
Gráfico de líneas que conecta puntos de datos secuenciales, ideal para mostrar tendencias a lo largo del tiempo.

### PieChart
Gráfico circular que representa proporciones de un total mediante segmentos ("porciones").

### ScatterChart
Gráfico de dispersión que grafica puntos individuales sin conectarlos, útil para identificar correlaciones o distribución de datos.

### StackedAreaChart
Variante de `AreaChart` donde múltiples series se apilan una sobre otra, mostrando tanto valores individuales como el total acumulado.

### StackedBarChart
Variante de `BarChart` donde las barras de distintas series se apilan dentro de una misma categoría.

---

## 3D

### AmbientLight
Luz que ilumina uniformemente toda la escena sin dirección ni sombras, usada como iluminación base en escenas 3D.

### ParallelCamera
Cámara que usa proyección paralela (ortográfica), sin efecto de perspectiva/profundidad — los objetos no cambian de tamaño con la distancia.

### PerspectiveCamera
Cámara con proyección de perspectiva realista, donde los objetos más lejanos se ven más pequeños, simulando la visión humana.

### PointLight
Fuente de luz puntual que emite en todas direcciones desde una posición específica, generando sombras y reflejos direccionales en objetos 3D.

---

# Configuraciones de archivos FXML y características obligatorias para implementación en Java con IntelliJ IDEA

**Qué es:** FXML es un formato de marcado XML declarativo usado por JavaFX para definir la estructura visual de una interfaz de usuario de forma independiente al código Java, permitiendo separar la vista (diseño) de la lógica (controlador).

**Estructura obligatoria del archivo FXML:**
- Encabezado XML (`<?xml version="1.0" encoding="UTF-8"?>`) e imports de las clases JavaFX usadas (`<?import javafx.scene.control.*?>`, etc.).
- Nodo raíz (ej. `<AnchorPane>`, `<VBox>`) con atributo `fx:controller` apuntando a la clase Java completa (ej. `fx:controller="com.app.MainController"`).
- Atributo `xmlns:fx="http://javafx.com/fxml"` obligatorio para habilitar directivas FXML (`fx:id`, `fx:controller`, `fx:action`).

**Configuración obligatoria en IntelliJ IDEA:**
1. El proyecto debe tener el **SDK de JavaFX** configurado (Project Structure → Libraries), añadiendo los JARs del SDK JavaFX (ya que desde Java 11 JavaFX no viene incluido en el JDK).
2. Configurar los **VM options** de ejecución con los módulos requeridos: `--module-path "RUTA_SDK_JAVAFX\lib" --add-modules javafx.controls,javafx.fxml` (y otros módulos como `javafx.web` si se usa `WebView`).
3. Si se usa Maven/Gradle, incluir las dependencias `javafx-controls`, `javafx-fxml` (y opcionalmente `javafx-web`, `javafx-media`) con el plugin correspondiente (`javafx-maven-plugin` u `org.openjfx.javafxplugin`).
4. La clase controlador debe estar en el classpath correcto y sus métodos/campos vinculados con `fx:id` deben coincidir exactamente en nombre con `@FXML private ...` en Java.
5. Cargar el FXML en el código con `FXMLLoader.load(getClass().getResource("archivo.fxml"))` dentro de la clase que extiende `Application`.
6. Cada control interactivo referenciado desde Java requiere `fx:id` en el FXML y su respectivo campo anotado `@FXML` en el controlador; los eventos (`onAction`, etc.) deben coincidir con métodos públicos/privados anotados `@FXML`.
7. Las hojas de estilo CSS se enlazan mediante `stylesheets="@archivo.css"` (ruta relativa al FXML) o programáticamente vía `scene.getStylesheets().add(...)`.

---

# Regex101

**Qué es:** Regex101 (regex101.com) es una herramienta web interactiva para escribir, probar, depurar y explicar expresiones regulares en tiempo real, ampliamente usada por desarrolladores para validar patrones antes de integrarlos en código.

**Para qué sirve:** Permite ingresar una expresión regular y un texto de prueba, mostrando de inmediato qué coincidencias (matches) se producen, resaltadas visualmente, junto con una explicación detallada de cada componente del patrón (cuantificadores, grupos, clases de caracteres).

**Características/configuraciones generales:**
- **Selector de "flavor" (motor regex):** PCRE2, PCRE (PHP), ECMAScript (JavaScript), Python, Golang, Java, .NET; cada uno tiene ligeras diferencias de sintaxis y comportamiento.
- **Panel de explicación automática:** desglosa el patrón token por token en lenguaje natural.
- **Generador de código:** exporta el patrón ya formateado para el lenguaje de programación seleccionado (incluyendo Java, con `Pattern.compile(...)`).
- **Sustitución (Substitution):** permite probar reemplazos de texto usando grupos capturados (`$1`, `$2`, etc.).
- **Unit tests / guardado:** permite guardar expresiones con casos de prueba para reutilizarlas o compartirlas mediante enlace.
- **Debugger paso a paso:** muestra el proceso interno del motor regex al evaluar el patrón, útil para detectar problemas de rendimiento (catastrophic backtracking).

---

# Expresiones Regulares (Regex)

**Qué son:** Secuencias de caracteres que definen un patrón de búsqueda, usadas para validar, buscar, extraer o reemplazar texto que cumple ciertas reglas estructurales (formatos de correo, teléfonos, contraseñas, etc.).

**Para qué sirven:** Validación de formularios, búsqueda y reemplazo avanzado en editores de texto, parsing de datos no estructurados, filtrado de logs, y validación de entradas de usuario en aplicaciones (incluyendo JavaFX mediante `TextFormatter` o validaciones manuales con la clase `java.util.regex.Pattern`).

**Elementos/configuraciones generales más comunes:**
- **Clases de caracteres:** `\d` (dígito), `\w` (palabra), `\s` (espacio), `.` (cualquier carácter), y sus negaciones (`\D`, `\W`, `\S`).
- **Cuantificadores:** `*` (0 o más), `+` (1 o más), `?` (0 o 1), `{n,m}` (rango específico de repeticiones).
- **Anclas:** `^` (inicio de línea), `$` (fin de línea), `\b` (límite de palabra).
- **Grupos y captura:** `(...)` (grupo de captura), `(?:...)` (grupo sin captura), `(?<nombre>...)` (grupo nombrado).
- **Alternancia:** `|` (operador "o lógico" entre patrones).
- **Clases personalizadas:** `[abc]` (cualquiera de esos caracteres), `[^abc]` (negación), `[a-z]` (rango).
- **Lookahead/Lookbehind:** `(?=...)`, `(?!...)`, `(?<=...)`, `(?<!...)` — validan contexto sin consumir caracteres.
- **Uso en Java:** clases `java.util.regex.Pattern` y `Matcher`; métodos comunes `matches()`, `find()`, `group()`, `replaceAll()`.

# Ejemplo de Implementación: Navegación entre Pantallas con FXML (Estilo Interfaz de IA)

A continuación se muestra un ejemplo completo de dos pantallas conectadas mediante `FXMLLoader`: una pantalla inicial con un `ComboBox`, texto descriptivo y un botón de inicio; y una segunda pantalla tipo "chat" con un área de texto no editable (respuestas), un campo de entrada para el usuario, un botón de envío y un botón para regresar.

## Estructura del proyecto

```
src/
 └── com/app/
     ├── MainApp.java
     ├── PantallaInicioController.java
     ├── PantallaChatController.java
resources/
 └── com/app/
     ├── pantalla_inicio.fxml
     └── pantalla_chat.fxml
```

## Pantalla 1: Selección e inicio (`pantalla_inicio.fxml`)

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ComboBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.VBox?>

<VBox xmlns:fx="http://javafx.com/fxml" fx:controller="com.app.PantallaInicioController"
      alignment="CENTER" spacing="15">
    <padding>
        <Insets top="30" bottom="30" left="30" right="30"/>
    </padding>

    <Label text="Bienvenido al Asistente Virtual" style="-fx-font-size: 18px; -fx-font-weight: bold;"/>
    <Label text="Seleccione el modelo con el que desea conversar:"/>

    <ComboBox fx:id="comboModelo" promptText="Seleccione un modelo"/>

    <Button fx:id="btnIniciar" text="Iniciar conversación" onAction="#irAPantallaChat"/>
</VBox>
```

## Controlador de la Pantalla 1 (`PantallaInicioController.java`)

```java
package com.app;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaInicioController {

    @FXML
    private ComboBox<String> comboModelo;

    // Se ejecuta automáticamente al cargar el FXML
    @FXML
    public void initialize() {
        comboModelo.setItems(FXCollections.observableArrayList(
                "Modelo Básico",
                "Modelo Avanzado",
                "Modelo Experimental"
        ));
    }

    @FXML
    private void irAPantallaChat(ActionEvent event) {
        String modeloSeleccionado = comboModelo.getValue();

        if (modeloSeleccionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Debe seleccionar un modelo antes de continuar.");
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pantalla_chat.fxml"));
            Parent root = loader.load();

            // Se pasa el modelo seleccionado al controlador de la siguiente pantalla
            PantallaChatController controller = loader.getController();
            controller.setModeloSeleccionado(modeloSeleccionado);

            Stage stage = (Stage) comboModelo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Conversación - " + modeloSeleccionado);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## Pantalla 2: Conversación tipo chat (`pantalla_chat.fxml`)

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ScrollPane?>
<?import javafx.scene.control.TextArea?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.layout.HBox?>

<BorderPane xmlns:fx="http://javafx.com/fxml" fx:controller="com.app.PantallaChatController">

    <top>
        <Button fx:id="btnVolver" text="&lt; Volver" onAction="#volverAPantallaInicio">
            <BorderPane.margin>
                <Insets top="10" left="10" bottom="5"/>
            </BorderPane.margin>
        </Button>
    </top>

    <center>
        <ScrollPane fitToWidth="true">
            <TextArea fx:id="areaRespuestas" editable="false" wrapText="true"
                      prefHeight="400" style="-fx-font-size: 14px;"/>
        </ScrollPane>
    </center>

    <bottom>
        <HBox spacing="10" alignment="CENTER">
            <padding>
                <Insets top="10" bottom="10" left="10" right="10"/>
            </padding>
            <TextField fx:id="campoEntrada" promptText="Escriba su mensaje..." HBox.hgrow="ALWAYS"/>
            <Button fx:id="btnEnviar" text="Enviar" onAction="#enviarMensaje"/>
        </HBox>
    </bottom>

</BorderPane>
```

## Controlador de la Pantalla 2 (`PantallaChatController.java`)

```java
package com.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaChatController {

    @FXML
    private TextArea areaRespuestas;

    @FXML
    private TextField campoEntrada;

    private String modeloSeleccionado;

    // Recibe el dato enviado desde la pantalla anterior
    public void setModeloSeleccionado(String modelo) {
        this.modeloSeleccionado = modelo;
        areaRespuestas.appendText("[Sistema] Conversación iniciada con: " + modelo + "\n\n");
    }

    @FXML
    private void enviarMensaje(ActionEvent event) {
        String mensaje = campoEntrada.getText().trim();

        if (mensaje.isEmpty()) {
            return;
        }

        areaRespuestas.appendText("Usuario: " + mensaje + "\n");

        // Simulación de respuesta (aquí se conectaría con la lógica real)
        String respuesta = generarRespuestaSimulada(mensaje);
        areaRespuestas.appendText(modeloSeleccionado + ": " + respuesta + "\n\n");

        campoEntrada.clear();
    }

    private String generarRespuestaSimulada(String entrada) {
        return "Procesando su mensaje de " + entrada.length() + " caracteres...";
    }

    @FXML
    private void volverAPantallaInicio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("pantalla_inicio.fxml"));
            Stage stage = (Stage) campoEntrada.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## Clase principal (`MainApp.java`)

```java
package com.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("pantalla_inicio.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Inicio");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

# Ejemplos de Expresiones Regulares (Regex) en Java

## Patrones comunes

| Propósito | Expresión regular | Coincide con |
|:---|:---|:---|
| Correo electrónico | `^[\w.+-]+@[\w-]+\.[a-zA-Z]{2,}$` | `usuario@dominio.com` |
| Número telefónico (CR) | `^\d{4}-\d{4}$` | `8888-1234` |
| Solo letras (sin tildes) | `^[a-zA-Z]+$` | `Hola` |
| Contraseña segura | `^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$` | Mínimo 8 caracteres, mayúscula, minúscula, número y símbolo |
| Fecha formato `dd/mm/aaaa` | `^(0[1-9]\|[12]\d\|3[01])/(0[1-9]\|1[0-2])/\d{4}$` | `15/08/2026` |
| Número decimal positivo | `^\d+(\.\d{1,2})?$` | `1234.56` |
| Cédula costarricense | `^\d{1}-\d{4}-\d{4}$` | `1-2345-6789` |
| URL básica | `^(https?://)?([\w-]+\.)+[\w-]{2,}(/\S*)?$` | `https://www.ejemplo.com/pagina` |

## Uso en Java con `Pattern` y `Matcher`

```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorRegex {

    public static void main(String[] args) {

        // Validar correo electrónico
        String correo = "usuario@dominio.com";
        Pattern patronCorreo = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
        Matcher matcherCorreo = patronCorreo.matcher(correo);
        System.out.println("¿Correo válido?: " + matcherCorreo.matches());

        // Extraer todos los números de un texto
        String texto = "Tengo 3 gatos, 12 peces y 5 perros";
        Pattern patronNumeros = Pattern.compile("\\d+");
        Matcher matcherNumeros = patronNumeros.matcher(texto);
        while (matcherNumeros.find()) {
            System.out.println("Número encontrado: " + matcherNumeros.group());
        }

        // Reemplazar texto usando grupos capturados
        String fecha = "2026-08-16";
        String fechaFormateada = fecha.replaceAll(
                "(\\d{4})-(\\d{2})-(\\d{2})",
                "$3/$2/$1"
        );
        System.out.println("Fecha reformateada: " + fechaFormateada); // 16/08/2026

        // Validar contraseña segura
        String contrasena = "Segura123!";
        String patronContrasena = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        System.out.println("¿Contraseña segura?: " + contrasena.matches(patronContrasena));
    }
}
```

## Uso de Regex en un `TextFormatter` de JavaFX

Permite restringir en tiempo real lo que el usuario puede escribir en un `TextField` (por ejemplo, aceptar solo números):

```java
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import java.util.regex.Pattern;

public class CampoSoloNumeros {

    public static TextFormatter<String> crearFormatterNumerico() {
        Pattern patron = Pattern.compile("\\d*");

        return new TextFormatter<>(change -> {
            String textoNuevo = change.getControlNewText();
            if (patron.matcher(textoNuevo).matches()) {
                return change; // se acepta el cambio
            }
            return null; // se rechaza el cambio
        });
    }

    // Uso:
    // TextField campo = new TextField();
    // campo.setTextFormatter(crearFormatterNumerico());
}
```

