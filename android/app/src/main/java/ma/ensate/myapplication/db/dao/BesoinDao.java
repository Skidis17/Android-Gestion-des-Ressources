package ma.ensate.myapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensate.myapplication.db.entity.BesoinEntity;

@Dao
public interface BesoinDao {
    @Insert
    long insert(BesoinEntity b);

    @Update
    void update(BesoinEntity b);

    @Delete
    void delete(BesoinEntity b);

    @Query("SELECT * FROM besoins ORDER BY localId DESC")
    List<BesoinEntity> getAll();

    @Query("SELECT * FROM besoins WHERE syncStatus != 0")
    List<BesoinEntity> getPending();
}
