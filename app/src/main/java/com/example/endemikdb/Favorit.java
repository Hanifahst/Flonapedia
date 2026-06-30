package com.example.endemikdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorit")
public class Favorit {

    @PrimaryKey
    @ColumnInfo(name = "id_endemik")
    private int idEndemik;

    @ColumnInfo(name = "nama")
    private String nama;

    @ColumnInfo(name = "gambar")
    private String gambar;

    @ColumnInfo(name = "kategori")
    private String kategori;

    public Favorit(int idEndemik, String nama, String gambar, String kategori) {
        this.idEndemik = idEndemik;
        this.nama = nama;
        this.gambar = gambar;
        this.kategori = kategori;
    }

    public int getIdEndemik() {
        return idEndemik;
    }
    public void setIdEndemik(int id) {
        this.idEndemik = id;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }
    public void setGambar(String g) {
        this.gambar = g;
    }

    public String getKategori() {
        return kategori;
    }
    public void setKategori(String k) {
        this.kategori = k;
    }
}