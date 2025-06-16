package com.example.projectpab_distribusimbg_teori;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditPengiriman extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int jadwalId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pengiriman);

        dbHelper = new DatabaseHelper(this);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Edit Pengiriman");
        }

        View customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setOnClickListener(v -> onBackPressed());
            }
        }

        AutoCompleteTextView etSekolah = findViewById(R.id.etSekolah);
        EditText etJumlahPengiriman = findViewById(R.id.etJumlahPengiriman);
        EditText etTanggalPengiriman = findViewById(R.id.etTanggalPengiriman);
        EditText etJadwalPengiriman = findViewById(R.id.etJadwalPengiriman);

        etTanggalPengiriman.setFocusable(false);
        etJadwalPengiriman.setFocusable(false);

        etTanggalPengiriman.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditPengiriman.this,
                    (view, year, month, dayOfMonth) -> {
                        // Format tanggal: day/month/year tanpa leading zero
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        etTanggalPengiriman.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        etJadwalPengiriman.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    EditPengiriman.this,
                    (view, hourOfDay, minute) -> {
                        // Format waktu dengan menit 2 digit
                        String selectedTime = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
                        etJadwalPengiriman.setText(selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });

        // Load autocomplete sekolah
        ArrayAdapter<String> sekolahAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                dbHelper.getSemuaNamaSekolah()
        );
        etSekolah.setAdapter(sekolahAdapter);
        etSekolah.setOnClickListener(v -> etSekolah.showDropDown());

        // Ambil data intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jadwalId = extras.getInt("jadwal_id", -1);
            etSekolah.setText(extras.getString("namaSekolah", ""));
            etTanggalPengiriman.setText(extras.getString("tanggalPengiriman", ""));
            etJadwalPengiriman.setText(extras.getString("jamPengiriman", ""));
            etJumlahPengiriman.setText(String.valueOf(extras.getInt("jumlahPengiriman", 0)));
        }

        Button btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(v -> {
            String namaSekolah = etSekolah.getText().toString().trim();
            String tanggalPengiriman = etTanggalPengiriman.getText().toString().trim();
            String jamPengiriman = etJadwalPengiriman.getText().toString().trim();
            int jumlahPengiriman;

            if (namaSekolah.isEmpty() || tanggalPengiriman.isEmpty() || jamPengiriman.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                jumlahPengiriman = Integer.parseInt(etJumlahPengiriman.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Jumlah pengiriman harus berupa angka", Toast.LENGTH_SHORT).show();
                return;
            }

            if (jadwalId == -1) {
                Toast.makeText(this, "ID jadwal tidak valid. Tidak dapat mengupdate data.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.updateJadwalPengiriman(jadwalId,
                    tanggalPengiriman,
                    jamPengiriman,
                    jumlahPengiriman,
                    namaSekolah);

            if (success) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("jadwal_id", jadwalId);
                resultIntent.putExtra("namaSekolah", namaSekolah);
                resultIntent.putExtra("tanggalPengiriman", tanggalPengiriman);
                resultIntent.putExtra("jamPengiriman", jamPengiriman);
                resultIntent.putExtra("jumlahPengiriman", jumlahPengiriman);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnBatal = findViewById(R.id.btnBatal);
        btnBatal.setOnClickListener(v -> {
            Toast.makeText(this, "Perubahan dibatalkan", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

