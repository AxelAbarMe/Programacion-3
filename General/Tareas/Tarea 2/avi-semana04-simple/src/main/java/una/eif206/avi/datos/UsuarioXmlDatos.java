package una.eif206.avi.datos;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import una.eif206.avi.dto.UsuarioDTO;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa de datos para lectura y escritura de usuarios en formato XML.
 */
public class UsuarioXmlDatos {

    private final Path rutaArchivo;

    public UsuarioXmlDatos(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public boolean existeArchivo() {
        return Files.exists(rutaArchivo);
    }

    public void guardarUsuarios(List<UsuarioDTO> usuarios) throws Exception {
        crearDirectorioSiEsNecesario();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.newDocument();

        Element raiz = documento.createElement("usuarios");
        documento.appendChild(raiz);

        for (UsuarioDTO usuario : usuarios) {
            Element nodoUsuario = documento.createElement("usuario");

            Element nombre = documento.createElement("nombre");
            nombre.setTextContent(usuario.getUsuario());
            nodoUsuario.appendChild(nombre);

            Element contrasena = documento.createElement("contrasena");
            contrasena.setTextContent(usuario.getContrasena());
            nodoUsuario.appendChild(contrasena);

            raiz.appendChild(nodoUsuario);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(documento), new StreamResult(rutaArchivo.toFile()));
    }

    public List<UsuarioDTO> leerUsuarios() throws Exception {
        List<UsuarioDTO> usuarios = new ArrayList<>();

        if (!Files.exists(rutaArchivo)) {
            return usuarios;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(rutaArchivo.toFile());
        documento.getDocumentElement().normalize();

        NodeList nodosUsuario = documento.getElementsByTagName("usuario");
        for (int i = 0; i < nodosUsuario.getLength(); i++) {
            Element elemento = (Element) nodosUsuario.item(i);
            String nombre = obtenerTexto(elemento, "nombre");
            String contrasena = obtenerTexto(elemento, "contrasena");
            usuarios.add(new UsuarioDTO(nombre, contrasena));
        }

        return usuarios;
    }

    public boolean validarCredenciales(String usuario, String contrasena) throws Exception {
        return leerUsuarios().stream()
                .anyMatch(u -> u.getUsuario().equals(usuario)
                        && u.getContrasena().equals(contrasena));
    }

    private String obtenerTexto(Element elemento, String etiqueta) {
        NodeList nodos = elemento.getElementsByTagName(etiqueta);
        return nodos.getLength() > 0 ? nodos.item(0).getTextContent() : "";
    }

    private void crearDirectorioSiEsNecesario() throws IOException {
        Path directorio = rutaArchivo.getParent();
        if (directorio != null) {
            Files.createDirectories(directorio);
        }
    }
}
