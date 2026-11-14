package una.eif206.avi.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;

import java.net.InetSocketAddress;
import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.Executors;

public class UsuarioServiceHTTP {

    private static final int PUERTO = 8080;

    private final UsuarioService usuarioService;

    private HttpServer servidor;


    public UsuarioServiceHTTP() {

        usuarioService =
                new UsuarioService();
    }


    public void iniciar()
            throws IOException {

        servidor =
                HttpServer.create(
                        new InetSocketAddress(
                                "0.0.0.0",
                                PUERTO
                        ),
                        0
                );


        servidor.createContext(
                "/usuario/login",
                new LoginHandler()
        );


        servidor.setExecutor(
                Executors.newFixedThreadPool(4)
        );


        servidor.start();


        System.out.println(
                "Servicio de usuarios iniciado."
        );

        System.out.println(
                "Puerto: " + PUERTO
        );

        System.out.println(
                "POST /usuario/login"
        );
    }


    private class LoginHandler
            implements HttpHandler {

        @Override
        public void handle(
                HttpExchange exchange)
                throws IOException {

            if (!exchange
                    .getRequestMethod()
                    .equalsIgnoreCase("POST")) {

                enviarRespuesta(
                        exchange,
                        405,
                        """
                        {
                            "error": "Metodo no permitido"
                        }
                        """
                );

                return;
            }


            try {

                String cuerpo =
                        new String(
                                exchange
                                        .getRequestBody()
                                        .readAllBytes(),
                                StandardCharsets.UTF_8
                        );


                Map<String, String> parametros =
                        obtenerParametros(
                                cuerpo
                        );


                String usuario =
                        parametros.get(
                                "usuario"
                        );


                String contrasena =
                        parametros.get(
                                "contrasena"
                        );


                /*
                 * Se llama al UsuarioService
                 * que ya tenemos.
                 */

                boolean autenticado =
                        usuarioService
                                .iniciarSesionBD(
                                        usuario,
                                        contrasena
                                );


                String respuesta =
                        """
                        {
                            "autenticado": %s
                        }
                        """
                                .formatted(
                                        autenticado
                                );


                enviarRespuesta(
                        exchange,
                        200,
                        respuesta
                );


            } catch (
                    IllegalArgumentException e) {

                enviarRespuesta(
                        exchange,
                        400,
                        """
                        {
                            "error": "%s"
                        }
                        """
                                .formatted(
                                        e.getMessage()
                                )
                );


            } catch (Exception e) {

                e.printStackTrace();


                enviarRespuesta(
                        exchange,
                        500,
                        """
                        {
                            "error":
                            "Error interno del servidor"
                        }
                        """
                );
            }
        }
    }


    private Map<String, String>
    obtenerParametros(
            String cuerpo) {

        Map<String, String> parametros =
                new HashMap<>();


        String[] pares =
                cuerpo.split("&");


        for (String par : pares) {

            String[] partes =
                    par.split(
                            "=",
                            2
                    );


            if (partes.length == 2) {

                String clave =
                        URLDecoder.decode(
                                partes[0],
                                StandardCharsets.UTF_8
                        );


                String valor =
                        URLDecoder.decode(
                                partes[1],
                                StandardCharsets.UTF_8
                        );


                parametros.put(
                        clave,
                        valor
                );
            }
        }


        return parametros;
    }


    private void enviarRespuesta(
            HttpExchange exchange,
            int codigo,
            String respuesta)
            throws IOException {

        byte[] datos =
                respuesta.getBytes(
                        StandardCharsets.UTF_8
                );


        exchange
                .getResponseHeaders()
                .set(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );


        exchange.sendResponseHeaders(
                codigo,
                datos.length
        );


        try (
                OutputStream salida =
                        exchange
                                .getResponseBody()
        ) {

            salida.write(datos);
        }
    }

    public static void main(
            String[] args) {

        try {

            UsuarioServiceHTTP servicio =
                    new UsuarioServiceHTTP();

            servicio.iniciar();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}