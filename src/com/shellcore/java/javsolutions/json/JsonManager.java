package com.shellcore.java.javsolutions.json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Cesar. 09/06/2017.
 */
public class JsonManager {


    public static double obtainTotalFromFile(String file) throws FileNotFoundException {
        double total = 0.0;
        JsonReader reader = Json.createReader(new FileReader(file));
        JsonObject root = reader.readObject();

        // Leyendo la factura dentro del JSON
        JsonObject invoice = root.getJsonObject("invoice");
        if (invoice != null) {
            total = Double.parseDouble(invoice.getString("total"));
        }
        return total;
    }
}
