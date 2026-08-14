# Aplicaciones dirigidas por Eventos

Escuchando o esperando que algo pase

Dirigidas por servicio, esperan a que un servicio se ejecute. Apps en segundo plano se llama Daemons.

Doble papel, app daemon orientada a servicios, app en segundo plano que espera y envia al servicio a ejecutarse.

## Ciclo básico

* Espera de evento, estado idle.
* Captura de evento, framework detecta.
* Cola de eventos, estructura FIFO y prioridad de eventos.
* Despacho de eventos
* Ejecución de la respuesta
* Retorno a la espera

## Eventos y pseudoeventos

Sucesos que ocurren y pueden ser detectados.

No provienen de una acción real, sino que son generados artificialmente.

## Eventos por hardware

Detectados por dispositivos físicos conectados. (Y flujos de red)

## Eventos del sistema

Generados por sistema operativo o el framework

## Eventos de aplicación

Generados internamente por la propia aplicación, no depende de una acción física real.

## Jerarquía

Determina el orden y prioridad

### Tipos

* Prioridad fija (Emergencias del sistema [Blue Screen])
* Secuencial (FIFO)
* Jerarquía por tipo de origen

# Conclusiones

Flexibles, reactivas y modulares.

