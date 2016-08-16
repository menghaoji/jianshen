/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author menghaoji
 */
public class PropertyReader {
    public static String getProperty(String key){
        Properties prop = new Properties();
        String value = "";
        InputStream in = PropertyReader.class.getResourceAsStream("/system.properties");   
        try {   
            prop.load(in);   
            value = prop.getProperty(key).trim();
        } catch (IOException e) {   
            e.printStackTrace();   
        }
        return value;
    } 
//    public static void main(String[] args){
//        System.out.println(PropertyReader.getProperty("test.latestTime"));
//    }
}
