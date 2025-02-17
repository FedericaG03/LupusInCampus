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
import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.R;

import java.util.List;

public class ListaAmiciAdapter extends RecyclerView.Adapter<ListaAmiciAdapter.ListaAmiciViewHolder> {
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

        holder.rimuoviAmicoBtn.setOnClickListener(v->{
            new AlertDialog.Builder(v.getContext())
                    .setMessage("Sei sicuro di voler rimuovere questo amico?")
                    .setPositiveButton("Sì", (dialog, which) -> removeFriend(ctx, position, player))
                    .setNegativeButton("No", null)
                    .show();
        });

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
        ImageView rimuoviAmicoBtn;

        public ListaAmiciViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_lista_amici);
            rimuoviAmicoBtn = itemView.findViewById(R.id.rimuovi_amico_btn);
        }
    }
}
