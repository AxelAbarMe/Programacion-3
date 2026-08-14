package avi.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class HomeViewController {
    //Vamos a definir los elementos (controles) que vamos a manipular desde Java
    //Los nombres de estos elementos deben ser exactamente iguales a los fx:id del diseño de SceneBuilder

    // Data Notations para que Java sepa cuando debe quedarse adentro o ir afuera
    @FXML private ComboBox<String> cbModel;
    @FXML private Button btnOp1;
    @FXML private Button btnOp2;

    @FXML
    private void initialize(){
        btnOp1.setOnAction(event -> cambiarPantalla(event, "InterfazPrincipal.fxml")); //O bien "chat-view.fxml"
        btnOp2.setOnAction(event -> cambiarPantalla(event, "InterfazChats.fxml"));  //O BIEN "chat-list-view.fxml"
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
