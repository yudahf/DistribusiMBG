package com.example.projectpab_distribusimbg_teori;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SekolahAdapter extends RecyclerView.Adapter<SekolahAdapter.SekolahViewHolder> {

    private List<Sekolah> sekolahList;
    private OnItemClickListener onItemClickListener;
    private DatabaseHelper dbHelper;

    public SekolahAdapter(List<Sekolah> sekolahList, DatabaseHelper dbHelper) {
        this.sekolahList = sekolahList;
        this.dbHelper = dbHelper;
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

        if ("aktif".equalsIgnoreCase(sekolah.getStatus())) {
            holder.tvStatus.setText("Aktif");
            holder.btnToggleStatus.setImageResource(R.drawable.ic_on);
        } else {
            holder.tvStatus.setText("Nonaktif");
            holder.btnToggleStatus.setImageResource(R.drawable.ic_off);
        }

        // 🌟 Toggle status saat diklik
        holder.btnToggleStatus.setOnClickListener(v -> {
            boolean isAktif = "aktif".equalsIgnoreCase(sekolah.getStatus());
            String newStatus = isAktif ? "nonaktif" : "aktif";
            sekolah.setStatus(newStatus); // Update objek

            // Ubah UI
            holder.tvStatus.setText(newStatus.substring(0, 1).toUpperCase() + newStatus.substring(1));
            holder.btnToggleStatus.setImageResource(
                    newStatus.equals("aktif") ? R.drawable.ic_on : R.drawable.ic_off
            );

            // TODO: Tambahkan update ke database jika perlu, misalnya:
            dbHelper.updateStatusSekolah(sekolah.getId(), newStatus);
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
        ImageView btnToggleStatus;
        TextView tvStatus;


        public SekolahViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaSekolah = itemView.findViewById(R.id.tvNamaSekolah);
            tvNamaOperator = itemView.findViewById(R.id.tvNamaOperator);
            btnToggleStatus = itemView.findViewById(R.id.btnToggleStatus);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(Sekolah sekolah) {
            tvNamaSekolah.setText(sekolah.getNamaSekolah());
            tvNamaOperator.setText(sekolah.getNamaOperator());
        }
    }

}
