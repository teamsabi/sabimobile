package com.example.ngikngik.berandaygy;

public class item_Jadwal {
    private String tanggal;
    private String namaKelas;
    private String namaMapel;
    private String namaLengkap;

    public item_Jadwal(String tanggal, String namaKelas, String namaMapel, String namaLengkap) {
        this.tanggal = tanggal;
        this.namaKelas = namaKelas;
        this.namaMapel = namaMapel;
        this.namaLengkap = namaLengkap;
    }

    // Getter dan setter
    public String getTanggal() { return tanggal; }
    public String getNamaKelas() { return namaKelas; }
    public String getNamaMapel() { return namaMapel; }
    public String getNamaLengkap() { return namaLengkap; }
}
