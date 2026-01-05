package ma.ensate.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Besoin;

public class BesoinAdapter extends RecyclerView.Adapter<BesoinAdapter.ViewHolder> {

    private List<Besoin> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(Besoin besoin);
    }

    public interface OnEditClickListener {
        void onEditClick(Besoin besoin);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Besoin besoin);
    }

    public void setItems(List<Besoin> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_besoin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Besoin b = items.get(position);
        holder.title.setText(b.getTypeBesoin() != null ? b.getTypeBesoin() : "Besoin #" + b.getId());
        holder.sub.setText(b.getDescription() != null ? b.getDescription() : "");
        
        String statut = b.getStatut() != null ? b.getStatut() : "EN_ATTENTE";
        holder.statut.setText(statut);
        holder.statut.setVisibility(View.VISIBLE);
        
        // Set status color
        int color = getStatusColor(statut);
        holder.statut.setTextColor(color);

        // Only allow edit/delete if status is EN_ATTENTE
        boolean canEdit = "EN_ATTENTE".equals(statut);
        holder.btnEdit.setEnabled(canEdit);
        holder.btnEdit.setAlpha(canEdit ? 1.0f : 0.3f);
        holder.btnDelete.setEnabled(canEdit);
        holder.btnDelete.setAlpha(canEdit ? 1.0f : 0.3f);

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(b);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (canEdit && onEditClickListener != null) {
                onEditClickListener.onEditClick(b);
            } else if (!canEdit) {
                // Show message that editing is not allowed
                android.widget.Toast.makeText(v.getContext(), "Le besoin ne peut être modifié qu'en statut EN_ATTENTE", android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (canEdit && onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(b);
            } else if (!canEdit) {
                android.widget.Toast.makeText(v.getContext(), "Le besoin ne peut être supprimé qu'en statut EN_ATTENTE", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getStatusColor(String statut) {
        switch (statut) {
            case "EN_ATTENTE":
                return 0xFFF59E0B; // Orange
            case "VALIDÉ":
                return 0xFF3B82F6; // Blue
            case "APPROUVÉ":
                return 0xFF10B981; // Green
            case "TRANSMIS_A_ECO":
                return 0xFF8B5CF6; // Purple
            case "REFUSÉ":
                return 0xFFEF4444; // Red
            default:
                return 0xFF6B7280; // Gray
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, sub, statut;
        ImageButton btnEdit, btnDelete;
        
        ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tv_title);
            sub = v.findViewById(R.id.tv_sub);
            statut = v.findViewById(R.id.tv_statut);
            btnEdit = v.findViewById(R.id.btn_edit);
            btnDelete = v.findViewById(R.id.btn_delete);
        }
    }
}