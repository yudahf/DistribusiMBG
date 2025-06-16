package com.example.projectpab_distribusimbg_teori;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class JadwalPengiriman extends AppCompatActivity {


    private AutoCompleteTextView etSekolah;
    private EditText etJumlahPengiriman, etTanggalPengiriman, etJadwalPengiriman;
    private Button btnSimpan, btnBatal;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_pengiriman);

        dbHelper = new DatabaseHelper(this);

        etSekolah = findViewById(R.id.etSekolah);
        etJumlahPengiriman = findViewById(R.id.etJumlahPengiriman);
        etTanggalPengiriman = findViewById(R.id.etTanggalPengiriman);
        etJadwalPengiriman = findViewById(R.id.etJadwalPengiriman);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Atur Jadwal Pengiriman");
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

        // Tampilkan dialog tanggal saat diklik
        etTanggalPengiriman.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    com.example.projectpab_distribusimbg_teori.JadwalPengiriman.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        etTanggalPengiriman.setText(date);
                    }, year, month, day);

            datePickerDialog.show();
        });

        // Tampilkan dialog waktu saat diklik
        etJadwalPengiriman.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    com.example.projectpab_distribusimbg_teori.JadwalPengiriman.this,
                    (view, hourOfDay, minute1) -> {
                        String time = hourOfDay + ":" + (minute1 < 10 ? "0" + minute1 : minute1);
                        etJadwalPengiriman.setText(time);
                    }, hour, minute, true);

            timePickerDialog.show();
        });

        // Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String sekolah = etSekolah.getText().toString().trim();
            String jumlahStr = etJumlahPengiriman.getText().toString().trim();
            String tanggal = etTanggalPengiriman.getText().toString().trim();
            String jadwal = etJadwalPengiriman.getText().toString().trim();

            if (sekolah.isEmpty() || jumlahStr.isEmpty() || tanggal.isEmpty() || jadwal.isEmpty()) {
                Toast.makeText(com.example.projectpab_distribusimbg_teori.JadwalPengiriman.this, "Harap lengkapi semua kolom", Toast.LENGTH_SHORT).show();
            } else {
                int jumlah = Integer.parseInt(jumlahStr);
                boolean sukses = dbHelper.tambahJadwalPengiriman(tanggal, jadwal, jumlah, sekolah);

                if (sukses) {
                    Toast.makeText(com.example.projectpab_distribusimbg_teori.JadwalPengiriman.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();

                    // Notify JadwalAktifFragment to refresh RecyclerView
                    Intent intent = new Intent("com.example.projectpab_distribusimbg.REFRESH_JADWAL");
                    sendBroadcast(intent);

                    finish();
                } else {
                    Toast.makeText(com.example.projectpab_distribusimbg_teori.JadwalPengiriman.this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Tombol Batal
        btnBatal.setOnClickListener(v -> {
            etSekolah.setText("");
            etJumlahPengiriman.setText("");
            etTanggalPengiriman.setText("");
            etJadwalPengiriman.setText("");
        });

        // Isi data sekolah ke dropdown
        isiDropdownSekolah();
    }

    private void isiDropdownSekolah() {
        ArrayList<String> listSekolah = dbHelper.getSemuaNamaSekolah();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listSekolah);
        etSekolah.setAdapter(adapter);
    }
}
