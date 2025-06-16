package com.example.projectpab_distribusimbg_teori;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditSekolah extends AppCompatActivity {

    private EditText etNamaSekolah, etAlamatSekolah, etNamaOperator, etNoTelepon;
    private Button btnSave;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_sekolah);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set judul navbar
        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Edit Data Sekolah");
        }

        // Tombol kembali
        View customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setOnClickListener(v -> onBackPressed());
            }
        }

        // Ambil data dari intent
        id = getIntent().getIntExtra("id", -1);
        String namaSekolah = getIntent().getStringExtra("nama_sekolah");
        String alamatSekolah = getIntent().getStringExtra("alamat_sekolah");
        String namaOperator = getIntent().getStringExtra("nama_operator");
        String noTelepon = getIntent().getStringExtra("no_telepon");

        // Inisialisasi view
        etNamaSekolah = findViewById(R.id.etNamaSekolah);
        etAlamatSekolah = findViewById(R.id.etAlamat);
        etNamaOperator = findViewById(R.id.etNamaOperator);
        etNoTelepon = findViewById(R.id.etNoHp);
        btnSave = findViewById(R.id.btnSimpan);

        // Tampilkan data awal
        etNamaSekolah.setText(namaSekolah);
        etAlamatSekolah.setText(alamatSekolah);
        etNamaOperator.setText(namaOperator);
        etNoTelepon.setText(noTelepon);

        // Aksi simpan
        btnSave.setOnClickListener(v -> {
            String updatedNamaSekolah = etNamaSekolah.getText().toString().trim();
            String updatedAlamatSekolah = etAlamatSekolah.getText().toString().trim();
            String updatedNamaOperator = etNamaOperator.getText().toString().trim();
            String updatedNoTelepon = etNoTelepon.getText().toString().trim();

            SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nama_sekolah", updatedNamaSekolah);
            values.put("alamat_sekolah", updatedAlamatSekolah);
            values.put("nama_operator", updatedNamaOperator);
            values.put("no_telepon", updatedNoTelepon);

            db.update("sekolah", values, "id = ?", new String[]{String.valueOf(id)});

            showCustomToast("Data Berhasil Diedit!", android.R.drawable.ic_menu_edit);
            finish();
        });
    }

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
