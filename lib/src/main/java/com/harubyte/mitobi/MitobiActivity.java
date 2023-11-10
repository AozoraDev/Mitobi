package com.harubyte.mitobi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MitobiActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
        
        new Mitobi(this)
        .setOnDialogClosed(() -> {
            Intent intent = new Intent(Configs.ACTION);
            startActivity(intent);
        });
    }
}
