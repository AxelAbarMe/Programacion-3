package avi.ui;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class InterfazHistorialController {
    // Definir controles que vamos a accionar desde la interfaz gráfica, preparar eventos que vamos a ejecutar
    @FXML private ListView<String> lvwChats;
    @FXML private Button btnVolver;
    @FXML private Button btnAbrirChat;
    @FXML private Button btnNuevoDesdeHistorial;

    @FXML
    private void initialize(){
        btnVolver.setOnAction(event -> cambiarPantalla(event, "InterfazInicio.fxml"));
        btnNuevoDesdeHistorial.setOnAction(event -> cambiarPantalla(event, "InterfazChat.fxml"));
        btnAbrirChat.setOnAction(event -> {
            if(lvwChats.getSelectionModel().getSelectedItem() != null){
                cambiarPantalla(event, "InterfazChat.fxml");
            }
        }); // Función Lambda
    }

    private void cambiarPantalla(ActionEvent event, String archivoFxml){
        try{
            Parent raiz = FXMLLoader.load(getClass().getResource(archivoFxml));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(raiz);
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }
}
