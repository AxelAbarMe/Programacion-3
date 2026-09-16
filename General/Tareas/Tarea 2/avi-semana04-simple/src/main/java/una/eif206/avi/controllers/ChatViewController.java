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

public class ChatViewController {

    @FXML private Button botonVolverChat;
    @FXML private Label etiquetaTituloChat;
    @FXML private Label etiquetaAvisoSesion;
    @FXML private TextArea areaHistorial;
    @FXML private TextField campoTexto;
    @FXML private Button botonEnviar;

    private final GeminiService geminiService = new GeminiService();

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
    }

    private void enviarMensaje() {
        String texto = campoTexto.getText().trim();
        if (texto.isEmpty()) return;

        areaHistorial.appendText("Tú: " + texto + "\n");
        campoTexto.clear();
        campoTexto.setDisable(true);
        botonEnviar.setDisable(true);

        Thread hiloFondo = new Thread(() -> {
            try {
                String respuesta = geminiService.enviarMensaje(texto);
                Platform.runLater(() -> {
                    areaHistorial.appendText("AVI: " + respuesta + "\n");
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

    private void habilitarEntrada() {
        campoTexto.setDisable(false);
        botonEnviar.setDisable(false);
        campoTexto.requestFocus();
    }
}