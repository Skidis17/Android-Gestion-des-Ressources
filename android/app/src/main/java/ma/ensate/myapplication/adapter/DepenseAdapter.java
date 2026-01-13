package ma.ensate.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Depense;

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.ViewHolder> {

    public interface OnDepenseClickListener {
        void onDepenseClick(Depense depense);
    }

    private List<Depense> items = new ArrayList<>();
    private OnDepenseClickListener clickListener;

    public void setItems(List<Depense> list) {
        this.items = list;
        notifyDataSetChanged();
    }
    
    public void setOnDepenseClickListener(OnDepenseClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_depense, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Depense d = items.get(position);
        
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDepenseClick(d);
            }
        });
        
        // Header
        holder.tvDepenseId.setText("Dépense #" + d.getId());
        holder.tvCategorie.setText(d.getCategorie() != null ? d.getCategorie() : "Non catégorisée");
        
        // Montant
        String montant = d.getMontant() != null ? d.getMontant().toString() : "0.00";
        holder.tvMontant.setText("Montant: " + montant + " MAD");
        
        // Fournisseur
        holder.tvFournisseur.setText(d.getFournisseur() != null ? d.getFournisseur() : "Non spécifié");
        
        // Date
        holder.tvDate.setText(d.getDateDepense() != null ? d.getDateDepense().toString() : "N/A");
        
        // Facture Number (optional)
        if (d.getFactureNumero() != null && !d.getFactureNumero().isEmpty()) {
            holder.layoutFacture.setVisibility(View.VISIBLE);
            holder.tvFactureNumero.setText(d.getFactureNumero());
        } else {
            holder.layoutFacture.setVisibility(View.GONE);
        }
        
        // Mode Paiement (optional)
        if (d.getModePaiement() != null && !d.getModePaiement().isEmpty() && !"À définir".equals(d.getModePaiement())) {
            holder.layoutModePaiement.setVisibility(View.VISIBLE);
            holder.tvModePaiement.setText(d.getModePaiement());
        } else {
            holder.layoutModePaiement.setVisibility(View.GONE);
        }
        
        // Description (optional)
        if (d.getDescription() != null && !d.getDescription().isEmpty()) {
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(d.getDescription());
        } else {
            holder.tvDescription.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepenseId, tvCategorie, tvMontant, tvFournisseur, tvDate;
        TextView tvFactureNumero, tvModePaiement, tvDescription;
        LinearLayout layoutFacture, layoutModePaiement;
        
        ViewHolder(View v) {
            super(v);
            tvDepenseId = v.findViewById(R.id.tv_depense_id);
            tvCategorie = v.findViewById(R.id.tv_categorie);
            tvMontant = v.findViewById(R.id.tv_montant);
            tvFournisseur = v.findViewById(R.id.tv_fournisseur);
            tvDate = v.findViewById(R.id.tv_date);
            tvFactureNumero = v.findViewById(R.id.tv_facture_numero);
            tvModePaiement = v.findViewById(R.id.tv_mode_paiement);
            tvDescription = v.findViewById(R.id.tv_description);
            layoutFacture = v.findViewById(R.id.layout_facture);
            layoutModePaiement = v.findViewById(R.id.layout_mode_paiement);
        }
    }
}