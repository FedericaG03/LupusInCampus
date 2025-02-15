package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<String> messages;
    private String nickaname;

    public MessageAdapter(List<String> messages, String nickaname) {
        this.messages = messages;
        this.nickaname = nickaname;
    }

    @Override
    public int getItemViewType(int position) {
        // Se il messaggio inizia con il nickname del giocatore, è un messaggio inviato
        if (messages.get(position).startsWith(this.nickaname + ": ")) {
            return 1; // Messaggio inviato
        } else {
            return 2; // Messaggio ricevuto
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            // Layout per i messaggi inviati
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
        } else {
            // Layout per i messaggi ricevuti
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.textMessage.setText(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
    }
}
