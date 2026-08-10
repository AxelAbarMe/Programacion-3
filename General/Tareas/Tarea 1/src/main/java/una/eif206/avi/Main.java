package una.eif206.avi;

import una.eif206.avi.model.ConsultaCaracteristicasJava;
import una.eif206.avi.model.ConsultaComparacionC;
import una.eif206.avi.model.ConsultaHistoriaJava;
import una.eif206.avi.model.ConsultaTema;
import una.eif206.avi.service.GeminiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GeminiService geminiService = new GeminiService();

        List<ConsultaTema> temas = new ArrayList<>();
        temas.add(new ConsultaHistoriaJava());
        temas.add(new ConsultaCaracteristicasJava());
        temas.add(new ConsultaComparacionC());

        System.out.println("=== AVI - Agente Virtual Inteligente (consola) ===");
        System.out.print("\n> ");
        for (ConsultaTema tema : temas) {
            System.out.println("--- " + tema.getNombre() + " ---");
            try {
                String respuesta = geminiService.enviarMensaje(tema.getPrompt());
                System.out.println(respuesta);
            } catch (IOException | InterruptedException e) {
                System.out.println("Ocurrio un error consultando este tema: " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("\nHasta luego!");

    }
}
