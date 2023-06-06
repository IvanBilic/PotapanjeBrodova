package com.bilicivan.potapanjebrodova;

public class Fregata extends Brod {

    public Fregata (String naziv, boolean prijateljski) {
        super (naziv, prijateljski);
        duljina = 3;
    }

    public int getDuljina() {
        return duljina;
    }
}
