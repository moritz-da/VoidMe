package de.hdmstuttgart.voidme.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM Location")
    List<Location> getAll();

    @Query("SELECT uid FROM Location WHERE title LIKE :title;")
    List<Integer> getLocationId(String title);

    @Query("SELECT * FROM Location WHERE title LIKE :title;")
    List<Location> searchLocation(String title);

    @Insert
    void insert(Location location);

    @Delete
    void delete(Location location);
}