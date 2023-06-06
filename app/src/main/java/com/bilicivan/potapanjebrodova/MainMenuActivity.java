package com.bilicivan.potapanjebrodova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("com.bilicivan.potapanjebrodova.postavke", Context.MODE_PRIVATE);

        int background = preferences.getInt("background_image", R.drawable.background1);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg, Intent intent) {
                String action = intent.getAction();
                if (action.equals("zavrsi_aktivnost")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("zavrsi_aktivnost"));

        ConstraintLayout mainMenu = findViewById(R.id.mainMenuLayout);
        mainMenu.setBackgroundResource(background);

        Button pokreni = findViewById(R.id.buttonPokreni);
        pokreni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGameActivity();
            }
        });

        Button postavke = findViewById(R.id.buttonPostavke);
        postavke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, Settings.class));
            }
        });

        Button statistika = findViewById(R.id.buttonStatistika);

        statistika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, Statistika.class));
            }
        });
    }

    public void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}