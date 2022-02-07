package de.hdmstuttgart.voidme.database;

import static androidx.room.OnConflictStrategy.IGNORE;

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

    @Query("SELECT * FROM LocationEntity WHERE category LIKE :category;")
    List<LocationEntity> searchCategory(String category);

    @Query("DELETE FROM LocationEntity;")
    void deleteAll();

    @Insert(onConflict = IGNORE)
    long insert(LocationEntity location);

    /*@Insert(onConflict = IGNORE)
    int insertOrThrow(LocationEntity location);*/

    @Delete
    void delete(LocationEntity location);
}