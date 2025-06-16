package com.example.projectpab_distribusimbg_teori;

public class Sekolah {
    private int id;
    private String NamaSekolah;
    private String alamat;
    private String namaOperator;
    private String noTelepon;
    private String status;

    public Sekolah(int id, String NamaSekolah, String alamat, String namaOperator, String noTelepon, String status) {
        this.id = id;
        this.NamaSekolah = NamaSekolah;
        this.alamat = alamat;
        this.namaOperator = namaOperator;
        this.noTelepon = noTelepon;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getNamaSekolah() {
        return NamaSekolah;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNamaOperator() {
        return namaOperator;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
