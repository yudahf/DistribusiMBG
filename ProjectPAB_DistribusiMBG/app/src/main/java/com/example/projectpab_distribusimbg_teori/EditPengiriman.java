package com.example.projectpab_distribusimbg_teori;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

        // Ambil jadwalId dari Intent
        jadwalId = getIntent().getIntExtra("jadwalId", -1);
        if (jadwalId != -1) {
            // Ambil data awal dari database
            JadwalPengirimanModel jadwal = dbHelper.getJadwalById(jadwalId);
            if (jadwal != null) {
                etSekolah.setText(jadwal.getNamaSekolah());
                etJumlahPengiriman.setText(String.valueOf(jadwal.getJumlahPengiriman()));
                etTanggalPengiriman.setText(jadwal.getTanggalPengiriman());
                etJadwalPengiriman.setText(jadwal.getJamPengiriman());
            }
        }

        Button btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(v -> {
            // Simpan perubahan ke database
            String namaSekolah = etSekolah.getText().toString();
            int jumlahPengiriman = Integer.parseInt(etJumlahPengiriman.getText().toString());
            String tanggalPengiriman = etTanggalPengiriman.getText().toString();
            String jamPengiriman = etJadwalPengiriman.getText().toString();
            String status = "Menunggu Verifikasi"; // Set status explicitly

            dbHelper.updateJadwalPengiriman(jadwalId, namaSekolah, tanggalPengiriman, jumlahPengiriman, jamPengiriman, status);

            // Kirim hasil edit kembali ke JadwalAktifFragment
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        Button btnBatal = findViewById(R.id.btnBatal);
        btnBatal.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
