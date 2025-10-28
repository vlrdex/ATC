package org.example.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    public static String getProperty(String filename){
        Properties properties = new Properties();
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                System.err.println("config.properties not found on classpath");
                return "";
            }
            properties.load(in);
            return properties.getProperty(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
