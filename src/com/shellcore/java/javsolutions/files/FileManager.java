package com.shellcore.java.javsolutions.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Cesar. 09/06/2017.
 */
public class FileManager {

    private static String path = "./src/files/"; // Ubicación del archivo de salida

    public static boolean saveFile(String fileName, String fileContent) {

        // Crear path, si no existe
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }

        BufferedWriter writer = null;
        boolean completed = false;

        try {
            writer = new BufferedWriter(new FileWriter(path + fileName));
            writer.write(fileContent);
            completed = true;
        } catch (IOException e) {
            System.out.println("Error en el guardado del archivo\n" + e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error en el guardado del archivo\n" + e);
                }
            }
        }

        return completed;
    }
}
