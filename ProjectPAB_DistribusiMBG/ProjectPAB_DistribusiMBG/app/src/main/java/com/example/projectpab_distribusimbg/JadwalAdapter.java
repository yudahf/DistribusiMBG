package com.example.projectpab_distribusimbg_teori;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder> {

    public interface OnEditClickListener {
        void onEditClick(JadwalPengirimanModel jadwal);
    }

    private final List<JadwalPengirimanModel> jadwalList;
    private final OnEditClickListener editClickListener;

    public JadwalAdapter(List<JadwalPengirimanModel> jadwalList, OnEditClickListener editClickListener) {
        this.jadwalList = jadwalList;
        this.editClickListener = editClickListener;
    }

    @NonNull
    @Override
    public JadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_jadwal, parent, false);
        return new JadwalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalViewHolder holder, int position) {
        JadwalPengirimanModel jadwal = jadwalList.get(position);
        holder.namaSekolah.setText(jadwal.getNamaSekolah());
        holder.tanggalPengiriman.setText(jadwal.getTanggalPengiriman());
        holder.jamPengiriman.setText(jadwal.getJamPengiriman() + " WIB");
        holder.jumlahPengiriman.setText(jadwal.getJumlahPengiriman() + " Paket");

        holder.icEdit.setOnClickListener(v -> {
            if (editClickListener != null) {
                editClickListener.onEditClick(jadwal);
            }
        });

        holder.icDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus jadwal pengiriman ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        Context context = v.getContext();
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("jadwal_pengiriman", "id = ?", new String[]{String.valueOf(jadwal.getId())});
                        jadwalList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, jadwalList.size());

                        // Kirim broadcast untuk refresh halaman "dalam proses"
                        Intent intent = new Intent("REFRESH_DALAM_PROSES");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                        db.close();
                    })
                    .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public void updateData(List<JadwalPengirimanModel> newJadwalList) {
        jadwalList.clear();
        jadwalList.addAll(newJadwalList);
        notifyDataSetChanged();
    }

    public static class JadwalViewHolder extends RecyclerView.ViewHolder {
        TextView namaSekolah, tanggalPengiriman, jamPengiriman, jumlahPengiriman;
        ImageView icEdit, icDelete;

        public JadwalViewHolder(@NonNull View itemView) {
            super(itemView);
            namaSekolah = itemView.findViewById(R.id.namaSekolah);
            tanggalPengiriman = itemView.findViewById(R.id.tanggalPengiriman);
            jamPengiriman = itemView.findViewById(R.id.jamPengiriman);
            jumlahPengiriman = itemView.findViewById(R.id.jumlahPengiriman);
            icEdit = itemView.findViewById(R.id.ic_edit);
            icDelete = itemView.findViewById(R.id.ic_delete);
        }
    }
}
