package com.example.antik;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antik.Annonce;
import com.example.antik.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.ViewHolder> implements Filterable {

    private List<Annonce.Annoncee> annonces;
    private List<Annonce.Annoncee> filteredList;
    private PopupWindow currentPopup;

    public void setCurrentPopup(PopupWindow popup) {
        this.currentPopup = popup;
    }

    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(Annonce.Annoncee annonce);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
   public AnnonceAdapter(List<Annonce.Annoncee> annonces) {
        this.annonces = annonces;
         this.filteredList = new ArrayList<>(annonces);
   }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_annonce, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Annonce.Annoncee annonce = annonces.get(position);
        Annonce.Annoncee annonce = filteredList.get(position);
        holder.textViewTitre.setText(annonce.getTitre());
        holder.textViewDescription.setText(annonce.getDescription());
        holder.text_view_prix.setText(String.valueOf(annonce.getPrix()+ " DT"));
        // Load and display the image using Picasso
        Picasso.get().load(annonce.getImage().getUrl()).into(holder.imageView);

        // Add other data binding as needed
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    dismissCurrentPopup();
                    onItemClickListener.onItemClick(annonce);
                }
            }
        });
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitre;
        TextView textViewDescription, text_view_prix;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitre = itemView.findViewById(R.id.text_view_titre);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            text_view_prix = itemView.findViewById(R.id.text_view_prix);
            imageView = itemView.findViewById(R.id.image_view); // Assuming you have an ImageView in your item layout
        }
    }
    // Add a method to dismiss the current popup
    private void dismissCurrentPopup() {
        if (currentPopup != null && currentPopup.isShowing()) {
            currentPopup.dismiss();
        }
    }
    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterPattern = constraint.toString().toLowerCase().trim();

                List<Annonce.Annoncee> filteredList = new ArrayList<>();
                if (filterPattern.isEmpty()) {
                    filteredList.addAll(annonces);
                } else {
                    for (Annonce.Annoncee annonce : annonces) {
                        if (annonce.getTitre().toLowerCase().contains(filterPattern) ||
                                annonce.getDescription().toLowerCase().contains(filterPattern)) {
                            filteredList.add(annonce);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<Annonce.Annoncee>) results.values);
                notifyDataSetChanged();
            }
        };
    }


}
