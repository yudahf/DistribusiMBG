package com.example.projectpab_distribusimbg_teori;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class KelolaStok extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StokAdapter stokAdapter;
    private ArrayList<StokModel> stokList;
    private TextView tvTotalStok;
    private static final int REQUEST_TAMBAH_STOK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kelola_stok);

        // Retrieve the id from the Intent
        int id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Log.e("KelolaStok", "No id passed to activity");
            finish(); // Close the activity if no id is provided
        } else {
            Log.d("KelolaStok", "Received id: " + id);
            // Use the id as needed
        }

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Kelola Stok Barang");
        }

        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        recyclerView = findViewById(R.id.recyclerViewKelolaStok);
        tvTotalStok = findViewById(R.id.tvTotalStok);

        stokList = new ArrayList<>();
        stokAdapter = new StokAdapter(stokList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stokAdapter);

        // Load data awal
        loadStokFromDatabase();

        Button btnTambahStok = findViewById(R.id.btnTambahStok);
        if (btnTambahStok != null) {
            btnTambahStok.setOnClickListener(v -> {
                Intent intent = new Intent(KelolaStok.this, TambahStok.class);
                startActivityForResult(intent, REQUEST_TAMBAH_STOK);
            });
        }

        View customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setOnClickListener(v -> onBackPressed());
            }
        }
    }

    private void loadStokFromDatabase() {
        stokList.clear();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_KEPALA_GUDANG,
                null, null, null, null, null, DatabaseHelper.COLUMN_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String namaBarang = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_BARANG));
                int jumlahStok = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_JUMLAH_STOK));
                String tanggalInput = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TANGGAL_INPUT));

                stokList.add(new StokModel(id, namaBarang, jumlahStok, tanggalInput));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        stokAdapter.notifyDataSetChanged();
        updateTotalStok();
    }

    public void updateTotalStok() {
        int totalStok = 0;
        for (StokModel item : stokList) {
            totalStok += item.getJumlahStok();
        }
        tvTotalStok.setText("Total Stok Keseluruhan: " + totalStok + " pcs");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAMBAH_STOK && resultCode == RESULT_OK) {
            loadStokFromDatabase(); // refresh data dan total stok
        }
    }
}
