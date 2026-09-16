package una.eif206.avi.datos;

import org.json.JSONArray;
import org.json.JSONObject;
import una.eif206.avi.dto.LoginDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class LoginJsonDatos {

    private final Path rutaArchivo;

    public LoginJsonDatos(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void registrarIntento(LoginDTO intento) throws IOException {
        List<LoginDTO> intentos = leerIntentos();
        intentos.add(intento);
        guardarIntentos(intentos);
    }

    public List<LoginDTO> leerIntentos() throws IOException {
        List<LoginDTO> intentos = new ArrayList<>();

        if (!Files.exists(rutaArchivo)) {
            return intentos;
        }

        String contenido = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
        JSONObject raiz = new JSONObject(contenido);
        JSONArray arreglo = raiz.optJSONArray("intentos");

        if (arreglo == null) {
            return intentos;
        }

        for (int i = 0; i < arreglo.length(); i++) {
            JSONObject objeto = arreglo.getJSONObject(i);
            intentos.add(new LoginDTO(
                    objeto.optString("usuario", ""),
                    objeto.optString("fecha", ""),
                    objeto.optString("hora", ""),
                    objeto.optString("fuente", ""),
                    objeto.optString("resultado", "")
            ));
        }

        return intentos;
    }

    private void guardarIntentos(List<LoginDTO> intentos) throws IOException {
        crearDirectorioSiEsNecesario();

        JSONArray arreglo = new JSONArray();
        for (LoginDTO intento : intentos) {
            JSONObject objeto = new JSONObject();
            objeto.put("usuario", intento.getUsuario());
            objeto.put("fecha", intento.getFecha());
            objeto.put("hora", intento.getHora());
            objeto.put("fuente", intento.getFuente());
            objeto.put("resultado", intento.getResultado());
            arreglo.put(objeto);
        }

        JSONObject raiz = new JSONObject();
        raiz.put("intentos", arreglo);

        Files.writeString(
                rutaArchivo,
                raiz.toString(4),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    private void crearDirectorioSiEsNecesario() throws IOException {
        Path directorio = rutaArchivo.getParent();
        if (directorio != null) {
            Files.createDirectories(directorio);
        }
    }
}