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
import ma.ensate.myapplication.model.Depense;

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.ViewHolder> {

    private List<Depense> items = new ArrayList<>();

    public void setItems(List<Depense> list) {
        this.items = list;
        notifyDataSetChanged();
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
        holder.title.setText(d.getCategorie() != null ? d.getCategorie() : "Dépense #" + d.getId());
        holder.sub.setText(d.getDescription() != null ? d.getDescription() : "");
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