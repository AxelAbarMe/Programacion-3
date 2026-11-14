package una.eif206.avi.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketCliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public String consultarExperto(String pregunta, String respuestaAVI) throws Exception
    {
        try(
                Socket socket = new Socket(HOST, PUERTO);
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                )
        {
            salida.println(pregunta);
            salida.println(respuestaAVI);

            return entrada.readLine();
        }
    }
}
