package una.eif206.avi.model;

public class ConsultaHistoriaJava extends ConsultaTema {

    public ConsultaHistoriaJava() {
        super("Historia de Java");
    }

    @Override
    public String getPrompt() {
        return "Explica brevemente la historia de Java: quien lo creo, en que año surgio, y por que se creo originalmente.";
    }
}