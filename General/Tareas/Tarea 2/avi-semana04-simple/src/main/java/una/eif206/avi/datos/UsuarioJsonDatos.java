package una.eif206.avi.datos;

import org.json.JSONArray;
import org.json.JSONObject;
import una.eif206.avi.dto.UsuarioDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa de datos para lectura y escritura de usuarios en formato JSON.
 */
public class UsuarioJsonDatos {

    private final Path rutaArchivo;

    public UsuarioJsonDatos(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public boolean existeArchivo() {
        return Files.exists(rutaArchivo);
    }

    public void guardarUsuarios(List<UsuarioDTO> usuarios) throws IOException {
        crearDirectorioSiEsNecesario();

        JSONArray arreglo = new JSONArray();
        for (UsuarioDTO usuario : usuarios) {
            JSONObject objeto = new JSONObject();
            objeto.put("usuario", usuario.getUsuario());
            objeto.put("contrasena", usuario.getContrasena());
            arreglo.put(objeto);
        }

        JSONObject raiz = new JSONObject();
        raiz.put("usuarios", arreglo);

        Files.writeString(
                rutaArchivo,
                raiz.toString(4),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public List<UsuarioDTO> leerUsuarios() throws IOException {
        List<UsuarioDTO> usuarios = new ArrayList<>();

        if (!Files.exists(rutaArchivo)) {
            return usuarios;
        }

        String contenido = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
        JSONObject raiz = new JSONObject(contenido);
        JSONArray arreglo = raiz.optJSONArray("usuarios");

        if (arreglo == null) {
            return usuarios;
        }

        for (int i = 0; i < arreglo.length(); i++) {
            JSONObject objeto = arreglo.getJSONObject(i);
            usuarios.add(new UsuarioDTO(
                    objeto.optString("usuario", ""),
                    objeto.optString("contrasena", "")
            ));
        }

        return usuarios;
    }

    public boolean validarCredenciales(String usuario, String contrasena) throws IOException {
        return leerUsuarios().stream()
                .anyMatch(u -> u.getUsuario().equals(usuario)
                        && u.getContrasena().equals(contrasena));
    }

    private void crearDirectorioSiEsNecesario() throws IOException {
        Path directorio = rutaArchivo.getParent();
        if (directorio != null) {
            Files.createDirectories(directorio);
        }
    }
}
