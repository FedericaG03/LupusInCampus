package com.example.lupusincampus.Amici;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.R;

public class ListaAmiciViewHolder extends RecyclerView.ViewHolder {
    TextView usernameTextView;
    ImageView invitaAmicoBtn;
    ImageView rimuoviAmicoBtn;
    ImageView bloccaAmicoBtn;
    public ListaAmiciViewHolder(@NonNull View itemView) {
        super(itemView);
        usernameTextView = itemView.findViewById(R.id.username_lista_amici);
        invitaAmicoBtn = itemView.findViewById(R.id.invita_amico_btn);
        rimuoviAmicoBtn = itemView.findViewById(R.id.rimuovi_amico_btn);
        bloccaAmicoBtn = itemView.findViewById(R.id.blocca_amico_btn);
    }
}
