package ma.ensate.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Demande;

public class DemandeAdapter extends RecyclerView.Adapter<DemandeAdapter.DemandeViewHolder> {
    private final List<Demande> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onClick(Demande demande); }
    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

    public void submit(List<Demande> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DemandeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demande, parent, false);
        return new DemandeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DemandeViewHolder holder, int position) {
        Demande d = items.get(position);
        holder.title.setText(titleFor(d));
        holder.subtitle.setText(rangeFor(d));
        holder.motif.setText(d.getMotif());
        holder.badge.setText(labelForStatus(d.getStatut()));
        holder.badge.setBackgroundResource(badgeForStatus(d.getStatut()));
        holder.badge.setTextColor(colorForStatus(d.getStatut()));
        holder.createdBy.setText(createdByFor(d.getCreatedBy()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(d);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class DemandeViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        TextView motif;
        TextView badge;
        TextView createdBy;

        DemandeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvDemandeTitle);
            subtitle = itemView.findViewById(R.id.tvDemandeSubtitle);
            motif = itemView.findViewById(R.id.tvDemandeMotif);
            badge = itemView.findViewById(R.id.tvDemandeBadge);
            createdBy = itemView.findViewById(R.id.tvDemandeCreatedBy);
        }
    }

    private String titleFor(Demande d) {
        String type = d.getType() == null ? "Demande" : d.getType();
        return "Demande " + type;
    }

    private String rangeFor(Demande d) {
        String deb = d.getDateDebut() == null ? "?" : d.getDateDebut();
        String fin = d.getDateFin() == null ? "?" : d.getDateFin();
        return "Du " + deb + " au " + fin;
    }

    private String createdByFor(Long createdBy) {
        return createdBy == null ? "Employé inconnu" : "Employé #" + createdBy;
    }

    private String labelForStatus(String statut) {
        if ("ACCEPTEE".equalsIgnoreCase(statut)) return "Acceptée";
        if ("REFUSEE".equalsIgnoreCase(statut)) return "Refusée";
        return "En attente";
    }

    private int badgeForStatus(String statut) {
        if ("ACCEPTEE".equalsIgnoreCase(statut)) return R.drawable.badge_bg_approved;
        if ("REFUSEE".equalsIgnoreCase(statut)) return R.drawable.badge_bg_rejected;
        return R.drawable.badge_bg_pending;
    }

    private int colorForStatus(String statut) {
        if ("ACCEPTEE".equalsIgnoreCase(statut)) return Color.parseColor("#10B981");
        if ("REFUSEE".equalsIgnoreCase(statut)) return Color.parseColor("#DC2626");
        return Color.parseColor("#F59E0B");
    }
}
