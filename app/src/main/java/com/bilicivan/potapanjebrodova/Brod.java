package com.bilicivan.potapanjebrodova;

import java.util.ArrayList;

public class Brod {

    protected String naziv;
    protected boolean prijateljski;
    protected int duljina;
    private boolean potopljen = false;

    private char label = '\u0000';

    public void setLabel(char label) {
        this.label = label;
    }

    public char getLabel() {
        return label;
    }

    private ArrayList<Integer> celije = new ArrayList<>(duljina);
    private ArrayList<Integer> aktivneCelije = new ArrayList<>(duljina);

    protected Brod (String naziv, boolean prijateljski) {
        this.naziv = naziv;
        this.prijateljski = prijateljski;
    }

    protected void setCelije(ArrayList<Integer> celije) {
        this.celije = celije;
    }

    protected ArrayList<Integer> getCelije() {
        return celije;
    }

    protected void resetCelije() {
        celije.clear();
        aktivneCelije.clear();
    }

    protected void pogodak(int pozicija) {
        aktivneCelije.remove(Integer.valueOf(pozicija));
    }

    protected boolean isPotopljen () {
        if(aktivneCelije.isEmpty()) {
            potopljen = true;
        }
        return potopljen;
    }

    protected void postavljen() {
        aktivneCelije.addAll(celije);
    }
}