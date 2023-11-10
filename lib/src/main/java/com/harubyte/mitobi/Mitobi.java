package com.harubyte.mitobi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mitobi {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private Context context;
    private Handler handler;
    private File data, externalData;
    private AlertDialog processDialog, mainDialog;
    
    public Mitobi(Context context) {
        this.context = context;
        
        // Set up some needed stuff
        this.handler = new Handler(context.getMainLooper());
        this.data = context.getDataDir();
        this.externalData = context.getExternalFilesDir("Mitobi");
        this.processDialog = new AlertDialog.Builder(context)
        .setCancelable(false)
        .create();
        
        Log.i(Configs.NAME, Configs.TITLE + " initiated!");
        
        start();
    }
    
    /*
    * Start the Mitobi (not back for default)
    */
    public void start() {
        start(false); // Just for fallback
    }
    
    /*
    * Start the Mitobi
    *
    * @param back    Is it just back to main dialog or not
    */
    public void start(Boolean back) {
        // If not back, create new dialog
        if (!back) {
            Log.i(Configs.NAME, "Started!");
            
            StringBuilder sb = new StringBuilder();
            ApplicationInfo ai = context.getApplicationInfo();
            String appLabel = ai.labelRes == 0 ? ai.nonLocalizedLabel.toString() : context.getString(ai.labelRes);
            
            sb.append("| " + appLabel + " |\n");
            sb.append(ai.packageName);
            sb.append("\n\nData 1: " + data.toPath());
            sb.append("\n\nData 2: " + externalData.getParentFile().toPath());
            
            mainDialog = new AlertDialog.Builder(context)
            .setTitle(Configs.TITLE)
            .setMessage(sb.toString())
            .setPositiveButton("Backup", (d, w) -> backupDialog())
            .setNegativeButton("Restore", (d, w) -> restoreDialog())
            .setNeutralButton("Close", (d, w) -> {
                // Don't shutdown the executor and handler if its a test app
                if (!ai.packageName.equals("com.harubyte.mitobiapp")) {
                    kill();
                    Log.i(Configs.NAME, "Handler and Executor terminated!");
                }
                
                Log.i(Configs.NAME, "Terminated!");
            })
            .setCancelable(false)
            .show();
        } else {
            // Otherwise, show the already created mainDialog
            mainDialog.show();
        }
    }
    
    // YWAHH!!!!
    public void kill() {
        executor.shutdownNow();
        handler = null;
    }
    
      /////////////
     // Dialogs //
    /////////////
    
    /*
    * Show dialog for restore data
    */
    private void restoreDialog() {
        StringBuilder sb = new StringBuilder();
        String fileLists = getFilesList(externalData);
        
        sb.append("WARNING: THE INTERNAL DATA OF THIS APPLICATION WILL BE DELETED\n\n");
        sb.append("The files to be restored: ");
        sb.append(fileLists);
        
        AlertDialog dialog = new AlertDialog.Builder(context)
        .setTitle(Configs.TITLE)
        .setMessage(sb.toString())
        .setCancelable(false)
        .setPositiveButton("Restore", null)
        .setNegativeButton("Back", (d, w) -> start(true))
        .create();
        
        dialog.setOnShowListener((d) -> {
            Button positiveButton = ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE);
            
            if (fileLists == "Empty") positiveButton.setEnabled(false);
            positiveButton.setOnClickListener(v -> {
                processDialog.setMessage("Restoring...");
                processDialog.show();
                
                executor.execute(() -> {
                    Utils.removeAll(data);
                    restore(externalData, true);
                });
            });
        });
        
        dialog.show();
    }
    
    /*
    * Show dialog for backup data
    */
    private void backupDialog() {
        StringBuilder sb = new StringBuilder();
        String fileLists = getFilesList(data);
        
        sb.append("WARNING: PREVIOUSLY BACKED-UP DATA WILL BE DELETED\n\n");
        sb.append("The files to be backed up: ");
        sb.append(fileLists);
        
        AlertDialog dialog = new AlertDialog.Builder(context)
        .setTitle(Configs.TITLE)
        .setMessage(sb.toString())
        .setPositiveButton("Backup", null)
        .setNegativeButton("Backup (with cache)", null)
        .setNeutralButton("Back", (d, w) -> start(true))
        .setCancelable(false)
        .create();
        
        // Using this to make the AlertDialog not dismissed when the button pressed
        dialog.setOnShowListener((d) -> {
            // Backup (Without cache)
            Button positiveButton = ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE);
            
            if (fileLists == "Empty") positiveButton.setEnabled(false);
            positiveButton.setOnClickListener(v -> {
                processDialog.setMessage("Backing up...");
                processDialog.show();
                
                executor.execute(() -> {
                    Utils.removeAll(externalData);
                    backup(data, false, true);
                });
            });
            
            // Backup (With cache)
            Button negativeButton = ((AlertDialog) d).getButton(AlertDialog.BUTTON_NEGATIVE);
            
            if (fileLists == "Empty") negativeButton.setEnabled(false);
            negativeButton.setOnClickListener(v -> {
                processDialog.setMessage("Backing up (with cache)...");
                processDialog.show();
                
                executor.execute(() -> {
                    Utils.removeAll(externalData);
                    backup(data, true, true);
                });
            });
        });
        
        dialog.show();
    }
    
      /////////////
     // Methods //
    /////////////
    
    /*
    * Restore external data to internal data
    *
    * @param files            Input directory as File
    * @param isMainProcess    Check if it's the main process
    */
    private void restore(File files, boolean isMainProcess) {
        for (File file : files.listFiles()) {
            // Need to convert external path to internal path first.
            String path = file.getAbsolutePath().replaceAll(externalData.getAbsolutePath(), data.getAbsolutePath());
            
            Log.i(Configs.NAME, "Restoring " + file.getAbsolutePath() + "...");
            
            // If current loop is directory,
            // create the dir and call backup again.
            if (file.isDirectory()) {
                File newDir = new File(path);
                if (!newDir.exists()) newDir.mkdirs();
                
                restore(file, false);
                continue;
            }
            
            try {
                Utils.write(file, path);
            } catch (Exception e) {
                handler.post(() -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                Log.e(Configs.NAME, e.getMessage());
                break;
            }
        }
        
        if (isMainProcess) {
            handler.post(() -> {
                Toast.makeText(context, "All data restored successfully!\nPlease re-open the app.", Toast.LENGTH_LONG).show();
                processDialog.dismiss();
                    
                kill();
                ((Activity) context).finishAffinity();
            });
        }
    }
    
    /*
    * Backup internal data to external data
    *
    * @param files            Input directories as File
    * @param withCache        Make the internal cache backed up also
    * @param isMainProcess    Check if it's the main process
    */
    private void backup(File files, boolean withCache, boolean isMainProcess) {
        for (File file : files.listFiles()) {
            // Need to convert internal path to external path first.
            String path = file.getAbsolutePath().replaceAll(data.getAbsolutePath(), externalData.getAbsolutePath());
            if (!withCache && path.contains("cache")) continue;
            
            Log.i(Configs.NAME, "Backing up " + file.getAbsolutePath() + "...");
            
            // If current loop is directory,
            // create the dir and call backup again.
            if (file.isDirectory()) {
                File newDir = new File(path);
                if (!newDir.exists()) newDir.mkdirs();
                
                backup(file, false, false);
                continue;
            }
            
            try {
                Utils.write(file, path);
            } catch (Exception e) {
                handler.post(() -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                Log.e(Configs.NAME, e.getMessage());
                break;
            }
        }
        
        if (isMainProcess) {
            handler.post(() -> {
                Toast.makeText(context, "All data backed up successfully!", Toast.LENGTH_LONG).show();
                processDialog.dismiss();
            });
        }
    }
    
    /*
    * Get list of the files for StringBuilder
    *
    * @param files    The File of the directoey
    * @return         List of the files
    */
    private String getFilesList(File files) {
        if (files.list().length == 0) return "Empty";
        
        String mainPath = (files.getAbsolutePath().contains(data.getAbsolutePath()))
        ? data.getAbsolutePath()
        : externalData.getAbsolutePath();
        
        StringBuilder sb = new StringBuilder();
        for (File file : files.listFiles()) {
            String path = file.getAbsolutePath().replaceAll(mainPath, "");
            sb.append("\n- " + path);
            if (file.isDirectory() && file.list().length > 0) sb.append(getFilesList(file));
        }
        
        return sb.toString();
    }
}
