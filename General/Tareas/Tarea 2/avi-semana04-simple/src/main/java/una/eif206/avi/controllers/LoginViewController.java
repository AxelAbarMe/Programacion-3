package una.eif206.avi.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import una.eif206.avi.service.UsuarioService;

public class LoginViewController {

    public static boolean sesionIniciada = false;

    private static final int MAX_INTENTOS_FALLIDOS = 3;
    private static final int SEGUNDOS_BLOQUEO = 30;

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoContrasena;
    @FXML private ComboBox<String> comboFuenteDatos;
    @FXML private Button botonIngresar;
    @FXML private Button botonCancelar;
    @FXML private Label etiquetaError;

    private final UsuarioService usuarioService = new UsuarioService();
    private int intentosFallidos = 0;
    private PauseTransition pausaBloqueo;

    @FXML
    private void initialize() {
        comboFuenteDatos.getItems().setAll("JSON", "XML");
        comboFuenteDatos.getSelectionModel().selectFirst();

        botonIngresar.setOnAction(this::intentarIngresar);
        botonCancelar.setOnAction(evento -> {
            detenerBloqueo();
            cambiarPantalla(evento, "home-view.fxml");
        });
        try {
            usuarioService.inicializarDatos();
        } catch (Exception e) {
            etiquetaError.setText("No fue posible inicializar las fuentes de datos.");
            e.printStackTrace();
        }
    }

    private void intentarIngresar(ActionEvent evento) {
        etiquetaError.setText("");

        String usuario = campoUsuario.getText();
        String contrasena = campoContrasena.getText();
        String fuente = comboFuenteDatos.getValue();

        try {
            boolean credencialesValidas = usuarioService.iniciarSesion(usuario, contrasena, fuente);

            if (credencialesValidas) {
                sesionIniciada = true;
                intentosFallidos = 0;
                mostrarMensaje(Alert.AlertType.INFORMATION,
                        "Inicio de sesión",
                        "Inicio de sesión exitoso usando " + fuente + ".");
                cambiarPantalla(evento, "chat-list-view.fxml");
            } else {
                sesionIniciada = false;
                intentosFallidos++;
                String mensaje = "Usuario o contraseña incorrectos. Intentos fallidos: "
                        + intentosFallidos + " de " + MAX_INTENTOS_FALLIDOS + ".";
                etiquetaError.setText(mensaje);
                mostrarMensaje(Alert.AlertType.ERROR, "Inicio de sesión", mensaje);

                if (intentosFallidos >= MAX_INTENTOS_FALLIDOS) {
                    bloquearTemporalmente();
                }
            }
        } catch (IllegalArgumentException e) {
            etiquetaError.setText(e.getMessage());
        } catch (Exception e) {
            etiquetaError.setText("Ocurrió un error al consultar la fuente de datos.");
            e.printStackTrace();
        }
    }

    private void bloquearTemporalmente() {
        campoUsuario.setDisable(true);
        campoContrasena.setDisable(true);
        comboFuenteDatos.setDisable(true);
        botonIngresar.setDisable(true);

        etiquetaError.setText("Demasiados intentos fallidos. Intente nuevamente en "
                + SEGUNDOS_BLOQUEO + " segundos.");

        pausaBloqueo = new PauseTransition(Duration.seconds(SEGUNDOS_BLOQUEO));
        pausaBloqueo.setOnFinished(finalizado -> {
            intentosFallidos = 0;
            campoUsuario.setDisable(false);
            campoContrasena.setDisable(false);
            comboFuenteDatos.setDisable(false);
            botonIngresar.setDisable(false);
            etiquetaError.setText("");
        });
        pausaBloqueo.play();
    }

    private void detenerBloqueo() {
        if (pausaBloqueo != null) {
            pausaBloqueo.stop();
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
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