package una.eif206.avi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ChatListViewController {

    @FXML private ListView<String> listaChats;
    @FXML private Button botonVolver;
    @FXML private Button botonNuevaDesdeHistorial;
    @FXML private Button botonAbrirChat;

    @FXML
    private void initialize() {
        botonVolver.setOnAction(evento -> cambiarPantalla(evento, "home-view.fxml"));
        botonNuevaDesdeHistorial.setOnAction(evento -> cambiarPantalla(evento, "chat-view.fxml"));
        botonAbrirChat.setOnAction(evento -> {
            if (listaChats.getSelectionModel().getSelectedItem() != null) {
                cambiarPantalla(evento, "chat-view.fxml");
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