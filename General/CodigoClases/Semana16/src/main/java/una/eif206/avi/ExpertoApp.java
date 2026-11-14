package una.eif206.avi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExpertoApp extends Application {

    @Override
    public void start(
            Stage stage)
            throws Exception {

        Parent raiz =
                FXMLLoader.load(
                        getClass()
                                .getResource(
                                        "/una/eif206/avi/ui/"
                                                + "experto-view.fxml"
                                )
                );


        Scene escena =
                new Scene(
                        raiz,
                        700,
                        650
                );


        stage.setTitle(
                "AVI - Consola del experto"
        );

        stage.setScene(
                escena
        );

        stage.show();
    }


    public static void main(
            String[] args) {

        launch(args);
    }
}