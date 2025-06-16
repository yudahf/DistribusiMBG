package com.example.projectpab_distribusimbg_teori;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DalamProsesFragment extends Fragment {

    private RecyclerView recyclerView;
    private DalamProsesAdapter adapter;
    private List<JadwalItem> jadwalList;

    private final BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isAdded()) return; // pastikan fragment masih attach
            String action = intent.getAction();
            if ("REFRESH_DALAM_PROSES".equals(action) || "REFRESH_JADWAL".equals(action)) {
                loadData();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_dalam_proses, container, false);

        TextView toolbarTitle = view.findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Dalam Proses");
        }

        View customToolbar = view.findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setVisibility(View.GONE);
                backButton.setOnClickListener(null);
            }
        }
        LinearLayout layoutSort = view.findViewById(R.id.layoutSort);
        layoutSort.setOnClickListener(v -> showSortMenu(v));

        recyclerView = view.findViewById(R.id.recyclerViewDalamProses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        jadwalList = new ArrayList<>();
        loadData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Daftarkan receiver hanya sekali di onStart
        IntentFilter filter = new IntentFilter();
        filter.addAction("REFRESH_DALAM_PROSES");
        filter.addAction("REFRESH_JADWAL");
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(refreshReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister receiver di onStop
        try {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(refreshReceiver);
        } catch (IllegalArgumentException e) {
            // Ignore jika receiver sudah tidak terdaftar
        }
    }

    private void loadData() {
        if (!isAdded()) return;

        try (DatabaseHelper dbHelper = new DatabaseHelper(requireContext())) {
            List<String> statusFilter = new ArrayList<>();
            statusFilter.add("Dalam Proses");

            List<JadwalPengirimanModel> models = dbHelper.getJadwalByStatus(statusFilter);

            jadwalList = new ArrayList<>();
            for (JadwalPengirimanModel model : models) {
                jadwalList.add(new JadwalItem(
                        String.valueOf(model.getId()),
                        model.getNamaSekolah(),
                        model.getTanggalPengiriman(),
                        model.getJamPengiriman() + " WIB",
                        model.getJumlahPengiriman() + " Paket"
                ));
            }
        }

        adapter = new DalamProsesAdapter(jadwalList, item -> {
            try (DatabaseHelper dbHelper = new DatabaseHelper(requireContext())) {
                dbHelper.updateStatusPengiriman(Integer.parseInt(item.getId()), "Selesai");
            }
            loadData();

            // Broadcast untuk refresh fragment lain
            Intent intentRiwayat = new Intent("REFRESH_RIWAYAT");
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intentRiwayat);

            Intent refreshAktif = new Intent("REFRESH_JADWAL");
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(refreshAktif);
        });

        recyclerView.setAdapter(adapter);
    }

    private void showSortMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sort_dalamproses, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.sort_tanggal_terbaru) {
                sortByTanggalTerbaru();
                return true;
            } else if (itemId == R.id.sort_tanggal_terlama) {
                sortByTanggalTerlama();
                return true;
            } else if (itemId == R.id.sort_nama_sekolah) {
                sortByNamaSekolah();
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void sortByTanggalTerbaru() {
        jadwalList.sort((o1, o2) -> {
            Date date1 = o1.getParsedDate();
            Date date2 = o2.getParsedDate();
            if (date1 == null || date2 == null) return 0;
            return date2.compareTo(date1);
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByTanggalTerlama() {
        jadwalList.sort((o1, o2) -> {
            Date date1 = o1.getParsedDate();
            Date date2 = o2.getParsedDate();
            if (date1 == null || date2 == null) return 0;
            return date1.compareTo(date2);
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByNamaSekolah() {
        jadwalList.sort((o1, o2) -> o1.getNamaSekolah().compareToIgnoreCase(o2.getNamaSekolah()));
        adapter.notifyDataSetChanged();
    }
}
