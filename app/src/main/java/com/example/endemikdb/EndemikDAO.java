package com.example.endemikdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EndemikDAO {

    @Query("SELECT * FROM endemik")
    List<Endemik> getAll();

    @Query("SELECT * FROM endemik WHERE kategori = :kategori")
    List<Endemik> getByKategori(String kategori);

    @Query("SELECT * FROM endemik WHERE nama LIKE :keyword OR deskripsi LIKE :keyword")
    List<Endemik> search(String keyword);

    @Query("SELECT * FROM endemik WHERE id = :id")
    Endemik getById(int id);

    @Query("SELECT COUNT(*) FROM endemik")
    int getCount();

    @Query("SELECT COUNT(*) FROM endemik WHERE kategori = 'Hewan'")
    int getCountHewan();

    @Query("SELECT COUNT(*) FROM endemik WHERE kategori = 'Tumbuhan'")
    int getCountTumbuhan();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Endemik> list);
}