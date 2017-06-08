package com.shellcore.java.javsolutions.stores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Cesar. 08/06/2017.
 */
public class GeneralStore {
    private String hostName;
    private int portNumber;
    private Socket clientSocket;

    public GeneralStore() {
        if (!readClientParameters()) {
            System.out.println("Se cancela la conexión con el servidor");
            System.exit(1);
        }
        try {
            clientSocket = new Socket(hostName, portNumber);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName + ":" + portNumber + ". Check that the server is running.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private boolean readClientParameters() {
        boolean dataCaptured = false;
        Scanner scanner = new Scanner(System.in);
        String stringValue = "";

        System.out.println("Bienvenido al sistema de control de la tienda general.");
        System.out.print("Por favor escriba la ip a la cual conectarse: ");
        int counter = 2;
        while ((stringValue = scanner.next()) != null && !stringValue.isEmpty() && counter > 0) {
            try {
                hostName = stringValue;
                break;
            } catch (NumberFormatException e) {
                if (counter > 0) {
                    System.out.println("El dato no es válido. Tiene (" + (counter) + ") intentos más antes de que el sistena cancele la conexión");
                    counter--;
                    hostName = null;
                }
            }
        }
        if (counter == 0) {
            return dataCaptured;
        }
        counter = 2;
        // Captura del puerto
        System.out.print("Por favor escriba el puerto de la conexión: ");
        while ((stringValue = scanner.next()) != null && !stringValue.isEmpty() && counter > 0) {
            try {
                portNumber = Integer.parseInt(stringValue);
                dataCaptured = true;
                break;
            } catch (NumberFormatException e) {
                if (counter > 0) {
                    System.out.println("El dato no es válido. Tiene (" + (counter) + ") intentos más antes de que el sistena cancele la conexión");
                    counter--;
                    hostName = null;
                }
            }
        }
        return dataCaptured;
    }

    public void start() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            //Read the welcome message from the server
            System.out.println(in.readLine());

            String userInput = null;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println(in.readLine());
            }
        } catch (IOException e) {
            System.err.println("I/O Exception");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        GeneralStore generalStore = new GeneralStore();
        generalStore.start();
    }
}
