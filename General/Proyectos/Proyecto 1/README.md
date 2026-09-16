# ResourceManager — Sistema de Reservas

Sistema de escritorio para la gestión de usuarios, categorías, recursos y reservas de espacios/equipos, desarrollado en **Java + JavaFX** siguiendo el patrón de arquitectura **MVC en capas**. Incluye generación de reportes en PDF y creación de reservas asistida por **IA (Gemini)**.

---

Tienes razón, estaban mal codificados (con `%C3%AD` en vez de la tilde real). GitHub genera los anchors con los caracteres Unicode tal cual, no con percent-encoding. Aquí está corregido:

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías utilizadas](#-tecnologías-utilizadas)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Arquitectura](#-arquitectura)
- [Requisitos previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Pruebas](#-pruebas)
- [Persistencia de datos](#-persistencia-de-datos)
- [Autor](#-autor)

---

## ✨ Características

- Gestión de usuarios (crear, editar, eliminar, búsqueda por id/nombre).
- Gestión de categorías y recursos reservables.
- Creación de reservas de forma manual o **mediante lenguaje natural con IA** (Gemini interpreta el texto y sugiere fecha, horario y categoría).
- Generación de reportes en **PDF** (tablas genéricas por reflexión, con soporte de gráficos e impresión directa).
- Estadísticas de uso por categoría y por semana.
- Persistencia en archivos **XML** mediante `Jackson XmlMapper` + `StAX`.
- Suite de pruebas unitarias y de integración con **JUnit 5**.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|:--|:--|
| **Java 26** | Lenguaje principal |
| **JavaFX 21** | Interfaz gráfica (Vista + Controller) |
| **Maven** | Gestión de dependencias y build |
| **Jackson (XmlMapper + JSR310)** | Serialización/deserialización XML |
| **org.json** | Manejo de JSON para el consumo del API de Gemini |
| **Gson** | Manejo de JSON en la capa de Lógica (IA) |
| **Apache PDFBox / OpenPDF** | Generación de reportes en PDF |
| **JUnit 5 (Jupiter)** | Pruebas unitarias y de integración |
| **Maven Surefire / Failsafe** | Ejecución separada de pruebas unitarias e integración |
| **API de Gemini (Google)** | Interpretación de reservas en lenguaje natural |

---

## 📁 Estructura del proyecto

```
Proyecto1Progra3/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── resourcemanager/
    │   │       ├── Launcher.java              ← Punto de entrada (extends Application)
    │   │       ├── data/                      ← Capa de Datos (persistencia XML)
    │   │       │   ├── DataPaths.java
    │   │       │   ├── MapperSingleton.java
    │   │       │   ├── LoadFromXML.java
    │   │       │   └── SaveToXML.java
    │   │       ├── model/                     ← Modelo / entidades
    │   │       │   ├── User.java
    │   │       │   ├── Resource.java
    │   │       │   ├── Category.java
    │   │       │   ├── Reservation.java
    │   │       │   ├── dto/                   ← DTOs (transporte entre capas)
    │   │       │   └── tables/                ← Filas auxiliares para TableView
    │   │       ├── logic/                     ← Capa de Lógica (reglas de negocio)
    │   │       │   ├── AuthLogic.java
    │   │       │   ├── UserLogic.java
    │   │       │   ├── CategoryLogic.java
    │   │       │   ├── ResourceLogic.java
    │   │       │   ├── ReservationLogic.java
    │   │       │   ├── StatsLogic.java
    │   │       │   └── PrintLogic.java
    │   │       ├── service/                   ← Capa de Servicios
    │   │       │   ├── AuthService.java
    │   │       │   ├── UserService.java
    │   │       │   ├── ReservationService.java
    │   │       │   ├── GeminiService.java
    │   │       │   └── PrintService.java
    │   │       ├── structure/                 ← Utilidades globales (Singletons)
    │   │       │   ├── AppContext.java
    │   │       │   └── CurrentSession.java
    │   │       └── ui/                        ← Controladores (Vista)
    │   │           ├── LoginController.java
    │   │           ├── MainController.java
    │   │           ├── UserTabController.java
    │   │           ├── CategoryTabController.java
    │   │           ├── ResourcesTabController.java
    │   │           ├── ReservationTabController.java
    │   │           ├── CalendarTabController.java
    │   │           ├── StatsTabController.java
    │   │           ├── ActivityTabController.java
    │   │           └── Utilities.java
    │   └── resources/
    │       └── resourcemanager/
    │           ├── ui/                        ← Archivos .fxml (Scene Builder)
    │           ├── images/                    ← Ícono de la aplicación
    │           └── data/                      ← Archivos XML (usuarios, recursos, etc.)
    └── test/
        └── java/
            └── resourcemanager/
                ├── logic/                     ← *Test.java (unitarias) y *IT.java (integración)
                ├── service/
                └── testsupport/               ← Utilidades compartidas para pruebas
```

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura **MVC en capas**, donde cada una tiene una única responsabilidad:

```
UI (Vista/Controller) → Servicios → Lógica → Datos → Archivos XML
```

| Capa | Responsabilidad |
|:--|:--|
| **UI** | Controladores JavaFX; capturan eventos (`setOnAction`) y muestran datos/alertas |
| **Servicios** | Punto de entrada único hacia la Lógica; no valida nada |
| **Lógica** | Reglas de negocio, validaciones y mapeo hacia/desde DTO |
| **Datos** | Lectura/escritura de los archivos XML mediante Jackson + StAX |
| **DTO** | Objetos de transporte de datos entre capas |

> La capa de Datos es la única que conoce la ubicación y el formato de los archivos XML; el resto del sistema solo trabaja con objetos DTO y del Modelo.

---

## ✅ Requisitos previos

- **JDK 21 o superior** instalado y configurado en el `PATH`.
- **Maven 3.9+**.
- Conexión a internet (solo necesaria para la funcionalidad de reservas por IA).
- Una **API Key de Google Gemini** (opcional, solo si se desea usar la creación de reservas asistida por IA).

---

## 📦 Instalación

1. Clonar el repositorio:

   ```bash
   git clone <url-del-repositorio>
   cd Proyecto1Progra3
   ```

2. Instalar las dependencias con Maven:

   ```bash
   mvn clean install
   ```

---

## ⚙️ Configuración

### Variable de entorno para IA (opcional)

Para habilitar la creación de reservas mediante lenguaje natural, se debe configurar la variable de entorno `GEMINI_API_KEY` **antes** de ejecutar la aplicación:

**Windows (PowerShell):**
```powershell
$Env:GEMINI_API_KEY="tu-clave-aqui"
```

**Linux / macOS:**
```bash
export GEMINI_API_KEY="tu-clave-aqui"
```

> ⚠️ La clave **nunca** debe escribirse directamente en el código fuente. Si la variable no está definida, el resto de la aplicación funciona con normalidad; únicamente la opción "Extraer con IA" mostrará un error al intentar usarse.

---

## ▶️ Ejecución

**Desde Maven (recomendado):**

```bash
mvn javafx:run
```

**Desde el IDE (IntelliJ IDEA):**

Ejecutar la clase `resourcemanager.Launcher`, asegurándose de que el SDK de JavaFX esté configurado en *Project Structure → Libraries*.

---

## 🧪 Pruebas

El proyecto separa las pruebas **unitarias** (`*Test.java`) de las pruebas de **integración** (`*IT.java`):

```bash
# Solo pruebas unitarias (rápidas, no tocan archivos ni red)
mvn test

# Pruebas unitarias + integración (archivos XML reales, generación de PDF, etc.)
mvn verify
```

| Plugin | Ejecuta | Convención |
|:--|:--|:--|
| **Surefire** | Pruebas unitarias | `*Test.java` |
| **Failsafe** | Pruebas de integración | `*IT.java` |

> Las pruebas de integración usan una carpeta de datos separada (`target/test-data`) mediante la propiedad de sistema `resourcemanager.dataDir`, por lo que **no afectan los archivos XML reales** de la aplicación.

---

## 💾 Persistencia de datos

Toda la información se almacena en archivos **XML** ubicados en `src/main/resources/resourcemanager/data/`:

| Archivo | Contenido |
|:--|:--|
| `users.xml` | Usuarios del sistema |
| `resources.xml` | Recursos reservables |
| `categories.xml` | Categorías de recursos |
| `reservations.xml` | Reservas realizadas |

La serialización/deserialización se realiza mediante `Jackson XmlMapper`, configurado como instancia única (*Singleton*) en `MapperSingleton`, garantizando un formato de fechas (`LocalDate`) consistente en toda la aplicación.

---

## 👤 Autor

**Axel Abarca y Daniel Muñoz**
Proyecto realizado para el curso de Programación III.
