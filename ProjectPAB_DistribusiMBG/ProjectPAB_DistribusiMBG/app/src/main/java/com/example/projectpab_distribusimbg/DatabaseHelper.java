package com.example.projectpab_distribusimbg_teori;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "project_pab_db";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_ACCOUNTS = "accounts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_JABATAN = "jabatan";

    private static final String TABLE_SEKOLAH = "sekolah";
    public static final String COLUMN_NAMA_SEKOLAH = "nama_sekolah";
    public static final String COLUMN_ALAMAT_SEKOLAH = "alamat_sekolah";
    public static final String COLUMN_NAMA_OPERATOR = "nama_operator";
    public static final String COLUMN_NO_TELEPON = "no_telepon";

    private static final String TABLE_JADWAL_PENGIRIMAN = "jadwal_pengiriman";
    public static final String COLUMN_TANGGAL_PENGIRIMAN = "tanggal_pengiriman";
    public static final String COLUMN_JADWAL_PENGIRIMAN = "jadwal_pengiriman";
    public static final String COLUMN_JUMLAH_PENGIRIMAN = "jumlah_pengiriman";

    private static final String TABLE_RIWAYAT = "riwayat";

    // CREATE TABLE statements
    private static final String CREATE_TABLE_ACCOUNTS = "CREATE TABLE " + TABLE_ACCOUNTS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAMA + " TEXT, "
            + COLUMN_USERNAME + " TEXT UNIQUE, "
            + COLUMN_EMAIL + " TEXT UNIQUE, "
            + COLUMN_PASSWORD + " TEXT, "
            + COLUMN_JABATAN + " TEXT"
            + ")";

    private static final String CREATE_TABLE_SEKOLAH = "CREATE TABLE " + TABLE_SEKOLAH + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAMA_SEKOLAH + " TEXT UNIQUE, "
            + COLUMN_ALAMAT_SEKOLAH + " TEXT, "
            + COLUMN_NAMA_OPERATOR + " TEXT, "
            + COLUMN_NO_TELEPON + " TEXT"
            + ")";

    private static final String CREATE_TABLE_JADWAL_PENGIRIMAN = "CREATE TABLE IF NOT EXISTS " + TABLE_JADWAL_PENGIRIMAN + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TANGGAL_PENGIRIMAN + " TEXT NOT NULL, "
            + COLUMN_JADWAL_PENGIRIMAN + " TEXT, "
            + COLUMN_JUMLAH_PENGIRIMAN + " INTEGER NOT NULL, "
            + COLUMN_NAMA_SEKOLAH + " TEXT NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_NAMA_SEKOLAH + ") REFERENCES " + TABLE_SEKOLAH + "(" + COLUMN_NAMA_SEKOLAH + ")"
            + ")";

    private static final String CREATE_TABLE_RIWAYAT = "CREATE TABLE IF NOT EXISTS " + TABLE_RIWAYAT + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAMA_SEKOLAH + " TEXT NOT NULL, "
            + COLUMN_TANGGAL_PENGIRIMAN + " TEXT NOT NULL, "
            + COLUMN_JADWAL_PENGIRIMAN + " TEXT, "
            + COLUMN_JUMLAH_PENGIRIMAN + " INTEGER NOT NULL"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNTS);
        db.execSQL(CREATE_TABLE_SEKOLAH);
        db.execSQL(CREATE_TABLE_JADWAL_PENGIRIMAN);
        db.execSQL(CREATE_TABLE_RIWAYAT); // Create the riwayat table

        ContentValues admin = new ContentValues();
        admin.put(COLUMN_NAMA, "Yuda");
        admin.put(COLUMN_USERNAME, "admin");
        admin.put(COLUMN_EMAIL, "admin@example.com");
        admin.put(COLUMN_PASSWORD, "admin123");
        admin.put(COLUMN_JABATAN, "Admin");
        db.insert(TABLE_ACCOUNTS, null, admin);

        ContentValues kepalaDistribusi = new ContentValues();
        kepalaDistribusi.put(COLUMN_NAMA, "Asep");
        kepalaDistribusi.put(COLUMN_USERNAME, "kepaladistribusi");
        kepalaDistribusi.put(COLUMN_EMAIL, "distribusi@example.com");
        kepalaDistribusi.put(COLUMN_PASSWORD, "distribusi123");
        kepalaDistribusi.put(COLUMN_JABATAN, "Kepala Distribusi");
        db.insert(TABLE_ACCOUNTS, null, kepalaDistribusi);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEKOLAH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JADWAL_PENGIRIMAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIWAYAT); // Drop the riwayat table if it exists

        onCreate(db); // Recreate all tables
    }

    // ========== Method Akun ==========

    public boolean addAccount(String nama, String username, String email, String password, String jabatan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_JABATAN, jabatan);

        long result = db.insert(TABLE_ACCOUNTS, null, values);
        db.close();
        return result != -1;
    }

    public Account checkAccountCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Account account = null;
        Cursor cursor = db.query(TABLE_ACCOUNTS,
                null,
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            account = new Account(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JABATAN))
            );
            cursor.close();
        }
        db.close();
        return account;
    }

    public Cursor getAllAccounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACCOUNTS, null);
    }

    public Cursor getAccountByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ACCOUNTS, null,
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
    }

    public Cursor getAccountByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ACCOUNTS, null,
                COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
    }

    public Integer deleteAccount(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ACCOUNTS, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return result;
    }

    public boolean updateAccount(int id, String nama, String username, String email, String password, String jabatan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_JABATAN, jabatan);

        int rowsAffected = db.update(TABLE_ACCOUNTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    // ========== Method Sekolah ==========

    public boolean addSekolah(String namaSekolah, String alamatSekolah, String namaOperator, String noTelepon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_SEKOLAH, namaSekolah);
        values.put(COLUMN_ALAMAT_SEKOLAH, alamatSekolah);
        values.put(COLUMN_NAMA_OPERATOR, namaOperator);
        values.put(COLUMN_NO_TELEPON, noTelepon);

        long result = db.insert(TABLE_SEKOLAH, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllSekolah() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SEKOLAH, null);
    }

    public ArrayList<String> getSemuaNamaSekolah() {
        ArrayList<String> listSekolah = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAMA_SEKOLAH + " FROM " + TABLE_SEKOLAH, null);

        if (cursor.moveToFirst()) {
            do {
                listSekolah.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_SEKOLAH)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listSekolah;
    }

    public boolean updateSekolah(int id, String namaSekolah, String alamatSekolah, String namaOperator, String noTelepon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_SEKOLAH, namaSekolah);
        values.put(COLUMN_ALAMAT_SEKOLAH, alamatSekolah);
        values.put(COLUMN_NAMA_OPERATOR, namaOperator);
        values.put(COLUMN_NO_TELEPON, noTelepon);

        int rowsAffected = db.update(TABLE_SEKOLAH, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Integer deleteSekolah(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SEKOLAH, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return result;
    }

    // ========== Method Jadwal Pengiriman ==========

    public boolean tambahJadwalPengiriman(String tanggal, String jadwal, int jumlah, String namaSekolah) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TANGGAL_PENGIRIMAN, tanggal);
        values.put(COLUMN_JADWAL_PENGIRIMAN, jadwal);
        values.put(COLUMN_JUMLAH_PENGIRIMAN, jumlah);
        values.put(COLUMN_NAMA_SEKOLAH, namaSekolah);

        long result = db.insert(TABLE_JADWAL_PENGIRIMAN, null, values);
        db.close();
        return result != -1;
    }

    public List<JadwalPengirimanModel> getAllJadwalPengiriman() {
        List<JadwalPengirimanModel> jadwalList = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_JADWAL_PENGIRIMAN, null, null, null, null, null, null)) {

            while (cursor.moveToNext()) {
                JadwalPengirimanModel jadwal = new JadwalPengirimanModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_SEKOLAH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL_PENGIRIMAN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JADWAL_PENGIRIMAN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_JUMLAH_PENGIRIMAN))
                );
                jadwalList.add(jadwal);
            }
        }
        return jadwalList;
    }

    public Integer deleteJadwalPengiriman(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_JADWAL_PENGIRIMAN, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return result;
    }

    public boolean updateJadwalPengiriman(int id, String tanggal, String jadwal, int jumlah, String namaSekolah) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TANGGAL_PENGIRIMAN, tanggal);
        values.put(COLUMN_JADWAL_PENGIRIMAN, jadwal);
        values.put(COLUMN_JUMLAH_PENGIRIMAN, jumlah);
        values.put(COLUMN_NAMA_SEKOLAH, namaSekolah);

        Log.d("DatabaseHelper", "Updating jadwal_pengiriman with id: " + id + ", tanggal: " + tanggal + ", jadwal: " + jadwal + ", jumlah: " + jumlah + ", namaSekolah: " + namaSekolah);
        int rowsAffected = db.update(TABLE_JADWAL_PENGIRIMAN, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        Log.d("DatabaseHelper", "Rows affected: " + rowsAffected);
        db.close();
        return rowsAffected > 0;
    }

    public List<JadwalItem> getJadwalDalamProses() {
        List<JadwalItem> jadwalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_JADWAL_PENGIRIMAN;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                JadwalItem item = new JadwalItem(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)), // Include the ID field
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_SEKOLAH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL_PENGIRIMAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JADWAL_PENGIRIMAN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_JUMLAH_PENGIRIMAN)) + " Paket"
                );
                jadwalList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return jadwalList;
    }

    public boolean addRiwayat(JadwalItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_SEKOLAH, item.getTitle());
        values.put(COLUMN_TANGGAL_PENGIRIMAN, item.getDate());
        values.put(COLUMN_JADWAL_PENGIRIMAN, item.getTime());
        values.put(COLUMN_JUMLAH_PENGIRIMAN, item.getPackageCount());

        long result = db.insert("riwayat", null, values);
        db.close();
        return result != -1;
    }

    public List<JadwalItem> getRiwayat() {
        List<JadwalItem> riwayatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String query = "SELECT * FROM riwayat";
            Log.d("DatabaseHelper", "Fetching riwayat data");
            Log.d("DatabaseHelper", "Query: " + query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    JadwalItem item = new JadwalItem(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_SEKOLAH)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL_PENGIRIMAN)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JADWAL_PENGIRIMAN)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JUMLAH_PENGIRIMAN))
                    );
                    riwayatList.add(item);
                } while (cursor.moveToNext());
            } else {
                Log.d("DatabaseHelper", "No data found in riwayat table");
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching riwayat data", e);
        } finally {
            db.close();
        }

        Log.d("DatabaseHelper", "Riwayat list size: " + riwayatList.size());
        return riwayatList;
    }
}