package com.shellcore.java.javsolutions.stores;

import com.shellcore.java.javsolutions.threads.GeneralStoreThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Server
 * Created by Cesar. 08/06/2017.
 */
public class CentralStore {

    private int port;
    private int clientConnectionCounter;
    private List<GeneralStoreThread> clients;
    private ServerSocket serverSocket = null;

    public CentralStore() {
        if (!readServerParameters()) {
            System.out.println("Se cancela la inicialización del servidor");
            System.exit(1);
        }
        clientConnectionCounter = 1;
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error en la inicialización del servidor: " + e);
        }
    }

    private boolean readServerParameters() {
        boolean dataCaptured = false;
        Scanner scanner = new Scanner(System.in);
        String portString = "";
        port = 0;
        /**
         * Si el usuario agrega valores aleatorios, no hace nada
         * Si el usuario agrega un valor numérico válido, se leé el puerto asignado
         */
        System.out.println("Bienvenido al sistema de control de la tienda central.");
        System.out.print("Por favor escriba el puerto de conexión para los clientes: ");
        int intentos = 2;
        while ((portString = scanner.next()) != null && !portString.isEmpty() && intentos > 0) {
            try {
                port = Integer.parseInt(portString);
                dataCaptured = true;
                break;
            } catch (NumberFormatException e) {
                if (intentos > 0) {
                    System.out.println("El número de puerto no es válido. Tiene " + (intentos) + " intentos más antes de que el sistema cancele la operación.");
                    port = 0;
                    intentos--;
                }
            }
        }
        return dataCaptured;
    }

    public void start() {
        Socket socket = null;
        GeneralStoreThread client = null;
        inizializeReportThread();

        System.out.println("El servidor está encendido");
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Error de I/O: " + e);
            }

            // Nuevo hilo para el cliente
            client = new GeneralStoreThread(socket, this, clientConnectionCounter++);
            client.start();
            clients.add(client);
            client = null;
        }
    }

    public void removeClient(GeneralStoreThread generalStoreThread) {
        clients.remove(generalStoreThread);

    }

    private void inizializeReportThread() {
        // TODO
    }

    public static void main(String[] args) {
        try {
            System.out.println("Server IP: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException: " + e);
        }
        CentralStore centralStore = new CentralStore();
        centralStore.start();
    }
}
