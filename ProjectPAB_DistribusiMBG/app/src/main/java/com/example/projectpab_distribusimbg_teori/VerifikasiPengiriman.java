package com.example.projectpab_distribusimbg_teori;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

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
import java.util.Date;
import java.util.List;

public class VerifikasiPengiriman extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VerifikasiAdapter adapter;
    private DatabaseHelper databaseHelper;

    private TextView tvTotalStok;
    private List<JadwalPengirimanModel> jadwalList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verifikasi_pengiriman);

        databaseHelper = new DatabaseHelper(this);
        tvTotalStok = findViewById(R.id.tvTotalStok);
        updateTotalStok();

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Verifikasi Pengiriman");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        LinearLayout layoutSort = findViewById(R.id.layoutSort);
        layoutSort.setOnClickListener(v -> showSortMenu(v));
        recyclerView = findViewById(R.id.recyclerViewVerifikasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        List<String> statusList = new ArrayList<>();
        statusList.add("Menunggu Verifikasi");
        jadwalList = databaseHelper.getJadwalByStatus(statusList); // Update jadwalList with data
        adapter = new VerifikasiAdapter(jadwalList, this::updateTotalStok); // Use jadwalList in adapter
        recyclerView.setAdapter(adapter);
    }

    private void updateTotalStok() {
        int totalStok = 0;
        List<StokModel> stokList = databaseHelper.getAllStok();
        for (StokModel item : stokList) {
            totalStok += item.getJumlahStok();
        }
        tvTotalStok.setText("Total Stok Saat Ini: " + totalStok);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTotalStok();
    }

    private void showSortMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sort_riwayat, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.sort_tanggal_terbaru) {
                sortByTanggalTerbaru();
            } else if (itemId == R.id.sort_tanggal_terlama) {
                sortByTanggalTerlama();
            } else if (itemId == R.id.sort_nama_sekolah) {
                sortByNamaSekolah();
            } else if (itemId == R.id.sort_jumlah_paket) {
                sortByJumlahPaket();
            } else if (itemId == R.id.sort_jumlah_paket_terkecil) {
                sortByJumlahPaketTerkecil();
            }
            return true;
        });

        popupMenu.show();
    }

    private void sortByTanggalTerbaru() {
        Collections.sort(jadwalList, (o1, o2) -> {
            Date d1 = o1.getParsedDate();
            Date d2 = o2.getParsedDate();
            if (d1 == null && d2 == null) return 0;
            if (d1 == null) return 1; // Null dates are considered older
            if (d2 == null) return -1;
            return d2.compareTo(d1); // Sort descending for latest dates
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByTanggalTerlama() {
        Collections.sort(jadwalList, (o1, o2) -> {
            Date d1 = o1.getParsedDate();
            Date d2 = o2.getParsedDate();
            if (d1 == null && d2 == null) return 0;
            if (d1 == null) return 1; // Null dates are considered older
            if (d2 == null) return -1;
            return d1.compareTo(d2); // Sort ascending for oldest dates
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByNamaSekolah() {
        Collections.sort(jadwalList, Comparator.comparing(JadwalPengirimanModel::getNamaSekolah, String::compareToIgnoreCase));
        adapter.notifyDataSetChanged();
    }

    private void sortByJumlahPaket() {
        Collections.sort(jadwalList, (o1, o2) -> Integer.compare(o2.getJumlahPengiriman(), o1.getJumlahPengiriman()));
        adapter.notifyDataSetChanged();
    }

    private void sortByJumlahPaketTerkecil() {
        Collections.sort(jadwalList, Comparator.comparingInt(JadwalPengirimanModel::getJumlahPengiriman));
        adapter.notifyDataSetChanged();
    }

    private int extractAngka(String text) {
        try {
            return Integer.parseInt(text.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
