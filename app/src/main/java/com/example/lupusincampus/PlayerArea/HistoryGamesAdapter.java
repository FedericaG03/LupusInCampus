package com.example.lupusincampus.PlayerArea;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.Model.Game;
import com.example.lupusincampus.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryGamesAdapter extends RecyclerView.Adapter<HistoryGamesAdapter.ViewHolder> {
    private  List<Game> gameList;

    public HistoryGamesAdapter(List<Game> gameList) {
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.gameIdTextView.setText("Game ID: " + game.getId());
        holder.gameDateTextView.setText("Date: " + game.getLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        holder.winnerTextView.setText("Winner : " + game.getWinnerNickname());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView gameIdTextView, gameDateTextView, winnerTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIdTextView = itemView.findViewById(R.id.game_id);
            gameDateTextView = itemView.findViewById(R.id.game_date);
            winnerTextView = itemView.findViewById(R.id.winner_id);
        }
    }
}
