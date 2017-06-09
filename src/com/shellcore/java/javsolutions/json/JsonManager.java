package com.shellcore.java.javsolutions.json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Cesar. 09/06/2017.
 */
public class JsonManager {

    public static double obtainTotalFromFile(String file) throws FileNotFoundException {
        double total = 0.0;
        JsonReader reader = Json.createReader(new FileReader(file));

        // Leyendo la factura dentro del JSON
        JsonStructure jsonStructure = reader.read();
        JsonObject object = (JsonObject) jsonStructure;
        JsonObject invoice = object.getJsonObject("invoice");
        if (invoice != null) {
            total = Double.parseDouble(invoice.getString("total"));
        }
        return total;
    }
}
