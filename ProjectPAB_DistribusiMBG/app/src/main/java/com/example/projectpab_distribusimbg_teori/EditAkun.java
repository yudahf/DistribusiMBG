package com.example.projectpab_distribusimbg_teori;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;

public class EditAkun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_akun);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Edit Akun");
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nama = extras.getString("account_nama", "");
            String username = extras.getString("account_username", "");
            String email = extras.getString("account_email", "");
            String password = extras.getString("account_password", "");
            String jabatan = extras.getString("account_jabatan", "");

            EditText etNama = findViewById(R.id.etNama);
            EditText etEmail = findViewById(R.id.etEmail);
            EditText etPassword = findViewById(R.id.etPassword);
            AutoCompleteTextView etJabatan = findViewById(R.id.etJabatan);
            EditText etUsername = findViewById(getResources().getIdentifier("etUsername", "id", getPackageName()));

            if (etNama != null) etNama.setText(nama);
            if (etEmail != null) etEmail.setText(email);
            if (etPassword != null) etPassword.setText(password);
            if (etJabatan != null) etJabatan.setText(jabatan);
            if (etUsername != null) etUsername.setText(username);
        }

        // Setup AutoCompleteTextView Jabatan
        String[] jabatanItems = {"Admin", "Kepala Gudang", "Kepala Distribusi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, jabatanItems);
        AutoCompleteTextView etJabatan = findViewById(R.id.etJabatan);
        etJabatan.setAdapter(adapter);
        etJabatan.setOnClickListener(v -> etJabatan.showDropDown());

        // Tombol Simpan
        findViewById(R.id.btnSimpan).setOnClickListener(v -> {
            EditText etNama = findViewById(R.id.etNama);
            EditText etEmail = findViewById(R.id.etEmail);
            EditText etPassword = findViewById(R.id.etPassword);
            EditText etUsername = findViewById(getResources().getIdentifier("etUsername", "id", getPackageName()));

            String nama = etNama.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String jabatan = etJabatan.getText().toString().trim(); // Use existing etJabatan
            String username = etUsername.getText().toString().trim();

            // Update data di database
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            boolean isUpdated = dbHelper.updateAccount(
                getIntent().getIntExtra("account_id", -1), nama, username, email, password, jabatan
            );

            if (isUpdated) {
                showCustomToast("Data Akun berhasil diupdate!", android.R.drawable.checkbox_on_background);
                setResult(RESULT_OK);
            } else {
                showCustomToast("Gagal menyimpan data!", android.R.drawable.ic_delete);
            }
            finish();
        });

        // Tombol Batal
        findViewById(R.id.btnBatal).setOnClickListener(v -> {
            showCustomToast("Edit Dibatalkan", android.R.drawable.ic_dialog_alert);
            finish();
        });

        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            Toast.makeText(this, "Layout root tidak ditemukan.", Toast.LENGTH_SHORT).show();
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
