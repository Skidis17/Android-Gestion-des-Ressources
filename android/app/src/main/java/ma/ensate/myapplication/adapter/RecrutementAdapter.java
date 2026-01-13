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
import ma.ensate.myapplication.model.Recrutement;

public class RecrutementAdapter extends RecyclerView.Adapter<RecrutementAdapter.ViewHolder> {

    private List<Recrutement> items = new ArrayList<>();
    public interface OnItemClick { void onClick(Recrutement r); }
    private OnItemClick onItemClick;
    public interface OnPdfClick { void onClick(Recrutement r); }
    private OnPdfClick onPdfClick;

    public void setOnItemClick(OnItemClick listener) {
        this.onItemClick = listener;
    }

    public void setOnPdfClick(OnPdfClick listener) {
        this.onPdfClick = listener;
    }

    public void setItems(List<Recrutement> list) {
        this.items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recrutement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recrutement r = items.get(position);
        holder.tvPoste.setText(r.getPoste());
        holder.tvDepartement.setText(r.getDepartement() != null ? r.getDepartement() : "N/A");
        holder.tvStatut.setText(r.getStatut() != null ? r.getStatut() : "N/A");
        String typeContrat = r.getTypeContrat() != null ? r.getTypeContrat() : "N/A";
        String nbPostes = r.getNombrePostes() != null ? String.valueOf(r.getNombrePostes()) + " postes" : "N/A";
        holder.tvMeta.setText(typeContrat + " • " + nbPostes);
        String dates = (r.getDateOuverture() != null ? r.getDateOuverture() : "?") +
                " → " + (r.getDateCloture() != null ? r.getDateCloture() : "?");
        holder.tvDates.setText(dates);
        holder.tvDescription.setText(r.getDescription() != null ? r.getDescription() : "Aucune description");
        boolean hasPdf = r.getPdfUrl() != null && !r.getPdfUrl().trim().isEmpty();
        holder.btnPdf.setVisibility(hasPdf ? View.VISIBLE : View.GONE);
        holder.btnPdf.setOnClickListener(v -> {
            if (onPdfClick != null) onPdfClick.onClick(r);
        });

        holder.itemView.setOnClickListener(v -> {
            if (onItemClick != null) onItemClick.onClick(r);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPoste, tvStatut, tvDepartement, tvMeta, tvDates, tvDescription;
        View btnPdf;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPoste = itemView.findViewById(R.id.tv_poste);
            tvStatut = itemView.findViewById(R.id.tv_statut);
            tvDepartement = itemView.findViewById(R.id.tv_departement);
            tvMeta = itemView.findViewById(R.id.tv_meta);
            tvDates = itemView.findViewById(R.id.tv_dates);
            tvDescription = itemView.findViewById(R.id.tv_description);
            btnPdf = itemView.findViewById(R.id.btn_offer_pdf);
        }
    }
}
