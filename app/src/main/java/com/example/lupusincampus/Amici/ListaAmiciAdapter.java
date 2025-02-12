package com.example.lupusincampus.Amici;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.API.FriendAPI;
import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.R;

import java.util.List;

public class ListaAmiciAdapter extends RecyclerView.Adapter<ListaAmiciViewHolder> {
    private List<Player> listaAmici;
    private FriendAPI friendAPI = new FriendAPI();

    public ListaAmiciAdapter(List<Player> listaAmici){
        this.listaAmici = listaAmici;
    }

    @NonNull
    @Override
    public ListaAmiciViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_amici_item, parent, false);
        return new ListaAmiciViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ListaAmiciViewHolder holder, int position) {
        Player player = listaAmici.get(position);
        Context ctx = holder.itemView.getContext();
        holder.usernameTextView.setText(player.getNickname());
        holder.invitaAmicoBtn.setOnClickListener(v->{
            new AlertDialog.Builder(v.getContext())
                    .setMessage("Sei sicuro di voler aggiungere questo amico?")
                    .setPositiveButton("Sì", (dialog, which) -> {
                        // Aggiungi amico dalla lista
                        addFriend(ctx, player);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.rimuoviAmicoBtn.setOnClickListener(v->{
            new AlertDialog.Builder(v.getContext())
                    .setMessage("Sei sicuro di voler rimuovere questo amico?")
                    .setPositiveButton("Sì", (dialog, which) -> {
                        // Rimuove l'amico dalla lista
                        removeFriend(ctx, position, player);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.bloccaAmicoBtn.setOnClickListener(v->{
            new AlertDialog.Builder(v.getContext())
                    .setMessage("Sei sicuro di voler bloccare questo giocatore?")
                    .setPositiveButton("Sì", (dialog, which) -> {
                        // Rimuove l'amico dalla lista
                        removeFriend(ctx, position, player);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void addFriend(Context ctx, Player player) {
        friendAPI.doSendFriendRequest(ctx, player.getId());
    }

    @Override
    public int getItemCount() {
        return listaAmici.size();
    }


    public void removeFriend(Context ctx,int position, Player player) {
        if (position >= 0 && position < listaAmici.size()) {
            listaAmici.remove(position); // Rimuove l'elemento dalla lista
            notifyItemRemoved(position); // Notifica che l'elemento è stato rimosso
            notifyItemRangeChanged(position, listaAmici.size()); // Rende fluido lo spostamento degli altri elementi
        }
    }
}
