package com.bilicivan.potapanjebrodova;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import java.util.Arrays;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GameActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    SharedPreferences preferences;
    Handler handler = new Handler();

    Ploca mojaPloca;
    Ploca njegovaPloca;

    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    MyRecyclerViewAdapter adapter2;

    TextView text;

    boolean naPotezu = false;
    boolean igraGotova = false;

    int runda = 0;
    int delay;

    @SuppressLint("NotifyDataSetChanged")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        preferences = getSharedPreferences("com.bilicivan.potapanjebrodova.postavke", MODE_PRIVATE);
        delay = preferences.getInt("move_delay", 3000);

        mojaPloca = new Ploca(this, true);
        njegovaPloca = new Ploca(this, false);

        recyclerView = findViewById(R.id.gameGrid);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 10));

        mojaPloca.randomizacijaPolozaja();

        adapter = new MyRecyclerViewAdapter(this, mojaPloca.data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        ImageButton button = findViewById(R.id.buttonRepeat);
        ImageButton button2 = findViewById(R.id.buttonConfirm);

        njegovaPloca.randomizacijaPolozaja();

        adapter2 = new MyRecyclerViewAdapter(this, njegovaPloca.data);
        adapter2.setClickListener(this);

        button.setOnClickListener(view -> {
            Log.i("TAG", Arrays.toString(mojaPloca.data));
            mojaPloca.clear();
            mojaPloca.randomizacijaPolozaja();
            adapter.notifyDataSetChanged();
        });

        button2.setOnClickListener(view -> {
            handler.postDelayed(new Runnable() {
                public void run() {

                    recyclerView.setAdapter(adapter2);
                    button.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);

                    naPotezu = true;
                }
            }, 350);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void simulacijaIgre(int pozicija) {

        runda++;

        if (njegovaPloca.pogodak(pozicija).equals("potopljen")) {
            adapter2.notifyDataSetChanged();
            if (njegovaPloca.fregata[0].isPotopljen() &&
                    njegovaPloca.fregata[1].isPotopljen() &&
                    njegovaPloca.korveta.isPotopljen() &&
                    njegovaPloca.bojniBrod.isPotopljen() &&
                    njegovaPloca.nosacZrakoplova.isPotopljen())
            {
                igraGotova = true;
                pobjeda(true);
            }
        } else {
            adapter2.notifyItemChanged(pozicija);
        }

        if (igraGotova) return;

        naPotezu = false;

        handler.postDelayed(new Runnable() {
            public void run() {
                recyclerView.setAdapter(adapter);
            }
        }, Math.round(delay / 3.0));

        handler.postDelayed(new Runnable() {
            public void run() {

                int polje = mojaPloca.computerPlayedUnit();
                adapter.notifyItemChanged(polje);

                if (mojaPloca.pogodak(polje).equals("potopljen")) {
                    if (mojaPloca.fregata[0].isPotopljen() &&
                            mojaPloca.fregata[1].isPotopljen() &&
                            mojaPloca.korveta.isPotopljen() &&
                            mojaPloca.bojniBrod.isPotopljen() &&
                            mojaPloca.nosacZrakoplova.isPotopljen()) {
                        igraGotova = true;
                        pobjeda(false);
                    }
                }
            }
        }, Math.round(delay / 1.5));

        handler.postDelayed(new Runnable() {
            public void run() {
                if (igraGotova) return;
                recyclerView.setAdapter(adapter2);
                naPotezu = true;
            }
        }, delay);
    }

    private void pobjeda(boolean pobijedio) {

        text = findViewById(R.id.pobjednik);
        SharedPreferences.Editor editor = preferences.edit();

        if (pobijedio) {
            text.setText(getResources().getString(R.string.pobjeda));
            editor.putInt("pobjede_ukupno", preferences.getInt("pobjede_ukupno", 0) + 1);
            if (preferences.getInt("tezina_igre", 1) == 1) {
                editor.putInt("pobjede_lagano", preferences.getInt("pobjede_lagano", 0) + 1);
            } else {
                if (preferences.getInt("tezina_igre", 1) == 2) {
                    editor.putInt("pobjede_srednje", preferences.getInt("pobjede_srednje", 0) + 1);
                } else {
                    if (preferences.getInt("tezina_igre", 1) == 3) {
                        editor.putInt("pobjede_tesko", preferences.getInt("pobjede_tesko", 0) + 1);
                    }
                }
            }
        } else {
            text.setText(getResources().getString(R.string.gubitak));
            editor.putInt("porazi_ukupno", preferences.getInt("porazi_ukupno", 0) + 1);

            if (preferences.getInt("tezina_igre", 1) == 1) {
                editor.putInt("porazi_lagano", preferences.getInt("porazi_lagano", 0) + 1);
            } else {
                if (preferences.getInt("tezina_igre", 1) == 2) {
                    editor.putInt("porazi_srednje", preferences.getInt("porazi_srednje", 0) + 1);
                } else {
                    if (preferences.getInt("tezina_igre", 1) == 3) {
                        editor.putInt("porazi_tesko", preferences.getInt("porazi_tesko", 0) + 1);
                    }
                }
            }
        }

        editor.apply();

        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 3000);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "Your board: You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        Log.i("TAG", "Enemy board: You clicked number "+ adapter2.getItem(position) + ", which is at cell position " + position);

        if (naPotezu && !igraGotova) {
            if (!njegovaPloca.odigranoPolje(position)) {
                simulacijaIgre(position);
            }
        }
    }
}