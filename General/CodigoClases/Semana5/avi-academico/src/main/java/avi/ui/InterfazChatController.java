package avi.ui;

import avi.service.GeminiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class InterfazChatController {

    @FXML private TextArea textResp;
    @FXML private TextArea textoPreg;
    @FXML private Button btnenviar;

    private final GeminiService servicioGemini = new GeminiService();

    @FXML
    private void enviarMensaje(ActionEvent event){
        String pregunta = textoPreg.getText().trim();
        if(pregunta.isEmpty()){
            return;
        }

        textResp.appendText("\nUsuario: " + pregunta + "\n");
        textoPreg.clear();

        try {
            String respuesta = servicioGemini.enviarMensaje(pregunta);
            textResp.appendText("AVI: " + respuesta + "\n");
        } catch (Exception error) {
            textResp.appendText("AVI: Ocurrió un error al consultar el modelo.\n");
            error.printStackTrace();
        }
    }
}
