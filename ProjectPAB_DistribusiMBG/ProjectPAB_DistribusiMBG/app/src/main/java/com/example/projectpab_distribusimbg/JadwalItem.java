package com.example.projectpab_distribusimbg_teori;

public class JadwalItem {

    private String id;
    private String title;
    private String date;
    private String time;
    private String packageCount;

    public JadwalItem(String id, String title, String date, String time, String packageCount) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.packageCount = packageCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(String packageCount) {
        this.packageCount = packageCount;
    }
}
