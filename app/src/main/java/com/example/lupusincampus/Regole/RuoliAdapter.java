package com.example.lupusincampus.Regole;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lupusincampus.Model.Ruolo;
import com.example.lupusincampus.R;

import java.util.List;

public class RuoliAdapter extends RecyclerView.Adapter<RuoliAdapter.RuoloViewHolder> {
    private List<Ruolo> listaRuoli;

    public RuoliAdapter(List<Ruolo> listaRuoli) {
        this.listaRuoli = listaRuoli;
    }

    @NonNull
    @Override
    public RuoloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ruolo_item, parent, false);
        return new RuoloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RuoloViewHolder holder, int position) {
        Ruolo ruolo = listaRuoli.get(position);
        holder.imgRuolo.setImageResource(ruolo.getImageResId());
        holder.nomeRuolo.setText(ruolo.getNome());
        holder.descrizioneRuolo.setText(ruolo.getDescrizione());
    }

    @Override
    public int getItemCount() {
        return listaRuoli.size();
    }

    public static class RuoloViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRuolo;
        TextView nomeRuolo, descrizioneRuolo;

        public RuoloViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRuolo = itemView.findViewById(R.id.imgRuolo);
            nomeRuolo = itemView.findViewById(R.id.nomeRuolo);
            descrizioneRuolo = itemView.findViewById(R.id.descrizioneRuolo);
        }
    }
}
