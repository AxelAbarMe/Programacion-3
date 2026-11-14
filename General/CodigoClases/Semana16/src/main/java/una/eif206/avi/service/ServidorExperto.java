package una.eif206.avi.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorExperto {

    public static void main(String[] args) {

        int puerto = 5000;

        try (
                ServerSocket servidor =
                        new ServerSocket(puerto);

                Scanner scanner =
                        new Scanner(System.in)
        ) {

            System.out.println(
                    "Servidor de expertos iniciado..."
            );

            System.out.println(
                    "Puerto: " + puerto
            );


            while (true) {

                System.out.println(
                        "\nEsperando consulta..."
                );

                try (
                        Socket cliente =
                                servidor.accept();

                        DataInputStream entrada =
                                new DataInputStream(
                                        cliente.getInputStream()
                                );

                        DataOutputStream salida =
                                new DataOutputStream(
                                        cliente.getOutputStream()
                                )
                ) {

                    String pregunta =
                            entrada.readUTF();

                    String respuestaAVI =
                            entrada.readUTF();


                    System.out.println(
                            "\n=============================="
                    );

                    System.out.println(
                            "CONSULTA AL EXPERTO"
                    );

                    System.out.println(
                            "=============================="
                    );


                    System.out.println(
                            "\nPregunta del usuario:"
                    );

                    System.out.println(
                            pregunta
                    );


                    System.out.println(
                            "\nRespuesta del AVI:"
                    );

                    System.out.println(
                            respuestaAVI
                    );


                    System.out.print(
                            "\nRespuesta del experto: "
                    );

                    String respuestaExperto =
                            scanner.nextLine();


                    salida.writeUTF(
                            respuestaExperto
                    );

                    salida.flush();


                    System.out.println(
                            "Respuesta enviada."
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}