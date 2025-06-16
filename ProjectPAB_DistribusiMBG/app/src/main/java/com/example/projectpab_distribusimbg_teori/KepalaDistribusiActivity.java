package com.example.projectpab_distribusimbg_teori;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class KepalaDistribusiActivity extends AppCompatActivity {

    Fragment jadwalFragment = new JadwalAktifFragment();
    Fragment prosesFragment = new DalamProsesFragment();
    Fragment riwayatFragment = new RiwayatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kepala_distribusi);

        Log.d("KepalaDistribusiActivity", "onCreate called. Initializing fragments.");
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, jadwalFragment, "JadwalAktifFragment")
                .add(R.id.fragment_container, prosesFragment, "DalamProsesFragment")
                .add(R.id.fragment_container, riwayatFragment, "RiwayatFragment")
                .hide(prosesFragment)
                .hide(riwayatFragment)
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            int itemId = item.getItemId();
            if (itemId == R.id.nav_jadwal) {
                Log.d("KepalaDistribusiActivity", "Switching to JadwalAktifFragment.");
                transaction.show(jadwalFragment).hide(prosesFragment).hide(riwayatFragment);
            } else if (itemId == R.id.nav_proses) {
                Log.d("KepalaDistribusiActivity", "Switching to DalamProsesFragment.");
                transaction.show(prosesFragment).hide(jadwalFragment).hide(riwayatFragment);
            } else if (itemId == R.id.nav_riwayat) {
                Log.d("KepalaDistribusiActivity", "Switching to RiwayatFragment.");
                transaction.show(riwayatFragment).hide(jadwalFragment).hide(prosesFragment);
            }

            transaction.commit();
            return true;
        });
    }
}
