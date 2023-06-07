package com.bilicivan.potapanjebrodova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Handler handler = new Handler();
    Toast toast;

    Button tezina1;
    Button tezina2;
    Button tezina3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = getSharedPreferences("com.bilicivan.potapanjebrodova.postavke", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Switch shipLabels = findViewById(R.id.switch1);
        Switch gridValues = findViewById(R.id.switch2);
        EditText delay = findViewById(R.id.editTextNumber);

        tezina1 = findViewById(R.id.tezina1);
        tezina2 = findViewById(R.id.tezina2);
        tezina3 = findViewById(R.id.tezina3);

        boje(preferences.getInt("tezina_igre", 1));

        ImageView background1 = findViewById(R.id.imageView);
        ImageView background2 = findViewById(R.id.imageView2);
        ImageView background3 = findViewById(R.id.imageView3);

        Button cancel = findViewById(R.id.settingsPonisti);
        Button apply = findViewById(R.id.settingsPrimijeni);

        boolean shipLabelsValue = preferences.getBoolean("ship_labels", false);
        boolean gridValuesValue = preferences.getBoolean("grid_values", false);
        String moveDelayValue = Integer.toString(preferences.getInt("move_delay", 3000));

        shipLabels.setChecked(shipLabelsValue);
        gridValues.setChecked(gridValuesValue);
        delay.setText(moveDelayValue);

        tezina1.setOnClickListener(view -> {
            editor.putInt("tezina_igre", 1);
            toast("težina");
            boje(1);
        });

        tezina2.setOnClickListener(view -> {
            editor.putInt("tezina_igre", 2);
            toast("težina");
            boje(2);
        });

        tezina3.setOnClickListener(view -> {
            editor.putInt("tezina_igre", 3);
            toast("težina");
            boje(3);
        });

        background1.setOnClickListener(view -> {
            editor.putInt("background_image", R.drawable.background1);
            toast("pozadina");
        });

        background2.setOnClickListener(view -> {
            editor.putInt("background_image", R.drawable.background2);
            toast("pozadina");
        });

        background3.setOnClickListener(view -> {
            editor.putInt("background_image", R.drawable.background3);
            toast("pozadina");
        });

        cancel.setOnClickListener(view -> {
            try {
                toast.cancel();
            } catch (Exception e) {
                Log.i("TAG", "Toast nije definiran");
            }
            this.finish();
        });

        apply.setOnClickListener(view -> {
            editor.putBoolean("ship_labels", shipLabels.isChecked());
            editor.putBoolean("grid_values", gridValues.isChecked());
            if (!delay.getText().toString().equals("")) {
                editor.putInt("move_delay", Integer.parseInt(delay.getText().toString()));
            }
            editor.apply();
            try {
                toast.cancel();
            } catch (Exception e) {
                Log.i("TAG", "Toast nije definiran");
            }

            Intent intent = new Intent("zavrsi_aktivnost");
            sendBroadcast(intent);

            this.finish();
            startActivity(new Intent(Settings.this, MainMenuActivity.class));
        });
    }

    private void toast (String akcija) {

        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(getApplicationContext(), akcija + " odabrana", Toast.LENGTH_SHORT);
        toast.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 800);
    }

    private void boje (int tezina) {

        tezina1.setBackgroundColor(getResources().getColor(R.color.light_gray));
        tezina2.setBackgroundColor(getResources().getColor(R.color.light_gray));
        tezina3.setBackgroundColor(getResources().getColor(R.color.light_gray));

        switch (tezina) {
            case 1:
                tezina1.setBackgroundColor(getResources().getColor(R.color.steel_blue));
                break;
            case 2:
                tezina2.setBackgroundColor(getResources().getColor(R.color.steel_blue));
                break;
            case 3:
                tezina3.setBackgroundColor(getResources().getColor(R.color.steel_blue));
                break;
        }
    }
}