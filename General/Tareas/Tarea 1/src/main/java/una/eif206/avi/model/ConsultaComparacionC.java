package una.eif206.avi.model;

public class ConsultaComparacionC extends ConsultaTema {

    public ConsultaComparacionC() {
        super("Comparacion Java vs C++");
    }

    @Override
    public String getPrompt() {
        return "Compara Java con C++: menciona tres diferencias clave entre ambos lenguajes.";
    }
}