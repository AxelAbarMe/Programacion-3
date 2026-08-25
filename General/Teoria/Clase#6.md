# Patrón de arquitectura MVC

> Es una forma de programar, no es herramienta ni solución

MVC es frontend, tiene interacción con el usuario y es peligroso manejar bases de datos u otro tipo de información sensible en MVC porque puede generar hackeos.

## Introducción

Sistemas necesitan organización en el código. Permite al utilizar patrones evitar código espaguetti y desorden, creando sistemas más fáciles de mantener.

> Páginas web y otros sistemas al manejar el HTML debe de mantenerse solamente con códigos de etiquetas, nunca se debe de implementar código fuente dentro

Código spagutti es cuando el desorden llega a extensivas líneas de código, diferentes lenguajes en un mismo lugar y queda una vulnerabilidad enorme en el sistema.

Para conectar código de diversas fuentes se produce a través de referencias entre los diferentes lenguajes.

MVC: Patrón separa Datos (Modelo), Interfaz (Vista) y lógica de control(Controlador). Objetivo: Escalable, mantenible.

Usado en web y escritorio, favorece trabajo en equipo, frameworks: Spring MVC, Django, ASP.NET, Rails.

## Modelo

Administra los datos y las reglas, clase Cliente, tabla Usuarios. No se encarga de transferir los datos.

## Vista

Muestra información al usuario, independiente de la lógica y tiene elementos gráficos.

## Controlador

Actúa como intermediario entre la Vista y el Modelo, procesa entradas del usuario

Un modelo son los datos (Clase o definición de objeto como tal), mientras que el controlador sería la implementación de sus funcionalidades, listas y métodos especializados que manejan los datos serializados del modelo, que esto es ya lo que pasa a la vista en la parte gráfica.

Vista envía los primeros datos como un sistema login, donde se transforma a través del modelo con un DTO la estructura que se manejará de los datos dentro del sistema, se comprueba a través del controlador para validar dichos datos ingresados. Al llegar a la capa lógica se debe de preparar los datos basados en las reglas de negocio, lo que significa datos procesables en la base de datos y se envían a la capa de datos que debe de insertar dicha información o traer la información de login en el sistema desde la base de datos, la respuesta de los datos por parte de la base de datos se envía a los datos y está solo la envía al siguiente capa de lógica, está los transforma en datos lógicos quitando los sistemas de SQL o de la base de datos, los servicios solo ajustan a donde deben dirigirse y se envían al controlador, el controlador válida la información para entregarle a la Vista los datos visibles, ya sea el login exitoso o un fallo crítico en el sistema. La base de datos puede realizar validaciones por parte de su propia sistema para enviarse. La capa de servicios existe para dividir frontend con backend. Dicha capa puede conectar a diferentes vistas (Interfaces), servicios es la más importante porque protege backend y usar frontend eficientemente, permite conectar diferentes vistas en diferentes lugares, como Amazon que al tener los servicios en diferentes partes del mundo.



> Vista Controlador es frontend y al trabajar con sockets, se puede manejar dos apps diferentes de frontend y backend que se comuniquen entre proyectos para no tenerlo todo en uno solo
