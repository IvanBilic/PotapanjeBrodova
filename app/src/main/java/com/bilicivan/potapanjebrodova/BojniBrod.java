package com.bilicivan.potapanjebrodova;

public class BojniBrod extends Brod {

    public BojniBrod (String naziv, boolean prijateljski) {
        super (naziv, prijateljski);
        duljina = 4;
    }

    public int getDuljina() {
        return duljina;
    }
}
