package com.example.projectpab_distribusimbg_teori;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DashboardKepalaGudang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_kepala_gudang);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Dashboard Kepala Gudang");
        }


        View customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            ImageView backButton = customToolbar.findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setVisibility(View.GONE);
                backButton.setOnClickListener(null);
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout btnVerifikasi = findViewById(R.id.btn_verifikasi_permintaan);
        if (btnVerifikasi != null) {
            btnVerifikasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("DashboardKepalaGudang", "Navigating to DataSekolah");
                    Intent intent = new Intent(DashboardKepalaGudang.this, VerifikasiPengiriman.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.e("DashboardKepalaGudang", "btn_verifikasi_pengiriman not found in layout");
        }


        LinearLayout btnKelolaStok = findViewById(R.id.btn_kelola_stok);
        if (btnKelolaStok != null) {
            btnKelolaStok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardKepalaGudang.this, KelolaStok.class);
                    intent.putExtra("id", 123); // Replace 123 with the actual id value
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
                        Intent intent = new Intent(DashboardKepalaGudang.this, Login.class);
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

