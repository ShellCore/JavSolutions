package com.shellcore.java.javsolutions.files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by Cesar. 09/06/2017.
 */
public class FileManagerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private String fileName;
    private String content;

    @Before
    public void setupFiles() {
        fileName = "prueba.txt";
        content = "{\n    \"invoice\":{\n        \"products\":[\n            {\"name\":\"Food\", \"price\":\"5.00\"}\n            {\"name\":\"Drink\", \"price\":\"2.00\"}\n        ],\n        \"total\":\"7.00\"\n    }\n}";
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanTest() {
        System.setOut(null);
        fileName = null;
        content = null;
    }

    @Test
    public void testExample() {
        boolean res = FileManager.saveFile(fileName, content);
        assertEquals(true, res);
    }

}