package una.eif206.avi.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import una.eif206.avi.service.GeminiService;
import una.eif206.avi.service.ExpertoSocketService;

public class ChatViewController {

    @FXML private Button botonVolverChat;
    @FXML private Label etiquetaTituloChat;
    @FXML private Label etiquetaAvisoSesion;
    @FXML private TextArea areaHistorial;
    @FXML private TextField campoTexto;
    @FXML private Button botonEnviar;
    @FXML
    private Button botonConsultarExperto;

    @FXML
    private Label etiquetaEstadoExperto;

    private final GeminiService geminiService = new GeminiService();
    private final ExpertoSocketService expertoSocketService = new ExpertoSocketService();

    private String ultimaPregunta;
    private String ultimaRespuestaAVI;

    @FXML
    private void initialize() {
        // Si no hay sesión iniciada, se lo advertimos de una vez: puede
        // conversar igual, pero no vamos a poder guardar nada (eso llega
        // con persistencia, semana 7).
        if (!LoginViewController.sesionIniciada) {
            etiquetaAvisoSesion.setText("No ha iniciado sesión: esta conversación no se podrá guardar.");
        }

        botonVolverChat.setOnAction(evento -> {
            try {
                Parent raiz = FXMLLoader.load(getClass().getResource("/una/eif206/avi/ui/chat-list-view.fxml"));
                Stage stage = (Stage) ((Node) evento.getSource()).getScene().getWindow();
                stage.getScene().setRoot(raiz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        botonEnviar.setOnAction(evento -> enviarMensaje());
        campoTexto.setOnAction(evento -> enviarMensaje());

        botonConsultarExperto.setOnAction(evento -> consultarExperto());
    }

    private void enviarMensaje() {
        String texto = campoTexto.getText().trim();
        if (texto.isEmpty()) return;

        ultimaPregunta = texto;
        ultimaRespuestaAVI = null;

        botonConsultarExperto.setDisable(true);
        etiquetaEstadoExperto.setText("");

        areaHistorial.appendText("Tú: " + texto + "\n");
        campoTexto.clear();
        campoTexto.setDisable(true);
        botonEnviar.setDisable(true);

        Thread hiloFondo = new Thread(() -> {
            try {
                String respuesta = geminiService.enviarMensaje(texto);
                Platform.runLater(() -> {
                    areaHistorial.appendText("AVI: " + respuesta + "\n");

                    ultimaRespuestaAVI = respuesta;
                    botonConsultarExperto.setDisable(false);

                    etiquetaEstadoExperto.setText(
                            "Puede solicitar validación humana."
                    );

                    habilitarEntrada();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    areaHistorial.appendText("Error: " + e.getMessage() + "\n");
                    habilitarEntrada();
                });
            }
        });
        hiloFondo.setDaemon(true);
        hiloFondo.start();
    }

    private void consultarExperto() {

        if (ultimaPregunta == null
                || ultimaRespuestaAVI == null) {

            return;
        }


        String pregunta =
                ultimaPregunta;

        String respuestaAVI =
                ultimaRespuestaAVI;


        botonConsultarExperto
                .setDisable(true);

        etiquetaEstadoExperto.setText(
                "Esperando respuesta del experto..."
        );


        areaHistorial.appendText(
                "\n--- Consulta humana solicitada ---\n"
        );


        Thread hiloExperto =
                new Thread(
                        () -> {

                            try {

                                String respuestaExperto =
                                        expertoSocketService
                                                .consultarExperto(
                                                        pregunta,
                                                        respuestaAVI
                                                );


                                Platform.runLater(() -> {

                                    areaHistorial.appendText(
                                            "\nEXPERTO: "
                                                    + respuestaExperto
                                                    + "\n\n"
                                    );

                                    etiquetaEstadoExperto.setText(
                                            "Validación recibida."
                                    );

                                    botonConsultarExperto
                                            .setDisable(false);
                                });


                            } catch (Exception e) {

                                Platform.runLater(() -> {

                                    areaHistorial.appendText(
                                            "\nNo fue posible contactar "
                                                    + "al experto.\n"
                                    );

                                    etiquetaEstadoExperto.setText(
                                            "Experto no disponible."
                                    );

                                    botonConsultarExperto
                                            .setDisable(false);
                                });

                                e.printStackTrace();
                            }

                        },
                        "Hilo-Consulta-Experto"
                );


        hiloExperto.setDaemon(true);

        hiloExperto.start();
    }

    private void habilitarEntrada() {
        campoTexto.setDisable(false);
        botonEnviar.setDisable(false);
        campoTexto.requestFocus();
    }
}