package com.shellcore.java.javsolutions.threads;

import com.shellcore.java.javsolutions.stores.GeneralStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Cesar. 09/06/2017.
 */
public class GeneralStoreListenThread extends Thread {

    public static final String CLOSE = "CLOSE";
    private final GeneralStore client;

    private String userInput;
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    public GeneralStoreListenThread(GeneralStore client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            standByForUserInput();
        } catch (IOException e) {
            System.out.println("Ocurrió un error en la lectura de datos por medio del servidor");
        }
    }

    private void standByForUserInput() throws IOException {
        while ((userInput = stdIn.readLine()) != null) {
            switch (userInput) {
                case CLOSE:
                    closeAllClientCommunication();
                    break;
                default:
                    System.out.println("Comando no reconocido\n");
                    System.out.println("Opciones:\n");
            }
        }
    }

    private void closeAllClientCommunication() {
        client.end();
    }
}
