package com.example.projectpab_distribusimbg_teori;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button startButton;
    private DatabaseHelper dbHelper;

    public static final String EXTRA_FRAGMENT_TO_LOAD = "fragment_to_load";
    public static final String FRAGMENT_JADWAL_AKTIF = "jadwal_aktif_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    showCustomToast("Username dan password harus diisi!", R.drawable.ic_launcher_foreground);
                } else {
                    Account loggedInAccount = dbHelper.checkAccountCredentials(username, password);

                    if (loggedInAccount != null) {
                        showCustomToast("Login Berhasil sebagai " + loggedInAccount.getJabatan() + "!", R.drawable.ic_launcher_foreground);

                        Intent intent;
                        String jabatan = loggedInAccount.getJabatan();

                        if ("Admin".equals(jabatan)) {
                            intent = new Intent(Login.this, dashboard_admin.class);
                        } else if ("Kepala Distribusi".equals(jabatan)) {
                            intent = new Intent(Login.this, KepalaDistribusiActivity.class);
                            intent.putExtra(EXTRA_FRAGMENT_TO_LOAD, FRAGMENT_JADWAL_AKTIF);
                        } else if ("Kepala Gudang".equals(jabatan)) {
                            intent = new Intent(Login.this, DashboardKepalaGudang.class);
                        } else {
                            intent = new Intent(Login.this, MainActivity.class);
                        }

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        showCustomToast("Username atau password salah!", R.drawable.ic_logo);
                    }
                }
            }
        });
    }

    private void showCustomToast(String message, int iconResId) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);

        ImageView toastIcon = layout.findViewById(R.id.toastIcon);
        TextView toastText = layout.findViewById(R.id.toastText);

        toastIcon.setImageResource(R.drawable.ic_logo);
        toastText.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
