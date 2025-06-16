package com.example.projectpab_distribusimbg_teori;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TambahSekolah extends AppCompatActivity {

    private EditText etNamaSekolah, etAlamat, etNamaOperator, etNoHp;
    private Button btnSimpan, btnBatal;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_sekolah);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Inisialisasi komponen
        etNamaSekolah = findViewById(R.id.etNamaSekolah);
        etAlamat = findViewById(R.id.etAlamat);
        etNamaOperator = findViewById(R.id.etNamaOperator);
        etNoHp = findViewById(R.id.etNoHp);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);

        // Set Judul Toolbar
        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Tambah Data Sekolah");
        }

        // Aksi tombol Back di Toolbar
        View customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setOnClickListener(v -> finish());
            }
        }

        // Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String namaSekolah = etNamaSekolah.getText().toString().trim();
            String alamat = etAlamat.getText().toString().trim();
            String namaOperator = etNamaOperator.getText().toString().trim();
            String noHp = etNoHp.getText().toString().trim();

            if (namaSekolah.isEmpty() || alamat.isEmpty() || namaOperator.isEmpty() || noHp.isEmpty()) {
                showCustomToast("Harap isi semua data!", android.R.drawable.ic_dialog_alert);
            } else {
                boolean isInserted = dbHelper.addSekolah(namaSekolah, alamat, namaOperator, noHp);
                if (isInserted) {
                    showCustomToast("Data berhasil disimpan!", android.R.drawable.checkbox_on_background);
                    etNamaSekolah.setText("");
                    etAlamat.setText("");
                    etNamaOperator.setText("");
                    etNoHp.setText("");
                } else {
                    showCustomToast("Gagal menyimpan data!", android.R.drawable.ic_delete);
                }
            }
        });

        // Tombol Batal
        btnBatal.setOnClickListener(v -> finish());
    }

    // Metode Custom Toast
    private void showCustomToast(String message, int iconResId) {
        View layout = getLayoutInflater().inflate(R.layout.custom_toast, null);

        ImageView toastIcon = layout.findViewById(R.id.toastIcon);
        TextView toastText = layout.findViewById(R.id.toastText);

        toastIcon.setImageResource(iconResId);
        toastText.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
