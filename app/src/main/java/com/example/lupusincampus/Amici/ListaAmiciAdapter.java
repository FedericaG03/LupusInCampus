package com.example.lupusincampus.Amici;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.API.FriendAPI;
import com.example.lupusincampus.API.LobbyAPI;
import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.R;

import java.util.List;

public class ListaAmiciAdapter extends RecyclerView.Adapter<ListaAmiciAdapter.ListaAmiciViewHolder> {
    private List<Player> listaAmici;
    private FriendAPI friendAPI = new FriendAPI();
    private LobbyAPI lobbyAPI = new LobbyAPI();

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
                    .setPositiveButton("Sì", (dialog, which) -> inviteToLobby(ctx, player))
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.rimuoviAmicoBtn.setOnClickListener(v->{
            new AlertDialog.Builder(v.getContext())
                    .setMessage("Sei sicuro di voler rimuovere questo amico?")
                    .setPositiveButton("Sì", (dialog, which) -> removeFriend(ctx, position, player))
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.bloccaAmicoBtn.setOnClickListener(v->{
            new AlertDialog.Builder(v.getContext())
                    .setMessage("Sei sicuro di voler bloccare questo giocatore?")
                    .setPositiveButton("Sì", (dialog, which) -> removeFriend(ctx, position, player))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void inviteToLobby(Context ctx, Player player) {

        lobbyAPI.doInviteFriendToLobby(ctx, player.getId(),0);
    }

    @Override
    public int getItemCount() {
        return listaAmici.size();
    }

    public void removeFriend(Context ctx, int position, Player player) {
        if (position >= 0 && position < listaAmici.size()) {
            friendAPI.doRemoveFriend(ctx, player.getId());
        }
    }

    public static class ListaAmiciViewHolder extends RecyclerView.ViewHolder {
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
}
