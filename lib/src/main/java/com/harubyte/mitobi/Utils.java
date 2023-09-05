package com.harubyte.mitobi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {
    /*
    * Writing file
    *
    * @param file    Input file
    * @param path    Output path
    */
    public static void write(File file, String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(path)) {
            byte[] buffer = new byte[1024 * 4];
            int read = 0;
            
            while ((read = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
        } 
    }
    
    /*
    * Delete a directory and its children
    *
    * @param files    The File of the directory
    * @return         truthy if all files deleted, otherwise falsy
    */
    public static boolean removeAll (File files) {
        File[] contents = files.listFiles();
        if (contents != null) {
            for (File file : contents) {
                removeAll(file);
            }
        }
        
        return files.delete();
    }
}
