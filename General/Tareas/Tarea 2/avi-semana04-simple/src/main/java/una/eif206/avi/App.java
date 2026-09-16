package una.eif206.avi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicación AVI.
 *
 * Semana 4-5 - Aplicaciones dirigidas por eventos (versión simple: toda la
 * navegación y el manejo de eventos vive directamente en cada controller,
 * sin clases utilitarias ni de modelo todavía).
 */
public class App extends Application {

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        Parent raiz = FXMLLoader.load(getClass().getResource("/una/eif206/avi/ui/home-view.fxml"));
        escenarioPrincipal.setTitle("AVI - Agente Virtual Inteligente");
        escenarioPrincipal.setScene(new Scene(raiz, 480, 640));
        escenarioPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
