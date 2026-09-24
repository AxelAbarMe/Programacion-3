# Bases de datos relacionales y normalización

## 1. Modelo relacional de Codd

| Concepto | Definición |
|---|---|
| **Relación** | Una tabla que representa un conjunto de datos del mismo tipo. |
| **Tupla** | Una fila de la relación: una ocurrencia o registro. |
| **Atributo** | Una columna que describe una propiedad del dato. |
| **Dominio** | Conjunto de valores válidos que puede tomar un atributo. |

> La posición de filas y columnas no define el significado: lo hacen los nombres, dominios y relaciones.

Los datos de las tablas no siempre se repiten. Las tablas deben relacionarse entre sí, ya que si una tabla no se relaciona con ninguna otra, sobra.

---

## 2. Anatomía de una tabla relacional

**Ejemplo: tabla `usuarios`**

| id_usuario | nombre_usuario | nombre | activo |
|---|---|---|---|
| 1 | admin | Administrador | 1 |
| 2 | maria | María López | 1 |
| 3 | carlos | Carlos Rojas | 0 |

| Elemento | Descripción |
|---|---|
| **Clave primaria** | Identifica cada fila de manera única (`id_usuario`). |
| **Atributos** | Describen las propiedades de la entidad (`nombre_usuario`, `nombre`, `activo`). |
| **Dominios** | Definen tipos y valores permitidos (por ejemplo, `activo` solo admite 0 o 1). |

---

## 3. Claves e integridad

| Clave | Función | Ejemplo |
|---|---|---|
| **Clave primaria (PK)** | Identifica de forma única cada fila. | `id_usuario` |
| **Clave foránea (FK)** | Relaciona una tabla con otra; la fila referenciada debe existir sí o sí. En la relación 1:N, la llave foránea siempre va en el lado N. | `id_usuario` en `conversaciones` |
| **Clave única (UQ)** | Evita valores duplicados en un atributo. | `nombre_usuario` |

### Reglas de integridad

Las reglas de integridad tienen como responsabilidad garantizar la consistencia del dato. Un dato no debería cambiar a menos que se generen cambios controlados.

- **Entidad:** una clave primaria no puede ser nula.
- **Referencial:** una FK debe apuntar a una fila existente.
- **Dominio:** cada atributo respeta su tipo y restricciones.

---

## 4. Relaciones entre tablas

| Tipo | Descripción | Ejemplo |
|---|---|---|
| **1:1** | Un registro se relaciona con uno solo. | `USUARIO` – `PERFIL` |
| **1:N** | Un registro se relaciona con muchos. | `USUARIO` – `CONVERSACIÓN` |
| **N:M** | Muchos registros se relacionan con muchos. | `ESTUDIANTE` – `CURSO` |

- En bases de datos relacionales, las relaciones se implementan mediante claves foráneas.
- Una relación N:M normalmente requiere una tabla intermedia.

---

## 5. Del modelo relacional al SQL

SQL permite definir estructuras, consultar datos y mantener las relaciones del modelo.

### DDL: define estructuras

```sql
CREATE TABLE usuarios (
  id_usuario INT PRIMARY KEY AUTO_INCREMENT,
  nombre_usuario VARCHAR(50) UNIQUE NOT NULL
);
```

### DML: manipula los datos

```sql
INSERT INTO usuarios(nombre_usuario)
VALUES ('admin');

SELECT * FROM usuarios;
```

> En Programación III se utilizará MySQL como SGBD y posteriormente JDBC para ejecutar estas operaciones desde Java.

---

## 6. Ejercicios de modelado (relacionar tablas)

Simplificar las tablas mediante el uso de PK y relacionarlas mediante FK.

### 6.1 Laboratorios y equipos

<img width="1280" height="720" alt="image" src="https://github.com/user-attachments/assets/7deeb897-d3f6-4bce-be52-2bcb7390482e" />

| Tabla | Atributos | Relación |
|---|---|---|
| **LABORATORIO** | ID (PK), NUM_LAB, FK_UBICACION, ESTADO | N:1 con UBICACION; 1:N con EQUIPOS |
| **UBICACION** | ID (PK), DETALLE | 1:N con LABORATORIO |
| **LICITACION** | ID (PK), NUMERO | 1:N con EQUIPOS |
| **EQUIPOS** | ID (PK), FK_MODELO, ESTADO, FK_LICITACION, FK_LAB | N:1 con MODELO, LICITACION y LABORATORIO |
| **MODELO** | ID (PK), TIPO, CARACTERISTICAS, FK (marca) | N:1 con MARCA; 1:N con EQUIPOS |
| **MARCA** | ID (PK), DETALLE | 1:N con MODELO |

**Datos de ejemplo**

| LABORATORIO | ID | NUM_LAB | FK_UBICACION | ESTADO |
|---|---|---|---|---|
| | 1 | 1001 | 1 | 1 |
| | 2 | 1002 | 1 | 1 |

| LICITACION | ID | NUMERO |
|---|---|---|
| | 1 | 11-2026 |
| | 2 | 08-2025 |

| UBICACION | ID | DETALLE |
|---|---|---|
| | 1 | Primer piso Ed. Benj. Núñez |

| MARCA | ID | DETALLE |
|---|---|---|
| | 1 | Dell |
| | 2 | HP |
| | 3 | Lenovo |

| MODELO | ID | TIPO | CARACTERISTICAS | FK |
|---|---|---|---|---|
| | 1 | Desktop | Design | 1 |
| | 2 | Thinkpad | Laptop | 3 |

| EQUIPOS | ID | FK_MODELO | ESTADO | FK_LICITACION | FK_LAB |
|---|---|---|---|---|---|
| | 1 | 1 | 1 | 1 | 1 |
| | 2 | 1 | 1 | 2 | 2 |
| | 3 | 2 | 1 | 1 | 1 |

### 6.2 Funcionarios y puestos

Relación N:M resuelta con la tabla intermedia **NOMBRAMIENTO**.

| Tabla | Atributos | Relación |
|---|---|---|
| **FUNCIONARIO** | ID (PK), NOM, P, DUR. | 1:N con NOMBRAMIENTO |
| **NOMBRAMIENTO** | ID (PK), FK_FUN, FK_PUE, ESTADO | N:1 con FUNCIONARIO y con PUESTO |
| **PUESTO** | ID (PK), NUM_PUESTO, DESC | 1:N con NOMBRAMIENTO |

**Datos de ejemplo**

| FUNCIONARIO | ID | NOM | P | DUR. |
|---|---|---|---|---|
| | 1 | Manuel | 1 | 2025-2026 |
| | 2 | Miguel | 2 | 2019-2024 |
| | 3 | Rosa | 1 | 2026- |

| PUESTO | ID | NUM_PUESTO | DESC |
|---|---|---|---|
| | 1 | 01-115 | ING |
| | 2 | 02-226 | DOC |

<img width="4000" height="2252" alt="image" src="https://github.com/user-attachments/assets/1f8f794f-f361-4cc8-b5c7-3b6569376a9a" />

---

## 7. Resumen expandido

### Fundamentos del modelo relacional
El modelo relacional, propuesto por Codd, organiza la información en **relaciones** (tablas) formadas por **tuplas** (filas) y **atributos** (columnas). Cada atributo toma sus valores de un **dominio**, es decir, el conjunto de valores válidos. El significado de los datos no depende de la posición de filas o columnas, sino de los nombres, dominios y relaciones definidos.

### Relación entre tablas
Los datos de las tablas no siempre se repiten, y las tablas deben estar relacionadas entre sí; una tabla aislada carece de utilidad dentro del modelo. Las relaciones pueden ser:
- **1:1:** un registro se asocia con uno solo.
- **1:N:** un registro se asocia con muchos; la llave foránea siempre va en el lado N.
- **N:M:** muchos con muchos; se resuelve con una tabla intermedia que contiene las FK de ambas tablas (como NOMBRAMIENTO entre FUNCIONARIO y PUESTO).

### Claves
- **Clave primaria (PK):** identifica de forma única cada fila y se usa para simplificar las tablas y evitar repetir datos.
- **Clave foránea (FK):** vincula una tabla con otra; la fila a la que apunta debe existir obligatoriamente.
- **Clave única (UQ):** impide valores repetidos en un atributo que no es la PK.

### Integridad de datos
Las reglas de integridad tienen la responsabilidad de mantener la **consistencia**. Un dato no debería cambiar salvo que se generen modificaciones controladas.
- **Integridad de entidad:** la PK nunca es nula.
- **Integridad referencial:** toda FK apunta a una fila existente.
- **Integridad de dominio:** cada atributo respeta su tipo y restricciones.

### SQL y aplicación en el curso
SQL permite definir estructuras (**DDL**, como `CREATE TABLE`) y manipular datos (**DML**, como `INSERT` y `SELECT`), manteniendo las relaciones del modelo. En Programación III se usará **MySQL** como SGBD y **JDBC** para ejecutar estas operaciones desde Java.
