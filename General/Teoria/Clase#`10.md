MySQL Installer ConnectorJ Platform Independent. MySQL Workbench Installer 8.0

Dirigirse a Schemes, un Esquema es una base de datos, crear nueva scheme. Aplicar el schema creado y crearlo.

En Query se utiliza

```SQL
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


SQL_FILE

CREATE USER IF NOT EXISTS 'avi_app' @'localhost'
IDENTIFIED BY 'Avi2026';

GRANT SELECT, INSERT, UPDATE, DELETE
ON avi_db.*
TO 'avi_app'@'localhost';

FLUSH PRIVILEGES;
```

> \* es traer todas las filas y columnas que existen
