package com.example.projectpab_distribusimbg_teori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KelolaStok extends AppCompatActivity {
    private Button btnTambahStok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kelola_stok);

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Kelola Stok");
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
        btnTambahStok = findViewById(R.id.btnTambahStok);

        if (btnTambahStok != null) {
            btnTambahStok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(KelolaStok.this, TambahStok.class);
                    startActivity(intent);
                }
            });
        }
    }
}