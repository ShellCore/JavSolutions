package com.shellcore.java.javsolutions.json;

import javax.json.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

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

    public static void obtainItemsFromFile(String file, Map<String, Integer> list) throws FileNotFoundException {

        JsonReader reader = Json.createReader(new FileReader(file));

        // Leyendo la factura dentro del JSON
        JsonStructure jsonStructure = reader.read();
        JsonObject object = (JsonObject) jsonStructure;
        JsonObject invoice = object.getJsonObject("invoice");
        if (invoice != null) {
            JsonArray products = invoice.getJsonArray("products");
            if (products != null) {
                for (JsonValue pr :
                        products) {
                    JsonObject product = (JsonObject) pr;
                    String name = product.getJsonString("name").toString();
                    if (list.containsKey(name)) {
                        list.put(name, list.get(name) + 1);
                    } else {
                        list.put(name, 1);
                    }
                }
            }
        }
    }
}
