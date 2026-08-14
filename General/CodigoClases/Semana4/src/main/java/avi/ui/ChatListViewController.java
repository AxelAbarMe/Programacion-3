package avi.ui;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.List;

public class ChatListViewController {
    //1-Definir controles que vamos a accionnar desde la interfaz gráfica, preparar eventos que vamos a ejecutar
    @FXML private ListView<String> lvwChats;
    @FXML private Button btnVolver;
    @FXML private Button btnAbrirChat;
    @FXML private Button btnNuevoDesdeHistorial;

    @FXML
    private void initialize(){
        btnVolver.setOnAction(event -> cambiarPantalla(event, "InterfazAVI_Model.fxml")); //O bien "home-view.fxml"
        btnNuevoDesdeHistorial.setOnAction(event -> cambiarPantalla(event, "InterfazPrincipal.fxml"));  //O BIEN "chat-view.fxml"
        btnAbrirChat.setOnAction(event -> {
            if(lvwChats.getSelectionModel().getSelectedItem() != null){
                cambiarPantalla(event, "InterfazPrincipal.fxml"); //O BIEN "chat-view.fxml"
            }
        }); //Funcion Lambda //get
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
