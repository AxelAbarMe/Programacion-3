package una.eif206.avi.logica;

import una.eif206.avi.datos.UsuarioJsonDatos;
import una.eif206.avi.datos.UsuarioXmlDatos;
import una.eif206.avi.datos.UsuarioDatos;
import una.eif206.avi.dto.UsuarioDTO;

import java.nio.file.Path;
import java.util.List;

/**
 * Capa de lógica: aplica las reglas de negocio del inicio de sesión.
 */
public class LoginLogica {

    public static final String FUENTE_JSON = "JSON";
    public static final String FUENTE_XML = "XML";

    private final UsuarioJsonDatos jsonDatos;
    private final UsuarioXmlDatos xmlDatos;

    private final UsuarioDatos usuarioDatos;

    public LoginLogica() {
        this.jsonDatos = new UsuarioJsonDatos(Path.of("data", "usuarios.json"));
        this.xmlDatos = new UsuarioXmlDatos(Path.of("data", "usuarios.xml"));

        this.usuarioDatos = new UsuarioDatos();
    }

    public boolean iniciarSesionBD(String usuario, String contrasena) throws Exception {
        if(usuario == null || usuario.isBlank())
        {
            throw new IllegalArgumentException("Debe ingresar el usuario.");
        }

        if(contrasena == null || contrasena.isBlank())
        {
            throw new IllegalArgumentException("Debe ingresar la contraseña.");
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.trim(), contrasena);

        return usuarioDatos.validarCredenciales(usuarioDTO);
    }

    public void inicializarDatos() throws Exception {
        inicializarArchivos();
    }

    /**
     * Crea los archivos con un usuario de ejemplo únicamente si todavía no existen.
     */
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
        return validarCredenciales(usuarioDTO, fuente);
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

    public void guardarUsuariosEnAmbasFuentes(List<UsuarioDTO> usuarios) throws Exception {
        jsonDatos.guardarUsuarios(usuarios);
        xmlDatos.guardarUsuarios(usuarios);
    }
}
