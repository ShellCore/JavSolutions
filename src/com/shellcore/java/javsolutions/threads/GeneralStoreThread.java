package com.shellcore.java.javsolutions.threads;

import com.shellcore.java.javsolutions.files.FileManager;
import com.shellcore.java.javsolutions.stores.CentralStore;

import java.io.*;
import java.net.Socket;

/**
 * Created by Cesar. 08/06/2017.
 */
public class GeneralStoreThread extends Thread {

    public static final int WAITING = 0;
    public static final int FILE_NAME = 1;
    public static final int FILE_WRITING = 2;

    public static final String NEWFILE = "NEWFILE";
    public static final String ENDFILE = "ENDFILE";
    public static final String CONNECTIONEND = "CONNECTIONEND";

    private CentralStore server;
    private Socket socket;

    InputStream inp = null;
    BufferedReader clientInput = null;
    PrintWriter clientOutput = null;

    private int fileState = WAITING;
    private String fileName = "";
    private String fileContent = "";
    private int id;

    public GeneralStoreThread(Socket clientSocket, CentralStore server, int id) {
        this.socket = clientSocket;
        this.server = server;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            waitForUserConnection();
        } catch (IOException e) {
            System.out.println("Error con el inicio del canal de comunicación con el cliente " + id);
            return;
        }
        standByForUserCommunication();
    }

    private void waitForUserConnection() throws IOException {
        inp = socket.getInputStream();
        clientInput = new BufferedReader(new InputStreamReader(inp));
        clientOutput = new PrintWriter(socket.getOutputStream());
    }

    private void standByForUserCommunication() {
        clientOutput.println("Welcome user#" + id);
        clientOutput.flush();

        String line;
        try {
            while ((line = clientInput.readLine()) != null) {
                String res = performMessage(line);
                clientOutput.println(res);
                clientOutput.flush();
            }
            socket.close();
        } catch (IOException e) {
        } finally {
            System.out.println("Conexión se cerró");
            server.removeClient(this);
            server = null;
            return;
        }
    }


    private String performMessage(String message) {
        String res = "";
        switch (fileState) {
            case WAITING:
                switch (message) {
                    case NEWFILE:
                        fileState = FILE_NAME;
                        res = "FILENAME";
                        break;
                    case CONNECTIONEND:
                        closeConnection();
                        break;
                    case ENDFILE:
                    default:
                        res = "El comando no es reconocido o se encuentra fuera del orden de recepción";
                        break;
                }
                break;
            case FILE_NAME:
                if (message.endsWith(".txt")) {
                    fileName = message;
                    res = "WAITING";
                    fileState = FILE_WRITING;
                } else {
                    res = "Nombre de archivo no contiene el formato requerido (*.txt). Vuelva a empezar desde el principio.";
                    fileState = WAITING;
                }
                break;
            case FILE_WRITING:
                if (message.equals(ENDFILE)) {
                    res = guardarArchivo();
                    fileState = WAITING;
                } else {
                    fileContent += message + "\n";
                }
                break;
        }
        return res;
    }

    private String guardarArchivo() {
        String respuesta = "";

        if (FileManager.saveFile(fileName, fileContent)) {
            respuesta = "El archivo se guardó correctamente.";
        } else {
            respuesta = "Hubo un error al crear el archivo. Vuelva a intentarlo desde el principio";
        }

        return respuesta;
    }

    private void closeConnection() {
        try {
            socket.close();
            server.removeClient(this);
        } catch (IOException e) {
            System.out.println("Error al intentar cerrar el hilo del cliente " + id);
        }
    }

    public void end() {
        closeConnection();
    }
}
