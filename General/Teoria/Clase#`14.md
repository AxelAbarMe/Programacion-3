# Comunicación entre Componentes Remotos: Sockets en Java: Resumen Académico

---

## 1. ¿Qué es un socket?

* Un **socket** es un **punto final de comunicación** entre dos programas.
* Permite el intercambio de datos entre procesos que están en la **misma máquina o en máquinas distintas**.
* Es la base de la comunicación **cliente-servidor**.
* **Ejemplo:** un chat entre dos computadoras usa sockets para enviar y recibir mensajes.

### Componentes de un socket

| Componente | Función |
|---|---|
| **Dirección IP** | Identifica el dispositivo en la red |
| **Puerto** | Indica el servicio o aplicación dentro del dispositivo |
| **Protocolo** | Define el método de comunicación (TCP o UDP) |

* La combinación **(IP, Puerto, Protocolo)** identifica un socket único.

---

## 2. Tipos de sockets

| Tipo | Protocolo | Características | Ejemplos de uso |
|---|---|---|---|
| **Stream Socket** | **TCP** | Conexión **confiable**, orientada a conexión, con flujo continuo de datos | Correo electrónico, web |
| **Datagram Socket** | **UDP** | Comunicación **sin conexión**, rápida, pero sin garantía de entrega | Juegos en línea, videollamadas |

---

## 3. Flujo de comunicación TCP

| Servidor | Cliente |
|---|---|
| 1. Crea un `ServerSocket` | 1. Crea un `Socket` |
| 2. Espera conexiones (`accept`) | 2. Se conecta al servidor |
| 3. Intercambia datos | 3. Envía y recibe datos |
| 4. Cierra la conexión | 4. Cierra la conexión |

| Clase | Función |
|---|---|
| `ServerSocket` | Se ejecuta en el servidor; permanece **en espera** de conexiones en un puerto |
| `Socket` | Representa la conexión; lo usa el cliente para conectarse y el servidor para comunicarse con cada cliente |
| `accept()` | Método **bloqueante** del servidor: espera hasta que un cliente se conecte y devuelve un `Socket` |
| `BufferedReader` / `PrintWriter` | Lectura y escritura de texto sobre los flujos del socket |

### 3.1 Servidor simple

```java
import java.io.*;
import java.net.*;

public class Servidor {

    public static void main(String[] args) throws IOException {

        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor esperando conexión...");

            try (Socket cliente = servidor.accept();
                 BufferedReader entrada = new BufferedReader(
                         new InputStreamReader(cliente.getInputStream()));
                 PrintWriter salida = new PrintWriter(
                         cliente.getOutputStream(), true)) {

                String mensaje = entrada.readLine();
                System.out.println("Cliente dice: " + mensaje);

                salida.println("Mensaje recibido: " + mensaje);
            }
        }
    }
}
```

### 3.2 Cliente

```java
import java.io.*;
import java.net.*;

public class Cliente {

    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter salida = new PrintWriter(
                     socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {

            salida.println("Hola servidor");

            String respuesta = entrada.readLine();
            System.out.println("Servidor responde: " + respuesta);
        }
    }
}
```

> **Importante:** siempre se inicia primero el **servidor** y después el **cliente**.

---

## 4. Configuración típica

| Aspecto | Consideración |
|---|---|
| **Puertos** | Se recomienda usar el rango **1024–65535** (los inferiores están reservados para servicios del sistema) |
| **Firewall** | Debe permitir el tráfico en el puerto utilizado |
| **Orden de ejecución** | Servidor → luego cliente |
| **Protocolo** | TCP si se requiere confiabilidad; UDP si se prioriza la velocidad sin conexión |

---

## 5. Limitación de los servidores simples

* Un servidor simple **se bloquea mientras atiende a un cliente**: los demás clientes deben **esperar su turno**.
* **Solución:** usar **hilos (threads)** para atender a varios clientes a la vez.

---

## 6. Servidor multihilo

* Cada vez que `accept()` recibe una conexión, se crea un **hilo nuevo** que atiende a ese cliente, mientras el servidor vuelve a esperar nuevas conexiones.
* Permite **múltiples conexiones simultáneas**, mejora el rendimiento y la experiencia del usuario.

```java
import java.io.*;
import java.net.*;

public class ServidorMultihilo {

    public static void main(String[] args) throws IOException {

        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor activo...");

            while (true) {
                Socket cliente = servidor.accept();   // Espera un cliente

                Thread hilo = new Thread(() -> atenderCliente(cliente));
                hilo.start();                          // Un hilo por cliente
            }
        }
    }

    private static void atenderCliente(Socket cliente) {
        try (cliente;
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(cliente.getInputStream()));
             PrintWriter salida = new PrintWriter(
                     cliente.getOutputStream(), true)) {

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("Recibido: " + mensaje);
                salida.println("Eco: " + mensaje);
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado");
        }
    }
}
```

| Elemento | Función |
|---|---|
| `while (true)` | Mantiene al servidor **siempre en espera** de nuevos clientes |
| Hilo por cliente | Atiende la comunicación sin bloquear al servidor |
| `readLine()` retorna `null` | Indica que el cliente cerró la conexión |

---

## 7. Arquitectura del ejemplo: chat con servidor intermedio

* El diagrama de la clase representa la evolución de la aplicación de chat: en lugar de comunicarse solo con un servicio de IA (**Gemini**, conexión tachada en el esquema), la aplicación se conecta a un **servidor** que comunica a los usuarios con un **chat experto**.

| Componente del diagrama | Función |
|---|---|
| **Usuarios (izquierda)** | Utilizan la aplicación cliente (ventana de chat) |
| **Aplicación cliente** | Envía las consultas al servidor mediante sockets |
| **Servidor** | Punto central que **recibe y redirige** los mensajes entre clientes y expertos |
| **Chat experto** | Canal donde los usuarios expertos reciben las preguntas y devuelven las respuestas |
| **Usuarios (derecha)** | Expertos que responden desde sus propios equipos |
| **Gemini** | Servicio externo de IA que ya no es la única vía de respuesta |

* **Flujo:** el cliente envía la pregunta al servidor → el servidor la reenvía al chat experto → el experto responde → el servidor devuelve la respuesta al cliente.
* El **`ServerSocket`** permanece **siempre en espera**: recibe una pregunta, reacciona, la envía; recibe la respuesta, reacciona y la devuelve. Esto es lo que hace a la aplicación **reactiva**.
* Se conectan **N usuarios desde N equipos**, con uno o varios servidores en medio.

---

## 8. Ventajas y desventajas

| Ventajas | Desventajas |
|---|---|
| Comunicación **directa y eficiente** | Requiere **manejo manual de errores** |
| **Control total** sobre los datos y la conexión | Necesita **configuración de seguridad** |
| Permite crear **sistemas distribuidos** reales | **Complejidad** en la sincronización de hilos |
| | **No es escalable** sin arquitecturas adicionales |

### Concurrencia y sincronización

* Con varios hilos accediendo a recursos compartidos, la **concurrencia** puede generar conflictos y la sincronización resulta delicada.
* **Ejemplo:** la venta de entradas en línea, donde muchos usuarios compiten por los mismos recursos al mismo tiempo.

### Escalabilidad

* Los sockets por sí solos no escalan: requieren **arquitectura adicional**, como una **aplicación servidor**, y a mayor demanda, **granjas de servidores**, *data warehouses*, **enrutadores** y **firewalls**.
* **Ejemplo:** una plataforma como Amazon, que conecta muchos proveedores con muchos clientes, necesita múltiples servidores e infraestructura de gran tamaño.

---

## 9. Propiedades de las aplicaciones con sockets e hilos

| Propiedad | Significado |
|---|---|
| **Distribuidas** | Los componentes se ejecutan en equipos distintos y se comunican por red |
| **Interactivas** | La comunicación ocurre en **tiempo real**: se reciben y responden paquetes de inmediato |
| **Concurrentes** | Varios usuarios pueden estar conectados **al mismo tiempo** |
| **Escalables** | Se pueden agregar usuarios, servidores o servicios (por ejemplo, otras IA además de Gemini) mientras el presupuesto y la arquitectura lo permitan |
| **Reactivas** | Siempre están procesando información y reaccionan en el momento en que se utilizan |

---

## 10. Resumen general del tema

* Un **socket** es un punto final de comunicación definido por **IP, puerto y protocolo**.
* **TCP** (`Socket`/`ServerSocket`) es confiable y orientado a conexión; **UDP** es rápido pero sin garantía.
* El **servidor** crea un `ServerSocket`, espera con `accept()` e intercambia datos; el **cliente** crea un `Socket` y se conecta. Siempre se inicia primero el servidor.
* Se recomiendan puertos entre **1024 y 65535**, y el firewall debe permitir el tráfico.
* Un servidor simple **bloquea** a los demás clientes; el **servidor multihilo** atiende a cada cliente en un hilo propio.
* Los sockets ofrecen control total y permiten sistemas distribuidos, pero exigen manejar errores, seguridad y sincronización, y **no escalan sin arquitectura adicional**.
* Combinados con hilos, los sockets permiten crear aplicaciones en red **distribuidas, interactivas, concurrentes, escalables y reactivas**.