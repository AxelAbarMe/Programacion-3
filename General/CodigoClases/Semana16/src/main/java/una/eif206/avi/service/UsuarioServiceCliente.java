package una.eif206.avi.service;

import java.net.URI;
import java.net.URLEncoder;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;

public class UsuarioServiceCliente {

    private static final String SERVIDOR =
            "http://localhost:8080";


    private final HttpClient cliente;


    public UsuarioServiceCliente() {

        cliente =
                HttpClient.newHttpClient();
    }


    public boolean iniciarSesion(
            String usuario,
            String contrasena)
            throws Exception {

        String datos =
                "usuario="
                        + URLEncoder.encode(
                        usuario,
                        StandardCharsets.UTF_8
                )
                        +
                        "&contrasena="
                        + URLEncoder.encode(
                        contrasena,
                        StandardCharsets.UTF_8
                );


        HttpRequest solicitud =
                HttpRequest
                        .newBuilder()
                        .uri(
                                URI.create(
                                        SERVIDOR
                                                + "/usuario/login"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/x-www-form-urlencoded"
                        )
                        .POST(
                                HttpRequest
                                        .BodyPublishers
                                        .ofString(datos)
                        )
                        .build();


        HttpResponse<String> respuesta =
                cliente.send(
                        solicitud,
                        HttpResponse
                                .BodyHandlers
                                .ofString()
                );


        if (respuesta.statusCode() != 200) {

            throw new RuntimeException(
                    "Error del servicio: "
                            + respuesta.body()
            );
        }


        String cuerpo =
                respuesta
                        .body()
                        .replaceAll(
                                "\\s",
                                ""
                        );


        return cuerpo.contains(
                "\"autenticado\":true"
        );
    }
}