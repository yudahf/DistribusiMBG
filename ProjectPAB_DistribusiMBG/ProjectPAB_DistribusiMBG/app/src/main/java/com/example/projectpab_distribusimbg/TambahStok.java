package com.example.projectpab_distribusimbg_teori;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class TambahStok extends AppCompatActivity {
    private EditText etTanggalInput,etNamaBarang,etJumlahStok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_stok);

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
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
        etTanggalInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    com.example.projectpab_distribusimbg_teori.TambahStok.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        etTanggalInput.setText(date);
                    }, year, month, day);

            datePickerDialog.show();
        });
    }
}