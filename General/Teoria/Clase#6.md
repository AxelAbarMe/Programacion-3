# Patrón de Arquitectura MVC

> MVC es una forma de programar, no es una herramienta ni una solución en sí misma; es un patrón de organización del código.

MVC pertenece al **frontend**: tiene interacción directa con el usuario, y es peligroso manejar bases de datos u otro tipo de información sensible directamente dentro de las capas de MVC (Vista/Controlador), ya que esto puede generar vulnerabilidades y facilitar hackeos si dicha lógica queda expuesta en el lado del cliente.

## Introducción

Los sistemas necesitan organización en el código. El uso de patrones permite evitar el **código espagueti** y el desorden, creando sistemas más fáciles de mantener.

> Las páginas web y otros sistemas, al manejar HTML, deben mantenerse solamente con código de etiquetas; nunca se debe implementar código fuente (lógica de negocio, consultas a bases de datos, etc.) dentro del HTML.

* El **código espagueti** ocurre cuando el desorden llega a líneas de código extensas, mezclando diferentes lenguajes en un mismo lugar, generando una vulnerabilidad enorme en el sistema.
* Para conectar código de diversas fuentes (por ejemplo, HTML con Java, o una Vista con su Controller), se procede a través de **referencias** entre los diferentes lenguajes o capas, nunca mezclando su código directamente.

**MVC** es el patrón que separa **Datos (Modelo)**, **Interfaz (Vista)** y **lógica de control (Controlador)**.

* **Objetivo:** lograr sistemas escalables y mantenibles.
* Es usado tanto en aplicaciones web como de escritorio; favorece el trabajo en equipo, ya que distintos desarrolladores pueden trabajar en Modelo, Vista y Controlador de forma simultánea sin pisarse el trabajo.
* **Frameworks conocidos que lo implementan:** Spring MVC, Django, ASP.NET, Ruby on Rails.

---

## Modelo

Administra los datos y las reglas asociadas a ellos.

* **Ejemplo:** una clase `Cliente`, que representa la estructura de la tabla `Usuarios` de la base de datos.
* El Modelo **no se encarga de transferir los datos**; únicamente define su estructura y las reglas que estos deben cumplir.

```java
public class Cliente {
    private String nombre;
    private String correo;

    public Cliente(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    // getters y setters
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}
```

## Vista

Muestra información al usuario; es independiente de la lógica y contiene los elementos gráficos (en JavaFX, correspondería al archivo `.fxml`).

```xml
<VBox fx:controller="app.LoginController" xmlns:fx="http://javafx.com/fxml">
    <TextField fx:id="txtUsuario" promptText="Usuario"/>
    <PasswordField fx:id="txtPassword" promptText="Contraseña"/>
    <Button text="Ingresar" onAction="#iniciarSesion"/>
</VBox>
```

## Controlador

Actúa como intermediario entre la Vista y el Modelo; procesa las entradas del usuario.

```java
public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    @FXML
    private void iniciarSesion(ActionEvent event) {
        Cliente cliente = new Cliente(txtUsuario.getText(), null);
        // El Controlador delega la validación real a la capa de Lógica
        boolean exito = ServicioLogin.validar(cliente, txtPassword.getText());
        // ... según el resultado, actualiza la Vista
    }
}
```

> Un **Modelo** son los datos (la clase o definición del objeto como tal, por ejemplo `Cliente`), mientras que el **Controlador** sería la implementación de sus funcionalidades: listas y métodos especializados que manejan los datos serializados del Modelo, y que son precisamente los que finalmente pasan a la Vista en su parte gráfica.

---

## Flujo completo: Ejemplo de un sistema de Login

A continuación se describe, paso a paso, cómo viaja la información a través de todas las capas (MVC + Datos + Lógica + Servicios) en un sistema de login, y luego se ejemplifica con pequeños fragmentos de código.

1. La **Vista** envía los primeros datos ingresados por el usuario (usuario y contraseña).
2. Dichos datos se transforman, a través del **Modelo**, en un **DTO**: la estructura que se manejará de los datos dentro del sistema.
3. Se comprueba a través del **Controlador**, que valida (a nivel de formulario) los datos ingresados —por ejemplo, que los campos no estén vacíos— antes de continuar.
4. Al llegar a la capa de **Lógica**, se deben preparar los datos con base en las reglas de negocio; es decir, transformarlos en datos procesables por la base de datos.
5. Esos datos se envían a la capa de **Datos**, que debe insertar dicha información, o bien traer la información de login desde la base de datos para compararla.
6. La respuesta de la base de datos se envía de vuelta a la capa de **Datos**, y esta únicamente la envía a la siguiente capa: **Lógica**.
7. La **Lógica** transforma esa respuesta en datos lógicos, quitando cualquier rastro de SQL o de la estructura interna de la base de datos.
8. Los **Servicios** solo ajustan hacia dónde deben dirigirse los datos, y los envían al **Controlador**.
9. El **Controlador** valida la información recibida para entregarle a la **Vista** los datos ya visibles: ya sea un login exitoso, o un mensaje de fallo crítico en el sistema.

> La base de datos también puede realizar sus propias validaciones internas (por ejemplo, restricciones de integridad, tipos de dato, llaves únicas) antes de enviar la respuesta hacia arriba en la cadena.

### Ejemplo simplificado del flujo en código

```java
// 1-3. Vista -> Controlador -> Modelo (DTO)
public class LoginController {
    @FXML
    private void iniciarSesion(ActionEvent event) {
        LoginDTO datos = new LoginDTO(txtUsuario.getText(), txtPassword.getText());
        ResultadoLoginDTO resultado = servicioLogin.autenticar(datos); // 8-9. Vía Servicios
        if (resultado.isExitoso()) {
            lblMensaje.setText("Bienvenido, " + resultado.getNombreUsuario());
        } else {
            lblMensaje.setText("Credenciales inválidas");
        }
    }
}

// 4-6. Lógica: aplica reglas de negocio y delega a Datos
public class LogicaLogin {
    public ResultadoLoginDTO autenticar(LoginDTO datos) {
        if (datos.getUsuario().isBlank()) {
            return new ResultadoLoginDTO(false, null); // Regla de negocio simple
        }
        UsuarioEntity entidad = capaDatos.buscarUsuario(datos.getUsuario()); // 6. Datos -> Lógica
        boolean valido = entidad != null && entidad.getPassword().equals(datos.getPassword());
        return new ResultadoLoginDTO(valido, entidad != null ? entidad.getNombre() : null);
    }
}

// 5. Datos: única capa que conoce la base de datos
public class CapaDatos {
    public UsuarioEntity buscarUsuario(String usuario) {
        // SELECT * FROM Usuarios WHERE usuario = ?
        return repositorio.findByUsuario(usuario);
    }
}
```

> Nótese cómo `LoginDTO` y `ResultadoLoginDTO` viajan entre capas transportando únicamente datos (sin lógica), mientras que `UsuarioEntity` (equivalente al dataset "crudo" de la base de datos) nunca sale de la capa de Datos ni de Lógica; el Controlador y la Vista jamás lo conocen directamente.

---

## Capa de Servicios: el puente entre Frontend y Backend

La capa de servicios existe para **dividir el frontend del backend**.

* Dicha capa puede conectar a diferentes Vistas (interfaces): la misma Lógica y los mismos Datos pueden servir tanto a una app de escritorio en JavaFX, como a una app web, como a una app móvil.
* Los **Servicios** son la capa más importante en cuanto a seguridad, porque **protege al backend** y permite usar el frontend de forma eficiente.
* Permite conectar diferentes Vistas ubicadas en diferentes lugares físicos; un ejemplo real de esto es **Amazon**, que mantiene sus servicios distribuidos en distintas partes del mundo (servidores/regiones), a los cuales se conectan múltiples interfaces (web, app móvil, apps de terceros) sin que cada una necesite reimplementar la lógica de negocio.

```java
// La capa de Servicios expone un único punto de entrada,
// sin importar si quien llama es JavaFX, una web o una app móvil
public class ServicioLogin {
    private static LogicaLogin logica = new LogicaLogin();

    public static ResultadoLoginDTO autenticar(LoginDTO datos) {
        return logica.autenticar(datos); // Simplemente reenvía a Lógica
    }
}
```

> **Vista** y **Controlador** son frontend; al trabajar con **sockets**, se puede llegar a manejar dos aplicaciones completamente diferentes —una de frontend y otra de backend— que se comuniquen entre sí como proyectos independientes, en lugar de tenerlo todo compilado dentro de una sola aplicación monolítica. Esto conecta directamente con lo visto en arquitectura de software: pasar de un enfoque monolítico a uno donde el frontend (Vista/Controlador) y el backend (Lógica/Servicios/Datos) pueden incluso desplegarse y escalar de forma completamente independiente, tal como ocurre en una arquitectura de microservicios.
