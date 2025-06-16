package com.example.projectpab_distribusimbg_teori;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    private final List<JadwalItem> riwayatList;

    public RiwayatAdapter(List<JadwalItem> riwayatList) {
        this.riwayatList = riwayatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalItem item = riwayatList.get(position);
        Log.d("RiwayatAdapter", "Binding item at position: " + position + ", Title: " + item.getTitle());
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.packageCount.setText(item.getPackageCount());
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time, packageCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            packageCount = itemView.findViewById(R.id.packageCount);
        }
    }
}
