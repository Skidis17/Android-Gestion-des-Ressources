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
import ma.ensate.myapplication.model.CandidatureRecrutement;

public class CandidatureAdapter extends RecyclerView.Adapter<CandidatureAdapter.ViewHolder> {
    private List<CandidatureRecrutement> items = new ArrayList<>();
    public interface OnItemClick { void onClick(CandidatureRecrutement c); }
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick listener) {
        this.onItemClick = listener;
    }

    public void setItems(List<CandidatureRecrutement> list) {
        this.items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidature, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CandidatureRecrutement c = items.get(position);
        holder.tvNom.setText(c.getPrenom() + " " + c.getNom());
        holder.tvEmail.setText(c.getEmail() != null ? c.getEmail() : "");
        holder.tvStatut.setText(c.getStatut() != null ? c.getStatut() : "EN_ATTENTE");
        holder.tvCin.setText(c.getCin() != null ? c.getCin() : "");

        holder.itemView.setOnClickListener(v -> {
            if (onItemClick != null) onItemClick.onClick(c);
        });
    }

    @Override
    public int getItemCount() { return items != null ? items.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvEmail, tvStatut, tvCin;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tv_nom);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvStatut = itemView.findViewById(R.id.tv_statut);
            tvCin = itemView.findViewById(R.id.tv_cin);
        }
    }
}
