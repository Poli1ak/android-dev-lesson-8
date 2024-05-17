package ru.mirea.panov.mirea_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EstablishmentsAdapter extends RecyclerView.Adapter<EstablishmentsAdapter.ViewHolder> {

    private List<Establishment> establishments;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Establishment establishment);
    }

    public EstablishmentsAdapter(List<Establishment> establishments, OnItemClickListener listener) {
        this.establishments = establishments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_establishment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Establishment establishment = establishments.get(position);
        holder.bind(establishment, listener);
    }

    @Override
    public int getItemCount() {
        return establishments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }

        public void bind(final Establishment establishment, final OnItemClickListener listener) {
            nameTextView.setText(establishment.getName());
            descriptionTextView.setText(establishment.getDescription());
            itemView.setOnClickListener(v -> listener.onItemClick(establishment));
        }
    }
}
