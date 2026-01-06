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
        String dates = (r.getDateOuverture() != null ? r.getDateOuverture() : "?") +
                " → " + (r.getDateCloture() != null ? r.getDateCloture() : "?");
        holder.tvDates.setText(dates);
        holder.tvDescription.setText(r.getDescription() != null ? r.getDescription() : "Aucune description");
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPoste, tvStatut, tvDepartement, tvDates, tvDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPoste = itemView.findViewById(R.id.tv_poste);
            tvStatut = itemView.findViewById(R.id.tv_statut);
            tvDepartement = itemView.findViewById(R.id.tv_departement);
            tvDates = itemView.findViewById(R.id.tv_dates);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
