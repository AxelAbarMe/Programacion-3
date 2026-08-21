# MVC

## Base de datos

Parte mas profunda es definir base de datos

> Función: Almacenamiento de los datos únicamente.
>
> Trigger: Apartir de una acción en una base de datos, provoca otro cambio en la base de datos

Ya no se hace dentro del flujo de datos, ahora solo se hace en el flujo de software.

Procedimientos almacenados solo para almacenamiento, hasta códigos pequeños fuera del flujo de datos.

## Backend
---
## Capa de datos

> Se encarga de comunicar código fuente con base de datos, es un intérprete que permite traerse los datos de la base, Función: Intercambiar datos con la base de datos únicamente Insertar, eliminar, buscar. Que exista, que exista conexión.

Datos no maneja nada, solo se comunica.

## Lógica

Cerebro de la app, calcula, válida datos. Función es preparar información y relacionarla con las reglas del negocio. Lógica para validar si fuese un AVI médico, que el usuario no sea menor de edad.

## Servicios

Capa opcional. Permite comunicar app con cualquier interfaz, podria ser Pearl, React, Movil de IOS. Una App debe de comunicarse con cualquier tipo de interfaz, al implementar servicios esto es posible de poder cambiar de interfaz hecha en SceneBuilder a un PhP o un HTML. Servicios dice a donde ir, sin importar quién pregunto. Capa más rápida. No se valida en un servicio.

## DTO ( Model - Entity {Data Transfer Object} )

Sirve solo para almacenar datos de forma lógica, set de clases cuyos elementos solo tienen propiedades, entidades no implementan métodos, solo set, get o constructor. Lista es definida por Datos, si se devuelve cédulas iniciadas en 4, se devuelve una lista de DTO Persona.

Todo lo de la base de datos debe de estar en el DTO, pero no todo lo del DTO debe de estar en la base. Mapear base de datos con DTO.

Dataset de la base de datos tal cuál como van los datos, el que mueve los dataset son los DTO, se mapea el dataset que viene de la base de datos hacia DTO por razones de seguridad, para eliminar los estándares, la capa Lógica es la encargada de dicho mapeo.

Todos los DTO desapareen al cerrar la app para que el cierre se haga en Datos, las capas de lógica, servicios y DTO no deben de conocer que exista una base de datos.




