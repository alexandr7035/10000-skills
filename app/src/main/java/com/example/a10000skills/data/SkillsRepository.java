package com.example.a10000skills.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SkillsRepository {

    private SkillsDao skillsDao;
    private LiveData<List<SkillEntity>> skillsList;
    private Executor executor;

    public SkillsRepository(Application application) {
        SkillsDatabase db = SkillsDatabase.getDatabase(application);
        skillsDao = db.skillsDao();

        // To run background tasks
        executor = Executors.newSingleThreadExecutor();

        // Livedata chats' list
        skillsList = skillsDao.getSkills();
    }

    public LiveData<List<SkillEntity>> getSkillsFromDb() {
        return skillsList;
    }


    public void addSkillToDb(final SkillEntity skill) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                skillsDao.insert(skill);
            }
        });
    }

}
