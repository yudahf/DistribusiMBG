package com.example.projectpab_distribusimbg_teori;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

public class VerifikasiAdapter extends RecyclerView.Adapter<VerifikasiAdapter.ViewHolder> {

    public interface OnStockUpdatedListener {
        void onStockUpdated();
    }

    private final OnStockUpdatedListener stockUpdatedListener;
    private final List<JadwalPengirimanModel> data;

    public VerifikasiAdapter(List<JadwalPengirimanModel> data, OnStockUpdatedListener listener) {
        this.data = data;
        this.stockUpdatedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verifikasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JadwalPengirimanModel item = data.get(position);
        holder.textViewNamaSekolah.setText(item.getNamaSekolah());
        holder.textViewJumlahBarang.setText(String.valueOf(item.getJumlahPengiriman() + " Paket"));
        holder.textViewTanggalPengiriman.setText(item.getTanggalPengiriman());

        holder.btnSetujui.setOnClickListener(v -> {
            try (DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext())) {
                int jumlahDiminta = item.getJumlahPengiriman();
                String namaBarang = item.getNamaBarang();
                int currentStock = databaseHelper.getStok("makanan bergizi");
                int updatedStock = currentStock - jumlahDiminta;

                Log.d("VerifikasiAdapter", "Stok sebelum update: " + currentStock);

                if (updatedStock >= 0) {
                    Log.d("VerifikasiAdapter", "Update stok jadi: " + updatedStock);
                    // Update stok
                    databaseHelper.kurangiStokSecaraBertahap(namaBarang, jumlahDiminta);
                    Log.d("VerifikasiAdapter", "Update stok selesai");

                    // Update status pengiriman jadi "Dalam Proses"
                    databaseHelper.updateStatusPengiriman(item.getId(), "Dalam Proses");

                    // Notify listener
                    if (stockUpdatedListener != null) {
                        stockUpdatedListener.onStockUpdated();
                    }

                    // Kirim broadcast refresh
                    Intent intent = new Intent("REFRESH_DALAM_PROSES");
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                    // Update UI: remove item
                    data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());
                } else {
                    // Stok tidak cukup, tampilkan pesan
                    Toast.makeText(holder.itemView.getContext(), "Stok tidak cukup untuk pengiriman ini", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.btnTolak.setOnClickListener(v -> {
            try (DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext())) {
                // Update status pengiriman menjadi "Ditolak"
                databaseHelper.updateStatusPengiriman(item.getId(), "Ditolak, Stok Tidak Cukup!");

                // Notify stock update (meskipun stok tidak berubah saat ditolak, tetap update UI)
                if (stockUpdatedListener != null) {
                    stockUpdatedListener.onStockUpdated();
                }

                // Kirim broadcast agar halaman JadwalAktif bisa refresh data
                Intent intent = new Intent("REFRESH_JADWAL_AKTIF");
                LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                // Hapus item dari list dan notify adapter
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNamaSekolah;
        TextView textViewJumlahBarang;
        TextView textViewTanggalPengiriman;
        Button btnSetujui;
        Button btnTolak;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNamaSekolah = itemView.findViewById(R.id.tvNamaSekolah);
            textViewJumlahBarang = itemView.findViewById(R.id.tvJumlahBarang);
            textViewTanggalPengiriman = itemView.findViewById(R.id.tvTanggal);
            btnSetujui = itemView.findViewById(R.id.btnSetujui);
            btnTolak = itemView.findViewById(R.id.btnTolak);
        }
    }
}

