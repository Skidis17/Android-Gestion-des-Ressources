package ma.ensate.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Recette;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecetteAdapter extends RecyclerView.Adapter<RecetteAdapter.VH> {

    private final List<Recette> items = new ArrayList<>();
    private final List<Recette> itemsFiltered = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#,###.00");
    private OnRecetteClickListener clickListener;

    public interface OnRecetteClickListener {
        void onRecetteClick(Recette recette);
    }

    public void setOnRecetteClickListener(OnRecetteClickListener listener) {
        this.clickListener = listener;
    }

    public void setItems(List<Recette> list, DecimalFormat df) {
        this.items.clear();
        this.itemsFiltered.clear();
        if (list != null) {
            this.items.addAll(list);
            this.itemsFiltered.addAll(list);
        }
        if (df != null)
            this.df = df;
        notifyDataSetChanged();
    }

    public void filter(String query) {
        itemsFiltered.clear();
        if (query == null || query.trim().isEmpty()) {
            itemsFiltered.addAll(items);
        } else {
            String q = query.toLowerCase().trim();
            for (Recette r : items) {
                if ((r.source != null && r.source.toLowerCase().contains(q)) ||
                        (r.reference != null && r.reference.toLowerCase().contains(q)) ||
                        (r.description != null && r.description.toLowerCase().contains(q)) ||
                        (r.categorie != null && r.categorie.toLowerCase().contains(q))) {
                    itemsFiltered.add(r);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recette, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Recette r = itemsFiltered.get(position);
        holder.tvSource.setText(r.source != null ? r.source : "—");
        holder.tvMontant.setText(r.montant != null ? df.format(r.montant) + " DH" : "—");
        String meta = (r.date != null ? r.date : "—") + "    " + (r.reference != null ? r.reference : "");
        holder.tvMeta.setText(meta);
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onRecetteClick(r);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSource, tvMontant, tvMeta;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvSource = itemView.findViewById(R.id.tvRecetteSource);
            tvMontant = itemView.findViewById(R.id.tvRecetteMontant);
            tvMeta = itemView.findViewById(R.id.tvRecetteMeta);
        }
    }
}