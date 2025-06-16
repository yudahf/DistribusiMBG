package com.example.projectpab_distribusimbg_teori;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JadwalPengirimanModel {

    private int id;
    private String namaSekolah;
    private String tanggalPengiriman;
    private String jamPengiriman;
    private int jumlahPengiriman;

    // Constructor
    public JadwalPengirimanModel(int id, String namaSekolah, String tanggalPengiriman, String jamPengiriman, int jumlahPengiriman) {
        this.id = id;
        this.namaSekolah = namaSekolah;
        this.tanggalPengiriman = tanggalPengiriman;
        this.jamPengiriman = jamPengiriman;
        this.jumlahPengiriman = jumlahPengiriman;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaSekolah() {
        return namaSekolah;
    }

    public void setNamaSekolah(String namaSekolah) {
        this.namaSekolah = namaSekolah;
    }

    public String getTanggalPengiriman() {
        return tanggalPengiriman;
    }

    public void setTanggalPengiriman(String tanggalPengiriman) {
        this.tanggalPengiriman = tanggalPengiriman;
    }

    public String getJamPengiriman() {
        return jamPengiriman;
    }

    public void setJamPengiriman(String jamPengiriman) {
        this.jamPengiriman = jamPengiriman;
    }

    public int getJumlahPengiriman() {
        return jumlahPengiriman;
    }

    public void setJumlahPengiriman(int jumlahPengiriman) {
        this.jumlahPengiriman = jumlahPengiriman;
    }

    public Date getParsedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(tanggalPengiriman);
}
