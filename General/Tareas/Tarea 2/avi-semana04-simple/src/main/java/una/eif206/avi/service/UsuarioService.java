package una.eif206.avi.service;

import una.eif206.avi.dto.UsuarioDTO;
import una.eif206.avi.logica.LoginLogica;

import java.nio.file.Path;
import java.util.List;

/**
 * Capa de servicios: comunica la lógica con las fuentes de datos.
 */
public class UsuarioService {

    private LoginLogica logica;

    public UsuarioService() {
        this.logica = new LoginLogica();
    }

    public boolean iniciarSesion(String usuario, String contrasena, String fuente) throws Exception {
        return logica.iniciarSesion(usuario, contrasena, fuente);
    }

    public void guardarUsuariosEnAmbasFuentes(List<UsuarioDTO> usuarios) throws Exception {
        logica.guardarUsuariosEnAmbasFuentes(usuarios);
    }

    public void inicializarDatos() throws Exception {
        logica.inicializarDatos();
    }
}
