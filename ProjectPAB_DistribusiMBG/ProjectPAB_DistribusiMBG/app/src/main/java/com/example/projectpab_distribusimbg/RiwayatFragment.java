package com.example.projectpab_distribusimbg_teori;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RiwayatFragment extends Fragment {

    private RecyclerView recyclerView;
    private RiwayatAdapter adapter;
    private List<JadwalItem> riwayatList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("RiwayatFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.activity_fragment_riwayat, container, false);

        TextView toolbarTitle = view.findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Riwayat Pengiriman");
        }

        View customToolbar = view.findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);

            if (backButton != null) {
                backButton.setOnClickListener(v -> {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                });
            }
        }

        recyclerView = view.findViewById(R.id.recyclerViewRiwayat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadRiwayatData();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort_riwayat, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRiwayat(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRiwayat(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_tanggal_terbaru:
                sortRiwayat((o1, o2) -> o2.getTanggal().compareTo(o1.getTanggal()));
                return true;
            case R.id.sort_tanggal_terlama:
                sortRiwayat((o1, o2) -> o1.getTanggal().compareTo(o2.getTanggal()));
                return true;
            case R.id.sort_nama_sekolah:
                sortRiwayat(Comparator.comparing(JadwalItem::getNamaSekolah));
                return true;
            case R.id.sort_jumlah_paket:
                sortRiwayat((o1, o2) -> Integer.compare(o2.getJumlahPaket(), o1.getJumlahPaket()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void filterRiwayat(String query) {
        List<JadwalItem> filteredList = new ArrayList<>();
        for (JadwalItem item : riwayatList) {
            if (item.getNamaSekolah().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateData(filteredList);
    }

    private void sortRiwayat(Comparator<JadwalItem> comparator) {
        Collections.sort(riwayatList, comparator);
        adapter.notifyDataSetChanged();
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
}

