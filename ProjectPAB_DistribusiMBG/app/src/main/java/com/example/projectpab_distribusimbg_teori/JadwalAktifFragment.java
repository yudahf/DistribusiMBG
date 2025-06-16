package com.example.projectpab_distribusimbg_teori;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JadwalAktifFragment extends Fragment {

    private JadwalAdapter jadwalAdapter;
    private List<JadwalPengirimanModel> jadwalList = new ArrayList<>();
    private RecyclerView recyclerViewJadwal;

    private ActivityResultLauncher<Intent> editJadwalLauncher;
    private Button btnTambahPengiriman;
    private ImageView backButton, btnLogout;
    private BroadcastReceiver refreshReceiver;
    private boolean isReceiverRegistered = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_jadwal_aktif, container, false);

        TextView toolbarTitle = rootView.findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Dashboard Kepala Distribusi");
        }

        LinearLayout layoutSort = rootView.findViewById(R.id.layoutSort);
        layoutSort.setOnClickListener(v -> showSortMenu(v));

        View customToolbar = rootView.findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setVisibility(View.GONE);
                backButton.setOnClickListener(null);
            }
        }

        recyclerViewJadwal = rootView.findViewById(R.id.recyclerViewJadwal);
        recyclerViewJadwal.setLayoutManager(new LinearLayoutManager(getContext()));

        btnTambahPengiriman = rootView.findViewById(R.id.btnTambahPengiriman);
        btnTambahPengiriman.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), JadwalPengiriman.class);
            startActivity(intent);
        });

        Button btnBatal = rootView.findViewById(R.id.btnBatal);
        if (btnBatal != null) {
            btnBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLogoutConfirmationDialog();
                }
            });
        }
        loadData();
        return rootView;
    }

    private void loadData() {
        try (DatabaseHelper dbHelper = new DatabaseHelper(requireContext())) {
            List<String> statusFilter = new ArrayList<>();
            statusFilter.add("Menunggu Verifikasi");
            statusFilter.add("Dalam Proses");
            statusFilter.add("Ditolak, Stok Tidak Cukup!");

            jadwalList = dbHelper.getJadwalByStatus(statusFilter);
        }

        jadwalAdapter = new JadwalAdapter(jadwalList, jadwal -> {
            Intent intent = new Intent(getContext(), EditPengiriman.class);
            intent.putExtra("jadwalId", jadwal.getId());
            startActivity(intent);
        });
        recyclerViewJadwal.setAdapter(jadwalAdapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getJadwalList().observe(getViewLifecycleOwner(), jadwalList -> {
            jadwalAdapter.updateData(jadwalList);
        });
    }

    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isReceiverRegistered) {
            refreshReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    loadData();
                }
            };
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(refreshReceiver, new IntentFilter("REFRESH_JADWAL"));
            isReceiverRegistered = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(refreshReceiver);
            isReceiverRegistered = false;
        }
    }

    private void showLogoutConfirmationDialog() {
        if (getActivity() == null) return;

        new AlertDialog.Builder(getActivity())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin akan keluar akun?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    Toast.makeText(getActivity(), "Anda telah keluar akun", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // Reload data from database and refresh RecyclerView
            loadData();
            jadwalAdapter.notifyDataSetChanged();

            // Kirim broadcast untuk memperbarui halaman verifikasi pengiriman
            Intent intent = new Intent("com.example.projectpab_distribusimbg.REFRESH_VERIFIKASI");
            requireActivity().sendBroadcast(intent);
        }
    }

    private void showSortMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sort, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.sort_tanggal) {
                sortByTanggal();
                return true;
            } else if (itemId == R.id.sort_tanggal_terbaru) {
                sortByTanggalTerbaru();
                return true;
            } else if (itemId == R.id.sort_status) {
                sortByStatus();
                return true;
            } else if (itemId == R.id.sort_nama) {
                sortByNamaSekolah();
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    private void sortByTanggal() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Collections.sort(jadwalList, (o1, o2) -> {
            try {
                Date d1 = sdf.parse(o1.getTanggalPengiriman());
                Date d2 = sdf.parse(o2.getTanggalPengiriman());
                return d1.compareTo(d2); // ascending: terlama → terbaru
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
        jadwalAdapter.notifyDataSetChanged();
    }

    private void sortByTanggalTerbaru() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Collections.sort(jadwalList, (o1, o2) -> {
            try {
                Date d1 = sdf.parse(o1.getTanggalPengiriman());
                Date d2 = sdf.parse(o2.getTanggalPengiriman());
                return d2.compareTo(d1); // ascending: terbaru → terlama
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
        jadwalAdapter.notifyDataSetChanged();
    }

    private void sortByStatus() {
        Collections.sort(jadwalList, Comparator.comparing(JadwalPengirimanModel::getStatus));
        jadwalAdapter.notifyDataSetChanged();
    }

    private void sortByNamaSekolah() {
        Collections.sort(jadwalList, Comparator.comparing(JadwalPengirimanModel::getNamaSekolah));
        jadwalAdapter.notifyDataSetChanged();
    }

}
