package una.eif206.avi.dto;

public class LoginDTO {
    private String usuario;
    private String fecha;
    private String hora;
    private String fuente;
    private String resultado;

    public LoginDTO() {
    }

    public LoginDTO(String usuario, String fecha, String hora, String fuente, String resultado) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.hora = hora;
        this.fuente = fuente;
        this.resultado = resultado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}