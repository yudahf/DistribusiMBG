package com.example.projectpab_distribusimbg_teori;

public class Account {
    private int id;
    private String nama;
    private String username; // KOLOM BARU
    private String email;
    private String password;
    private String jabatan;

    public Account(int id, String nama, String username, String email, String password, String jabatan) { // Perbarui constructor
        this.id = id;
        this.nama = nama;
        this.username = username; // Inisialisasi username
        this.email = email;
        this.password = password;
        this.jabatan = jabatan;
    }

    // Constructor tanpa ID, berguna jika ID otomatis dihasilkan oleh DB
    public Account(String nama, String username, String email, String password, String jabatan) { // Perbarui constructor
        this.nama = nama;
        this.username = username; // Inisialisasi username
        this.email = email;
        this.password = password;
        this.jabatan = jabatan;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() { // Getter untuk username
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getJabatan() {
        return jabatan;
    }

    // Setter methods (opsional, tergantung kebutuhan)
    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setUsername(String username) { // Setter untuk username
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }
}
