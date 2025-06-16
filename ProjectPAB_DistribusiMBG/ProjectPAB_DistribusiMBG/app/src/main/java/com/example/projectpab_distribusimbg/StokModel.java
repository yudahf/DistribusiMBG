package com.example.projectpab_distribusimbg_teori;

public class StokModel {
    private String namaBarang;
    private int jumlahStok;
    private String tanggalInput;
    private int id;

    public StokModel(int id, String namaBarang, int jumlahStok, String tanggalInput) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.jumlahStok = jumlahStok;
        this.tanggalInput = tanggalInput;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public int getJumlahStok() {
        return jumlahStok;
    }

    public String getTanggalInput() {
        return tanggalInput;
    }

    public int getId() {
        return id;
    }
}
