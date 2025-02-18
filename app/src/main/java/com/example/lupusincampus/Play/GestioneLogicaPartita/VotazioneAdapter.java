package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class VotazioneAdapter extends RecyclerView.Adapter<VotazioneAdapter.VotazioneViewHolder> {
    private List<String> players;
    private onItemClickListener listener;
    private int selectedPosition = -1;

    public interface onItemClickListener {
        void onItemClick(String playerName);
    }

    public VotazioneAdapter(List<String> players, onItemClickListener listener) {
        this.players = players;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VotazioneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_activated_1, parent, false);
        return new VotazioneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VotazioneViewHolder holder, int position) {
        String playerName = players.get(position);
        holder.playerName.setText(playerName);

        // Cambia il colore dello sfondo se l'elemento è selezionato
        holder.itemView.setActivated(selectedPosition == holder.getAdapterPosition());

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return; // Controllo di sicurezza

            selectedPosition = currentPosition;
            listener.onItemClick(playerName);
            notifyDataSetChanged(); // Aggiorna la lista per riflettere la selezione
        });
    }


    @Override
    public int getItemCount() {
        return players != null ? players.size() : 0;
    }




    public static class VotazioneViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;

        public VotazioneViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(android.R.id.text1);
        }
    }





}