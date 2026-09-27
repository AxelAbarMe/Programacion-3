# Enunciado

Una empresa de comercio electrónico está desarrollando una nueva plataforma. El sistema debe cumplir con las siguientes necesidades:

* 1. Gestión de productos: la aplicación debe mostrar un catálogo de productos que puede crecer hasta miles de referencias. Se debe optimizar el uso de memoria y evitar duplicar información repetida (por ejemplo, descripciones y atributos que se repiten en muchos productos).

* 2. Carrito de compras: cada vez que un cliente agrega un producto al carrito, se debe crear un objeto que represente ese ítem. Dependiendo de la categoría del producto, la forma de instanciar puede variar (productos digitales, físicos, con envío internacional, etc.).

* 3. Notificaciones al cliente: cuando una orden cambia de estado (pendiente, pagada, enviada, entregada), se debe notificar al cliente. En el futuro se podrían agregar nuevas formas de notificación (correo electrónico, SMS, WhatsApp, notificaciones push).

* 4. Pasarela de pagos: la aplicación debe poder integrar diferentes proveedores de pago (PayPal, tarjeta de crédito, criptomonedas) sin modificar el código principal del sistema cuando se agregue un nuevo proveedor.

Analice el caso y explique qué patrones de diseño aplicaría en cada una de las necesidades descritas. Justifique su respuesta relacionando cada patrón con el problema que resuelve.

Respuesta:

---

# VIDEOS PATRONES

* [VIDEO PATRONES INTRO](https://www.youtube.com/watch?v=cwfuydUHZ7o)
* [VIDEO SINGLETON](https://www.youtube.com/watch?v=gocJeOHtj9w)
* [VIDEO FACTORY](https://www.youtube.com/watch?v=R6Ef64hDwGo)
* [VIDEO ABSTRACT-FACTORY](https://www.youtube.com/watch?v=QmE-o5R7ZF4)
* [VIDEO PROTOTYPE](https://www.youtube.com/watch?v=M3VT1v54cq4)
* [VIDEO FACADE](https://www.youtube.com/watch?v=6dYwdDbhpwQ)
* [VIDEO DECORATOR](https://www.youtube.com/watch?v=mOhrurNEgGQ)
* [VIDEO PROXY](https://www.youtube.com/watch?v=LUJbqdthTzA)
* [VIDEO COMMAND](https://www.youtube.com/watch?v=hDBOfyzFKEU)
* [VIDEO MEMENTO](https://www.youtube.com/watch?v=Q5CL1b-FD9E)
* [VIDEO OBSERVER](https://www.youtube.com/watch?v=QiKrKNTdGGs)
* [VIDEO STRATEGY](https://www.youtube.com/watch?v=GyT2IWgUILU)
* [VIDEO DAO](https://www.youtube.com/watch?v=VVbTSkzhtA8)
* [VIDEO INYECCION DE DEPENDENCIAS](https://www.youtube.com/watch?v=MdjiNNv9m8A)
* [VIDEO MVC](https://www.youtube.com/watch?v=igZChgl8-kc)
* [VIDEO MVC + DAO + ID + FACTORY EJEMPLO](https://www.youtube.com/watch?v=P_87EMpY9R0)
* [VIDEO ANTIPATRONES](https://www.youtube.com/watch?v=hKizhA69h2k)
