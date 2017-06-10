package com.shellcore.java.javsolutions.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * Created by Cesar. 09/06/2017.
 */
public class JsonManagerTest {

    String path;

    @Before
    public void setup() {
        path = "./src/files/prueba.txt";
    }

    @After
    public void clean() {
        path = null;
    }

    @Test
    public void readTotal() {
        try {
            double total = JsonManager.obtainTotalFromFile(path);
            assertEquals(7.0, total);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}