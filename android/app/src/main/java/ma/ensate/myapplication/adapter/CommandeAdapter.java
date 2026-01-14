package ma.ensate.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Commande;

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.ViewHolder> {

    private List<Commande> items = new ArrayList<>();
    private OnCommandeClickListener listener;

    public interface OnCommandeClickListener {
        void onCommandeClick(Commande commande);
    }

    public void setOnCommandeClickListener(OnCommandeClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Commande> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commande, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commande c = items.get(position);
        
        // Fournisseur
        holder.tvFournisseur.setText(c.getFournisseur() != null ? c.getFournisseur() : "Non spécifié");
        
        // Statut
        String statut = c.getStatut() != null ? c.getStatut() : "EN_COURS";
        holder.tvStatut.setText(statut);
        int statutColor;
        switch (statut) {
            case "EN_COURS":
                statutColor = 0xFFF59E0B; // Amber
                break;
            case "LIVRÉ":
                statutColor = 0xFF10B981; // Green
                break;
            case "ANNULÉ":
                statutColor = 0xFFEF4444; // Red
                break;
            default:
                statutColor = 0xFF6B7280; // Gray
        }
        holder.tvStatut.setTextColor(statutColor);
        
        // Montant
        String montant = c.getMontantTotal() != null ? c.getMontantTotal().toString() : "0.00";
        holder.tvMontant.setText("Montant: " + montant + " MAD");
        
        // Date commande
        holder.tvDateCommande.setText("Date: " + (c.getDateCommande() != null ? c.getDateCommande() : "N/A"));
        
        // Date livraison - show actual date if delivered, otherwise show expected date
        String livraison;
        if ("LIVRÉ".equals(statut) && c.getDateLivraisonEffective() != null) {
            livraison = c.getDateLivraisonEffective().toString();
        } else {
            livraison = c.getDateLivraisonPrevue() != null ? c.getDateLivraisonPrevue().toString() : "N/A";
        }
        holder.tvLivraison.setText("Livraison: " + livraison);
        
        // BON Commande
        holder.tvBonCommande.setText("BON: " + (c.getBonCommandeNumero() != null ? c.getBonCommandeNumero() : "N/A"));
        
        // Notes
        if (c.getNotes() != null && !c.getNotes().isEmpty()) {
            holder.tvNotes.setText(c.getNotes());
            holder.tvNotes.setVisibility(View.VISIBLE);
        } else {
            holder.tvNotes.setVisibility(View.GONE);
        }
        
        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCommandeClick(c);
            }
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFournisseur, tvStatut, tvMontant, tvDateCommande, tvLivraison, tvBonCommande, tvNotes;
        ViewHolder(View v) {
            super(v);
            tvFournisseur = v.findViewById(R.id.tv_fournisseur);
            tvStatut = v.findViewById(R.id.tv_statut);
            tvMontant = v.findViewById(R.id.tv_montant);
            tvDateCommande = v.findViewById(R.id.tv_date_commande);
            tvLivraison = v.findViewById(R.id.tv_livraison);
            tvBonCommande = v.findViewById(R.id.tv_bon_commande);
            tvNotes = v.findViewById(R.id.tv_notes);
        }
    }
}