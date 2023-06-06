package com.bilicivan.potapanjebrodova;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Ploca {

    SharedPreferences preferences;

    boolean gridValues;
    boolean shipLabels;

    Random random = new Random();
    int tezina;
    boolean prviPogodak = false;

    boolean vlastita;
    ArrayList<Integer> aktivnaPolja = new ArrayList<>(17);

    NosacZrakoplova nosacZrakoplova;
    BojniBrod bojniBrod;
    Fregata[] fregata = new Fregata[2];
    Korveta korveta;

    public Ploca (Context context, boolean vlastita) {

        this.vlastita = vlastita;
        preferences = context.getSharedPreferences("com.bilicivan.potapanjebrodova.postavke", Context.MODE_PRIVATE);
        gridValues = preferences.getBoolean("grid_values", false);
        shipLabels = preferences.getBoolean("ship_labels", false);

        tezina = preferences.getInt("tezina_igre", 1);
        if (tezina == 1) tezina = 25;
        if (tezina == 2) tezina = 20;
        if (tezina == 3) tezina = 23;

        nosacZrakoplova = new NosacZrakoplova("Nosac zrakoplova", vlastita);
        bojniBrod = new BojniBrod("Bojni brod", vlastita);

        for (int i = 0; i < 2; i++) {
            fregata[i] = new Fregata("Fregata " + i, vlastita);
        }

        korveta = new Korveta("Korveta", vlastita);


        if (gridValues) {
            System.arraycopy(data, 0, dataBackup, 0, 100);
        } else {
            Arrays.fill(dataBackup, "");
            clear();
        }
    }

    String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
            "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
            "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52",
            "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65",
            "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78",
            "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91",
            "92", "93", "94", "95", "96", "97", "98", "99", "100"};

    String[] dataBackup = new String[100];

    public void clear() {
        System.arraycopy(dataBackup, 0, data, 0, 100);
    }

    private void labeli () {

        ArrayList<Character> labelsList = new ArrayList<>(5);

        labelsList.add('A');
        labelsList.add('B');
        labelsList.add('C');
        labelsList.add('D');
        labelsList.add('E');

        Collections.shuffle(labelsList);

        nosacZrakoplova.setLabel(labelsList.get(0));
        bojniBrod.setLabel(labelsList.get(1));
        fregata[0].setLabel(labelsList.get(2));
        fregata[1].setLabel(labelsList.get(3));
        korveta.setLabel(labelsList.get(4));
    }

    public void randomizacijaPolozaja() {

        int duljina;

        if (shipLabels) {
            labeli();
        }

        // Brod s duljinom 5:
        duljina = nosacZrakoplova.getDuljina();
        nosacZrakoplova.resetCelije();

        if (random.nextBoolean()) {
            nosacZrakoplova.setCelije(postavljanjePolozaja(duljina, "vertikalno"));
        } else {
            nosacZrakoplova.setCelije(postavljanjePolozaja(duljina, "horizontalno"));
        }

        if(vlastita) {
            for (int celija : nosacZrakoplova.getCelije()) {
                data[celija] = "\uD83D\uDD34" + nosacZrakoplova.getLabel();
            }
        }

        nosacZrakoplova.postavljen();

        // Brod s duljinom 4:
        duljina = bojniBrod.getDuljina();

        do {

            bojniBrod.resetCelije();

            if (random.nextBoolean()) {
                bojniBrod.setCelije(postavljanjePolozaja(duljina, "vertikalno"));
            } else {
                bojniBrod.setCelije(postavljanjePolozaja(duljina, "horizontalno"));
            }
        } while(provjeraPolozaja(bojniBrod.getCelije()));

        if(vlastita) {
            for (int celija : bojniBrod.getCelije()) {
                data[celija] = "\uD83D\uDFE2" + bojniBrod.getLabel();
            }
        }

        bojniBrod.postavljen();

        // Brodovi s duljinom 3:
        duljina = fregata[0].getDuljina();

        for (int j = 0; j < 2; j++) {

            do {

                fregata[j].resetCelije();

                if (random.nextBoolean()) {
                    fregata[j].setCelije(postavljanjePolozaja(duljina, "vertikalno"));
                } else {
                    fregata[j].setCelije(postavljanjePolozaja(duljina, "horizontalno"));
                }

            } while (provjeraPolozaja(fregata[j].getCelije()));

            if(vlastita) {
                for (int celija : fregata[j].getCelije()) {
                    data[celija] = "\uD83D\uDD35" + fregata[j].getLabel();
                }
            }

            fregata[j].postavljen();
        }

        // Brod s duljionom 2:
        duljina = korveta.getDuljina();

        do {

            korveta.resetCelije();

            if (random.nextBoolean()) {
                korveta.setCelije(postavljanjePolozaja(duljina, "vertikalno"));
            } else {
                korveta.setCelije(postavljanjePolozaja(duljina, "horizontalno"));
            }

        } while (provjeraPolozaja(korveta.getCelije()));

        if(vlastita) {
            for (int celija : korveta.getCelije()) {
                data[celija] = "\uD83D\uDFE1" + korveta.getLabel();
            }
        }

        korveta.postavljen();

        aktivnaPolja.clear();

        aktivnaPolja.addAll(nosacZrakoplova.getCelije());
        aktivnaPolja.addAll(bojniBrod.getCelije());
        aktivnaPolja.addAll(fregata[0].getCelije());
        aktivnaPolja.addAll(fregata[1].getCelije());
        aktivnaPolja.addAll(korveta.getCelije());

        Log.i("TAG", nosacZrakoplova.getCelije().toString());
        Log.i("TAG", bojniBrod.getCelije().toString());
        Log.i("TAG", fregata[0].getCelije().toString());
        Log.i("TAG", fregata[1].getCelije().toString());
        Log.i("TAG", korveta.getCelije().toString());
    }

    private boolean provjeraPolozaja (ArrayList<Integer> celije){

        // Opcionalno: Umjesto da provjerava sve čelije odabranog broda sa svakom
        //             iz drugih brodova neka provjerava jednu po jednu čeliju odabranog
        //             broda sa svakom čelijom ostalih brodova, ali i ovako radi.

        for (int i : nosacZrakoplova.getCelije()) {
            for (int j : celije) {
                if (i == j) {
                    return true;
                }
            }
        }

        if (celije.size() < 4) {

            for (int i : bojniBrod.getCelije()) {
                for (int j : celije) {
                    if (i == j) {
                        return true;
                    }
                }
            }

            for (int i : fregata[0].getCelije()) {
                for (int j : fregata[1].getCelije()) {
                    if (i == j) {
                        return true;
                    }
                }
            }

            for (int index = 0; index < 2; index++) {
                if (celije.size() == 2) {
                    for (int i : fregata[index].getCelije()) {
                        for (int j : celije) {
                            if (i == j) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private ArrayList<Integer> postavljanjePolozaja (int duljina, String orijentacija) {

        int startnoPolje;
        ArrayList<Integer> polozaj = new ArrayList<>(duljina);

        if (orijentacija.equals("vertikalno")) {
            startnoPolje = random.nextInt(109 - (duljina * 10));

            for (int i = 0; i < duljina; i++) {
                polozaj.add(startnoPolje + (10 * i));
            }
        }

        if (orijentacija.equals("horizontalno")) {
            do {
                startnoPolje = random.nextInt(99);
            } while (startnoPolje % 10 > 10 - duljina);

            for (int i = 0; i < duljina; i++) {
                polozaj.add(startnoPolje + i);
            }
        }
        return polozaj;
    }

    public boolean odigranoPolje (int pozicija) {

        return dataBackup[pozicija].equals("0");
    }

    public int computerPlayedUnit() {

        boolean provjera;

        int pozicija = 0;

        do {
            provjera = false;

            if (random.nextInt(100) < tezina) {
                if (prviPogodak) {
                    if (preferences.getInt("tezina_igre", 1) != 3) {
                        do {
                            pozicija = aktivnaPolja.get(random.nextInt(aktivnaPolja.size()));
                        } while (dataBackup[pozicija].equals("0"));
                        if (preferences.getInt("tezina_igre", 1) == 2) tezina += 1;
                    }
                } else {
                    if (preferences.getInt("tezina_igre", 1) == 1 ||
                            preferences.getInt("tezina_igre", 1) == 2) {
                        do {
                            pozicija = random.nextInt(100);
                        } while (dataBackup[pozicija].equals("0"));
                    }
                }

                if (preferences.getInt("tezina_igre", 1) == 3) {
                    do {
                        pozicija = aktivnaPolja.get(random.nextInt(aktivnaPolja.size()));
                    } while (dataBackup[pozicija].equals("0"));
                    tezina += 2;
                }

            } else {
                pozicija = random.nextInt(100);
            }

            if (dataBackup[pozicija].equals("0")) {
                provjera = true;
            }
        } while (provjera);

        return pozicija;
    }

    public String pogodak (int pozicija) {

        // Optimizirati kod po potrebi

        for (int i : nosacZrakoplova.getCelije()) {
            if (i == pozicija) {
                prviPogodak = true;
                data[pozicija] = "⚫" + nosacZrakoplova.getLabel();
                dataBackup[pozicija] = "0";
                nosacZrakoplova.pogodak(pozicija);
                if (nosacZrakoplova.isPotopljen()) {
                    if (!vlastita) {
                        for (int k : nosacZrakoplova.getCelije()) {
                            data[k] = "\uD83D\uDD34" + nosacZrakoplova.getLabel();
                        }
                    }
                    return "potopljen";
                }
                return "pogodak";
            }
        }

        for (int i : bojniBrod.getCelije()) {
            if (i == pozicija) {
                prviPogodak = true;
                data[pozicija] = "⚫" + bojniBrod.getLabel();
                dataBackup[pozicija] = "0";
                bojniBrod.pogodak(pozicija);
                if (bojniBrod.isPotopljen()) {
                    if (!vlastita) {
                        for (int k : bojniBrod.getCelije()) {
                            data[k] = "\uD83D\uDFE2" + bojniBrod.getLabel();
                        }
                    }
                    return "potopljen";
                }
                return "pogodak";
            }
        }

        for (int j = 0; j < 2; j++) {
            for (int i : fregata[j].getCelije()) {
                if (i == pozicija) {
                    prviPogodak = true;
                    data[pozicija] = "⚫" + fregata[j].getLabel();
                    dataBackup[pozicija] = "0";
                    fregata[j].pogodak(pozicija);
                    if (fregata[j].isPotopljen()) {
                        if (!vlastita) {
                            for (int k : fregata[j].getCelije()) {
                                data[k] = "\uD83D\uDD35" + fregata[j].getLabel();
                            }
                        }
                        return "potopljen";
                    }
                    return "pogodak";
                }
            }
        }

        for (int i : korveta.getCelije()) {
            if (i == pozicija) {
                prviPogodak = true;
                data[pozicija] = "⚫" + korveta.getLabel();
                dataBackup[pozicija] = "0";
                korveta.pogodak(pozicija);
                if (korveta.isPotopljen()) {
                    if (!vlastita) {
                        for (int k : korveta.getCelije()) {
                            data[k] = "\uD83D\uDFE1" + korveta.getLabel();
                        }
                    }
                    return "potopljen";
                }
                return "pogodak";
            }
        }

        data[pozicija] = "❌";
        dataBackup[pozicija] = "0";
        return "promašaj";
    }
}