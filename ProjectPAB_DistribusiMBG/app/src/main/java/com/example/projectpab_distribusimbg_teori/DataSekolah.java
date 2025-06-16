package com.example.projectpab_distribusimbg_teori;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataSekolah extends AppCompatActivity {

    private static final String TAG = "DataSekolahDebug";

    private Button btnTambahSekolah;
    private RecyclerView recyclerViewSekolah;
    private SekolahAdapter sekolahAdapter;
    private List<Sekolah> sekolahList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_sekolah);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Data Sekolah");
        }

        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
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

        btnTambahSekolah = findViewById(R.id.btnTambahSekolah);

        if (btnTambahSekolah != null) {
            btnTambahSekolah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DataSekolah.this, TambahSekolah.class);
                    startActivity(intent);
                }
            });
        }

        dbHelper = new DatabaseHelper(this);
        Log.d(TAG, "DatabaseHelper initialized.");

        LinearLayout layoutSort = findViewById(R.id.layoutSort);
        layoutSort.setOnClickListener(v -> showSortMenu(v));
        recyclerViewSekolah = findViewById(R.id.recyclerViewSekolah);
        recyclerViewSekolah.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "RecyclerView and LayoutManager initialized.");

        sekolahList = new ArrayList<>();
        sekolahAdapter = new SekolahAdapter(sekolahList, dbHelper);
        recyclerViewSekolah.setAdapter(sekolahAdapter);
        Log.d(TAG, "SekolahAdapter set.");

        loadSekolahFromDatabase();

        setupRecyclerViewActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Reloading schools from database.");
        loadSekolahFromDatabase();
    }

    private void loadSekolahFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sekolah", null);

        sekolahList.clear();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("nama_sekolah"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("alamat_sekolah"));
                String operator = cursor.getString(cursor.getColumnIndexOrThrow("nama_operator"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("no_telepon"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                sekolahList.add(new Sekolah(id, name, address, operator, phone, status));
            } while (cursor.moveToNext());
        }

        cursor.close();
        sekolahAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewActions() {
        sekolahAdapter.setOnItemClickListener(new SekolahAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Sekolah sekolah) {
                Intent intent = new Intent(DataSekolah.this, EditSekolah.class); // Corrected target activity
                intent.putExtra("id", sekolah.getId());
                intent.putExtra("nama_sekolah", sekolah.getNamaSekolah());
                intent.putExtra("alamat_sekolah", sekolah.getAlamat());
                intent.putExtra("nama_operator", sekolah.getNamaOperator());
                intent.putExtra("no_telepon", sekolah.getNoTelepon());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Sekolah sekolah) {
                new AlertDialog.Builder(DataSekolah.this)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("sekolah", "id = ?", new String[]{String.valueOf(sekolah.getId())});
                        loadSekolahFromDatabase();
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
            }
        });
    }

    private void showSortMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sort_sekolah, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.sort_sekolah_A) {
                sortBySekolahA();
                return true;
            } else if (itemId == R.id.sort_sekolah_B) {
                sortBySekolahB();
                return true;
            } else if (itemId == R.id.sort_status) {
                sortByStatus();
            return true;
            }  else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void sortByStatus() {
        sekolahList.sort((s1, s2) -> getStatusOrder(s1.getStatus()) - getStatusOrder(s2.getStatus()));
        sekolahAdapter.notifyDataSetChanged();
    }

    private int getStatusOrder(String status) {
        if ("Aktif".equalsIgnoreCase(status)) return 0;
        if ("Nonaktif".equalsIgnoreCase(status)) return 1;
        return 99;
    }

    private void sortBySekolahA() {
        sekolahList.sort((s1, s2) -> s1.getNamaSekolah().compareToIgnoreCase(s2.getNamaSekolah()));
        sekolahAdapter.notifyDataSetChanged();
    }
    private void sortBySekolahB() {
        sekolahList.sort((s1, s2) -> s2.getNamaSekolah().compareToIgnoreCase(s1.getNamaSekolah()));
        sekolahAdapter.notifyDataSetChanged();
    }
}
