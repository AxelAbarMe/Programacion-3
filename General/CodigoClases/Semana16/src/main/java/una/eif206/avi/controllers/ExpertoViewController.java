package una.eif206.avi.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import una.eif206.avi.dto.ConsultaExpertoDTO;
import una.eif206.avi.service.ServidorExpertoService;

public class ExpertoViewController {

    @FXML
    private Label etiquetaEstadoServidor;

    @FXML
    private TextArea areaPregunta;

    @FXML
    private TextArea areaRespuestaAVI;

    @FXML
    private TextArea areaRespuestaExperto;

    @FXML
    private Button botonEnviarRespuesta;


    private final ServidorExpertoService servidor =
            new ServidorExpertoService(5000);


    @FXML
    private void initialize() {

        botonEnviarRespuesta.setOnAction(
                evento -> enviarRespuesta()
        );

        iniciarServidor();
    }


    private void iniciarServidor() {

        Thread hiloServidor =
                new Thread(
                        () -> {

                            try {

                                servidor.iniciar();


                                Platform.runLater(() -> {

                                    etiquetaEstadoServidor.setText(
                                            "Servidor activo - Puerto 5000"
                                    );
                                });


                                esperarSiguienteConsulta();


                            } catch (Exception e) {

                                Platform.runLater(() -> {

                                    etiquetaEstadoServidor.setText(
                                            "No fue posible iniciar "
                                                    + "el servidor."
                                    );
                                });

                                e.printStackTrace();
                            }

                        },
                        "Hilo-Servidor-Experto"
                );


        hiloServidor.setDaemon(true);

        hiloServidor.start();
    }


    private void esperarSiguienteConsulta() {

        Thread hiloEspera =
                new Thread(
                        () -> {

                            try {

                                ConsultaExpertoDTO consulta =
                                        servidor
                                                .esperarConsulta();


                                Platform.runLater(() -> {

                                    areaPregunta.setText(
                                            consulta.getPregunta()
                                    );


                                    areaRespuestaAVI.setText(
                                            consulta.getRespuestaAVI()
                                    );


                                    areaRespuestaExperto.clear();


                                    etiquetaEstadoServidor.setText(
                                            "Consulta recibida."
                                    );


                                    botonEnviarRespuesta
                                            .setDisable(false);
                                });


                            } catch (Exception e) {

                                Platform.runLater(() -> {

                                    etiquetaEstadoServidor.setText(
                                            "Error esperando consulta."
                                    );
                                });

                                e.printStackTrace();
                            }

                        },
                        "Hilo-Espera-Consulta"
                );


        hiloEspera.setDaemon(true);

        hiloEspera.start();
    }


    private void enviarRespuesta() {

        String respuesta =
                areaRespuestaExperto
                        .getText()
                        .trim();


        if (respuesta.isEmpty()) {

            etiquetaEstadoServidor.setText(
                    "Debe escribir una respuesta."
            );

            return;
        }


        botonEnviarRespuesta
                .setDisable(true);


        Thread hiloRespuesta =
                new Thread(
                        () -> {

                            try {

                                servidor.enviarRespuesta(
                                        respuesta
                                );


                                Platform.runLater(() -> {

                                    etiquetaEstadoServidor.setText(
                                            "Respuesta enviada. "
                                                    + "Esperando nueva consulta..."
                                    );


                                    areaPregunta.clear();

                                    areaRespuestaAVI.clear();

                                    areaRespuestaExperto.clear();


                                    esperarSiguienteConsulta();
                                });


                            } catch (Exception e) {

                                Platform.runLater(() -> {

                                    etiquetaEstadoServidor.setText(
                                            "No fue posible enviar "
                                                    + "la respuesta."
                                    );

                                    botonEnviarRespuesta
                                            .setDisable(false);
                                });

                                e.printStackTrace();
                            }

                        },
                        "Hilo-Respuesta-Experto"
                );


        hiloRespuesta.setDaemon(true);

        hiloRespuesta.start();
    }
}