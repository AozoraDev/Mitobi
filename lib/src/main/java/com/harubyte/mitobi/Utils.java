package com.harubyte.mitobi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
    * Reading content inside a file
    *
    * @param file    Input of the file
    */
    public static String read(File file) throws IOException {
        try (FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr)) {
            String line;
            StringBuilder sb = new StringBuilder();
            
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            return sb.toString();
        }
    }
    
    /*
    * Writing string to file
    *
    * @param path       Output path for the file
    * @param content    The content to write
    */
    public static void writeString(File file, String content) throws IOException {
        try (FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
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
    
    /*
    * Check storage permission for saving data externally.
    * Since Android 29+ require some bullshit to access the storage,
    * Permission check only executed for Android 28 and below.
    *
    * @return    `true` if has storage permission, `false` if not.
    */
    public static boolean checkStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            return (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED);
        } else {
            // Above Android 9, falsy.
            return false;
        }
    }
}
