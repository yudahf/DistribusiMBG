package com.example.projectpab_distribusimbg_teori;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Statistik extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistik);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Statistik");
        }

        View customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load statistics data
        loadStatistics();
    }

    private void loadStatistics() {
        SQLiteDatabase db = null;
        try {
            // Assuming a database helper class exists
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            db = dbHelper.getReadableDatabase();

            // Query for total schools
            Cursor cursor = db.rawQuery("SELECT COUNT(nama_sekolah) AS total FROM sekolah", null);
            if (cursor.moveToFirst()) {
                int totalSchools = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
                TextView tvTotalSekolah = findViewById(R.id.tvTotalSekolah);
                tvTotalSekolah.setText(String.valueOf(totalSchools));
            }
            cursor.close();

            // Query for active schools
            cursor = db.rawQuery("SELECT COUNT(*) AS total FROM sekolah WHERE status = ?", new String[]{"aktif"});
            if (cursor.moveToFirst()) {
                int sekolahAktif = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
                TextView tvSekolahAktif = findViewById(R.id.tvSekolahAktif);
                tvSekolahAktif.setText(String.valueOf(sekolahAktif));
            }
            cursor.close();

            // Query for packages in process
            cursor = db.rawQuery("SELECT COUNT(*) AS total FROM jadwal_pengiriman WHERE status = ?", new String[]{"Dalam Proses"});
            if (cursor.moveToFirst()) {
                int packagesInProcess = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
                TextView tvPaketProses = findViewById(R.id.tvPaketProses);
                tvPaketProses.setText(String.valueOf(packagesInProcess));
            }
            cursor.close();

            // Query for available stock
            cursor = db.rawQuery("SELECT SUM(jumlah_stok) AS total FROM kepala_gudang", null);
            if (cursor.moveToFirst()) {
                int availableStock = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
                TextView tvStokTersedia = findViewById(R.id.tvStokTersedia);
                tvStokTersedia.setText(String.valueOf(availableStock));
            }
            cursor.close();

            // Query for delivered packages
            cursor = db.rawQuery("SELECT COUNT(*) AS total FROM jadwal_pengiriman WHERE status = ?", new String[]{"Selesai"});
            if (cursor.moveToFirst()) {
                int deliveredPackages = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
                TextView tvPaketTerkirim = findViewById(R.id.tvPaketTerkirim);
                tvPaketTerkirim.setText(String.valueOf(deliveredPackages));
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

