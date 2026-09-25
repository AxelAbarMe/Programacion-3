# Enunciado

Una empresa de comercio electrónico está desarrollando una nueva plataforma. El sistema debe cumplir con las siguientes necesidades:

* 1. Gestión de productos: la aplicación debe mostrar un catálogo de productos que puede crecer hasta miles de referencias. Se debe optimizar el uso de memoria y evitar duplicar información repetida (por ejemplo, descripciones y atributos que se repiten en muchos productos).

* 2. Carrito de compras: cada vez que un cliente agrega un producto al carrito, se debe crear un objeto que represente ese ítem. Dependiendo de la categoría del producto, la forma de instanciar puede variar (productos digitales, físicos, con envío internacional, etc.).

* 3. Notificaciones al cliente: cuando una orden cambia de estado (pendiente, pagada, enviada, entregada), se debe notificar al cliente. En el futuro se podrían agregar nuevas formas de notificación (correo electrónico, SMS, WhatsApp, notificaciones push).

* 4. Pasarela de pagos: la aplicación debe poder integrar diferentes proveedores de pago (PayPal, tarjeta de crédito, criptomonedas) sin modificar el código principal del sistema cuando se agregue un nuevo proveedor.

Analice el caso y explique qué patrones de diseño aplicaría en cada una de las necesidades descritas. Justifique su respuesta relacionando cada patrón con el problema que resuelve.
