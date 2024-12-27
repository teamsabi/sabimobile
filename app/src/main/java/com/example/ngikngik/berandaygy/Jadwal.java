package com.example.ngikngik.berandaygy;

public class Jadwal {
    private String hari;
    private String namaMapel;

    public Jadwal(String hari, String namaMapel) {
        this.hari = hari;
        this.namaMapel = namaMapel;
    }

    public String getHari() {
        return hari;
    }

    public String getNamaMapel() {
        return namaMapel;
    }
}
