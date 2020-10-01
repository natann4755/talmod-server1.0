package com.example.myapp.dataBase;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.model.Daf;

@Database(entities = {Daf.class}, version = 6,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "DataBaseLearning";
    static AppDataBase INSTANCE;
    public abstract DaoDaf daoLearning();

    public static AppDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }





}

