package com.example.projectpab_distribusimbg_teori;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class TambahStok extends AppCompatActivity {
    private EditText etTanggalInput, etNamaBarang, etJumlahStok;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_stok);

        dbHelper = new DatabaseHelper(this);

        etTanggalInput = findViewById(R.id.etTanggalInput);
        etNamaBarang = findViewById(R.id.etNamaBarang);
        etJumlahStok = findViewById(R.id.etJumlahStok);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Tambah Stok Barang");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setOnClickListener(v -> onBackPressed());
            }
        }

        etTanggalInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TambahStok.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        etTanggalInput.setText(date);
                    }, year, month, day);

            datePickerDialog.show();
        });

        Button btnSimpan = findViewById(R.id.btnSimpan);
        Button btnBatal = findViewById(R.id.btnBatal);

        btnSimpan.setOnClickListener(v -> {
            String tanggalInput = etTanggalInput.getText().toString();
            String namaBarang = "makanan bergizi"; // bisa disesuaikan kalau nanti dinamis
            String jumlahStok = etJumlahStok.getText().toString();

            if (!tanggalInput.isEmpty() && !jumlahStok.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_NAMA_BARANG, namaBarang);
                values.put(DatabaseHelper.COLUMN_JUMLAH_STOK, Integer.parseInt(jumlahStok));
                values.put(DatabaseHelper.COLUMN_TANGGAL_INPUT, tanggalInput);

                long result = db.insert(DatabaseHelper.TABLE_KEPALA_GUDANG, null, values);
                db.close();

                if (result != -1) {
                    showCustomToast("Data berhasil disimpan!", android.R.drawable.checkbox_on_background);

                    // Kirim data ke KelolaStok
                    Intent intent = new Intent();
                    intent.putExtra("id", (int) result); // ID dari insert
                    intent.putExtra("namaBarang", namaBarang);
                    intent.putExtra("jumlahStok", Integer.parseInt(jumlahStok));
                    intent.putExtra("tanggalInput", tanggalInput);

                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showCustomToast("Gagal Menambahkan Stok!", android.R.drawable.ic_delete);
                }
            } else {
                showCustomToast("Gagal Menambahkan Stok!", android.R.drawable.ic_dialog_alert);
            }
        });

        btnBatal.setOnClickListener(v -> finish());
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
