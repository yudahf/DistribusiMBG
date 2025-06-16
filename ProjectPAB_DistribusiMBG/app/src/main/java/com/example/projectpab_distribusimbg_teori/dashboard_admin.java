package com.example.projectpab_distribusimbg_teori;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class dashboard_admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_dashboard_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Dashboard Admin");
        }


        View customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setVisibility(View.GONE);
                backButton.setOnClickListener(null);
            }
        }


        LinearLayout btnDataSekolah = findViewById(R.id.btn_data_sekolah);
        if (btnDataSekolah != null) {
            btnDataSekolah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("dashboard_admin", "Navigating to DataSekolah");
                    Intent intent = new Intent(dashboard_admin.this, DataSekolah.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.e("dashboard_admin", "btn_data_sekolah not found in layout");
        }


        LinearLayout btnStatistik = findViewById(R.id.btn_statistik);
        if (btnStatistik != null) {
            btnStatistik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(dashboard_admin.this, Statistik.class);
                    startActivity(intent);
                }
            });
        }

        LinearLayout btnKelolaAkun = findViewById(R.id.btn_kelola_akun);
        if (btnKelolaAkun != null) {
            btnKelolaAkun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(dashboard_admin.this, kelola_akun.class);
                    startActivity(intent);
                }
            });
        }


        Button btnBatal = findViewById(R.id.btnBatal);
        if (btnBatal != null) {
            btnBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLogoutConfirmationDialog();
                }
            });
        }
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin akan keluar akun?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(dashboard_admin.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showLogoutConfirmationDialog();
    }
}
