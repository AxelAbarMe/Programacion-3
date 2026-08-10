package una.eif206.avi.model;

public class ConsultaCaracteristicasJava extends ConsultaTema {

    public ConsultaCaracteristicasJava() {
        super("Caracteristicas de Java");
    }

    @Override
    public String getPrompt() {
        return "Enumera y explica de forma breve las principales caracteristicas del lenguaje de programacion Java.";
    }
}