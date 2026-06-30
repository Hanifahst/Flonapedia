package com.example.endemikdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritDAO {

    @Query("SELECT * FROM favorit")
    List<Favorit> getAll();

    @Query("SELECT COUNT(*) FROM favorit WHERE id_endemik = :idEndemik")
    int isFavorit(int idEndemik);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favorit favorit);

    @Query("DELETE FROM favorit WHERE id_endemik = :idEndemik")
    void delete(int idEndemik);
}