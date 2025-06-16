package com.example.projectpab_distribusimbg_teori;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JadwalAktifFragment extends Fragment {

    private JadwalAdapter jadwalAdapter;
    private List<JadwalPengirimanModel> jadwalList = new ArrayList<>();
    private RecyclerView recyclerViewJadwal;

    private ActivityResultLauncher<Intent> editJadwalLauncher;
    private Button btnTambahPengiriman;
    private ImageView backButton, btnLogout;
    private BroadcastReceiver refreshReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_jadwal_aktif, container, false);

        recyclerViewJadwal = rootView.findViewById(R.id.recyclerViewJadwal);
        recyclerViewJadwal.setLayoutManager(new LinearLayoutManager(getContext()));

        btnTambahPengiriman = rootView.findViewById(R.id.btnTambahPengiriman);
        btnTambahPengiriman.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), JadwalPengiriman.class);
            startActivityForResult(intent, 1);
        });

        TextView toolbarTitle = rootView.findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Jadwal Aktif");
        }

        View customToolbar = rootView.findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setOnClickListener(v -> {
                    showLogoutConfirmationDialog();
                });
            }
        }

        Button btnBatal = rootView.findViewById(R.id.btnBatal);
        if (btnBatal != null) {
            btnBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLogoutConfirmationDialog();
                }
            });
        }

        jadwalAdapter = new JadwalAdapter(jadwalList, jadwal -> {
            Intent intent = new Intent(getActivity(), EditPengiriman.class);
            intent.putExtra("jadwal_id", jadwal.getId());
            intent.putExtra("namaSekolah", jadwal.getNamaSekolah());
            intent.putExtra("tanggalPengiriman", jadwal.getTanggalPengiriman());
            intent.putExtra("jamPengiriman", jadwal.getJamPengiriman());
            intent.putExtra("jumlahPengiriman", jadwal.getJumlahPengiriman());
            editJadwalLauncher.launch(intent);
        });

        recyclerViewJadwal.setAdapter(jadwalAdapter);

        loadJadwalFromDatabase();

        editJadwalLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        int jadwalId = data.getIntExtra("jadwal_id", -1);
                        String namaSekolah = data.getStringExtra("namaSekolah");
                        String tanggalPengiriman = data.getStringExtra("tanggalPengiriman");
                        String jamPengiriman = data.getStringExtra("jamPengiriman");
                        int jumlahPengiriman = data.getIntExtra("jumlahPengiriman", 0);

                        for (int i = 0; i < jadwalList.size(); i++) {
                            JadwalPengirimanModel jadwal = jadwalList.get(i);
                            if (jadwal.getId() == jadwalId) {
                                jadwal.setNamaSekolah(namaSekolah);
                                jadwal.setTanggalPengiriman(tanggalPengiriman);
                                jadwal.setJamPengiriman(jamPengiriman);
                                jadwal.setJumlahPengiriman(jumlahPengiriman);
                                jadwalAdapter.notifyItemChanged(i);
                                break;
                            }
                        }
                    }
                }
        );

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getJadwalList().observe(getViewLifecycleOwner(), jadwalList -> {
            jadwalAdapter.updateData(jadwalList);
        });
    }

    private void loadJadwalFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        jadwalList.clear();

        Cursor cursor = db.query("jadwal_pengiriman", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String namaSekolah = cursor.getString(cursor.getColumnIndexOrThrow("nama_sekolah"));
            String tanggalPengiriman = cursor.getString(cursor.getColumnIndexOrThrow("tanggal_pengiriman"));
            String jamPengiriman = cursor.getString(cursor.getColumnIndexOrThrow("jadwal_pengiriman"));
            int jumlahPengiriman = cursor.getInt(cursor.getColumnIndexOrThrow("jumlah_pengiriman"));

            jadwalList.add(new JadwalPengirimanModel(id, namaSekolah, tanggalPengiriman, jamPengiriman, jumlahPengiriman));
        }
        cursor.close();
        db.close();

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getMutableJadwalList().setValue(new ArrayList<>(jadwalList));

        if (recyclerViewJadwal.getAdapter() != null) {
            recyclerViewJadwal.getAdapter().notifyDataSetChanged();
        }
    }
    public void onResume() {
        super.onResume();
        loadJadwalFromDatabase();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Daftarkan receiver untuk update otomatis
        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadJadwalFromDatabase(); // reload data saat broadcast diterima
            }
        };
        IntentFilter filter = new IntentFilter("com.example.projectpab_distribusimbg.REFRESH_JADWAL");
        requireActivity().registerReceiver(refreshReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Jangan lupa unregister receiver untuk menghindari memory leak
        if (refreshReceiver != null) {
            requireActivity().unregisterReceiver(refreshReceiver);
            refreshReceiver = null;
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
            loadJadwalFromDatabase();
            jadwalAdapter.notifyDataSetChanged();
        }
    }
}
