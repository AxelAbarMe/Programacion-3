# Instalación de MySQL Workbench y creación de una base de datos en MySQL

## 1. Herramientas necesarias

| Herramienta | Para qué sirve | ¿Obligatoria? |
|---|---|---|
| **MySQL Installer** | Instalador de Windows que descarga, instala y actualiza los productos de MySQL | Sí (en Windows) |
| **MySQL Server 8.0** | El **motor** de base de datos; es quien almacena y ejecuta las consultas | Sí |
| **MySQL Workbench 8.0** | Programa **gráfico (cliente)** para escribir consultas, crear esquemas y administrar usuarios | Sí |
| **Connector/J (Platform Independent)** | Conector (*driver*) JDBC que permite a un programa **Java** conectarse a MySQL | Solo si se programa en Java |

* **MySQL Workbench no es la base de datos**: es únicamente un cliente. Sin **MySQL Server** instalado y en ejecución, Workbench no tiene a qué conectarse.
* **Connector/J "Platform Independent"** significa que el mismo archivo (`.zip` o `.tar.gz` con un `.jar` dentro) sirve para cualquier sistema operativo, porque Java es multiplataforma.

---

## 2. Instalación paso a paso (Windows)

### 2.1 Descargar el instalador

1. Entrar a la página oficial de descargas de MySQL: `https://dev.mysql.com/downloads/`.
2. Elegir **MySQL Installer for Windows**.
3. Descargar la versión **completa** (`mysql-installer-community`, de mayor tamaño) para poder instalar sin conexión; la versión *web* descarga los componentes durante la instalación.
4. Si el sitio solicita iniciar sesión, se puede omitir con **"No thanks, just start my download"**.

### 2.2 Instalar con MySQL Installer

1. Ejecutar el instalador y elegir el tipo de instalación **Custom** (personalizada), para escoger solo lo necesario.
2. En la lista de productos, agregar a la columna de instalación:
   * **MySQL Server 8.0.x**
   * **MySQL Workbench 8.0.x**
   * *(Opcional)* **Connectors → Connector/J**, si se va a usar Java. También se puede descargar por separado como *Platform Independent*.
3. Presionar **Next** y luego **Execute** para que se descarguen e instalen los componentes.
4. En la configuración del servidor, dejar los valores por defecto en general:

| Opción | Valor recomendado |
|---|---|
| **Config Type** | Development Computer |
| **Puerto (Port)** | `3306` (puerto estándar de MySQL) |
| **Authentication Method** | Use Strong Password Encryption (recomendado) |
| **Root Password** | Una contraseña segura para el usuario `root`; **anotarla**, porque se usará para conectarse |
| **Windows Service** | Marcado, para que el servidor inicie junto con Windows |

5. Finalizar con **Execute** y **Finish**. Al terminar, Workbench puede abrirse automáticamente.

### 2.3 Instalar Connector/J (solo para Java)

1. En `https://dev.mysql.com/downloads/connector/j/`, elegir **Platform Independent** en el selector de sistema operativo.
2. Descargar el archivo `.zip` (Windows) o `.tar.gz` (Linux/Mac) y **extraerlo**.
3. Dentro estará el archivo `mysql-connector-j-8.x.x.jar`.
4. Agregar ese `.jar` al proyecto Java (en el *classpath* o como librería externa del IDE, por ejemplo Eclipse, IntelliJ o NetBeans).
5. La URL de conexión desde Java tiene este formato:

```
jdbc:mysql://localhost:3306/avi_db
```

### 2.4 Verificar que el servidor esté activo

1. Presionar `Win + R`, escribir `services.msc` y buscar el servicio **MySQL80**.
2. Su estado debe ser **En ejecución**; si no, clic derecho → **Iniciar**.

---

## 3. Crear la conexión en MySQL Workbench

1. Abrir **MySQL Workbench**.
2. En la pantalla principal, en **MySQL Connections**, debe aparecer `Local instance MySQL80`. Si no aparece, presionar el signo **+** para crear una conexión:

| Campo | Valor |
|---|---|
| **Connection Name** | Cualquier nombre (ej. `Local`) |
| **Hostname** | `127.0.0.1` o `localhost` |
| **Port** | `3306` |
| **Username** | `root` |

3. Hacer doble clic en la conexión e ingresar la contraseña de `root` que se configuró en la instalación.
4. Se abrirá el área de trabajo con el panel **Navigator** (izquierda) y una pestaña de **Query** (centro).

---

## 4. Crear el esquema (base de datos)

* En MySQL, un **Schema (esquema) es una base de datos**; ambos términos son sinónimos.

### Pasos con el entorno gráfico

1. En el panel **Navigator**, ir a la pestaña **Schemas**.
2. Presionar el botón **Create a new schema** (icono de cilindro con un `+`) de la barra superior.
3. En **Name**, escribir `avi_db`.
4. Dejar el **Charset/Collation** por defecto (`utf8mb4` / `utf8mb4_0900_ai_ci`), que soporta tildes, ñ y emojis.
5. Presionar **Apply** (aplicar). Aparecerá una ventana con la sentencia SQL que se va a ejecutar:

```sql
CREATE SCHEMA `avi_db`;
```

6. Presionar **Apply** de nuevo y luego **Finish**.
7. Presionar el botón de **refrescar** en el panel Schemas; `avi_db` debe aparecer en la lista.

### Equivalente por código

```sql
CREATE SCHEMA avi_db;
-- o también:
CREATE DATABASE avi_db;
```

---

## 5. Ejecutar código en la pestaña Query

* Para escribir SQL, abrir una nueva pestaña con **File → New Query Tab** (o `Ctrl + T`).

| Acción | Atajo / botón |
|---|---|
| Ejecutar **toda** la pestaña (o solo lo seleccionado) | `Ctrl + Shift + Enter` o el **rayo** |
| Ejecutar **solo la sentencia donde está el cursor** | `Ctrl + Enter` o el **rayo con cursor** |
| Ver resultados y mensajes | Panel **Result Grid** y **Output** (parte inferior) |

* Los **comentarios** en SQL se escriben con `--` (seguido de un espacio) para una línea.
* Cada sentencia termina en **punto y coma** (`;`).
* **Recomendación:** ejecutar las sentencias **una por una** mientras se aprende, ya que algunas generan errores a propósito.

---

## 6. Código de la práctica (Query)

```sql
QUERY:

USE avi_db;

CREATE TABLE usuario
(
	pk_usuario INT AUTO_INCREMENT,
    nombre_usuario varchar(15) NOT NULL,
	contrasena varchar(15) NOT NULL,
    activo boolean NOT NULL DEFAULT FALSE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT id_usuario PRIMARY KEY(pk_usuario),
    CONSTRAINT uq_usuario UNIQUE (nombre_usuario)
);

INSERT INTO usuario (nombre_usuario, contrasena) VALUES ('admin', 'admin');
INSERT INTO usuario (nombre_usuario, contrasena) VALUES ('Isaac', 'thebindingofisaac');
INSERT INTO usuario (nombre_usuario, contrasena) VALUES ('Saac', 'TheSaac');
INSERT INTO usuario (nombre_usuario, contrasena) VALUES ('roger.leon', 'root');
INSERT INTO usuario (nombre_usuario, contrasena) VALUES ('roger.leon', 'root123'); -- Da fallo, ya que nombre_usuarioes UNIQUE

SELECT * FROM usuario;
SELECT nombre_usuario, contrasena, activo FROM usuario;
SELECT nombre_usuario, contrasena, activo FROM usuario WHERE activo = TRUE;

UPDATE usuario SET activo = TRUE WHERE pk_usuarion = 1;

DELETE FROM usuario; -- No hacer

DELETE FROM usuario WHERE pk_usuario = 3;
```

> `*` es traer **todas las filas y columnas** que existen.

* **Nota:** en el `UPDATE`, la columna aparece escrita como `pk_usuarion` (con una `n` de más). El nombre correcto de la columna es **`pk_usuario`**; con la `n` MySQL responde `Error 1054: Unknown column 'pk_usuarion'`. La línea correcta es:

```sql
UPDATE usuario SET activo = TRUE WHERE pk_usuario = 1;
```

---

## 7. Explicación del código, línea por línea

### 7.1 `USE avi_db;`

* Le indica a MySQL **en qué base de datos se trabajará**. Todas las sentencias siguientes se ejecutan sobre `avi_db`.
* Sin esta línea (o sin doble clic sobre el esquema para dejarlo en negrita como "predeterminado"), MySQL da el error `No database selected`.

### 7.2 `CREATE TABLE usuario (...)`

* Crea la tabla `usuario` con sus columnas y reglas.

| Columna | Tipo | Restricciones | Explicación |
|---|---|---|---|
| `pk_usuario` | `INT` | `AUTO_INCREMENT`, clave primaria | Identificador numérico único; MySQL lo genera solo (1, 2, 3, ...). El prefijo `pk_` indica *primary key* |
| `nombre_usuario` | `varchar(15)` | `NOT NULL`, `UNIQUE` | Texto de **hasta 15 caracteres**; obligatorio y **no se puede repetir** |
| `contrasena` | `varchar(15)` | `NOT NULL` | Texto de hasta 15 caracteres; obligatorio. Se escribe sin "ñ" para evitar problemas de codificación |
| `activo` | `boolean` | `NOT NULL DEFAULT FALSE` | Verdadero/falso; si no se indica, queda en `FALSE` (0) |
| `fecha_registro` | `DATETIME` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Fecha y hora; si no se indica, toma **la fecha y hora actual** al insertar |

#### Conceptos clave

| Concepto | Significado |
|---|---|
| `AUTO_INCREMENT` | El número se incrementa automáticamente en cada inserción; no se envía en el `INSERT` |
| `NOT NULL` | La columna **no puede quedar vacía** (nula) |
| `DEFAULT` | Valor que se asigna cuando no se especifica uno en el `INSERT` |
| `CURRENT_TIMESTAMP` | Función que devuelve la fecha y hora actuales del servidor |
| `varchar(n)` | Texto de longitud variable con un máximo de `n` caracteres |
| `boolean` | En MySQL es un **alias de `TINYINT(1)`**: `TRUE` equivale a `1` y `FALSE` a `0` |

#### Las restricciones con nombre (`CONSTRAINT`)

```sql
CONSTRAINT id_usuario PRIMARY KEY(pk_usuario),
CONSTRAINT uq_usuario UNIQUE (nombre_usuario)
```

* `CONSTRAINT nombre` permite **darle un nombre a la regla**, útil para identificarla en mensajes de error o para eliminarla después.
* **`PRIMARY KEY(pk_usuario)`:** identifica de forma única cada fila; no admite nulos ni repetidos.
* **`UNIQUE (nombre_usuario)`:** impide que dos usuarios tengan el mismo nombre.
* Por convención: `pk_` para clave primaria, `uq_` para única, `fk_` para clave foránea.

### 7.3 `INSERT INTO ... VALUES ...`

```sql
INSERT INTO usuario (nombre_usuario, contrasena) VALUES ('admin', 'admin');
```

* Inserta una **nueva fila** indicando solo las columnas obligatorias que no tienen valor por defecto.
* Las demás columnas se llenan solas: `pk_usuario` (autoincremental), `activo` (`FALSE`) y `fecha_registro` (fecha actual).
* El orden de los valores debe coincidir con el orden de las columnas listadas.

| Inserción | Resultado esperado |
|---|---|
| `'admin'` / `'admin'` | Correcta (`pk_usuario = 1`) |
| `'Isaac'` / `'thebindingofisaac'` | Correcta (`pk_usuario = 2`) |
| `'Saac'` / `'TheSaac'` | Correcta (`pk_usuario = 3`) |
| `'roger.leon'` / `'root'` | Correcta (`pk_usuario = 4`) |
| `'roger.leon'` / `'root123'` | **Falla**: `Error 1062: Duplicate entry 'roger.leon' for key 'usuario.uq_usuario'` |

* El último `INSERT` falla porque `nombre_usuario` es **`UNIQUE`** y `'roger.leon'` ya existe.
* Si se ejecuta todo el script de una vez, el error **detiene** la ejecución de las sentencias siguientes; por eso conviene ejecutar por separado o continuar desde el `SELECT`.
* Detalle: aunque el `INSERT` falle, el contador `AUTO_INCREMENT` puede avanzar, por lo que el siguiente registro válido podría saltarse un número (ej. 6 en lugar de 5). Esto es normal.

### 7.4 `SELECT` (consultar datos)

```sql
SELECT * FROM usuario;
SELECT nombre_usuario, contrasena, activo FROM usuario;
SELECT nombre_usuario, contrasena, activo FROM usuario WHERE activo = TRUE;
```

| Sentencia | Qué hace |
|---|---|
| `SELECT * FROM usuario;` | Trae **todas las filas y todas las columnas** (`*` = todo) |
| `SELECT nombre_usuario, contrasena, activo FROM usuario;` | Trae **solo las columnas indicadas**, de todas las filas |
| `... WHERE activo = TRUE;` | Trae solo las filas que **cumplen la condición** (usuarios activos) |

* `WHERE` filtra filas; sin él, el `SELECT` devuelve todas.
* Recomendación: evitar `SELECT *` en programas reales y pedir solo las columnas necesarias, para ser más eficiente.
* Justo después de crear los usuarios, la tercera consulta devuelve **0 filas**, porque todos tienen `activo = FALSE` por defecto.

### 7.5 `UPDATE` (modificar datos)

```sql
UPDATE usuario SET activo = TRUE WHERE pk_usuario = 1;
```

* Cambia el valor de `activo` a `TRUE` **solo en la fila** cuyo `pk_usuario` es 1 (el usuario `admin`).
* **Siempre debe llevar `WHERE`**; sin él, se modificarían **todas** las filas de la tabla.
* Después de ejecutarlo, la consulta `WHERE activo = TRUE` devuelve al usuario `admin`.

### 7.6 `DELETE` (eliminar datos)

```sql
DELETE FROM usuario; -- No hacer
DELETE FROM usuario WHERE pk_usuario = 3;
```

| Sentencia | Efecto |
|---|---|
| `DELETE FROM usuario;` | **Borra todas las filas** de la tabla. **No hacer** salvo que ese sea realmente el objetivo |
| `DELETE FROM usuario WHERE pk_usuario = 3;` | Borra **solo** al usuario con `pk_usuario = 3` (`Saac`) |

* Por defecto, Workbench activa el **Safe Updates Mode**, que bloquea `UPDATE` y `DELETE` sin `WHERE` sobre una clave. Si se intenta, aparece el `Error 1175`.
* Desactivarlo (**Edit → Preferences → SQL Editor → Safe Updates**) **no se recomienda**, justamente para evitar borrados accidentales.
* `DELETE` borra filas pero **no** la tabla; para eliminar la tabla completa se usa `DROP TABLE usuario;`.

---

## 8. Resumen de operaciones (CRUD)

| Operación | Sentencia SQL | Ejemplo |
|---|---|---|
| **C**reate (crear) | `INSERT INTO` | `INSERT INTO usuario (...) VALUES (...);` |
| **R**ead (leer) | `SELECT` | `SELECT * FROM usuario;` |
| **U**pdate (actualizar) | `UPDATE` | `UPDATE usuario SET activo = TRUE WHERE pk_usuario = 1;` |
| **D**elete (borrar) | `DELETE` | `DELETE FROM usuario WHERE pk_usuario = 3;` |

---

## 9. Crear un usuario para la aplicación (SQL_FILE)

```sql
SQL_FILE

CREATE USER IF NOT EXISTS 'avi_app' @'localhost'
IDENTIFIED BY 'Avi2026';

GRANT SELECT, INSERT, UPDATE, DELETE
ON avi_db.*
TO 'avi_app'@'localhost';

FLUSH PRIVILEGES;
```

* `SQL_FILE` indica que este bloque se guarda como **archivo de script** (`.sql`), no como una consulta suelta. En Workbench: **File → New Query Tab**, escribir el código y **File → Save Script As...**; o abrir uno existente con **File → Open SQL Script...**.
* Este script se ejecuta con el usuario **`root`**, porque solo un administrador puede crear usuarios y asignar permisos.

### Explicación

| Sentencia | Qué hace |
|---|---|
| `CREATE USER IF NOT EXISTS 'avi_app'@'localhost' IDENTIFIED BY 'Avi2026';` | Crea el usuario `avi_app`, que solo puede conectarse desde la **misma máquina** (`localhost`), con la contraseña `Avi2026`. `IF NOT EXISTS` evita error si ya existe |
| `GRANT SELECT, INSERT, UPDATE, DELETE ON avi_db.* TO 'avi_app'@'localhost';` | Le da permiso de **leer, insertar, actualizar y borrar** datos en **todas las tablas** (`.*`) de `avi_db` |
| `FLUSH PRIVILEGES;` | Recarga las tablas de permisos para asegurar que los cambios se apliquen |

### ¿Por qué un usuario distinto de `root`?

* Principio de **mínimo privilegio**: la aplicación solo recibe los permisos que necesita.
* `avi_app` **no puede** crear ni eliminar tablas (`CREATE`, `DROP`), ni modificar otras bases de datos.
* Si las credenciales de la aplicación se filtran, el daño queda limitado a los datos de `avi_db`.

### Probar el nuevo usuario

1. En la pantalla principal de Workbench, presionar **+** junto a *MySQL Connections*.
2. Configurar: **Username** `avi_app`, **Hostname** `localhost`, **Port** `3306`.
3. Al conectar, ingresar la contraseña `Avi2026`.
4. Ejecutar `SELECT * FROM avi_db.usuario;` (funciona) y `DROP TABLE avi_db.usuario;` (debe dar error de permisos).

---

## 10. Errores comunes y soluciones

| Error | Causa | Solución |
|---|---|---|
| `Can't connect to MySQL server on 'localhost'` | El servicio MySQL80 está detenido | Iniciar el servicio desde `services.msc` |
| `Access denied for user 'root'` | Contraseña incorrecta | Usar la contraseña definida en la instalación |
| `Error 1046: No database selected` | Falta `USE avi_db;` | Ejecutar `USE avi_db;` o hacer doble clic en el esquema |
| `Error 1050: Table 'usuario' already exists` | La tabla ya se creó antes | Eliminarla con `DROP TABLE usuario;` o no volver a ejecutar el `CREATE` |
| `Error 1062: Duplicate entry` | Valor repetido en una columna `UNIQUE` | Usar otro valor (el error del `roger.leon` es intencional) |
| `Error 1054: Unknown column` | Nombre de columna mal escrito (ej. `pk_usuarion`) | Corregir el nombre |
| `Error 1175: Safe update mode` | `UPDATE`/`DELETE` sin `WHERE` sobre clave | Agregar `WHERE pk_usuario = ...` |

---

## 11. Buenas prácticas mostradas en el ejemplo

* **Nombrar las restricciones** (`id_usuario`, `uq_usuario`) para identificarlas fácilmente.
* **Usar una clave primaria numérica autoincremental**, en lugar de usar el nombre como identificador.
* **Definir valores por defecto** (`activo`, `fecha_registro`) para reducir datos que enviar en cada `INSERT`.
* **Usar siempre `WHERE`** en `UPDATE` y `DELETE`.
* **No guardar contraseñas en texto plano** en sistemas reales: el ejemplo las guarda tal cual solo con fines de práctica; en la realidad se almacenan con *hash* (ej. bcrypt, SHA-256 con *salt*), y la columna necesita más de 15 caracteres.
* **Crear un usuario por aplicación** con permisos mínimos, en vez de usar `root`.

---

## 12. Resumen general del tema

* Para trabajar con MySQL se necesita **MySQL Server** (el motor) y **MySQL Workbench** (el cliente gráfico); **Connector/J** solo se usa para conectar programas **Java**.
* **MySQL Installer** permite instalar todo en Windows; se recomienda la opción **Custom**, el puerto `3306` y guardar la contraseña de `root`.
* Un **schema es una base de datos**; se crea desde el panel **Schemas** con **Apply**, o con `CREATE SCHEMA avi_db;`.
* En la pestaña **Query** se escribe y ejecuta SQL; `USE avi_db;` selecciona la base de datos de trabajo.
* `CREATE TABLE` define columnas, tipos de datos y restricciones: `AUTO_INCREMENT`, `NOT NULL`, `DEFAULT`, `PRIMARY KEY` y `UNIQUE`.
* `INSERT` agrega filas; un valor repetido en una columna `UNIQUE` produce un **error 1062**.
* `SELECT` consulta datos (`*` trae todas las filas y columnas) y `WHERE` filtra las filas.
* `UPDATE` y `DELETE` **siempre deben llevar `WHERE`**; `DELETE FROM usuario;` borra toda la tabla y **no se debe hacer**.
* Con `CREATE USER` y `GRANT` se crea un usuario de aplicación (`avi_app`) con permisos limitados sobre `avi_db`, siguiendo el principio de **mínimo privilegio**.
