package com.shellcore.java.javsolutions.threads;

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
    protected Socket socket;
    public int id;

    private int fileState = 0;

    public GeneralStoreThread(Socket clientSocket, CentralStore server, int id) {
        this.socket = clientSocket;
        this.server = server;
        this.id = id;
    }

    @Override
    public void run() {
        InputStream inp = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            inp = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inp));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        out.println("Welcome user#" + id);
        out.flush();

        String line;
        try {
            while ((line = in.readLine()) != null) {
                String res = interpretarMensaje(line);
                out.println(res);
                out.flush();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.removeClient(this);
        server = null;
    }



    private String interpretarMensaje(String message) {
        String res = "";
        switch (fileState) {
            case WAITING:
                switch (message) {
                    case NEWFILE:
                        fileState = FILE_NAME;
                        res = "En espera del nombre de archivo";
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
                break;
            case FILE_WRITING:
                break;
        }
        return res;
    }

    private void closeConnection() {
        try {
            socket.close();
            server.removeClient(this);
        } catch (IOException e) {
            System.out.println("Error al intentar cerrar el hilo del cliente " + id);
        }
    }
}
