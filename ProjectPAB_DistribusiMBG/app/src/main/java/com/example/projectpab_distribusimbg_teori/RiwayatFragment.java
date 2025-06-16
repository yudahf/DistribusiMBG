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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class RiwayatFragment extends Fragment {

    private RecyclerView recyclerView;
    private RiwayatAdapter adapter;
    private List<JadwalItem> riwayatList;
    private final BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("REFRESH_RIWAYAT".equals(intent.getAction())) {
                loadRiwayatData();
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("RiwayatFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.activity_fragment_riwayat, container, false);

        // Register receiver untuk refresh data
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(refreshReceiver, new IntentFilter("REFRESH_RIWAYAT"));

        TextView toolbarTitle = view.findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Riwayat Pengiriman");
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

        recyclerView = view.findViewById(R.id.recyclerViewRiwayat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadRiwayatData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister receiver
        LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(refreshReceiver);
    }

    private void loadRiwayatData() {
        Log.d("RiwayatFragment", "Loading data for RecyclerView");

        // Use a background thread to fetch data
        new Thread(() -> {
            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
            riwayatList = dbHelper.getRiwayat();
            Log.d("RiwayatFragment", "Data size: " + riwayatList.size());

            // Update the RecyclerView on the main thread
            requireActivity().runOnUiThread(() -> {
                adapter = new RiwayatAdapter(riwayatList);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
    private void showSortMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sort_riwayat, popupMenu.getMenu());

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
            } else if (itemId == R.id.sort_jumlah_paket) {
                    sortByJumlahPaket();
                    return true;
            } else if (itemId == R.id.sort_jumlah_paket_terkecil) {
                sortByJumlahPaketTerkecil();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
    private void sortByTanggalTerbaru() {
        riwayatList.sort((o1, o2) -> {
            Date date1 = o1.getParsedDate();
            Date date2 = o2.getParsedDate();
            if (date1 == null || date2 == null) return 0;
            return date2.compareTo(date1);
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByTanggalTerlama() {
        riwayatList.sort((o1, o2) -> {
            Date date1 = o1.getParsedDate();
            Date date2 = o2.getParsedDate();
            if (date1 == null || date2 == null) return 0;
            return date1.compareTo(date2);
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByNamaSekolah() {
        riwayatList.sort((o1, o2) -> o1.getNamaSekolah().compareToIgnoreCase(o2.getNamaSekolah()));
        adapter.notifyDataSetChanged();
    }

    private void sortByJumlahPaket() {
        riwayatList.sort((o1, o2) -> {
            int jumlah1 = extractAngka(o1.getPackageCount());
            int jumlah2 = extractAngka(o2.getPackageCount());
            return Integer.compare(jumlah2, jumlah1); // terbesar ke terkecil
        });
        adapter.notifyDataSetChanged();
    }
    private void sortByJumlahPaketTerkecil() {
        riwayatList.sort((o1, o2) -> {
            int jumlah1 = extractAngka(o1.getPackageCount());
            int jumlah2 = extractAngka(o2.getPackageCount());
            return Integer.compare(jumlah1, jumlah2);
        });
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

