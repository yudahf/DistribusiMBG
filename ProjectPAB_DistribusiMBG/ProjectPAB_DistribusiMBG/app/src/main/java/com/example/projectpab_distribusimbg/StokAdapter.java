package com.example.projectpab_distribusimbg_teori;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StokAdapter extends RecyclerView.Adapter<StokAdapter.StokViewHolder> {

    private ArrayList<StokModel> stokList;

    public StokAdapter(ArrayList<StokModel> stokList) {
        this.stokList = stokList;
    }

    @NonNull
    @Override
    public StokViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kelola_stok, parent, false);
        return new StokViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StokViewHolder holder, int position) {
        StokModel stok = stokList.get(position);
        holder.tvJudulStok.setText("INPUT PADA TANGGAL: " + stok.getTanggalInput());
        holder.tvJumlahStok.setText("Stok: " + stok.getJumlahStok() + " pcs");

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah anda yakin ingin menghapus?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        DatabaseHelper dbHelper = new DatabaseHelper(holder.itemView.getContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(DatabaseHelper.TABLE_KEPALA_GUDANG, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(stok.getId())});
                        db.close();

                        stokList.remove(position);
                        notifyItemRemoved(position);

                        Toast.makeText(holder.itemView.getContext(), "Stok berhasil dihapus", Toast.LENGTH_SHORT).show();

                        // Trigger penghitungan ulang total stok dari Activity, kalau diperlukan
                        if (holder.itemView.getContext() instanceof KelolaStok) {
                            ((KelolaStok) holder.itemView.getContext()).updateTotalStok();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return stokList.size();
    }

    // Tambahkan item baru
    public void addItem(StokModel stok) {
        stokList.add(stok);
        notifyItemInserted(stokList.size() - 1);
    }

    // Dapatkan semua data
    public ArrayList<StokModel> getStokList() {
        return stokList;
    }

    public static class StokViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudulStok, tvJumlahStok;
        ImageView btnDelete;

        public StokViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudulStok = itemView.findViewById(R.id.tvJudulStok);
            tvJumlahStok = itemView.findViewById(R.id.tvJumlahStok);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
