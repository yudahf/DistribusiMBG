package com.example.projectpab_distribusimbg_teori;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class kelola_akun extends AppCompatActivity implements AccountAdapter.OnItemClickListener {

    private static final String TAG = "KelolaAkunDebug";

    private Button btnTambahAkun;
    private RecyclerView recyclerViewAkun;
    private AccountAdapter accountAdapter;
    private List<Account> accountList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kelola_akun);

        dbHelper = new DatabaseHelper(this);
        Log.d(TAG, "DatabaseHelper initialized.");

        TextView toolbarTitle = findViewById(R.id.JudulNavbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Kelola Akun");
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

        btnTambahAkun = findViewById(R.id.btnTambahAkun);

        if (btnTambahAkun != null) {
            btnTambahAkun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(kelola_akun.this, TambahAkun.class);
                    startActivity(intent);
                }
            });
        }

        recyclerViewAkun = findViewById(R.id.recyclerViewAkun);
        recyclerViewAkun.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "RecyclerView and LayoutManager initialized.");

        accountList = new ArrayList<>();
        accountAdapter = new AccountAdapter(accountList, this);
        recyclerViewAkun.setAdapter(accountAdapter);
        Log.d(TAG, "AccountAdapter set. Adapter item count: " + accountAdapter.getItemCount());
        loadAccountsFromDatabase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Reloading accounts from database.");
        loadAccountsFromDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: Data updated. Reloading accounts.");
            loadAccountsFromDatabase();
            accountAdapter.notifyDataSetChanged();
        }
    }

    private void loadAccountsFromDatabase() {
        accountList.clear();
        Log.d(TAG, "Account list cleared. Current size: " + accountList.size());

        Cursor cursor = null;
        try {
            cursor = dbHelper.getAllAccounts();
            Log.d(TAG, "Cursor obtained from dbHelper.getAllAccounts().");

            if (cursor != null) {
                Log.d(TAG, "Cursor count: " + cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                        String nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA));
                        String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
                        String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                        String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                        String jabatan = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_JABATAN));

                        Account account = new Account(id, nama, username, email, password, jabatan);
                        accountList.add(account);
                        Log.d(TAG, "Added account to list: ID=" + id + ", Nama=" + nama + ", Jabatan=" + jabatan);
                    } while (cursor.moveToNext());
                    Log.d(TAG, "Finished adding accounts from cursor. Total accounts in list: " + accountList.size());
                } else {
                    Log.d(TAG, "Cursor is empty (no data found).");
                }
            } else {
                Log.e(TAG, "Cursor is NULL from dbHelper.getAllAccounts()!");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading accounts from database: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
                Log.d(TAG, "Cursor closed.");
            }
        }
        accountAdapter.notifyDataSetChanged();
        Log.d(TAG, "Adapter notified. Adapter item count: " + accountAdapter.getItemCount());
    }

    @Override
    public void onEditClick(Account account) {
        Log.d(TAG, "Edit clicked for account: " + account.getNama());
        Intent intent = new Intent(kelola_akun.this, EditAkun.class);
        intent.putExtra("account_id", account.getId());
        intent.putExtra("account_nama", account.getNama());
        intent.putExtra("account_username", account.getUsername());
        intent.putExtra("account_email", account.getEmail());
        intent.putExtra("account_password", account.getPassword());
        intent.putExtra("account_jabatan", account.getJabatan());
        startActivityForResult(intent, 1); // Menggunakan startActivityForResult
    }

    @Override
    public void onDeleteClick(Account account) {
        Log.d(TAG, "Delete clicked for account: " + account.getNama());
        new AlertDialog.Builder(this)
                .setTitle("Hapus Akun")
                .setMessage("Apakah Anda yakin ingin menghapus akun " + account.getNama() + "?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    Integer deletedRows = dbHelper.deleteAccount(String.valueOf(account.getId()));
                    if (deletedRows > 0) {
                        Toast.makeText(kelola_akun.this, "Akun berhasil dihapus!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Account " + account.getNama() + " deleted. Reloading list.");
                        loadAccountsFromDatabase();
                    } else {
                        Toast.makeText(kelola_akun.this, "Gagal menghapus akun.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to delete account: " + account.getNama());
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}
