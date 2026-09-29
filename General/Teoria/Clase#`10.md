MySQL Installer ConnectorJ Platform Independent. MySQL Workbench Installer 8.0

Dirigirse a Schemes, un Esquema es una base de datos, crear nueva scheme. Aplicar el schema creado y crearlo.

En Query se utiliza

```SQL
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

INSERT INTO usuario (nombre_usuario, contrasena) VALUES ('admin', 'admin')
```
