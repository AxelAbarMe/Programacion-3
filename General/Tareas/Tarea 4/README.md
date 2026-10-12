# Tarea 4

Como parte del diseño de la base de datos del Proyecto #2: Sistema de Reserva de Recursos, cada estudiante deberá elaborar el Modelo Entidad–Relación (MER) que represente de forma completa la información necesaria para soportar las funcionalidades del sistema. El proyecto establece que el Backend será el único responsable de almacenar y recuperar la información utilizando una base de datos MySQL, por lo que el modelo deberá servir posteriormente como base para la creación de las tablas y la implementación de la capa de acceso a datos.

El modelo deberá contemplar, como mínimo, la información relacionada con las funcionalidades persistentes del proyecto, entre ellas usuarios/funcionarios, reservas, categorías de recursos, recursos, calendarización y actividades, considerando además los datos necesarios para realizar posteriormente las estadísticas solicitadas en el sistema. Estas funcionalidades forman parte explícita de los requerimientos evaluados del proyecto. 

## El diagrama deberá:

* Identificar claramente todas las entidades/tablas necesarias para representar el sistema.
* Incluir los atributos principales de cada entidad.
* Identificar claramente las llaves primarias (PK).
* Identificar las llaves foráneas (FK) que permiten establecer las relaciones.
* Representar gráficamente las relaciones entre las entidades.
* Especificar la cardinalidad de cada relación, por ejemplo: 1:1, 1:N o N:M.
* Cuando exista una relación muchos a muchos (N:M), deberá resolverse mediante la correspondiente entidad o tabla intermedia.
* Indicar, cuando corresponda, si la participación en una relación es obligatoria u opcional.
* Evitar redundancias innecesarias y procurar que el diseño permita posteriormente aplicar adecuadamente los principios de normalización estudiados en clase.
* Mantener coherencia con las funcionalidades y reglas definidas para el Sistema de Reserva de Recursos.

**Entregable:** un único diagrama Entidad–Relación legible, elaborado con una herramienta de modelado como MySQL Workbench, draw.io, Lucidchart, Visual Paradigm o equivalente. El modelo presentado deberá corresponder con la estructura que posteriormente será implementada en MySQL y entregada como parte del script de base de datos del proyecto, tal como se solicita en las reglas de entrega.
