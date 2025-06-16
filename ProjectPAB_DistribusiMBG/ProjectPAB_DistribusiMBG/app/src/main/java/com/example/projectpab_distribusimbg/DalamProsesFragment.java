package com.example.projectpab_distribusimbg_teori;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DalamProsesFragment extends Fragment {

    private RecyclerView recyclerView;
    private DalamProsesAdapter adapter;
    private List<JadwalItem> jadwalList;
    private SharedViewModel sharedViewModel;

    private final BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("REFRESH_DALAM_PROSES".equals(intent.getAction())) {
                loadData(); // Method to reload data from the database
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_dalam_proses, container, false);

        TextView toolbarTitle = view.findViewById(R.id.JudulNavbar); // Replaced 'rootView' with 'view'
        if (toolbarTitle != null) {
            toolbarTitle.setText("Dalam Proses");
        }

        View customToolbar = view.findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);

            if (backButton != null) {
                backButton.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
            }
        }

        recyclerView = view.findViewById(R.id.recyclerViewDalamProses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try (DatabaseHelper dbHelper = new DatabaseHelper(requireContext())) {
            jadwalList = dbHelper.getJadwalDalamProses(); // Fetch data from Jadwal Aktif
        }

        adapter = new DalamProsesAdapter(jadwalList, item -> {
            try (DatabaseHelper dbHelper = new DatabaseHelper(requireContext())) {
                // Move the item to the Riwayat table
                dbHelper.addRiwayat(item);

                // Remove the item from the jadwal_pengiriman table
                dbHelper.deleteJadwalPengiriman(item.getId());
            }

            // Update the RecyclerView
            jadwalList.remove(item);
            adapter.notifyItemRemoved(jadwalList.indexOf(item));
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getJadwalList().observe(getViewLifecycleOwner(), jadwalList -> {
            this.jadwalList.clear();
            for (JadwalPengirimanModel model : jadwalList) {
                this.jadwalList.add(new JadwalItem(
                    String.valueOf(model.getId()),
                    model.getNamaSekolah(),
                    model.getTanggalPengiriman(),
                    model.getJamPengiriman() + " WIB",
                    model.getJumlahPengiriman() + " Paket"
                ));
            }
            adapter.notifyDataSetChanged();
        });

        // Register the broadcast receiver
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                refreshReceiver, new IntentFilter("REFRESH_DALAM_PROSES"));

        // Initial data load
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the broadcast receiver
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(refreshReceiver);
    }

    private List<JadwalItem> getJadwalAktif() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        List<JadwalPengirimanModel> jadwalPengirimanModels = dbHelper.getAllJadwalPengiriman(); // Assuming this method fetches all active schedules
        List<JadwalItem> jadwalItems = new ArrayList<>();

        for (JadwalPengirimanModel model : jadwalPengirimanModels) {
            jadwalItems.add(new JadwalItem(
                String.valueOf(model.getId()),
                model.getNamaSekolah(),
                model.getTanggalPengiriman(),
                model.getJamPengiriman() + " WIB",
                model.getJumlahPengiriman() + " Paket"
            ));
        }

        return jadwalItems;
    }

    private void moveToRiwayat(JadwalItem item) {
        jadwalList.remove(item);
        adapter.notifyDataSetChanged();
        // Logic to add the item to Riwayat page can be implemented here
    }

    private void loadData() {
        // Example logic to reload data from the database
        try (DatabaseHelper dbHelper = new DatabaseHelper(requireContext())) {
            jadwalList = dbHelper.getJadwalDalamProses(); // Assuming this method exists
        }
        adapter = new DalamProsesAdapter(jadwalList, item -> {
            try (DatabaseHelper dbHelperInner = new DatabaseHelper(requireContext())) {
                // Move the item to the Riwayat table
                dbHelperInner.addRiwayat(item);

                // Remove the item from the jadwal_pengiriman table
                dbHelperInner.deleteJadwalPengiriman(item.getId());
            }

            // Update the RecyclerView
            jadwalList.remove(item);
            adapter.notifyItemRemoved(jadwalList.indexOf(item));
        });
        recyclerView.setAdapter(adapter);
    }
}
