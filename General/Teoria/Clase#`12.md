# Procedimientos Almacenados, Funciones y Triggers en MySQL: Resumen Académico

---

## 1. Unión de tablas (`JOIN` implícito)

* Permite consultar datos de **varias tablas relacionadas** mediante **llaves primarias y foráneas**.
* Después de cada tabla en el `FROM` se escribe una **letra o alias** (`u`, `c`, `m`) que funciona como nombre corto para identificarla.
* La condición de unión se compara con **un solo `=`**: la llave foránea de una tabla debe coincidir con la llave primaria de la otra.
* Se agrega `AND` para **filtrar** (por ejemplo, solo las conversaciones de un usuario específico).

```sql
USE avi_db;

SELECT u.id_usuario, u.nombre, c.titulo, m.emisor, m.contenido
FROM usuario u, conversacion c, mensaje m
WHERE u.id_usuario = c.id_usuario
  AND u.id_usuario = 2
  AND c.id_conversacion = m.id_conversacion;
```

| Elemento | Función |
|---|---|
| **Alias** (`u`, `c`, `m`) | Nombre corto para referirse a una tabla |
| `u.id_usuario = c.id_usuario` | Une cada conversación con su usuario |
| `c.id_conversacion = m.id_conversacion` | Une cada mensaje con su conversación |
| `u.id_usuario = 2` | Filtra solo los datos de un usuario |

* Esta consulta devuelve, en una misma tabla, el usuario, sus conversaciones y los mensajes de cada una.

### El problema

* Cuando hay **muchas tablas y datos específicos**, las consultas se vuelven largas y complejas, y se tendrían que repetir cada vez que se necesiten.

---

## 2. Procedimientos almacenados

* Un **procedimiento almacenado** es un bloque de código SQL guardado en la base de datos, que funciona como una **función o método en Java**: permite **evitar repetir código**.
* `PROCEDURE` es la versión SQL de método/función (palabra reservada).
* Por convención, el nombre inicia con **`SP_`** (*stored procedure*).
* Puede tener o no parámetros.

| Elemento | Función |
|---|---|
| `IN` | Parámetro de **entrada** (dato que se envía al procedimiento) |
| `OUT` | Parámetro de **salida** (dato que el procedimiento devuelve) |
| `p_nombre` | Convención para nombrar parámetros; después se indica el **tipo de dato**, igual al de la columna en la base de datos |
| `BEGIN` ... `END` | Equivalen a las llaves `{ }` de apertura y cierre en Java |
| `DELIMITER //` | Cambia temporalmente el delimitador de sentencias |
| `CALL` | Invoca el procedimiento |

### 2.1 `DELIMITER`

* El punto y coma (`;`) normalmente termina una sentencia. Como dentro del procedimiento hay varias sentencias con `;`, se cambia el delimitador a `//` para que el cuerpo completo se envíe como una sola unidad.
* Al finalizar se restablece con `DELIMITER ;` (**debe llevar un espacio** entre `DELIMITER` y el punto y coma).

### 2.2 Ejemplo: registrar un usuario

```sql
DELIMITER //

CREATE PROCEDURE SP_REGISTRAR_USUARIO
(
    IN p_usuario VARCHAR(50),
    IN p_contrasena VARCHAR(255),
    IN p_nombre VARCHAR(100)
)
BEGIN
    INSERT INTO usuarios(usuario, contrasena, nombre)
    VALUES (p_usuario, p_contrasena, p_nombre);
END //

DELIMITER ;

CALL SP_REGISTRAR_USUARIO('alonso', 'abc1234', 'Alonso Gomez');
```

### 2.3 Ejemplo: validar credenciales (con parámetro `OUT`)

* En este caso el procedimiento **sí debe devolver un resultado**, por lo que se usa un parámetro `OUT`.
* `SELECT COUNT(*)` cuenta las filas que coinciden; es más sencillo devolver un número (**0 o 1**) que otro tipo de dato como `VARCHAR`.
* La lógica: buscar usuarios con el **mismo usuario y contraseña** y que estén **activos**. Solo debería existir uno, por lo que el resultado es 0 o 1.
* `COUNT(*) > 0` se convierte en un valor booleano, que se guarda en `p_valido` mediante `INTO`.

```sql
DELIMITER //

CREATE PROCEDURE SP_VALIDAR_LOGIN(
    IN p_usuario VARCHAR(50),
    IN p_contrasena VARCHAR(255),
    OUT p_valido BOOLEAN
)
BEGIN
    -- Almacenar el resultado del SELECT en p_valido
    SELECT COUNT(*) > 0
    INTO p_valido
    FROM usuarios
    WHERE usuario = p_usuario
      AND contrasena = p_contrasena
      AND activo = TRUE;
END //

DELIMITER ;

CALL SP_VALIDAR_LOGIN('admin', '1234', @resultado);
SELECT @resultado AS resultado_login;
```

* `@resultado` es una **variable de sesión** de MySQL que recibe el valor del parámetro `OUT`. Se usa porque la prueba se hace directamente en SQL; en Java, el valor se obtendría directamente como `boolean`.

### 2.4 Ventajas

* Desde Java se invoca con **una sola línea** (`CALL ...`), en lugar de escribir la consulta completa.
* El código SQL queda **centralizado** en la base de datos y se reutiliza.

---

## 3. Funciones (`FUNCTION`)

* **No confundir** con las funciones de Java: en SQL, procedimiento almacenado y función son elementos distintos.

| Característica | Procedimiento almacenado | Función |
|---|---|---|
| **Retorno** | No necesariamente devuelve algo | **Siempre retorna un valor** |
| **Uso principal** | Manipular datos que existen en las tablas o insertar datos nuevos | Calcular un valor que no necesariamente está almacenado (ej. la edad a partir de la fecha de nacimiento) |
| **Palabra reservada** | `PROCEDURE` | `FUNCTION` |
| **Cómo se invoca** | `CALL` | Dentro de un `SELECT` |

| Elemento | Función |
|---|---|
| `RETURNS tipo` | Declara el tipo de dato que devuelve |
| `DETERMINISTIC` | Indica que la función devuelve **un solo dato** (no una tabla con múltiples valores) |
| `DECLARE` | Declara una variable local; aquí se usa para guardar el resultado que se retorna |
| `RETURN` | Devuelve el valor calculado |
| `AS alias` | Asigna un nombre a la columna para que el resultado se vea claro |

### 3.1 Ejemplo: total de mensajes de una conversación

```sql
DELIMITER //

CREATE FUNCTION FN_TOTAL_MENSAJES(
    p_id_conversacion INT
)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT;

    SELECT COUNT(*)
    INTO total -- guarda la cantidad de mensajes que coinciden
    FROM mensajes
    WHERE id_conversacion = p_id_conversacion;

    RETURN total;
END //

DELIMITER ;

SELECT FN_TOTAL_MENSAJES(1) AS total_mensajes;
```

### 3.2 Ejemplo: último mensaje de una conversación

* Como la llave primaria es **autoincremental**, el **número mayor** corresponde al mensaje más reciente.
* La función devuelve el **ID** del último mensaje, que luego puede usarse para buscarlo desde otra consulta.

```sql
DELIMITER //

CREATE FUNCTION FN_ULTIMO_MENSAJE(
    p_id_conversacion INT
)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT;

    SELECT MAX(id_mensaje)
    INTO total -- guarda el ID más alto (el último mensaje)
    FROM mensajes
    WHERE id_conversacion = p_id_conversacion;

    RETURN total;
END //

DELIMITER ;
```

---

## 4. Triggers (disparadores)

* Un **trigger** es código que se ejecuta **automáticamente** cuando ocurre un evento en una tabla. Funciona con la lógica **acción-reacción**: si se hace algo en una tabla, se dispara algo en la misma o en otra.
* Se crea con `CREATE TRIGGER nombre`.

| Elemento | Función |
|---|---|
| `BEFORE INSERT ON tabla` | Define el evento y el momento: se ejecuta **antes** de insertar |
| `AFTER INSERT ON tabla` | Se ejecuta **después** de insertar |
| `FOR EACH ROW` | Ejecuta el trigger **por cada fila** afectada; es importante en inserciones masivas |
| `NEW` | Hace referencia a la fila que se está insertando |

* El trigger se ejecuta **siempre**, ya sea que el dato se inserte con un `INSERT` normal o mediante un procedimiento almacenado.

### 4.1 Ejemplo con `BEFORE`: normalizar el nombre de usuario

* Antes de insertar un usuario, el trigger convierte el nombre a **minúsculas** y elimina los **espacios** sobrantes.

```sql
DELIMITER //

CREATE TRIGGER TRG_USUARIOS_BEFORE_INSERT
BEFORE INSERT ON usuario
FOR EACH ROW
BEGIN
    -- Poner todo en minúscula y quitar espacios
    SET NEW.usuario = LOWER(TRIM(NEW.usuario));
END //

DELIMITER ;
```

### 4.2 Ejemplo con `AFTER`: actualizar la fecha de la conversación

* Cuando se envía un **nuevo mensaje**, se actualiza automáticamente la **fecha de última modificación** de su conversación.

```sql
DELIMITER //

CREATE TRIGGER TRG_MENSAJES_AFTER_INSERT
AFTER INSERT ON mensaje
FOR EACH ROW
BEGIN
    UPDATE conversacion
    SET fecha_actualizacion = CURRENT_TIMESTAMP() -- fecha y hora actual
    WHERE id_conversacion = NEW.id_conversacion;  -- ID de la conversación del nuevo mensaje
END //

DELIMITER ;
```

> **Observaciones sobre el código original:**
> * En el trigger `BEFORE`, la expresión debe usar `NEW.usuario` dentro de `LOWER(TRIM(...))`, no solo `usuario`.
> * En el trigger `AFTER`, `NEW.id_conversacion` se escribe **sin paréntesis**.
> * Los nombres de tablas deben ser coherentes en todo el script (`usuario`/`usuarios`, `mensaje`/`mensajes`).

---

## 5. Comparación general

| Objeto | Se invoca | Retorna | Uso típico |
|---|---|---|---|
| **Procedimiento almacenado** | `CALL` | Opcional (mediante `OUT`) | Insertar, actualizar o validar datos |
| **Función** | Dentro de un `SELECT` | Siempre un valor | Calcular valores derivados |
| **Trigger** | Automático, por un evento | No retorna | Reglas automáticas (normalizar datos, actualizar fechas) |

---

## 6. Resumen general del tema

* Unir tablas mediante llaves foráneas y primarias permite consultar datos relacionados, pero las consultas se vuelven complejas y repetitivas.
* Un **procedimiento almacenado** (`SP_`) encapsula código SQL reutilizable; usa `IN` para entradas, `OUT` para salidas y se invoca con `CALL`.
* `DELIMITER` permite definir bloques con varias sentencias (`BEGIN ... END`).
* Llamar un procedimiento desde Java requiere **una sola línea**, lo que simplifica el código.
* Una **función** siempre retorna un valor (`RETURNS`), sirve para **calcular** datos derivados y se usa dentro de un `SELECT`.
* Un **trigger** ejecuta código automáticamente **antes o después** de un evento (`BEFORE`/`AFTER INSERT`), con `FOR EACH ROW` para aplicarse a cada fila.
* Los triggers garantizan reglas de negocio sin importar cómo se inserten los datos.