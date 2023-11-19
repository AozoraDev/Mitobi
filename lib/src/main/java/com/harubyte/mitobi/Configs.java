package com.harubyte.mitobi;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class Configs {
    public static final String NAME = "Mitobi";
    public static final String VERSION = "1.2";
    public static final String TITLE = String.format("%s (v%s)", NAME, VERSION);
    public static final String ACTION = "com.harubyte.mitobi.action.MAIN";
    
    /*
    * Default configuration for Mitobi
    *
    * @return    Default configuration
    */
    public static JSONObject defaultConfig() {
        JSONObject config = new JSONObject();
        
        try {
            config.put("autoBackup", false);
            config.put("autoBackup_withCache", false);
            config.put("useMaterialTheme", true);
            config.put("forceDarkMode", false);
            config.put("hideDialog", false);
            config.put("useExternalStorage", false);
        } catch (JSONException err) {
            // Nothing i can do.
        }
        
        return config;
    }
    
    /*
    * Create or update the config file
    *
    * @param file    Output for config file
    */
    public static void setupConfig(File file) throws JSONException, IOException {
        // Get default config
    	JSONObject config = defaultConfig();
        
        if (file.exists()) {
            // It's exist. We should update it or nah.
            
            // Saved config file
            JSONObject savedConfig = new JSONObject(Utils.read(file));
            
            // Update saved config if not same with default config
            Iterator<String> keys = config.keys();
            boolean isSame = true;
            while (keys.hasNext()) {
                String key = keys.next();
                
                if (savedConfig.has(key)) continue; // Already exist.
                if (isSame) isSame = false; // Once this code area executed, then the config is not same.
                
                savedConfig.put(key, config.get(key)); // Not exist. Add it.
            }
            
            // Write it back if not same
            if (!isSame) Utils.writeString(file, savedConfig.toString(2));
        } else {
            // Not exist. Create new config file.
            Log.w(NAME, "Config file is not exist. Creating new one.");
            Utils.writeString(file, config.toString(2));
        }
    }
    
    /*
    * List all configuration as String
    *
    * @param config    The configuration as JSONObject
    */
    public static String listConfigs(JSONObject config) {
        StringBuilder sb = new StringBuilder();
        JSONObject dc = defaultConfig();
        Iterator<String> iterator = dc.keys();
        
        while (iterator.hasNext()) {
            String key = iterator.next();
            sb.append(String.format("- %s: %s\n",
                key,
                (config.has(key)) ? config.opt(key) : dc.opt(key)
            ));
        }
        
        return sb.toString();
    }
}