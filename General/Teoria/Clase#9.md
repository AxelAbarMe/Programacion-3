# Bases de Datos Relacionales: Normalización y Diseño

## 1. El Modelo Relacional de Codd

* **Definición:** modelo de datos que organiza la información en **relaciones** (tablas), compuestas por **tuplas** (filas) y **atributos** (columnas), donde cada atributo toma valores de un **dominio** definido.

### Comparación: Modelo de Objetos vs. Modelo Relacional

| Aspecto | Modelo de objetos | Modelo relacional |
|---|---|---|
| **Unidad principal** | Objeto / instancia | Tupla dentro de una relación |
| **Estructura** | Atributos + métodos | Atributos definidos por columnas |
| **Identidad** | Referencia / identidad del objeto | Clave primaria |
| **Relaciones** | Referencias entre objetos | Claves foráneas |
| **Herencia** | Natural en POO | No forma parte directa del modelo relacional |
| **Colecciones** | List, Set, Map… | Filas relacionadas en otras tablas |
| **Comportamiento** | Incluido en clases y métodos | Los datos no incorporan comportamiento |

---

## 2. Mal Ejemplo de Diseño: Base de Datos de Fútbol (sin normalizar)

<img width="800" height="500" alt="image" src="https://github.com/user-attachments/assets/acca6fb0-21ea-4765-82df-5945843b6863" />

> Este diagrama ilustra un diseño que **incumple los principios básicos del modelo relacional**.

### ¿Por qué es un mal ejemplo?

* **Tablas sobrecargadas de atributos heterogéneos:** la tabla `Equipos` mezcla datos que pertenecen a **entidades distintas** dentro de una sola tabla: identidad del equipo (`Nombre`, `Nombre Oficial`), **ubicación** (`Dirección`, `CP`, `Provincia`, `Pais`, `Localidad`), **contacto** (`Dirección Internet`, `Email`, `Telefono`, `Fax`) e **información histórica** (`Fecha de fundación`, `Historia`, `Himno`).
* **Viola el principio de una entidad = una tabla:** al concentrar ubicación, contacto e historia dentro de `Equipos`, se generan **grupos de datos que en realidad son entidades independientes** y deberían modelarse como tablas propias (como sí ocurre en el buen ejemplo con `ubicacion`, `pais`, `provincia` e `informacion_contacto`).
* **Redundancia e inconsistencia:** si varios equipos comparten la misma provincia o país, esos datos se repiten en cada fila de `Equipos` en lugar de referenciarse mediante una clave foránea, lo que **dificulta la actualización** (hay que cambiar el dato en múltiples filas) y **abre la puerta a inconsistencias**.
* **Relaciones difíciles de interpretar:** el diagrama muestra líneas cruzadas entre `Jugadores`, `Equipos`, `Paises`, `Pie`, `Demarcación`, `Provincias` y `Situación de nacionalidad` sin claridad sobre las **cardinalidades** (1:1, 1:N, N:M), lo cual dificulta identificar cuál es la llave primaria y cuál la foránea en cada relación.
* **Falta de atomicidad implícita:** al concentrar tantos atributos de distinta naturaleza en una sola tabla, es común que terminen apareciendo **campos con múltiples valores o datos compuestos** (por ejemplo, "Otras Secciones" o "Dirección" mezclando calle, número y detalles), lo cual **incumple la Primera Forma Normal (1FN)**.

> **Regla general que se rompe:** *"Los datos de las tablas no siempre se repiten. Las tablas deben relacionarse entre sí; si una tabla no se relaciona con ninguna otra, sobra."* En este mal ejemplo ocurre lo contrario: en vez de relacionar tablas pequeñas y específicas, se **concentra todo en una tabla grande**, lo que anula el propósito de tener relaciones normalizadas.

---

## 3. Buen Ejemplo de Diseño: Base de Datos Normalizada

<img width="800" alt="image" src="https://github.com/user-attachments/assets/18d05528-c191-44f2-9bbd-5f69ae2937eb" />

> Este segundo diseño toma la misma temática (equipos, ubicación, jugadores) pero la **separa correctamente en entidades independientes**, cada una con su propia clave primaria y relacionadas mediante claves foráneas.

### Tablas del modelo normalizado

| Tabla | Atributos principales | Relación |
|---|---|---|
| **equipo** | `pk_equipo`, `nombre`, `nombre_oficial`, `fecha_fundacion`, `reseña_historica` | 1:N con `jugador` |
| **ubicacion** | `pk_ubicacion`, `direccion_exacta`, `codigo_postal`, `fk_pais`, `fk_provincia` | N:1 con `pais` y `provincia` |
| **pais** | `pk_pais`, `fk_provincia` | 1:N con `ubicacion` |
| **provincia** | `pk_provincia` | 1:N con `pais` |
| **informacion_contacto** | `pk_informacion_contacto`, `email`, `telefono`, `url` | 1:1 con `estadio` |
| **estadio** | `pk_estadio`, `nombre`, `capacidad`, `anio_inauguracion`, `propietario`, `dimension`, `fk_ubicacion`, `fk_informacion_contacto` | N:1 con `ubicacion` e `informacion_contacto` |
| **jugador** | `pk_jugador`, `apodo`, `nombre`, `primer_apellido`, `segundo_apellido`, `fecha_nacimiento`, `fk_ubicacion` | N:1 con `ubicacion` |

### ¿Por qué es un buen ejemplo?

* **Cada entidad tiene su propia tabla:** la ubicación, el país, la provincia, la información de contacto y el estadio están **separados**, en vez de mezclarse dentro de `equipo` como ocurría en el mal ejemplo.
* **Uso correcto de claves:** cada tabla posee una **clave primaria** (`pk_...`) claramente identificada, y las relaciones se implementan mediante **claves foráneas** (`fk_ubicacion`, `fk_informacion_contacto`, `fk_pais`, `fk_provincia`).
* **Evita la redundancia:** si dos estadios están en la misma ubicación o país, esa información se referencia una sola vez mediante la FK, en lugar de repetirse.
* **Facilita el mantenimiento:** actualizar un dato (por ejemplo, el teléfono de contacto) requiere modificar **una sola fila** en `informacion_contacto`, y ese cambio se refleja automáticamente en todas las tablas que la referencian.

---

## 4. Primera Forma Normal (1FN)

> Un atributo cumple la 1FN cuando **contiene valores atómicos** (indivisibles) y **no existen grupos repetitivos**.

### Ejemplo: NO 1FN

| id | usuario | teléfonos |
|---|---|---|
| 1 | Ana | 8888-1111, 8777-2222 |

* El atributo `teléfonos` **contiene varios valores** en un mismo campo, lo cual **incumple la atomicidad** exigida por la 1FN.

### Ejemplo: 1FN (corregido)

| id_usuario | usuario |
|---|---|
| 1 | Ana |

| id_usuario | teléfono |
|---|---|
| 1 | 8888-1111 |
| 1 | 8777-2222 |

* Se separan los teléfonos en **filas independientes**, relacionadas mediante `id_usuario`, garantizando que cada campo contenga un único valor atómico.

---

## 5. Beneficios y Costo Práctico de Normalizar

> Normalizar mejora la calidad del modelo, pero **no significa dividir cada dato en una tabla distinta**.

| Beneficios | Costo práctico |
|---|---|
| Reduce duplicación | Más tablas |
| Facilita actualizaciones | Más JOIN |
| Protege integridad | Consultas más complejas |
| Aclara dependencias | Puede requerir optimización |

* La normalización es un **equilibrio**: divide los datos lo suficiente para evitar redundancia e inconsistencias (como en el buen ejemplo de fútbol), pero sin fragmentar excesivamente al punto de complicar las consultas con demasiados `JOIN`.

---

## 6. El "Desajuste" Objeto–Relacional

> Java y el modelo relacional representan la información de manera diferente. Integrarlos exige **transformar estructuras** mediante mapeo.

| Java / Objetos | MySQL / Relación |
|---|---|
| `class UsuarioDTO { int idUsuario; String usuario; String nombre; boolean activo; }` | Tabla `usuarios` con columnas `id_usuario`, `usuario`, `nombre`, `activo` |
| Objeto con identidad, estado y posible comportamiento | Fila identificada por PK; relaciones mediante FK |

* El **mapeo** convierte cada instancia de una clase Java en una fila de una tabla relacional, y viceversa.
* Este desajuste es la razón por la cual se necesitan herramientas como **JDBC** para traducir entre ambos mundos.

---

## 7. De la Teoría al Proyecto Java

> Arquitectura por capas que conecta la interfaz de usuario con la base de datos.

| Capa | Responsabilidad |
|---|---|
| **Vista / JavaFX** | Captura datos del usuario |
| **Controller** | Procesa eventos de la interfaz |
| **Servicios** | Coordina solicitudes entre capas |
| **Lógica** | Aplica reglas de negocio |
| **Datos / JDBC** | Ejecuta SQL y transforma el `ResultSet` |
| **MySQL** | Persiste usuarios, conversaciones y mensajes |

* El flujo va de arriba hacia abajo: la vista captura la entrada, el controlador la procesa, los servicios coordinan la solicitud, la lógica aplica las reglas de negocio, la capa de datos ejecuta el SQL vía JDBC, y finalmente MySQL almacena la información de forma persistente.

---

## 8. Resumen general del tema

* El **modelo relacional de Codd** organiza los datos en tablas (relaciones), filas (tuplas) y columnas (atributos), a diferencia del modelo de objetos que se basa en instancias, referencias y herencia.
* Un **mal diseño** (como la base de datos de fútbol sin normalizar) mezcla entidades distintas en una sola tabla, genera redundancia, dificulta el mantenimiento e incumple la atomicidad de los datos.
* Un **buen diseño** separa cada entidad en su propia tabla (equipo, ubicación, país, provincia, contacto, estadio, jugador), relacionándolas mediante **claves primarias y foráneas** correctamente definidas.
* La **Primera Forma Normal (1FN)** exige que los atributos contengan valores atómicos y que no existan grupos repetitivos dentro de una misma celda.
* **Normalizar** trae beneficios claros (menos duplicación, mejor integridad) pero también un costo práctico (más tablas y JOINs), por lo que debe aplicarse con equilibrio.
* El **"desajuste objeto-relacional"** surge porque Java y el modelo relacional representan la información de forma distinta, y se resuelve mediante **mapeo** (por ejemplo, con JDBC).
* En un proyecto Java real, la teoría se traduce en una **arquitectura por capas**: Vista → Controller → Servicios → Lógica → Datos/JDBC → Base de datos MySQL.
