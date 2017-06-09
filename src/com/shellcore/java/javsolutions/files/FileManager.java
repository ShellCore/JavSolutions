package com.shellcore.java.javsolutions.files;

import com.shellcore.java.javsolutions.json.JsonManager;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static void clearInvoiceFolder() {
        Arrays.stream(new File(path).listFiles())
                .forEach(File::delete);
        System.out.println("Los archivos se borraron correctamente.");
    }

    public static double readTotalForInvoices() {
        List<Double> totals = new ArrayList<>();

        List<String> files = Arrays.asList(new File(path).list());
        files.forEach(file -> {
            try {
                totals.add(JsonManager.obtainTotalFromFile(file));
            } catch (FileNotFoundException e) {
                System.out.println("No se pudo leer el archivo \"" + file + "\"");
            }
        });

        return totals.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
