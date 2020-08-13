package com.example.a10000skills.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.a10000skills.data.SkillEntity;
import com.example.a10000skills.data.SkillsRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SkillViewModel extends ViewModel {

    private SkillsRepository repository;
    private LiveData<SkillEntity> skill;

    public SkillViewModel(Application application, int skill_id) {
        repository = new SkillsRepository(application);

        skill = repository.getSkillFromDb_LD(skill_id);

    }

    public LiveData<SkillEntity> getSkillLData() {
        return skill;
    }

    public void updateSkill(SkillEntity skill) {
        repository.updateSkillInDb(skill);
    }

}
