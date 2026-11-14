# Sistema de Reserva de Recursos — Proyecto #2 (EIF206 Programación 3)

Arquitectura distribuida **Frontend – Backend – Base de datos**:

```
 Frontend (JavaFX, MVC)  ──sockets──▶  Backend (servidor sin interfaz)  ──JDBC──▶  MySQL
 Frontend (JavaFX, MVC)  ──sockets──▶        │
        ▲                                     │
        └──── alertas asincrónicas ◀──────────┘   (cada vez que alguien registra una reserva)
```

- **Backend**: capas Lógica y Datos. Escucha en un puerto, atiende a varios Frontend a la vez (un hilo por cliente) y es el único que toca la base de datos.
- **Frontend**: capa de Presentación (Modelo-Vista-Controlador). No tiene ninguna referencia a la base de datos. Se conecta al Backend por sockets y puede correr en otra máquina.
- **Patrón Proxy**: el Frontend solo conoce la interfaz `BackendService`. La implementa `ServerConnectionProxy` (representante local que envía cada llamada por el socket) y la implementa `BackendServiceImpl` (el Service real en el servidor).
- **Alertas**: cada Frontend abre un segundo canal dedicado. Cuando un usuario crea una reserva, el Backend la empuja a todos los demás usuarios conectados (nunca al autor).

## 1. Requisitos

| Herramienta | Versión |
|---|---|
| JDK | 26 (el `pom.xml` compila con `maven.compiler.source/target = 26`; si tu JDK es otro, ajusta esas dos propiedades) |
| Maven | 3.9 o superior |
| MySQL | 8.x (el script usa la colación `utf8mb4_0900_ai_ci`) |
| JavaFX | 21.0.2 (lo descarga Maven) |

## 2. Base de datos

### 2.1 Base de datos de la aplicación

Ejecute el script **`Query para BD.md`** (es SQL puro) como administrador de MySQL:

```bash
mysql -u root -p < "Query para BD.md"
```

Crea la base `resource_db`, el usuario `resource_app` (solo con permisos `SELECT, INSERT, UPDATE, DELETE`), las tablas y los usuarios de prueba.

**Usuarios de prueba**

| Rol | Usuario (ID) | Clave |
|---|---|---|
| Administrador | `admin123` | `admin123@` |
| Funcionario | `user123` | `123@` |
| Funcionario | `123` | `123@` |

> Las claves se guardan con hash (PBKDF2 + sal). Las del script vienen en texto plano y el servidor las convierte a hash la primera vez que el usuario inicia sesión.

Los funcionarios nuevos se crean con la **clave igual a su ID de 9 dígitos**; en su primer ingreso el sistema los obliga a cambiarla.

### 2.2 Base de datos de pruebas (solo para las pruebas de integración)

Las pruebas de integración **borran los datos** de la base donde corren, por eso usan una base aparte llamada `resource_db_test` (el nombre debe contener `_test`; si no, las pruebas se niegan a correr).

```sql
-- como administrador de MySQL
DROP DATABASE IF EXISTS resource_db_test;
CREATE DATABASE resource_db_test CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
GRANT SELECT, INSERT, UPDATE, DELETE ON resource_db_test.* TO 'resource_app'@'localhost';
FLUSH PRIVILEGES;
USE resource_db_test;
```

Luego cree las mismas tablas: copie de `Query para BD.md` desde `CREATE TABLE user` hasta `CREATE INDEX idx_resource ...` (no copie los `INSERT` de usuarios de prueba) y ejecútelas con la base `resource_db_test` seleccionada.

## 3. Variables de configuración

Cada proceso lee su configuración de **propiedades del sistema** (`-Dnombre=valor`) o de **variables de entorno**, en ese orden.

### Backend (servidor)

| Variable de entorno | Propiedad | Obligatoria | Descripción | Por defecto |
|---|---|---|---|---|
| `RESOURCEMANAGER_DB_USER` | `resourcemanager.db.user` | **Sí** | Usuario de MySQL | — |
| `RESOURCEMANAGER_DB_PASSWORD` | `resourcemanager.db.password` | **Sí** | Clave del usuario de MySQL | — |
| `RESOURCEMANAGER_DB_URL` | `resourcemanager.db.url` | No | URL JDBC | `jdbc:mysql://localhost:3306/resource_db?useSSL=false&allowPublicKeyRetrieval=true` |
| `RESOURCEMANAGER_PORT` | `resourcemanager.port` | No | Puerto donde escucha | `5000` |
| `GEMINI_API_KEY` | — | Solo para "Extraer con IA" | Llave de la API de Gemini. Se configura **en la máquina del servidor** | — |

### Frontend (cliente)

| Variable de entorno | Propiedad | Descripción | Por defecto |
|---|---|---|---|
| `RESOURCEMANAGER_HOST` | `resourcemanager.host` | Dirección (IP o nombre) de la máquina del servidor | `localhost` |
| `RESOURCEMANAGER_PORT` | `resourcemanager.port` | Puerto del servidor (el mismo del Backend) | `5000` |

**Cómo definirlas**

```powershell
# Windows (PowerShell) — solo para la sesión actual
$env:RESOURCEMANAGER_DB_USER = "resource_app"
$env:RESOURCEMANAGER_DB_PASSWORD = "ResourceApp"
$env:GEMINI_API_KEY = "su-llave"
```

```bash
# Linux / macOS
export RESOURCEMANAGER_DB_USER=resource_app
export RESOURCEMANAGER_DB_PASSWORD=ResourceApp
export GEMINI_API_KEY=su-llave
```

En IntelliJ: *Run → Edit Configurations → Environment variables* de cada configuración de ejecución.

## 3.1 Credenciales y seguridad

- La clave de MySQL **no está escrita en el código**: se lee del entorno.
- Las contraseñas de los usuarios se guardan con hash y **nunca** se envían a ningún Frontend.
- El Backend valida permisos en cada operación (las de administración solo las puede ejecutar un administrador; solo el dueño o un administrador cancela una reserva).
- Cambio de clave: se exige la clave actual y verificar el teléfono registrado; la política de clave (mínimo 8 caracteres con mayúscula, minúscula, número y símbolo) la valida el servidor.

## 4. Ejecución

### 4.1 Iniciar el Backend (primero)

Desde IntelliJ: ejecute la clase `resourcemanager.ServerLauncher`.

Desde la terminal:

```bash
mvn compile exec:java -Dexec.mainClass=resourcemanager.ServerLauncher
```

Debe aparecer `Esperando clientes en el puerto 5000...`. Si MySQL no está disponible o faltan las credenciales, las operaciones responden con un error claro al cliente.

### 4.2 Iniciar el Frontend

Desde IntelliJ: ejecute `resourcemanager.ClientLauncher`.

Desde la terminal:

```bash
mvn javafx:run
```

Para abrir **varios Frontend** al mismo tiempo, inicie el comando varias veces (una terminal por cliente) o duplique la configuración de ejecución. Para probar las alertas inicie sesión con **dos funcionarios distintos** (por ejemplo `user123` y `123`): al reservar con uno, el otro recibe la alerta.

### 4.3 Frontend y Backend en máquinas separadas

1. En la máquina del servidor: inicie el Backend y permita el puerto (por defecto `5000/TCP`) en el firewall.
2. En cada máquina cliente defina el host del servidor antes de iniciar:

```bash
export RESOURCEMANAGER_HOST=192.168.1.50   # IP de la máquina del servidor
export RESOURCEMANAGER_PORT=5000
mvn javafx:run
```

Solo el servidor necesita acceso a MySQL; los clientes no.

## 5. Funcionalidades

| Módulo | Qué incluye |
|---|---|
| Ingreso y cambio de clave | Login, cambio de clave desde la pantalla de ingreso y desde la principal, cambio obligatorio en el primer ingreso |
| Reservas | Crear por categoría (el sistema asigna el primer recurso libre), cancelar, imprimir PDF, **llenado con IA** a partir de una frase |
| Funcionarios *(admin)* | Agregar, buscar por ID o nombre, modificar, eliminar, imprimir |
| Categorías *(admin)* | Agregar, buscar, modificar, eliminar, imprimir |
| Recursos *(admin)* | Agregar, buscar, modificar, eliminar, imprimir |
| Calendarización | Matriz de horas por recurso para una categoría y fecha, con impresión |
| Actividades | Matriz semanal de reservas, con impresión |
| Estadísticas | Recursos usados por categoría y reservas por semana, con gráfico e impresión |
| Alertas *(funcionario)* | Área fija a la derecha, siempre visible; botón **ver** (detalle) y **descartar** (con confirmación, sin avisar al Backend) |

**Reglas de las reservas** (las valida el servidor): descripción de 1 a 60 caracteres, no pueden estar en el pasado, deben empezar y terminar el mismo día (la hora de fin puede ser hasta las 24:00) y una reserva que termina a las 10:00 no choca con otra que empieza a las 10:00 sobre el mismo recurso.

## 6. Pruebas (JUnit Jupiter)

| Tipo | Plugin | Archivos | Necesitan MySQL |
|---|---|---|---|
| **Unitarias** | Surefire | `*Test.java` | No |
| **Integración** | Failsafe | `*IT.java` | Sí (`resource_db_test`) |

```bash
# solo unitarias
mvn test

# unitarias + integración (requiere la base resource_db_test y las variables RESOURCEMANAGER_DB_USER / _PASSWORD)
mvn verify

# todo menos las pruebas de integración
mvn verify -DskipITs
```

Para apuntar las pruebas de integración a otra base o servidor:

```bash
mvn verify -Dresourcemanager.test.db.url="jdbc:mysql://localhost:3306/resource_db_test?useSSL=false&allowPublicKeyRetrieval=true"
```

Qué cubren:

- **Unitarias**: política y hash de contraseñas, validaciones de usuarios, categorías, reservas y estadísticas, filtrado por fechas, serialización de lo que viaja por el socket y comunicación del Proxy.
- **Integración**: lógica contra la base real (usuarios, categorías, recursos, reservas, estadísticas, impresión de PDF) y el servicio completo con sesión, permisos y **alertas entre dos usuarios**.

## 7. Estructura del proyecto

```
src/main/java/resourcemanager
├── ClientLauncher.java / ServerLauncher.java      puntos de entrada
├── shared/                                        lo que comparten Frontend y Backend
│   ├── service/BackendService.java                contrato del Service (Proxy)
│   ├── model/ (entidades, dto, socket)            datos que viajan por la red
│   └── NetworkConfig.java, Limits.java            configuración y límites
├── client/                                        FRONTEND
│   ├── ServerConnectionProxy.java                 Proxy: envía las peticiones por el socket
│   ├── AlertClient.java                           canal de alertas asincrónicas (se reconecta solo)
│   ├── Async.java                                 ejecuta la red fuera del hilo de JavaFX
│   ├── service/                                   servicios que usan los controladores
│   └── ui/                                        controladores (MVC)
└── server/                                        BACKEND
    ├── Server.java, ClientResponder.java          escucha y atiende un hilo por cliente
    ├── BackendServiceImpl.java                    Service real: sesión y permisos
    ├── AlertBroadcaster.java                      envío de alertas a los demás clientes
    ├── logic/                                     reglas de negocio
    ├── data/                                      acceso a MySQL (JDBC)
    └── ai/GeminiService.java                      llamada a la IA
src/main/resources/resourcemanager/ui              pantallas FXML
src/main/resources/resourcemanager/images          íconos
src/test/java                                      pruebas unitarias e integración
```

## 8. Solución de problemas

| Síntoma | Causa probable |
|---|---|
| "No se pudo conectar con el servidor (host:puerto)" | El Backend no está iniciado, `RESOURCEMANAGER_HOST/PORT` no coinciden o el firewall bloquea el puerto |
| "Faltan las credenciales de la base de datos" | Defina `RESOURCEMANAGER_DB_USER` y `RESOURCEMANAGER_DB_PASSWORD` en el entorno del **servidor** |
| "No se pudo completar la operación por un problema con la base de datos" | MySQL apagado, base inexistente o credenciales incorrectas (el detalle está en la consola del servidor) |
| "El servidor no tiene configurada la variable GEMINI_API_KEY" | Defina `GEMINI_API_KEY` en la máquina del servidor |
| "Su sesión no es válida o expiró" | El servidor se reinició; vuelva a iniciar sesión |
| Las pruebas de integración dicen que solo corren en una BD `_test` | `resourcemanager.db.url` no apunta a `resource_db_test` |
| Las pruebas de integración fallan todas | Falta crear `resource_db_test`, sus tablas o los permisos (ver sección 2.2) |
| El área de alertas dice "Sin conexión con el servidor. Reintentando..." | Se perdió la conexión; el canal se reconecta solo cuando el servidor vuelve |
