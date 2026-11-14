# Conexión de Java con MySQL: Resumen Académico

---

## 1. Contexto de la sesión

* La clase se impartió de forma **virtual**; la del siguiente lunes también será virtual y la del jueves se mantiene presencial.
* **Objetivo:** levantar oficialmente la base de datos y conectar la **capa de datos** de la aplicación Java con **MySQL**, sustituyendo el almacenamiento anterior basado en archivos (**XML** y **JSON**).
* El docente utilizó **XAMPP** en lugar de MySQL Workbench.

| Herramienta | Descripción |
|---|---|
| **XAMPP** | Panel de control que funciona como un mini servidor de aplicaciones; incluye Apache y MySQL ya configurados |
| **MySQL Workbench** | Cliente gráfico recomendado a los estudiantes para administrar la base de datos |
| **Apache** | Servidor web incluido en XAMPP (no es indispensable para la conexión Java-MySQL) |

* **Idea central:** se trasladó todo lo construido en la sesión anterior (diseño de base de datos) hacia Java, conectándolo con la capa de datos existente.

---

## 2. Configuración de la base de datos

### 2.1 Usuario de la aplicación y permisos

* Se crea un usuario específico (`avi_app`) para que la aplicación se conecte, en lugar de utilizar `root`.

```sql
CREATE USER IF NOT EXISTS 'avi_app'@'localhost'
IDENTIFIED BY 'Avi2026!';

GRANT SELECT, INSERT, UPDATE, DELETE
ON avi_db.*
TO 'avi_app'@'localhost';

FLUSH PRIVILEGES;
```

| Sentencia | Función |
|---|---|
| `CREATE USER IF NOT EXISTS` | Crea el usuario solo si no existe; `@'localhost'` restringe la conexión a la misma máquina |
| `IDENTIFIED BY` | Define la contraseña del usuario |
| `GRANT ... ON avi_db.*` | Otorga permisos de lectura, inserción, actualización y borrado sobre todas las tablas de `avi_db` |
| `FLUSH PRIVILEGES` | Recarga los permisos para que los cambios surtan efecto |

* **Principio de mínimo privilegio:** la aplicación recibe únicamente los permisos que necesita; no puede crear ni eliminar tablas, lo que limita el daño si las credenciales se filtran.

### 2.2 Tabla `usuario`

```sql
USE avi_db;

CREATE TABLE usuario (
    pk_usuario INT AUTO_INCREMENT,
    usuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT id_usuario
        PRIMARY KEY (pk_usuario),

    CONSTRAINT uq_usuarios_usuario
        UNIQUE (usuario)
);
```

| Columna | Tipo | Restricciones | Explicación |
|---|---|---|---|
| `pk_usuario` | `INT` | `AUTO_INCREMENT`, clave primaria | Identificador único generado automáticamente |
| `usuario` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | Nombre de usuario obligatorio y no repetible |
| `contrasena` | `VARCHAR(255)` | `NOT NULL` | Longitud amplia pensada para almacenar contraseñas cifradas (*hash*) |
| `activo` | `BOOLEAN` | `NOT NULL DEFAULT FALSE` | Indica si el usuario puede iniciar sesión; por defecto inactivo |
| `fecha_registro` | `DATETIME` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Fecha y hora de creación, asignada automáticamente |

#### Conceptos clave

| Concepto | Significado |
|---|---|
| `AUTO_INCREMENT` | El motor genera el siguiente número de forma automática en cada inserción |
| `NOT NULL` | La columna no admite valores vacíos |
| `DEFAULT` | Valor asignado cuando no se especifica uno al insertar |
| `CONSTRAINT` | Regla con nombre, útil para identificarla en errores o eliminarla después |
| `PRIMARY KEY` | Identifica de forma única cada fila; no admite nulos ni duplicados |
| `UNIQUE` | Impide valores repetidos en la columna |

---

## 3. Capa de datos en Java

* La **capa de datos** es la responsable de comunicarse directamente con la fuente de datos (antes archivos, ahora MySQL).
* Paquete utilizado: `una.eife206.avi.datos`.

### 3.1 Clase `ConexionBD`

* Centraliza la información necesaria para conectarse a MySQL.
* Contiene constantes `private static final` con la URL, el usuario y la contraseña.
* Su constructor es **privado**, por lo que la clase no se instancia; se usa mediante un método **estático**.
* Requiere agregar la **dependencia del conector MySQL (JDBC)** en el archivo `pom.xml` (Maven).

```java
package una.eife206.avi.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/avi_db"
                                    + "?useSSL=false"
                                    + "&allowPublicKeyRetrieval=true"
                                    + "&serverTimezone=UTC";

    private static final String USER = "avi_app";
    private static final String PASSWORD = "Avi2026!";

    private ConexionBD() {}

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

#### Anatomía de la URL de conexión

| Parte | Significado |
|---|---|
| `jdbc:mysql://` | Protocolo JDBC para MySQL |
| `localhost:3306` | Servidor y puerto estándar de MySQL |
| `/avi_db` | Base de datos a la que se conecta |
| `useSSL=false` | Desactiva el cifrado SSL (aceptable en desarrollo local) |
| `allowPublicKeyRetrieval=true` | Permite obtener la clave pública del servidor para autenticarse |
| `serverTimezone=UTC` | Define la zona horaria para evitar errores de fecha y hora |

#### Conceptos clave

| Concepto | Significado |
|---|---|
| **JDBC** | API estándar de Java para conectarse a bases de datos relacionales |
| **`DriverManager`** | Clase que administra los controladores y crea las conexiones |
| **Patrón de clase utilitaria** | Constructor privado y métodos estáticos para impedir la instanciación |

> **Advertencia de seguridad:** las credenciales están escritas directamente en el código ("quemadas"), lo cual es una mala práctica por tratarse de datos sensibles. Más adelante en el curso se aprenderá a ocultarlas.

### 3.2 Clase `UsuarioDatos`

* Contiene la operación que valida las credenciales directamente contra MySQL.

```java
package una.eife206.avi.datos;

import una.eif206.avi.dto.UsuarioDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDatos {

    public boolean validarCredenciales(UsuarioDTO usuario) throws SQLException
    {
        String sql = "SELECT pk_usuario " +
                     "FROM usuario " +
                     "WHERE usuario = ? " +
                     "AND password = ? " +
                     "AND activo = TRUE";

        try (
            Connection conexion = ConexionBD.obtenerConexion();
            PreparedStatement sentencia = conexion.prepareStatement(sql)
        )
        {
            sentencia.setString(1, usuario.getUsuario());
            sentencia.setString(2, usuario.getContrasena());

            try (ResultSet resultado = sentencia.executeQuery())
            {
                return resultado.next();
            }
        }
    }
}
```

> **Observación:** en la captura, la consulta usa `password` como nombre de columna, mientras que la tabla define la columna como `contrasena`. Ambos nombres deben coincidir para que la consulta funcione.

#### Elementos de la clase

| Elemento | Función |
|---|---|
| `Connection` | Representa la conexión activa con la base de datos |
| `PreparedStatement` | Sentencia SQL precompilada con parámetros `?` |
| `setString(índice, valor)` | Asigna el valor a cada parámetro según su posición (comienza en 1) |
| `executeQuery()` | Ejecuta un `SELECT` y devuelve un `ResultSet` |
| `ResultSet` | Conjunto de filas devuelto por la consulta |
| `resultado.next()` | Devuelve `true` si existe al menos una fila; indica credenciales válidas |
| `try-with-resources` | Cierra automáticamente conexión, sentencia y resultado al terminar |
| `throws SQLException` | Delega el manejo de errores de base de datos a la capa superior |

#### Ventajas de `PreparedStatement`

* **Previene inyección SQL**, ya que los valores se envían como parámetros y no como texto concatenado.
* Permite reutilizar la sentencia precompilada, mejorando el rendimiento.
* Hace el código más legible y mantenible.

---

## 4. Arquitectura en capas y cambios realizados

* La aplicación sigue una **arquitectura en capas**: cada capa tiene una responsabilidad específica y se comunica solo con la capa contigua.

| Capa | Responsabilidad | Cambio realizado |
|---|---|---|
| **DTO** (`UsuarioDTO`) | Transportar datos entre capas | Ninguno; solo usa usuario y contraseña |
| **Datos** (`UsuarioDatos`, `ConexionBD`) | Acceso directo a la fuente de datos | Se reemplaza la lectura de archivos por consultas a MySQL |
| **Lógica** (`LoginLogica`) | Reglas de negocio | Nuevo método que invoca `UsuarioDatos.validarCredenciales`; propaga la excepción si algo falla |
| **Servicio** (`UsuarioService`) | Punto de acceso para los controllers | Nuevo método simplificado `iniciarSesionBaseDatos(usuario, contrasena)` que ya no recibe la "fuente" |
| **Controller** (`LoginViewController`) | Gestiona la interfaz gráfica | Se elimina la fuente de datos, se redirecciona al nuevo método y se ajustan los mensajes |
| **Vista** (diseño) | Interfaz de usuario | Se elimina el combo de selección de fuente de datos |

### Ejemplo del cambio en la capa de servicio

```java
public boolean iniciarSesionBaseDatos(String usuario, String contrasena) {
    return logica.iniciarSesionBD(usuario, contrasena);
}
```

* El parámetro **fuente** (XML o JSON) ya no es necesario, porque la única fuente es ahora la base de datos.
* El método anterior que dependía de la fuente podría incluso eliminarse.

---

## 5. Flujo de inicio de sesión

1. El usuario ingresa su **usuario y contraseña** en la pantalla de login (interfaz gráfica).
2. El **controller** recibe la información y la envía a la capa de **servicios**.
3. El **servicio** invoca la capa de **lógica**.
4. La **lógica** llama a `UsuarioDatos.validarCredenciales`; si ocurre un error, lanza una excepción.
5. `UsuarioDatos` obtiene una conexión mediante **`ConexionBD`** y ejecuta la consulta en MySQL.
6. MySQL devuelve el resultado y la capa de datos retorna `true` o `false`.
7. La respuesta sube por las capas hasta el controller, que permite o niega el acceso.

---

## 6. Conclusiones

* La clase de datos se redujo de **casi 100 líneas a aproximadamente 45**, al delegar el trabajo en SQL.
* Un cambio de gran magnitud (migrar de archivos en memoria a una base de datos) requirió modificar solo **una o dos líneas por capa**.
* Esto demuestra dos propiedades fundamentales de un buen diseño:

| Propiedad | Definición |
|---|---|
| **Escalabilidad** | Capacidad de ampliar, quitar o modificar funcionalidades sin reescribir grandes partes del código |
| **Mantenibilidad** | Facilidad para corregir o modificar el código con un esfuerzo mínimo |

* Lo anterior confirma los principios de **Programación II**: un diseño bien estructurado absorbe los cambios con un impacto mínimo.

---

## 7. Próximos pasos

* Introducir el tema nuevo de la siguiente unidad, sin abandonar el de base de datos.
* Estudiar la **encriptación de contraseñas** y otros aspectos de bases de datos útiles para cursos posteriores.
* Aprender a **ocultar datos sensibles** (credenciales) del código fuente.
* El curso lleva una semana de ventaja en el cronograma, lo que permitirá dedicar las últimas semanas a **practicar** antes del último examen, que será más exigente.

---

## 8. Resumen general del tema

* La conexión Java-MySQL se realiza mediante **JDBC**, con una clase (`ConexionBD`) que centraliza la URL, el usuario y la contraseña.
* Se usa un **usuario de aplicación** con permisos limitados, siguiendo el principio de **mínimo privilegio**.
* La tabla `usuario` define restricciones (`PRIMARY KEY`, `UNIQUE`, `NOT NULL`, `DEFAULT`) que garantizan la integridad de los datos.
* `UsuarioDatos.validarCredenciales` emplea **`PreparedStatement`** y **`try-with-resources`** para consultar de forma segura y liberar recursos.
* La **arquitectura en capas** permitió migrar de archivos a base de datos con cambios mínimos, evidenciando **escalabilidad** y **mantenibilidad**.
* Las credenciales en el código fuente son una mala práctica; se corregirá con técnicas de ocultamiento y **encriptación** en próximas sesiones.
