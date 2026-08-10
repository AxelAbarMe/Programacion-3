package una.eif206.avi.model;

public abstract class ConsultaTema {

    private String nombre;

    protected ConsultaTema(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract String getPrompt();
}
