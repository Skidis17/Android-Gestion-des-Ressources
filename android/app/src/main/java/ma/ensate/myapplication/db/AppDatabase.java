package ma.ensate.myapplication.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ma.ensate.myapplication.db.dao.BesoinDao;
import ma.ensate.myapplication.db.dao.CommandeDao;
import ma.ensate.myapplication.db.dao.DepenseDao;
import ma.ensate.myapplication.db.entity.BesoinEntity;
import ma.ensate.myapplication.db.entity.CommandeEntity;
import ma.ensate.myapplication.db.entity.DepenseEntity;

@Database(entities = {CommandeEntity.class, BesoinEntity.class, DepenseEntity.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CommandeDao commandeDao();
    public abstract BesoinDao besoinDao();
    public abstract DepenseDao depenseDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
