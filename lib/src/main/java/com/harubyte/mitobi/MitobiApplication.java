package com.harubyte.mitobi;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.os.Bundle;

public class MitobiApplication extends Application implements ActivityLifecycleCallbacks {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }
    
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Intent intent = activity.getIntent();
        
        if (intent.getAction().equals(Intent.ACTION_MAIN)
        && intent.hasCategory(Intent.CATEGORY_LAUNCHER)) {
            new Mitobi(activity);
        }
    }
    
    @Override
    public void onActivityStarted(Activity activity) {
        // TODO: Implement this method
    }
    
    @Override
    public void onActivityResumed(Activity activity) {
        // TODO: Implement this method
    }
    
    @Override
    public void onActivityStopped(Activity activity) {
        // TODO: Implement this method
    }
    
    @Override
    public void onActivityPaused(Activity activity) {
        // TODO: Implement this method
    }
    
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        // TODO: Implement this method
    }
    
    @Override
    public void onActivityDestroyed(Activity activity) {
        // TODO: Implement this method
    }
    
}