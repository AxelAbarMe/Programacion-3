package una.eif206.avi.service;

import una.eif206.avi.dto.ConsultaExpertoDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorExpertoService {

    private final int puerto;

    private ServerSocket servidor;

    private Socket clienteActual;

    private DataInputStream entradaActual;

    private DataOutputStream salidaActual;

    public ServidorExpertoService(int puerto) {
        this.puerto = puerto;
    }

    public void iniciar() throws IOException
    {
        servidor = new ServerSocket(puerto);
    }

    public synchronized ConsultaExpertoDTO esperarConsulta() throws IOException
    {
        clienteActual = servidor.accept();

        entradaActual = new DataInputStream(clienteActual.getInputStream());

        salidaActual = new DataOutputStream(clienteActual.getOutputStream());

        String pregunta = entradaActual.readUTF();

        String respuestaAVI = entradaActual.readUTF();

        return new ConsultaExpertoDTO(
                pregunta,
                respuestaAVI
        );
    }

    public synchronized void enviarRespuesta(String respuesta) throws IOException
    {
        if(salidaActual == null)
        {
            throw new IOException("No existe ninguna consulta activa. Conexión rechazada.");
        }

        salidaActual.writeUTF(respuesta);

        salidaActual.flush();

        cerrarClienteActual();
    }

    private void cerrarClienteActual()
    {
        try {
            if(entradaActual != null)
            {
                entradaActual.close();
            }
        }
        catch (Exception ignorado)
        {

        }

        try {
            if(salidaActual != null)
            {
                salidaActual.close();
            }
        }
        catch (Exception ignorado)
        {

        }

        try {
            if(clienteActual != null)
            {
                clienteActual.close();
            }
        }
        catch(Exception ignorado)
        {

        }

        entradaActual = null;
        salidaActual = null;
        clienteActual = null;
    }
}
