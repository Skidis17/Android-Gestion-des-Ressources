package ma.ensate.myapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensate.myapplication.db.entity.DepenseEntity;

@Dao
public interface DepenseDao {
    @Insert
    long insert(DepenseEntity d);

    @Update
    void update(DepenseEntity d);

    @Delete
    void delete(DepenseEntity d);

    @Query("SELECT * FROM depenses ORDER BY localId DESC")
    List<DepenseEntity> getAll();

    @Query("SELECT * FROM depenses WHERE syncStatus != 0")
    List<DepenseEntity> getPending();
}
