package com.example.lupusincampus.Play;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.Model.Player;

import java.util.List;

public class NickFriendsAdapter extends RecyclerView.Adapter<NickFriendsAdapter.NickViewHolder> {
    private List<Player> items;
    private OnItemClickListener listener;

    // Interfaccia per gestire il clic sull'elemento
    public interface OnItemClickListener {
        void onItemClick(Player player);
    }

    // Costruttore
    public NickFriendsAdapter(List<Player> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for each item in the list (using simple list item)
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new NickViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NickViewHolder holder, int position) {
        Player player = items.get(position);
        holder.textView.setText(player.getNickname());

        // Gestisci il clic
        holder.itemView.setOnClickListener(v -> listener.onItemClick(player));
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }


    // ViewHolder per ogni elemento della lista
    public static class NickViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public NickViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
