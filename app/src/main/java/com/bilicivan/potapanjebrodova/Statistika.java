package com.bilicivan.potapanjebrodova;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Statistika extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Button povratak = findViewById(R.id.povratak);
        povratak.setOnClickListener(view -> this.finish());

        SharedPreferences preferences = getSharedPreferences("com.bilicivan.potapanjebrodova.postavke", MODE_PRIVATE);

        DecimalFormat petDecimala = new DecimalFormat("#.#####");
        petDecimala.setRoundingMode(RoundingMode.HALF_UP);

        DecimalFormat dvijeDecimale = new DecimalFormat("#.##");
        petDecimala.setRoundingMode(RoundingMode.HALF_UP);

        TextView pobjede = findViewById(R.id.pobjede);
        TextView porazi = findViewById(R.id.porazi);
        TextView ukupno = findViewById(R.id.sveukupno);
        TextView omjer = findViewById(R.id.omjer);

        TextView pobjedeTesko = findViewById(R.id.pobjedeTesko);
        TextView stopaTesko = findViewById(R.id.stopaTesko);

        TextView pobjedeSrednje = findViewById(R.id.pobjedeSrednje);
        TextView stopaSrednje = findViewById(R.id.stopaSrednje);

        TextView pobjedeLagano = findViewById(R.id.pobjedeLagano);
        TextView stopaLagano = findViewById(R.id.stopaLagano);

        int brojPobjedeUkupno = preferences.getInt("pobjede_ukupno", 0);
        int brojPoraziUkupno = preferences.getInt("porazi_ukupno", 0);
        int brojUkupno = brojPobjedeUkupno + brojPoraziUkupno;
        String brojOmjer = petDecimala.format((float) brojPobjedeUkupno / brojPoraziUkupno);

        pobjede.setText(String.valueOf(brojPobjedeUkupno));
        porazi.setText(String.valueOf(brojPoraziUkupno));
        ukupno.setText(String.valueOf(brojUkupno));
        omjer.setText(brojOmjer);

        int brojPobjedeTesko = preferences.getInt("pobjede_tesko", 0);
        int brojIgreTesko = brojPobjedeTesko + preferences.getInt("porazi_tesko", 0);
        String postotakStopaTesko = dvijeDecimale.format(100 * (float) brojPobjedeTesko / brojIgreTesko) + getString(R.string.postotak);

        pobjedeTesko.setText(String.valueOf(brojPobjedeTesko));
        if (brojIgreTesko != 0) {
            stopaTesko.setText(postotakStopaTesko);
        }

        int brojPobjedeSrednje = preferences.getInt("pobjede_srednje", 0);
        int brojIgreSrenje = brojPobjedeSrednje + preferences.getInt("porazi_srednje", 0);
        String postotakStopaSrednje = dvijeDecimale.format(100 * (float) brojPobjedeSrednje / brojIgreSrenje) + getString(R.string.postotak);

        pobjedeSrednje.setText(String.valueOf(brojPobjedeSrednje));
        if (brojIgreSrenje != 0) {
            stopaSrednje.setText(postotakStopaSrednje);
        }

        int brojPobjedeLagano = preferences.getInt("pobjede_lagano", 0);
        int brojIgreLagano = brojPobjedeLagano + preferences.getInt("porazi_lagano", 0);
        String postotakStopaLagano = dvijeDecimale.format(100 * (float) brojPobjedeLagano / brojIgreLagano) + getString(R.string.postotak);

        pobjedeLagano.setText(String.valueOf(brojPobjedeLagano));
        if (brojIgreLagano != 0) {
            stopaLagano.setText(postotakStopaLagano);
        }
    }
}