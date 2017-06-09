package com.shellcore.java.javsolutions.threads;

import com.shellcore.java.javsolutions.files.FileManager;
import com.shellcore.java.javsolutions.stores.CentralStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            while ((userInput = stdIn.readLine()) != null) {
                switch (userInput) {
                    case REPORT:
                        showReport();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showReport() {
        double res = 0.0;

        res = leerTotales();

        System.out.println("Reporte: ");
        System.out.println("Total: " + res);
    }

    private double leerTotales() {
        double res = 0;

        FileManager.readTotalForInvoices();

        return res;
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
