package una.eif206.avi.dto;

public class ConsultaExpertoDTO {

    private String pregunta;
    private String respuestaAVI;

    public ConsultaExpertoDTO(String pregunta, String respuestaAVI) {
        this.pregunta = pregunta;
        this.respuestaAVI = respuestaAVI;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuestaAVI() {
        return respuestaAVI;
    }
}
