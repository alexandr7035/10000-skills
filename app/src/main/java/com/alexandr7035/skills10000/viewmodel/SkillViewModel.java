package com.alexandr7035.skills10000.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alexandr7035.skills10000.data.SkillEntity;
import com.alexandr7035.skills10000.data.SkillsRepository;

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
