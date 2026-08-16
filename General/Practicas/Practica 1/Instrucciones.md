# Práctica 1
## Características de Java, UX/Usabilidad, JavaFX y Programación Dirigida por Eventos

---

### Ejemplo 1

Un desarrollador está diseñando una clase en Java y debe decidir cómo aplicar los pilares del lenguaje.

* a) Explique qué significa el encapsulamiento en Java y cómo se relaciona con ocultar los detalles internos de una clase.
* b) Explique la diferencia entre un tipo primitivo y una clase envoltorio (Wrapper Class), dando un ejemplo de cada una.
* c) Explique por qué en Java "todo son referencias" y qué papel cumple el Garbage Collector en este contexto.
* d) Explique qué son los Generics y por qué mejoran la seguridad de tipos en tiempo de compilación, usando como ejemplo la sintaxis `Map<K, V>`.

---

### Ejemplo 2

Una empresa está evaluando la experiencia de sus usuarios con una nueva aplicación de escritorio.

* a) Explique la diferencia entre Utilidad y Usabilidad, y cómo se combinan para que un producto sea "útil".
* b) Mencione tres principios de usabilidad vistos en el curso (por ejemplo, curva de aprendizaje, eficiencia, memoria).
* c) Mencione dos heurísticas de usabilidad y explique brevemente en qué consisten.
* d) Explique la diferencia entre una limitación de accesibilidad permanente, incidental y ambiental, dando un ejemplo de cada una.

---

### Ejemplo 3

Un equipo está diseñando la interfaz de una aplicación en JavaFX Scene Builder y debe elegir los contenedores adecuados.

* a) Explique la diferencia entre `GridPane` y `FlowPane` en cuanto a cómo organizan a sus nodos hijos.
* b) Explique la diferencia entre `BorderPane` y `StackPane` en cuanto a la disposición de su contenido.
* c) Explique qué es un `Pane` (contenedor base) y por qué no maneja alineación automática como sí lo hace un `VBox`.
* d) Mencione dos propiedades obligatorias que debe tener un archivo FXML para poder vincularse correctamente con una clase Controller en Java.

---

### Ejemplo 4

Se necesita validar entradas de usuario en un formulario utilizando expresiones regulares.

* a) Explique qué es una expresión regular y para qué se utiliza dentro de una aplicación.
* b) Explique la diferencia entre los cuantificadores `*`, `+` y `?`.
* c) Explique qué hace un grupo de captura `(...)` y en qué se diferencia de un grupo sin captura `(?:...)`.
* d) Mencione las clases de Java utilizadas para trabajar con expresiones regulares y dos métodos comunes de cada una.

---

### Ejemplo 5

Una aplicación de escritorio debe reaccionar constantemente a las acciones del usuario sin bloquearse.

* a) Explique en qué consiste el paradigma de programación dirigida por eventos (Event-Driven Programming).
* b) Describa las seis etapas del ciclo básico de una aplicación dirigida por eventos (Event Loop).
* c) Explique la diferencia entre un evento real y un pseudoevento, dando un ejemplo de cada uno.
* d) Explique la diferencia entre los tres tipos de jerarquía de eventos: prioridad fija, secuencial (FIFO) y por tipo de origen.

---

## RESPUESTAS

---

### Ejemplo 1

* a) Consiste en ocultar los detalles internos de implementación de una clase, exponiendo únicamente lo necesario a través de métodos públicos (getters/setters), evitando que otras clases accedan o modifiquen directamente sus atributos internos.
* b) Un tipo primitivo (como `int`) almacena directamente un valor simple en memoria, mientras que su clase envoltorio (como `Integer`) representa ese mismo valor como un objeto, permitiendo usarlo en contextos que requieren objetos, como colecciones genéricas (`List<Integer>`).
* c) Porque las variables de tipo objeto no almacenan el objeto en sí, sino una referencia (dirección) hacia donde está ubicado en memoria; el Garbage Collector se encarga de liberar automáticamente la memoria de aquellos objetos que ya no tienen ninguna referencia apuntándolos, evitando que el programador deba gestionar la memoria manualmente.
* d) Los Generics permiten definir clases, interfaces y métodos que trabajan con tipos de datos parametrizables; en `Map<K, V>`, `K` representa el tipo de la llave y `V` el tipo del valor, permitiendo que el compilador detecte errores de tipo en tiempo de compilación en lugar de en tiempo de ejecución.

---

### Ejemplo 2

* a) La Utilidad es que el producto cumpla la función para la cual fue diseñado; la Usabilidad es qué tan fácil resulta usarlo. Un producto verdaderamente útil combina ambas cualidades (Útil = Usabilidad + Utilidad).
* b) Curva de aprendizaje, eficiencia de uso y capacidad de recordar cómo usar el producto (memoria) — también podrían mencionarse el manejo de errores o la satisfacción general.
* c) Visibilidad del estado del sistema: el usuario siempre debe saber qué está pasando en la aplicación. Control y libertad del usuario: el usuario debe poder deshacer acciones o salir fácilmente de una situación no deseada (también podrían mencionarse consistencia, simplicidad o coincidencia con el mundo real).
* d) Permanente: una discapacidad visual total; Incidental (temporal): un brazo enyesado que impide usar el mouse con normalidad; Ambiental: no poder escuchar el audio de una aplicación porque se está en un lugar ruidoso.

---

### Ejemplo 3

* a) `GridPane` posiciona los nodos en filas y columnas mediante índices específicos (`rowIndex`, `columnIndex`), siendo ideal para formularios estructurados; `FlowPane` organiza los nodos en un flujo continuo que se ajusta automáticamente al espacio disponible, sin necesidad de definir filas/columnas manualmente.
* b) `BorderPane` divide el área en cinco regiones fijas (top, bottom, left, right, center), donde cada región admite un solo nodo; `StackPane` superpone todos sus nodos hijos en capas, uno encima de otro, alineados por defecto al centro.
* c) Un `Pane` es un contenedor base sin política de layout automática; sus hijos se posicionan mediante coordenadas absolutas (`layoutX`/`layoutY`), a diferencia de un `VBox`, que alinea automáticamente a sus hijos verticalmente sin necesidad de indicar coordenadas.
* d) Debe tener el nodo raíz con el atributo `fx:controller` apuntando a la clase Java completa del controlador, y el atributo `xmlns:fx="http://javafx.com/fxml"`, que habilita las directivas FXML como `fx:id` y `fx:controller`.

---

### Ejemplo 4

* a) Es una secuencia de caracteres que define un patrón de búsqueda, utilizada para validar, buscar, extraer o reemplazar texto que cumple ciertas reglas estructurales, como formatos de correo, teléfonos o contraseñas.
* b) `*` significa 0 o más repeticiones; `+` significa 1 o más repeticiones; `?` significa 0 o 1 repetición (el elemento es opcional).
* c) Un grupo de captura `(...)` almacena el texto que coincide con esa parte del patrón para poder reutilizarlo posteriormente (por ejemplo, en un reemplazo con `$1`); un grupo sin captura `(?:...)` agrupa una parte del patrón sin guardar dicho texto para uso posterior.
* d) Las clases `java.util.regex.Pattern` y `java.util.regex.Matcher`; métodos comunes de `Matcher` son `matches()`, `find()` y `group()`; de `String` se usa comúnmente `replaceAll()`.

---

### Ejemplo 5

* a) Es un paradigma donde el flujo del programa no se ejecuta de forma secuencial y predecible, sino que responde a estímulos externos o internos según van ocurriendo; la aplicación permanece "escuchando" o esperando que algo pase en lugar de forzar constantemente la ejecución de instrucciones.
* b) Espera de evento (estado idle), captura de evento, cola de eventos, despacho de eventos, ejecución de la respuesta y retorno a la espera.
* c) Un evento real ocurre de forma natural, como el clic de un mouse; un pseudoevento es generado artificialmente por la aplicación o el sistema, como un `Timer` que dispara un evento cada cierto intervalo sin que exista interacción física del usuario.
* d) Prioridad fija: ciertos eventos siempre tienen preferencia absoluta sin importar el orden de llegada (ej. una emergencia del sistema). Secuencial (FIFO): los eventos se procesan en el mismo orden en que fueron capturados. Por tipo de origen: la prioridad depende de si el evento proviene de hardware, del sistema o de la propia aplicación.

---

# Práctica de Laboratorio
## Características de Java, Generics, Lambdas/Streams, Regex y Programación Dirigida por Eventos

---

### Ejercicio 1: Clase genérica con encapsulamiento

Implemente una clase genérica `Caja<T>` que encapsule un valor de tipo `T`, con métodos `guardar(T valor)` y `obtener()`. Los atributos deben ser privados (encapsulamiento). Cree un `main` que use dos instancias de `Caja`: una con `Integer` y otra con `String`, mostrando el contenido de ambas.

---

### Ejercicio 2: Herencia, interfaces y polimorfismo

Cree una interfaz `Figura` con el método `calcularArea()`. Implemente dos clases, `Circulo` y `Rectangulo`, que implementen dicha interfaz (`implements`), sobreescribiendo el método (`@Override`). En el `main`, almacene ambas figuras en un arreglo de tipo `Figura` y recorra el arreglo imprimiendo el área de cada una, demostrando el polimorfismo.

---

### Ejercicio 3: Lambdas y Streams

Dada una lista de nombres de estudiantes (`List<String>`), utilice expresiones **Lambda** y la API de **Streams** para:
* Filtrar únicamente los nombres que tengan más de 5 caracteres.
* Convertir los nombres filtrados a mayúsculas.
* Imprimir el resultado final como una lista.

---

### Ejercicio 4: Validador de formulario con Regex

Implemente una clase `ValidadorFormulario` con métodos estáticos que validen, usando `Pattern` y `Matcher`:
* `esCorreoValido(String correo)` → valida formato de correo electrónico.
* `esTelefonoValido(String telefono)` → valida el formato `dddd-dddd` (ej. `8888-1234`).

En el `main`, pruebe ambos métodos con al menos dos valores válidos y dos inválidos, imprimiendo el resultado de cada validación.

---

### Ejercicio 5: Simulación de una cola de eventos (sin interfaz gráfica)

Implemente una simulación simple del ciclo de eventos visto en el curso, sin usar JavaFX:
* Cree una clase `Evento` con un atributo `nombre` (String) y `prioridad` (int, donde un número menor representa mayor prioridad).
* Use una `PriorityQueue<Evento>` para simular la cola de eventos, ordenando por prioridad.
* Cree un método `despacharEventos()` que vaya sacando los eventos de la cola (uno por uno, respetando la prioridad) e imprima un mensaje simulando su "ejecución" (por ejemplo: `"Procesando evento: [nombre]"`).
* En el `main`, agregue al menos 4 eventos con distintas prioridades y ejecute el despacho completo.

---

## RESPUESTAS

---

### Ejercicio 1

```java
public class Caja<T> {
    private T valor;

    public void guardar(T valor) {
        this.valor = valor;
    }

    public T obtener() {
        return valor;
    }

    public static void main(String[] args) {
        Caja<Integer> cajaNumero = new Caja<>();
        cajaNumero.guardar(42);

        Caja<String> cajaTexto = new Caja<>();
        cajaTexto.guardar("Hola Mundo");

        System.out.println("Contenido de cajaNumero: " + cajaNumero.obtener());
        System.out.println("Contenido de cajaTexto: " + cajaTexto.obtener());
    }
}
```

---

### Ejercicio 2

```java
interface Figura {
    double calcularArea();
}

class Circulo implements Figura {
    private double radio;

    public Circulo(double radio) {
        this.radio = radio;
    }

    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;
    }
}

class Rectangulo implements Figura {
    private double base;
    private double altura;

    public Rectangulo(double base, double altura) {
        this.base = base;
        this.altura = altura;
    }

    @Override
    public double calcularArea() {
        return base * altura;
    }
}

public class PruebaFiguras {
    public static void main(String[] args) {
        Figura[] figuras = {
            new Circulo(5),
            new Rectangulo(4, 6)
        };

        for (Figura figura : figuras) {
            System.out.println("Área: " + figura.calcularArea());
        }
    }
}
```

---

### Ejercicio 3

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FiltradoEstudiantes {
    public static void main(String[] args) {
        List<String> nombres = Arrays.asList("Ana", "Alejandro", "Luis", "Marcela", "Fer", "Valentina");

        List<String> resultado = nombres.stream()
                .filter(nombre -> nombre.length() > 5)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println("Nombres filtrados y en mayúsculas: " + resultado);
    }
}
```

---

### Ejercicio 4

```java
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ValidadorFormulario {

    public static boolean esCorreoValido(String correo) {
        String patron = "^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    public static boolean esTelefonoValido(String telefono) {
        String patron = "^\\d{4}-\\d{4}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(telefono);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println("usuario@correo.com -> " + esCorreoValido("usuario@correo.com"));
        System.out.println("usuario_correo.com -> " + esCorreoValido("usuario_correo.com"));

        System.out.println("8888-1234 -> " + esTelefonoValido("8888-1234"));
        System.out.println("88881234 -> " + esTelefonoValido("88881234"));
    }
}
```

---

### Ejercicio 5

```java
import java.util.PriorityQueue;
import java.util.Comparator;

class Evento {
    private String nombre;
    private int prioridad;

    public Evento(String nombre, int prioridad) {
        this.nombre = nombre;
        this.prioridad = prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }
}

public class SimuladorColaEventos {

    private PriorityQueue<Evento> colaEventos =
            new PriorityQueue<>(Comparator.comparingInt(Evento::getPrioridad));

    public void agregarEvento(Evento evento) {
        colaEventos.add(evento);
    }

    public void despacharEventos() {
        while (!colaEventos.isEmpty()) {
            Evento evento = colaEventos.poll(); // Despacho del evento con mayor prioridad
            procesarEvento(evento);
        }
    }

    private void procesarEvento(Evento evento) {
        System.out.println("Procesando evento: " + evento.getNombre()
                + " (prioridad " + evento.getPrioridad() + ")");
    }

    public static void main(String[] args) {
        SimuladorColaEventos simulador = new SimuladorColaEventos();

        simulador.agregarEvento(new Evento("Clic en botón Enviar", 3));
        simulador.agregarEvento(new Evento("Emergencia del sistema", 0));
        simulador.agregarEvento(new Evento("Carga de datos completada", 2));
        simulador.agregarEvento(new Evento("Movimiento de mouse", 4));

        simulador.despacharEventos();
    }
}
```
