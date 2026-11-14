package una.eif206.avi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import una.eif206.avi.service.UsuarioServiceCliente;

public class LoginViewController {

    public static boolean sesionIniciada = false;

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoContrasena;

    @FXML
    private Button botonIngresar;

    @FXML
    private Button botonCancelar;

    @FXML
    private Label etiquetaError;


    /*
     * Ya no utilizamos directamente UsuarioService.
     *
     * El Controller pertenece al frontend y utiliza
     * UsuarioServiceCliente para comunicarse por HTTP
     * con el backend.
     */
    private final UsuarioServiceCliente usuarioService =
            new UsuarioServiceCliente();


    @FXML
    private void initialize() {

        botonIngresar.setOnAction(
                this::intentarIngresar
        );

        botonCancelar.setOnAction(
                evento ->
                        cambiarPantalla(
                                evento,
                                "home-view.fxml"
                        )
        );
    }


    private void intentarIngresar(
            ActionEvent evento) {

        etiquetaError.setText("");

        String usuario =
                campoUsuario.getText();

        String contrasena =
                campoContrasena.getText();


        /*
         * Validaciones básicas de la interfaz.
         *
         * Estas evitan enviar solicitudes HTTP
         * innecesarias al backend.
         */

        if (usuario == null
                || usuario.isBlank()) {

            etiquetaError.setText(
                    "Debe ingresar el usuario."
            );

            return;
        }


        if (contrasena == null
                || contrasena.isBlank()) {

            etiquetaError.setText(
                    "Debe ingresar la contraseña."
            );

            return;
        }


        try {

            /*
             * ANTES:
             *
             * usuarioService.iniciarSesionBD(...)
             *
             *
             * AHORA:
             *
             * Se realiza una solicitud HTTP al backend.
             */

            boolean credencialesValidas =
                    usuarioService
                            .iniciarSesion(
                                    usuario,
                                    contrasena
                            );


            if (credencialesValidas) {

                sesionIniciada = true;


                mostrarMensaje(
                        Alert.AlertType.INFORMATION,
                        "Inicio de sesión",
                        "Inicio de sesión exitoso."
                );


                cambiarPantalla(
                        evento,
                        "chat-list-view.fxml"
                );


            } else {

                sesionIniciada = false;


                etiquetaError.setText(
                        "Usuario o contraseña incorrectos."
                );


                mostrarMensaje(
                        Alert.AlertType.ERROR,
                        "Inicio de sesión",
                        "Usuario o contraseña incorrectos."
                );
            }


        } catch (Exception e) {

            sesionIniciada = false;


            etiquetaError.setText(
                    "No fue posible comunicarse "
                            + "con el servicio de usuarios."
            );


            e.printStackTrace();
        }
    }


    private void mostrarMensaje(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alerta =
                new Alert(tipo);

        alerta.setTitle(
                titulo
        );

        alerta.setHeaderText(
                null
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }


    private void cambiarPantalla(
            ActionEvent evento,
            String archivoFxml) {

        try {

            Parent raiz =
                    FXMLLoader.load(
                            getClass()
                                    .getResource(
                                            "/una/eif206/avi/ui/"
                                                    + archivoFxml
                                    )
                    );


            Stage stage =
                    (Stage)
                            ((Node)
                                    evento.getSource())
                                    .getScene()
                                    .getWindow();


            stage
                    .getScene()
                    .setRoot(
                            raiz
                    );


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}