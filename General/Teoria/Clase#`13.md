# Clases Activas, Procesos e Hilos en Java: Resumen Académico

---

## 1. Clases activas

* Son clases que mantienen su **propia ejecución independiente**.
* Contienen uno o más **hilos (threads)** que se ejecutan en paralelo.
* Se usan cuando un objeto debe realizar tareas de forma **continua o concurrente**.
* **Ejemplo:** un servidor que escucha constantemente las peticiones de los clientes.

---

## 2. Procesos e hilos

| Concepto | Definición |
|---|---|
| **Proceso** | Programa en ejecución con su **propio espacio de memoria** y sus propios recursos (variables, archivos abiertos, etc.) |
| **Hilo (thread)** | Subtarea dentro de un proceso; **comparte la memoria** del proceso principal |

* Los hilos permiten ejecutar varias tareas al mismo tiempo, por ejemplo:
  * Descargar un archivo mientras se muestra una barra de progreso.
  * Reproducir música mientras el usuario navega por la aplicación.
  * Consultar un servicio externo (como una IA) sin congelar la interfaz gráfica.

---

## 3. Creación de hilos en Java

Existen dos formas principales:

| Forma | Descripción | Ventajas y limitaciones |
|---|---|---|
| **Extender `Thread`** | La clase hereda de una clase base que ya representa un hilo | Directa y sencilla, pero **limita la herencia** (Java solo permite extender una clase) |
| **Implementar `Runnable`** | La clase implementa una interfaz que define lo que hará el hilo | **Más flexible y recomendada**: permite heredar de otra clase, facilita la reutilización y la programación orientada a interfaces |

### 3.1 Extendiendo `Thread`

```java
public class MiHilo extends Thread {

    @Override
    public void run() {
        System.out.println("Ejecutando en: " + getName());
    }
}

// Uso
MiHilo hilo = new MiHilo();
hilo.start();
```

### 3.2 Implementando `Runnable`

```java
public class MiTarea implements Runnable {

    @Override
    public void run() {
        System.out.println("Ejecutando en: " + Thread.currentThread().getName());
    }
}

// Uso
Thread hilo = new Thread(new MiTarea());
hilo.start();

// Con expresión lambda
Thread hilo2 = new Thread(() -> System.out.println("Tarea en segundo plano"));
hilo2.start();
```

> **Importante:** el código se ejecuta en un hilo nuevo solo al invocar **`start()`**. Si se llama directamente a `run()`, se ejecuta en el hilo actual, sin concurrencia.

---

## 4. Ciclo de vida de un hilo

| Estado | Descripción |
|---|---|
| **Nuevo** | Se crea el objeto del hilo |
| **Ejecutable** | Se llamó al método `start()` |
| **En ejecución** | El método `run()` se está ejecutando |
| **Bloqueado o en espera** | Espera recursos o eventos |
| **Terminado** | `run()` finaliza |

### Métodos comunes

| Método | Función |
|---|---|
| `start()` | Inicia el hilo y ejecuta `run()` en paralelo |
| `sleep(ms)` | Pausa el hilo durante un tiempo determinado |
| `join()` | Hace que un hilo espere a que otro termine |
| `interrupt()` | Solicita la interrupción del hilo |
| `isAlive()` | Indica si el hilo sigue en ejecución |

```java
Thread hilo = new Thread(() -> {
    try {
        Thread.sleep(2000);
        System.out.println("Tarea terminada");
    } catch (InterruptedException e) {
        System.out.println("Hilo interrumpido");
    }
});

hilo.start();
hilo.join();                       // El hilo principal espera
System.out.println(hilo.isAlive()); // false
```

### Hilos demonio (daemon)

* Un **hilo demonio** es un hilo de servicio en segundo plano; la JVM **no espera** a que termine para cerrar la aplicación.
* Se configura **antes** de llamar a `start()`.

```java
Thread hilo = new Thread(tarea);
hilo.setDaemon(true);
hilo.start();
```

---

## 5. Sincronización de hilos

* Cuando varios hilos acceden al **mismo recurso** (una variable, un archivo), pueden ocurrir **condiciones de carrera**: el resultado depende del orden impredecible en que se ejecutan los hilos.
* La **sincronización** garantiza que **solo un hilo a la vez** ejecute la **sección crítica** (la porción de código que modifica el recurso compartido).

```java
public class Contador {

    private int valor = 0;

    public synchronized void incrementar() {
        valor++;
    }

    public synchronized int getValor() {
        return valor;
    }
}
```

| Concepto | Significado |
|---|---|
| **Condición de carrera** | Error causado por el acceso simultáneo no controlado a datos compartidos |
| **Sección crítica** | Código que solo debe ejecutar un hilo a la vez |
| **`synchronized`** | Palabra clave que bloquea el acceso al método o bloque mientras un hilo lo usa |

---

## 6. Comunicación entre hilos

* Los hilos pueden compartir datos mediante **variables comunes**.
* También se coordinan con los métodos de `Object`:

| Método | Función |
|---|---|
| `wait()` | El hilo se suspende hasta que otro lo notifique |
| `notify()` | Despierta a **un** hilo en espera |
| `notifyAll()` | Despierta a **todos** los hilos en espera |

### Ejemplo clásico: productor-consumidor

* Un hilo **produce** datos (mensajes) y los coloca en una cola; otro hilo los **consume**.

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

BlockingQueue<String> cola = new LinkedBlockingQueue<>();

Thread productor = new Thread(() -> {
    try {
        cola.put("Mensaje 1");
        cola.put("Mensaje 2");
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});

Thread consumidor = new Thread(() -> {
    try {
        while (true) {
            String mensaje = cola.take(); // Espera si la cola está vacía
            System.out.println("Procesando: " + mensaje);
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});

productor.start();
consumidor.start();
```

---

## 6.1 Comunicación entre componentes locales

* Permite que distintas partes de una aplicación se comuniquen entre sí. En Java puede hacerse mediante:

| Mecanismo | Descripción |
|---|---|
| **Eventos** | `ActionEvent`, `MouseEvent`, etc. |
| **Colas de mensajes** | `BlockingQueue`, `LinkedBlockingQueue` |
| **Sockets locales** | `ServerSocket` y `Socket` sobre `localhost` |

---

## 7. Hilos en aplicaciones con interfaz gráfica (JavaFX)

* La interfaz gráfica corre en un hilo especial: el **hilo de la interfaz (JavaFX Application Thread)**.
* Las tareas lentas (consultar una IA, una base de datos o un servicio web) **no deben ejecutarse en ese hilo**, porque la ventana se congelaría.
* La solución es ejecutar la tarea en un **hilo de fondo** y, al terminar, devolver el resultado a la interfaz con **`Platform.runLater()`**.

| Elemento | Función |
|---|---|
| **Hilo de fondo** | Ejecuta la tarea lenta sin bloquear la interfaz |
| **`Platform.runLater(...)`** | Encola código para que se ejecute en el hilo de la interfaz (único autorizado para modificar controles visuales) |

```java
Thread hilo = new Thread(() -> {
    try {
        String respuesta = servicioIA.consultar(pregunta); // Tarea lenta

        Platform.runLater(() -> lblRespuesta.setText(respuesta));

    } catch (Exception e) {
        Platform.runLater(() -> lblRespuesta.setText("Ocurrió un error"));
        // Registrar el error en la bitácora de la base de datos
        bitacoraServicio.registrarError(e.getMessage());
    }
});

hilo.setDaemon(true);
hilo.start();
```

### Manejo de errores con hilos

* Si la consulta falla, el sistema debe ser **transparente con el usuario**: informarle del error y además **guardarlo en la bitácora de la base de datos**.
* Si ambas operaciones (informar y registrar) se ejecutan en **hilos independientes**, se realizan **en paralelo**.
* **Ejemplo de tiempos:** si la respuesta con error tarda 5 segundos y el registro en la base de datos otros 5, ejecutados secuencialmente serían 10 segundos de espera. Al usar hilos, el usuario solo espera **5 segundos**, porque el registro ocurre en segundo plano.
* Así, aun cuando se cae en el `catch`, todo el proceso sigue **controlado mediante hilos**, y la aplicación funciona de forma pareja.

---

## 8. Consideraciones importantes

* Los hilos comparten la misma memoria, lo que puede producir **conflictos de acceso** cuando varios modifican los mismos datos a la vez.
* Se deben usar **mecanismos de sincronización** para evitar errores o comportamientos inesperados.
* El uso excesivo de hilos puede **saturar el procesador** o degradar el rendimiento.
* Es esencial **cerrar correctamente** los hilos al terminar su tarea, para liberar recursos.
* Es un problema frecuente que un hilo quede **bloqueado** (por ejemplo, esperando indefinidamente una respuesta externa); debe preverse una forma de salir de esa situación.

---

## 9. Ventajas y desventajas

| Ventajas | Desventajas y riesgos |
|---|---|
| Mejor aprovechamiento de la CPU | Complejidad en la depuración |
| Programas más responsivos | Problemas de sincronización y bloqueos |
| Posibilidad de paralelizar tareas intensivas | Consumo de recursos si se crean muchos hilos |
| Ideal para servidores concurrentes y aplicaciones interactivas | Dificultad para mantener el código |

---

## 10. Resumen general del tema

* Un **proceso** es un programa en ejecución con memoria propia; un **hilo** es una subtarea que **comparte la memoria** del proceso.
* En Java se crean hilos **extendiendo `Thread`** o, preferiblemente, **implementando `Runnable`**; se inician siempre con `start()`.
* Un hilo pasa por los estados **nuevo, ejecutable, en ejecución, bloqueado/en espera y terminado**.
* La **sincronización** (`synchronized`) evita condiciones de carrera en secciones críticas.
* Los hilos se coordinan con `wait()`, `notify()`, `notifyAll()` o con colas como `BlockingQueue` (patrón **productor-consumidor**).
* En JavaFX, las tareas lentas se ejecutan en **hilos de fondo** (incluso **daemon**) y los resultados se muestran con **`Platform.runLater()`**.
* Los hilos permiten registrar errores en segundo plano sin aumentar el tiempo de espera del usuario.
* Un buen uso de hilos produce **interfaces reactivas, servidores concurrentes y aplicaciones distribuidas**, pero exige cuidar la sincronización y el consumo de recursos.

