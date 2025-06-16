package com.example.projectpab_distribusimbg_teori;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JadwalPengirimanModel {

    private int id;
    private String namaSekolah;
    private String tanggalPengiriman;
    private String jamPengiriman;
    private int jumlahPengiriman;
    private String status;
    private String namaBarang;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    // Constructor
    public JadwalPengirimanModel(int id, String namaSekolah, String tanggalPengiriman, String jamPengiriman, int jumlahPengiriman, String status, String namaBarang) {
        this.id = id;
        this.namaSekolah = namaSekolah;
        this.tanggalPengiriman = tanggalPengiriman;
        this.jamPengiriman = jamPengiriman;
        this.jumlahPengiriman = jumlahPengiriman;
        this.status = status;
        this.namaBarang = namaBarang;
    }

    public int getId() {
        return id;
    }

    public String getNamaSekolah() {
        return namaSekolah;
    }

    public String getTanggalPengiriman() {
        return tanggalPengiriman;
    }

    public String getJamPengiriman() {
        return jamPengiriman;
    }

    public int getJumlahPengiriman() {
        return jumlahPengiriman;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public String getStatus() {
        return status;
    }

    public Date getParsedDate() {
        if (tanggalPengiriman == null || tanggalPengiriman.isEmpty()) {
            return null; // Return null if the date string is empty or null
        }

        // Define multiple date formats to handle different input formats
        String[] dateFormats = {"dd-MM-yyyy", "dd/MM/yyyy"};

        for (String format : dateFormats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                return sdf.parse(tanggalPengiriman);
            } catch (ParseException ignored) {
                // Try the next format
            }
        }

        Log.e("JadwalPengirimanModel", "Error parsing date: " + tanggalPengiriman);
        return null; // Return null if all parsing attempts fail
    }
}

