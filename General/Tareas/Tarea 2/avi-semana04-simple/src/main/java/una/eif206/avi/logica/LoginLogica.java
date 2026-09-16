package una.eif206.avi.logica;

import una.eif206.avi.datos.LoginJsonDatos;
import una.eif206.avi.datos.UsuarioJsonDatos;
import una.eif206.avi.datos.UsuarioXmlDatos;
import una.eif206.avi.dto.LoginDTO;
import una.eif206.avi.dto.UsuarioDTO;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoginLogica {

    public static final String FUENTE_JSON = "JSON";
    public static final String FUENTE_XML = "XML";
    private static final String RESULTADO_EXITOSO = "Exitoso";
    private static final String RESULTADO_FALLIDO = "Fallido";

    private final UsuarioJsonDatos jsonDatos;
    private final UsuarioXmlDatos xmlDatos;
    private final LoginJsonDatos bitacoraDatos;

    public LoginLogica() {
        this.jsonDatos = new UsuarioJsonDatos(Path.of("data", "usuarios.json"));
        this.xmlDatos = new UsuarioXmlDatos(Path.of("data", "usuarios.xml"));
        this.bitacoraDatos = new LoginJsonDatos(Path.of("data", "bitacora.json"));
    }

    public void inicializarDatos() throws Exception {
        inicializarArchivos();
    }

    private void inicializarArchivos() throws Exception {
        UsuarioDTO usuarioInicial = new UsuarioDTO("admin", "1234");
        List<UsuarioDTO> usuarios = List.of(usuarioInicial);

        if (!jsonDatos.existeArchivo()) {
            jsonDatos.guardarUsuarios(usuarios);
        }

        if (!xmlDatos.existeArchivo()) {
            xmlDatos.guardarUsuarios(usuarios);
        }
    }

    public boolean iniciarSesion(String usuario, String contrasena, String fuente) throws Exception {
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar el usuario.");
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar la contraseña.");
        }

        if (fuente == null || fuente.isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar JSON o XML.");
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.trim(), contrasena);
        boolean credencialesValidas = validarCredenciales(usuarioDTO, fuente);
        registrarIntento(usuarioDTO.getUsuario(), fuente, credencialesValidas);
        return credencialesValidas;
    }

    private boolean validarCredenciales(UsuarioDTO usuario, String fuente) throws Exception {
        if (FUENTE_JSON.equalsIgnoreCase(fuente)) {
            return jsonDatos.validarCredenciales(usuario.getUsuario(), usuario.getContrasena());
        }

        if (FUENTE_XML.equalsIgnoreCase(fuente)) {
            return xmlDatos.validarCredenciales(usuario.getUsuario(), usuario.getContrasena());
        }

        throw new IllegalArgumentException("Fuente de datos no válida.");
    }

    private void registrarIntento(String usuario, String fuente, boolean exitoso) throws Exception {
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora = ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String resultado = exitoso ? RESULTADO_EXITOSO : RESULTADO_FALLIDO;

        bitacoraDatos.registrarIntento(new LoginDTO(usuario, fecha, hora, fuente, resultado));
    }

    public void guardarUsuariosEnAmbasFuentes(List<UsuarioDTO> usuarios) throws Exception {
        jsonDatos.guardarUsuarios(usuarios);
        xmlDatos.guardarUsuarios(usuarios);
    }
}