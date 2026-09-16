package una.eif206.avi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class HomeViewController {

    @FXML private ComboBox<String> comboPersonalidad;
    @FXML private Button botonNuevaConversacion;
    @FXML private Button botonVerHistorial;

    @FXML
    private void initialize() {
        botonNuevaConversacion.setOnAction(evento -> cambiarPantalla(evento, "chat-view.fxml"));
        botonVerHistorial.setOnAction(evento -> {
            if (LoginViewController.sesionIniciada) {
                cambiarPantalla(evento, "chat-list-view.fxml");
            } else {
                cambiarPantalla(evento, "login-view.fxml");
            }
        });
    }

    private void cambiarPantalla(ActionEvent evento, String archivoFxml) {
        try {
            Parent raiz = FXMLLoader.load(getClass().getResource("/una/eif206/avi/ui/" + archivoFxml));
            Stage stage = (Stage) ((Node) evento.getSource()).getScene().getWindow();
            stage.getScene().setRoot(raiz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}