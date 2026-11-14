package una.eif206.avi.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ExpertoSocketService {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public String consultarExperto(String pregunta, String respuestaAVI) throws IOException
    {
        try(
                Socket socket = new Socket(HOST, PUERTO);

                DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                )
        {
            //Enviar la pregunta original
            salida.writeUTF(pregunta);

            //Enviar para validar la respuesta del AVI
            salida.writeUTF(respuestaAVI);

            salida.flush();

            //Esperamos la respuesta del experto
            return entrada.readUTF();
        }
    }

}
