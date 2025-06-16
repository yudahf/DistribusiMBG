package com.example.projectpab_distribusimbg_teori;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SekolahAdapter extends RecyclerView.Adapter<SekolahAdapter.SekolahViewHolder> {

    private List<Sekolah> sekolahList;
    private OnItemClickListener onItemClickListener;

    public SekolahAdapter(List<Sekolah> sekolahList) {
        this.sekolahList = sekolahList;
    }

    public interface OnItemClickListener {
        void onEditClick(Sekolah sekolah);
        void onDeleteClick(Sekolah sekolah);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public SekolahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sekolah, parent, false);
        return new SekolahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SekolahViewHolder holder, int position) {
        Sekolah sekolah = sekolahList.get(position);
        holder.bind(sekolah);

        holder.itemView.findViewById(R.id.btnEdit).setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onEditClick(sekolah);
            }
        });

        holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDeleteClick(sekolah);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sekolahList.size();
    }

    public void updateData(List<Sekolah> newSekolahList) {
        this.sekolahList.clear();
        this.sekolahList.addAll(newSekolahList);
        notifyDataSetChanged();
    }

    public static class SekolahViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaSekolah;
        TextView tvNamaOperator;

        public SekolahViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaSekolah = itemView.findViewById(R.id.tvNamaSekolah);
            tvNamaOperator = itemView.findViewById(R.id.tvNamaOperator);
        }

        public void bind(Sekolah sekolah) {
            tvNamaSekolah.setText(sekolah.getNamaSekolah());
            tvNamaOperator.setText(sekolah.getNamaOperator());
        }
    }
}
