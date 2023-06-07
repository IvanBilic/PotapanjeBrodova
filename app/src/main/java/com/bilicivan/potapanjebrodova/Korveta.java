package com.bilicivan.potapanjebrodova;

public class Korveta extends Brod {

    public Korveta (String naziv, boolean prijateljski) {
        super (naziv, prijateljski);
        duljina = 2;
    }

    public int getDuljina() {
        return duljina;
    }
}