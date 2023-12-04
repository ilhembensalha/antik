package com.example.antik;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.ViewHolder> {

    private List<Annonce.Annoncee> annonces;

    public AnnonceAdapter(List<Annonce.Annoncee> annonces) {
        this.annonces = annonces;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_annonce, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Annonce.Annoncee annonce = annonces.get(position);

        holder.textViewTitre.setText(annonce.getTitre());
        holder.textViewDescription.setText(annonce.getDescription());
        holder.text_view_prix.setText( String.valueOf(annonce.getPrix()));
        // Load and display the image using Picasso
        Picasso.get().load(annonce.getImage().getUrl()).into(holder.imageView);

        // Add other data binding as needed
    }

    @Override
    public int getItemCount() {
        return annonces.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitre;
        TextView textViewDescription,text_view_prix;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitre = itemView.findViewById(R.id.text_view_titre);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            text_view_prix =itemView.findViewById(R.id.text_view_prix);
            imageView = itemView.findViewById(R.id.image_view); // Assuming you have an ImageView in your item layout
        }
    }
}
