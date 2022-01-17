package de.hdmstuttgart.voidme.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM LocationEntity")
    List<LocationEntity> getAll();

    @Query("SELECT uid FROM LocationEntity WHERE title LIKE :title;")
    List<Integer> getLocationId(String title);

    @Query("SELECT * FROM LocationEntity WHERE title LIKE :title;")
    List<LocationEntity> searchLocation(String title);

    @Insert
    void insert(LocationEntity location);

    @Delete
    void delete(LocationEntity location);
}