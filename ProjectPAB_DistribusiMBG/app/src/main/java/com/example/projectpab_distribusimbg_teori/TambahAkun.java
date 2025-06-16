package com.example.projectpab_distribusimbg_teori;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class TambahAkun extends AppCompatActivity {

    private EditText etNama, etUsername, etEmail, etPassword; // Tambahkan etUsername
    private MaterialAutoCompleteTextView etJabatan;
    private Button btnSimpan, btnBatal;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_akun);

        dbHelper = new DatabaseHelper(this);

        etNama = findViewById(R.id.etNama);
        etUsername = findViewById(R.id.etUsername); // Inisialisasi etUsername
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etJabatan = findViewById(R.id.etJabatan);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Tambah Akun");
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

        String[] jabatanItems = {"Admin", "Kepala Gudang", "Kepala Distribusi"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, jabatanItems);
        etJabatan.setAdapter(adapter);

        etJabatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etJabatan.showDropDown();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = etNama.getText().toString().trim();
                String username = etUsername.getText().toString().trim(); // Ambil input username
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String jabatan = etJabatan.getText().toString().trim();

                if (nama.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || jabatan.isEmpty()) { // Validasi username
                    Toast.makeText(TambahAkun.this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                } else {
                    addAccountToSQLite(nama, username, email, password, jabatan); // Teruskan username
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addAccountToSQLite(String nama, String username, String email, String password, String jabatan) { // Tambahkan username
        boolean isInserted = dbHelper.addAccount(nama, username, email, password, jabatan); // Teruskan username

        if (isInserted) {
            showCustomToast("Gagal menambahkan akun. Username atau Email mungkin sudah terdaftar.", android.R.drawable.checkbox_on_background);
            etNama.setText("");
            etUsername.setText(""); // Bersihkan username
            etEmail.setText("");
            etPassword.setText("");
            etJabatan.setText("");
        } else {
            showCustomToast("Gagal menambahkan akun. Username atau Email mungkin sudah terdaftar.", android.R.drawable.ic_dialog_dialer);
        }
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
