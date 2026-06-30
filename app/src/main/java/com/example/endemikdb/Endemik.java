package com.example.endemikdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "endemik")
public class Endemik {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "nama")
    private String nama;

    @ColumnInfo(name = "gambar")
    private String gambar;

    @ColumnInfo(name = "deskripsi")
    private String deskripsi;

    @ColumnInfo(name = "kategori")
    private String kategori;

    @ColumnInfo(name = "wilayah")
    private String wilayah;

    @ColumnInfo(name = "nama_latin")
    private String namaLatin;

    @ColumnInfo(name = "famili")
    private String famili;

    @ColumnInfo(name = "genus")
    private String genus;

    @ColumnInfo(name = "asal")
    private String asal;

    @ColumnInfo(name = "sebaran")
    private String sebaran;

    @ColumnInfo(name = "status")
    private String status;

    public Endemik(int id, String nama, String gambar, String deskripsi,
                   String kategori, String wilayah,
                   String namaLatin, String famili, String genus,
                   String asal, String sebaran, String status) {
        this.id = id;
        this.nama = nama;
        this.gambar = gambar;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.wilayah = wilayah;
        this.namaLatin = namaLatin;
        this.famili = famili;
        this.genus = genus;
        this.asal = asal;
        this.sebaran = sebaran;
        this.status = status;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String v) {
        nama = v;
    }

    public String getGambar() {
        return gambar;
    }
    public void setGambar(String v) {
        gambar = v;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
    public void setDeskripsi(String v) {
        deskripsi = v;
    }

    public String getKategori() {
        return kategori;
    }
    public void setKategori(String v) {
        kategori = v;
    }

    public String getWilayah() {
        return wilayah;
    }
    public void setWilayah(String v) {
        wilayah = v;
    }

    public String getNamaLatin() {
        return namaLatin;
    }
    public void setNamaLatin(String v) {
        namaLatin = v;
    }

    public String getFamili() {
        return famili;
    }
    public void setFamili(String v) {
        famili = v;
    }

    public String getGenus() {
        return genus;
    }
    public void setGenus(String v) {
        genus = v;
    }

    public String getAsal() {
        return asal;
    }
    public void setAsal(String v) {
        asal = v;
    }

    public String getSebaran() {
        return sebaran;
    }
    public void setSebaran(String v) {
        sebaran = v;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String v) {
        status = v;
    }
}