package com.bilicivan.potapanjebrodova;

public class NosacZrakoplova extends Brod {

    public NosacZrakoplova (String naziv, boolean prijateljski) {
        super (naziv, prijateljski);
        duljina = 5;
    }

    public int getDuljina() {
        return duljina;
    }
}