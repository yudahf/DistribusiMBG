package com.example.projectpab_distribusimbg_teori;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "project_pab_db";
    private static final int DATABASE_VERSION = 14; // Incremented database version

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
    public static final String COLUMN_STATUS = "status";

    public static final String TABLE_KEPALA_GUDANG = "kepala_gudang";
    public static final String COLUMN_NAMA_BARANG = "nama_barang";
    public static final String COLUMN_JUMLAH_STOK = "jumlah_stok";
    public static final String COLUMN_TANGGAL_INPUT = "tanggal_input";

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
            + COLUMN_NO_TELEPON + " TEXT, "
            + "status TEXT DEFAULT 'aktif'"
            + ")";

    private static final String CREATE_TABLE_JADWAL_PENGIRIMAN = "CREATE TABLE IF NOT EXISTS " + TABLE_JADWAL_PENGIRIMAN + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TANGGAL_PENGIRIMAN + " TEXT NOT NULL, "
            + COLUMN_JADWAL_PENGIRIMAN + " TEXT, "
            + COLUMN_JUMLAH_PENGIRIMAN + " INTEGER NOT NULL, "
            + COLUMN_NAMA_SEKOLAH + " TEXT NOT NULL, "
            + COLUMN_STATUS + " TEXT DEFAULT 'Menunggu Verifikasi', "
            + COLUMN_NAMA_BARANG + " TEXT DEFAULT 'Makanan Bergizi', "
            + "FOREIGN KEY (" + COLUMN_NAMA_SEKOLAH + ") REFERENCES " + TABLE_SEKOLAH + "(" + COLUMN_NAMA_SEKOLAH + ")"
            + ")";

    private static final String CREATE_TABLE_KEPALA_GUDANG = "CREATE TABLE " + TABLE_KEPALA_GUDANG + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAMA_BARANG + " TEXT NOT NULL DEFAULT 'Makanan Bergizi', "
            + COLUMN_JUMLAH_STOK + " INTEGER NOT NULL, "
            + COLUMN_TANGGAL_INPUT + " TEXT NOT NULL"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNTS);
        db.execSQL(CREATE_TABLE_SEKOLAH);
        db.execSQL(CREATE_TABLE_JADWAL_PENGIRIMAN);
        db.execSQL(CREATE_TABLE_KEPALA_GUDANG);

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

        ContentValues kepalaGudang = new ContentValues();
        kepalaGudang.put(COLUMN_NAMA, "Ujang");
        kepalaGudang.put(COLUMN_USERNAME, "kepalagudang");
        kepalaGudang.put(COLUMN_EMAIL, "kepalagudang@example.com");
        kepalaGudang.put(COLUMN_PASSWORD, "gudang123");
        kepalaGudang.put(COLUMN_JABATAN, "Kepala Gudang");
        db.insert(TABLE_ACCOUNTS, null, kepalaGudang);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 9) {
            db.execSQL("ALTER TABLE " + TABLE_JADWAL_PENGIRIMAN + " ADD COLUMN " + COLUMN_NAMA_BARANG + " TEXT DEFAULT 'Makanan Bergizi'");
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEKOLAH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JADWAL_PENGIRIMAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEPALA_GUDANG);

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
        values.put("status", "aktif");

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

    public boolean updateStatusSekolah(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        int rows = db.update(TABLE_SEKOLAH, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }


    public int getJumlahSekolahAktif() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sekolah WHERE status = 'aktif'", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }


    // ========== Method Jadwal Pengiriman ==========

    public boolean tambahJadwalPengiriman(String tanggal, String jadwal, int jumlah, String namaSekolah) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TANGGAL_PENGIRIMAN, tanggal);
        values.put(COLUMN_JADWAL_PENGIRIMAN, jadwal);
        values.put(COLUMN_JUMLAH_PENGIRIMAN, jumlah);
        values.put(COLUMN_NAMA_SEKOLAH, namaSekolah);
        values.put(COLUMN_STATUS, "Menunggu Verifikasi"); // Kolom baru

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
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_JUMLAH_PENGIRIMAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_BARANG))
                );
                jadwalList.add(jadwal);
            }
        }
        return jadwalList;
    }

    public List<JadwalPengirimanModel> getJadwalByStatus(List<String> status) {
        List<JadwalPengirimanModel> jadwalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Bangun query dengan jumlah ? sesuai jumlah status
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + TABLE_JADWAL_PENGIRIMAN + " WHERE " + COLUMN_STATUS + " IN (");
        for (int i = 0; i < status.size(); i++) {
            queryBuilder.append("?");
            if (i < status.size() - 1) {
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");

        Cursor cursor = db.rawQuery(queryBuilder.toString(), status.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                JadwalPengirimanModel jadwal = new JadwalPengirimanModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_SEKOLAH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL_PENGIRIMAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JADWAL_PENGIRIMAN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_JUMLAH_PENGIRIMAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_BARANG))
                );
                jadwalList.add(jadwal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return jadwalList;
    }


    public JadwalPengirimanModel getJadwalById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JADWAL_PENGIRIMAN,
                new String[]{COLUMN_ID, COLUMN_NAMA_SEKOLAH, COLUMN_TANGGAL_PENGIRIMAN, COLUMN_JADWAL_PENGIRIMAN, COLUMN_JUMLAH_PENGIRIMAN, COLUMN_STATUS, COLUMN_NAMA_BARANG},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            JadwalPengirimanModel jadwal = new JadwalPengirimanModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_SEKOLAH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL_PENGIRIMAN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JADWAL_PENGIRIMAN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_JUMLAH_PENGIRIMAN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_BARANG))
            );
            cursor.close();
            return jadwal;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public Integer deleteJadwalPengiriman(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_JADWAL_PENGIRIMAN, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return result;
    }

    public boolean updateJadwalPengiriman(int id, String namaSekolah, String tanggal, int jumlah, String jam, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_SEKOLAH, namaSekolah);
        values.put(COLUMN_TANGGAL_PENGIRIMAN, tanggal);
        values.put(COLUMN_JUMLAH_PENGIRIMAN, jumlah);
        values.put(COLUMN_JADWAL_PENGIRIMAN, jam);
        values.put(COLUMN_STATUS, status);

        int result = db.update(TABLE_JADWAL_PENGIRIMAN, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public void updateStatusPengiriman(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        db.update(TABLE_JADWAL_PENGIRIMAN, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ========== Method Riwayat ==========

    public List<JadwalItem> getRiwayat() {
        List<JadwalItem> riwayatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_JADWAL_PENGIRIMAN +
                " WHERE " + COLUMN_STATUS + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"Selesai"});

        if (cursor.moveToFirst()) {
            do {
                JadwalItem item = new JadwalItem(
                        String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_SEKOLAH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL_PENGIRIMAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JADWAL_PENGIRIMAN)) + " WIB",
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_JUMLAH_PENGIRIMAN)) + " Paket"
                );
                riwayatList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return riwayatList;
    }


    // ========== Method Stok ==========

    public List<StokModel> getAllStok() {
        List<StokModel> stokList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_KEPALA_GUDANG, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String namaBarang = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_BARANG));
                int jumlahStok = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_JUMLAH_STOK));
                String tanggalInput = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL_INPUT));

                StokModel stok = new StokModel(id, namaBarang, jumlahStok, tanggalInput);
                stokList.add(stok);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return stokList;
    }

    // Corrected getStok method to use the correct table and column names
    public int getStok(String namaBarang) {
        String normalizedNamaBarang = normalizeNamaBarang(namaBarang);
        SQLiteDatabase db = this.getReadableDatabase();
        int jumlahStok = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_JUMLAH_STOK + ") FROM " + TABLE_KEPALA_GUDANG + " WHERE LOWER(" + COLUMN_NAMA_BARANG + ") = ?", new String[]{namaBarang.toLowerCase()});
        if (cursor != null && cursor.moveToFirst()) {
            jumlahStok = cursor.getInt(0); // ambil dari kolom pertama (SUM)
            cursor.close();
        }
        db.close();
        return jumlahStok;
    }

    // Corrected updateStok method to use the correct table and column names
    public void updateStok(String namaBarang, int updatedStock) {
        String normalizedNamaBarang = normalizeNamaBarang(namaBarang);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUMLAH_STOK, updatedStock);

        db.update(TABLE_KEPALA_GUDANG, values, COLUMN_NAMA_BARANG + " = ?", new String[]{namaBarang});
        db.close();
    }
    public void kurangiStokSecaraBertahap(String namaBarang, int jumlahYangDikurangi) {
        String normalizedNamaBarang = normalizeNamaBarang(namaBarang);
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, " + COLUMN_JUMLAH_STOK + " FROM " + TABLE_KEPALA_GUDANG +
                        " WHERE LOWER(" + COLUMN_NAMA_BARANG + ") = ? AND " + COLUMN_JUMLAH_STOK + " > 0 ORDER BY jumlah_stok ASC",
                new String[]{namaBarang.toLowerCase()}
        );

        db.beginTransaction();
        try {
            while (cursor.moveToNext() && jumlahYangDikurangi > 0) {
                int id = cursor.getInt(0);
                int stok = cursor.getInt(1);

                int dikurangi = Math.min(jumlahYangDikurangi, stok);
                int sisa = stok - dikurangi;

                db.execSQL("UPDATE " + TABLE_KEPALA_GUDANG + " SET " + COLUMN_JUMLAH_STOK + " = ? WHERE id = ?",
                        new Object[]{sisa, id});

                jumlahYangDikurangi -= dikurangi;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            cursor.close();
        }
        db.close();
    }

    public String normalizeNamaBarang(String namaBarang) {
        return namaBarang.toLowerCase();
    }

}

