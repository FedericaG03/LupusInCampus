package com.example.lupusincampus.Model;

public class Ruolo {
    private String nome;
    private String descrizione;
    private int imageResId; // ID della risorsa drawable

    public Ruolo(String nome, String descrizione, int imageResId) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.imageResId = imageResId;
    }

    // Getter
    public String getNome() { return nome; }
    public String getDescrizione() { return descrizione; }
    public int getImageResId() { return imageResId; }
}
