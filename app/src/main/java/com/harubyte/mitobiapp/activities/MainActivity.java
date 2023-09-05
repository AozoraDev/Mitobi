package com.harubyte.mitobiapp.activities;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.Toolbar;

import com.harubyte.mitobiapp.R;
import com.harubyte.mitobi.Mitobi;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Mitobi mitobi = new Mitobi(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        
        Button show = (Button) findViewById(R.id.show);
        show.setOnClickListener((v) -> {
            mitobi.start();
        });
    }
}
