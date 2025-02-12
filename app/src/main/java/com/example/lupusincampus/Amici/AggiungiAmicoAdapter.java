package com.example.lupusincampus.Amici;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.Model.Player;
import com.example.lupusincampus.R;

import java.util.List;

public class AggiungiAmicoAdapter extends RecyclerView.Adapter<AggiungiAmicoAdapter.FriendViewHolder> {
    private List<Player> searchResult;
    private OnFriendClickListener listener;

    public AggiungiAmicoAdapter(List<Player> searchResult, OnFriendClickListener listener) {
        this.searchResult = searchResult;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ricerca_amici_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Player friend = searchResult.get(position);
        holder.friendNameTextView.setText(friend.getNickname());
        holder.addButton.setOnClickListener(v -> listener.onAddFriend(friend));
    }

    @Override
    public int getItemCount() {
        return searchResult.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView friendNameTextView;
        CardView addButton;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.username_lista_amici);
            addButton = itemView.findViewById(R.id.aggiungi_giocatore_btn);
        }
    }

    public interface OnFriendClickListener {
        void onAddFriend(Player friend);
    }
}
