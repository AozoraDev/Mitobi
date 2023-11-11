package com.harubyte.mitobi;

import android.content.Context;
import android.content.res.Configuration;
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
    * Delete all contents inside the directory
    *
    * @param files    The directory as File
    */
    public static void removeAll (File files) {
        File[] contents = files.listFiles();
        if (contents != null) for (File file : contents) {
            if (file.isDirectory()) {
                removeAll(file);
            }
            
            file.delete();
        }
    }
    
    /*
    * Check if the device is in dark mode
    *
    * @param context    Activity context
    */
    public static boolean isNightMode(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode
        & Configuration.UI_MODE_NIGHT_MASK;
        
        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            return true;
        } else {
            return false;
        }
    }
}
