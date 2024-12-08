package com.example.ngikngik.berandaygy;

public class Jadwal {
    private String hari;
    private String mataPelajaran;

    public Jadwal(String hari, String mataPelajaran) {
        this.hari = hari;
        this.mataPelajaran = mataPelajaran;
    }

    public String getHari() {
        return hari;
    }

    public String getMataPelajaran() {
        return mataPelajaran;
    }
}
