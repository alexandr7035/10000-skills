package com.example.a10000skills.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SkillsRepository {

    private SkillsDao skillsDao;
    private LiveData<List<SkillEntity>> skillsList;
    private ExecutorService executor;

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


    public SkillEntity getSkillFromDb(final int id) throws ExecutionException, InterruptedException {
        Future foo = executor.submit(new Callable() {
            @Override
            public SkillEntity call() {
                return skillsDao.getSkillById(id);
            }
        });


        return (SkillEntity) foo.get();
    }

    public LiveData<SkillEntity> getSkillFromDb_LD(int id) {
        return skillsDao.getSkillById_LD(id);
    }

    public void updateSkillInDb(final SkillEntity skill) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                skillsDao.updateSkill(skill);
            }
        });
    }
}
