package com.alexandr7035.skills10000.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SkillEntity.class}, version = 1)
public abstract class SkillsDatabase extends RoomDatabase {
    public abstract SkillsDao skillsDao();

    private static SkillsDatabase INSTANCE;

    // Use singleton approach
    public static SkillsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SkillsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SkillsDatabase.class, "skills")
                            // FixMe
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

