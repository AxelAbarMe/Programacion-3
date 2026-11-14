package una.eif206.avi.dto;

/**
 * DTO utilizado para transportar los datos de un usuario entre las capas.
 */
public class UsuarioDTO {
    private String usuario;
    private String contrasena;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
