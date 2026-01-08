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
    private DecimalFormat df = new DecimalFormat("#,###.00");

    public void setItems(List<Recette> list, DecimalFormat df) {
        this.items.clear();
        if (list != null) this.items.addAll(list);
        if (df != null) this.df = df;
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
        Recette r = items.get(position);
        holder.tvSource.setText(r.source != null ? r.source : "—");
        holder.tvMontant.setText(r.montant != null ? df.format(r.montant) + " DH" : "—");
        String meta = (r.date != null ? r.date : "—") + "    " + (r.reference != null ? r.reference : "");
        holder.tvMeta.setText(meta);
        holder.itemView.setOnClickListener(v -> {
            // placeholder: open details later
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

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