# Aplicaciones dirigidas por Eventos

## Concepto General

Este tipo de aplicaciones funciona bajo el paradigma de **Event-Driven Programming**, donde el flujo del programa no se ejecuta de forma secuencial y predecible (como un script que corre de arriba hacia abajo), sino que responde a estímulos externos o internos según van ocurriendo. La aplicación permanece "escuchando" o esperando que algo pase, en lugar de forzar constantemente la ejecución de instrucciones.

Este modelo es la base de las interfaces gráficas (como JavaFX), los servidores web, los sistemas operativos y muchas aplicaciones modernas de tiempo real.

## Aplicaciones dirigidas por servicio (Daemons)

Existen aplicaciones que están **dirigidas por servicio**, es decir, esperan a que un servicio determinado se ejecute o esté disponible para reaccionar. A este tipo de aplicaciones que corren en segundo plano se les conoce como **Daemons** (demonios).

* Un daemon normalmente **no tiene interfaz gráfica** y se ejecuta de forma continua en el sistema operativo.
* Cumplen un **doble papel**: por un lado son aplicaciones orientadas a servicios (esperan que algo suceda), y por otro son aplicaciones en segundo plano que, al detectar el evento correspondiente, envían la señal para que dicho servicio se ejecute.
* **Ejemplos comunes de daemons:** un servicio de impresión que espera trabajos en cola, un antivirus que monitorea el sistema de archivos en tiempo real, un servidor web (como Apache o Nginx) que espera peticiones HTTP, o un servicio de sincronización de archivos en la nube.

---

## Ciclo básico de una aplicación dirigida por eventos

El funcionamiento interno de este tipo de aplicaciones sigue un ciclo repetitivo conocido como **Event Loop** (bucle de eventos):

1. **Espera de evento (estado idle):** La aplicación no consume recursos activamente procesando instrucciones; se mantiene en un estado de reposo o espera, similar a un ciclo `while(true)` que revisa constantemente si hay algo nuevo que atender.

2. **Captura de evento:** El framework o el sistema operativo detecta que ha ocurrido algo relevante (por ejemplo, un clic del mouse, una tecla presionada, la llegada de un paquete de red, o el vencimiento de un temporizador).

3. **Cola de eventos:** Los eventos detectados no se procesan de inmediato uno por uno de forma arbitraria; se organizan en una estructura de datos tipo **cola (FIFO)**, respetando además niveles de **prioridad**, para determinar cuál evento debe atenderse primero cuando ocurren varios simultáneamente.

4. **Despacho de eventos (Event Dispatching):** El sistema toma el evento correspondiente de la cola y lo envía al componente o método encargado de manejarlo (el "handler" o "listener").

5. **Ejecución de la respuesta:** Se ejecuta el bloque de código asociado a ese evento específico (por ejemplo, el método `onAction` de un botón en JavaFX).

6. **Retorno a la espera:** Una vez ejecutada la respuesta, la aplicación regresa al estado de espera (idle), listo para capturar el siguiente evento.

> **Ejemplo aplicado a JavaFX:** cuando un usuario presiona un `Button`, el evento `ActionEvent` se genera, se coloca en la cola de eventos de la aplicación, se despacha al método vinculado mediante `setOnAction()` o `onAction="#metodo"` en el FXML, se ejecuta dicho método, y la interfaz vuelve a esperar la siguiente interacción del usuario.

---

## Eventos y pseudoeventos

* **Eventos:** Son sucesos reales que ocurren en el sistema y que pueden ser detectados de forma natural, como el clic de un mouse, la pulsación de una tecla o la llegada de datos por red.

* **Pseudoeventos:** No provienen de una acción real del usuario o del hardware, sino que son **generados artificialmente** por la propia aplicación o el sistema, con el fin de simular o forzar un comportamiento como si fuera un evento genuino.
  * **Ejemplo:** un `Timer` que dispara un evento cada cierto intervalo de tiempo sin que nadie haya interactuado físicamente con el sistema; o el método `fireEvent()` en JavaFX, que permite disparar manualmente un evento sobre un nodo, simulando que el usuario hizo clic sin haberlo hecho realmente (útil para pruebas automatizadas de interfaz).

---

## Clasificación de eventos según su origen

### Eventos por hardware

Son detectados directamente por dispositivos físicos conectados al sistema, así como por flujos de datos provenientes de la red.

* **Ejemplos:** movimiento del mouse, pulsación de teclas, señal de un sensor, llegada de un paquete de red (socket), conexión/desconexión de un dispositivo USB.

### Eventos del sistema

Son generados por el sistema operativo o por el framework sobre el cual corre la aplicación, sin que exista una acción directa del usuario en ese instante.

* **Ejemplos:** el sistema operativo notificando que se quedó sin memoria disponible, el framework indicando que una ventana terminó de cargarse (`onShown` en JavaFX), una notificación de batería baja, o el cierre inesperado de una aplicación.

### Eventos de aplicación

Son generados internamente por la propia lógica de la aplicación, sin depender de una acción física real del usuario ni del sistema operativo.

* **Ejemplos:** un evento personalizado que se dispara cuando finaliza la validación de un formulario, cuando se completa la carga de datos desde una base de datos, o cuando cambia el estado interno de un objeto (`PropertyChangeEvent` en Java, o `Bindings`/`ChangeListener` en JavaFX).

---

## Jerarquía de eventos

La jerarquía determina el **orden y la prioridad** con la que los eventos son atendidos cuando existen varios pendientes de procesar en la cola.

### Tipos de jerarquía

* **Prioridad fija:** Ciertos eventos tienen preferencia absoluta sobre cualquier otro, sin importar el orden de llegada. Se reservan generalmente para situaciones críticas del sistema.
  * **Ejemplo:** una emergencia del sistema operativo, como una pantalla azul (Blue Screen) o un evento de corte de energía inminente, que debe procesarse de inmediato, interrumpiendo cualquier otro evento en curso.

* **Secuencial (FIFO):** Los eventos se procesan en el mismo orden en que fueron generados o capturados, sin distinción de importancia; el primero en llegar es el primero en ser atendido.

* **Jerarquía por tipo de origen:** La prioridad se determina según de dónde proviene el evento (hardware, sistema o aplicación), otorgando mayor peso a ciertos orígenes sobre otros según las reglas definidas por el framework o sistema operativo.

---

# Conclusiones

Las aplicaciones dirigidas por eventos se caracterizan por ser:

* **Flexibles:** Se adaptan fácilmente a distintos tipos de interacción y fuentes de entrada sin necesidad de reestructurar todo el flujo del programa.
* **Reactivas:** Responden en tiempo real a los estímulos que van ocurriendo, en lugar de seguir un flujo rígido y predefinido de instrucciones.
* **Modulares:** Cada evento puede manejarse de forma independiente mediante su propio handler o listener, favoreciendo un código más organizado, mantenible y desacoplado (tal como se ve en el patrón MVC, donde el Controller se encarga de manejar los eventos generados por la Vista).
