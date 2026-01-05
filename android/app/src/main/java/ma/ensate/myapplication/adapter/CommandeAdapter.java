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
        holder.title.setText(c.getFournisseur() != null ? c.getFournisseur() : "Commande #" + c.getId());
        holder.sub.setText(c.getNotes() != null ? c.getNotes() : "");
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, sub;
        ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tv_title);
            sub = v.findViewById(R.id.tv_sub);
        }
    }
}