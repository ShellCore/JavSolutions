package com.shellcore.java.javsolutions.stores;

import java.io.*;
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

    private PrintWriter serverOut;
    private BufferedReader serverIn;

    private BufferedReader userIn;

    public GeneralStore() {
        if (!readClientParameters()) {
            System.out.println("Se cancela la conexión con el servidor");
            System.exit(1);
        }
        checkServerStatus();
    }

    public static void main(String args[]) {
        GeneralStore generalStore = new GeneralStore();
        generalStore.start();
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

    private void checkServerStatus() {
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

    public void start() {
        try {
            connectToServer();
            standByForUserInput();
        } catch (IOException e) {
            System.err.println("I/O Exception");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void connectToServer() throws IOException {
        userIn = new BufferedReader(new InputStreamReader(System.in));
        serverOut = new PrintWriter(clientSocket.getOutputStream(), true);
        serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String serverWelcome = serverIn.readLine();
        if (serverWelcome != null && !serverWelcome.isEmpty()) {
            //Read the welcome message from the server
            System.out.println(serverWelcome);
        } else {
            throw new IOException("Error en la conexión");
        }
    }

    private void standByForUserInput() throws IOException {
        String userInput = null;
        while ((userInput = userIn.readLine()) != null) {
            performUserInput(userInput);
        }
        System.out.println("La conexión se ha cerrado");
    }

    private void performUserInput(String message) throws IOException {
        String[] mensaje = message.split(" ");

        switch (mensaje[0]) {
            case "SEND":
                String nombreArchivo = message.substring(5);
                if (nombreArchivo.contains(".txt")) {
                    if (!new File(nombreArchivo).exists()) {
                        System.out.println("El archivo no existe");
                    } else {
                        System.out.println(sendFile(nombreArchivo));
                    }
                } else {
                    System.out.println("El archivo no tiene el formato requerido\n");
                    showoptions();
                }
                break;
            case "CLOSE":
                serverOut.println("CONNECTIONEND");
                System.exit(0);
                break;
            default:
                System.out.println("Comando no reconocido\n");
                showoptions();
        }
    }

    private void showoptions() {
        System.out.println("Opciones:\n");
        System.out.println("SEND <nombre_archivo>: Envío de un archivo");
        System.out.println("CLOSE: Cerrar la conexión con el servidor");
        System.out.println();
    }

    private String sendFile(String filePath) throws IOException {
        Scanner input = null;

        String[] fileList = filePath.split("\\\\");
        String fileName = fileList[fileList.length - 1];

        serverOut.println("NEWFILE");
        String serverMessage = serverIn.readLine();
        if (serverMessage.equals("FILENAME")) {
            serverOut.println(fileName);
            serverMessage = serverIn.readLine();

            if (serverMessage.equals("WAITING")) {
                input = new Scanner(new BufferedReader(new FileReader(filePath)));
                while (input.hasNext()) {
                    serverOut.println(input.nextLine());
                }
                serverOut.println("ENDFILE");
                if (input != null) {
                    input.close();
                }
                return "El archivo fue enviado";
            } else {
                throw new IOException(serverMessage);
            }
        } else {
            throw new IOException(serverMessage);
        }
    }
}
