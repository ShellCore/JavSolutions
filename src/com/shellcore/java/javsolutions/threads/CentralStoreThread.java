package com.shellcore.java.javsolutions.threads;

import com.shellcore.java.javsolutions.files.FileManager;
import com.shellcore.java.javsolutions.stores.CentralStore;
import javafx.beans.property.MapPropertyBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by Cesar. 09/06/2017.
 */
public class CentralStoreThread extends Thread {

    public static final String REPORT = "REPORT";
    public static final String DELETE = "DELETE";
    public static final String CLOSE = "CLOSE";
    private final CentralStore server;

    private String userInput;
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    public CentralStoreThread(CentralStore server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            standByForUserInput();
        } catch (IOException e) {
            System.out.println("Ocurrió un error en la lectura de datos por medio del usuario del servidor");
        }
    }

    private void standByForUserInput() throws IOException {
        while ((userInput = stdIn.readLine()) != null) {
            switch (userInput) {
                case REPORT:
                    showTotalReport();
                    showTendencyReport();
                    break;
                case DELETE:
                    deleteFiles();
                    break;
                case CLOSE:
                    closeAllClientCommunication();
                    break;
                default:
                    System.out.println("Comando no reconocido\n");
                    System.out.println("Opciones:\n");
                    showoptions();
            }
        }
    }

    private void showTotalReport() {
        double res = 0.0;
        res = FileManager.readTotalForInvoices();
        System.out.println("Reporte: ");
        System.out.println("Total: " + res);
        System.out.println();
    }

    private void showTendencyReport() {
        Map<String, Integer> bestItems = FileManager.readBestItems();
        System.out.println("Tendencias:");
        bestItems.forEach((item, cuantity) -> System.out.println(cuantity + " : " + item));
        System.out.println();
    }

    private void deleteFiles() {
        FileManager.clearInvoiceFolder();
    }

    private void closeAllClientCommunication() {
        server.closeClients();
        server.end();
    }

    private void showoptions() {
        System.out.println(REPORT + ": Impresión del reporte");
        System.out.println(DELETE + ": Borrar las facturas del sistema");
        System.out.println(CLOSE + ": Cerrar conexión de los clientes y cerrar el servidor");
        System.out.println();
    }
}
