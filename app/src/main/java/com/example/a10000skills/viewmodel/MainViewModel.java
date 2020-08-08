package com.example.a10000skills.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.a10000skills.data.SkillEntity;
import com.example.a10000skills.data.SkillsRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private SkillsRepository repository;
    private LiveData<List<SkillEntity>> skillsList;

    public MainViewModel(Application application) {
        repository = new SkillsRepository(application);
        skillsList = repository.getSkillsFromDb();
    }

    public LiveData<List<SkillEntity>> getSkills() {
        return skillsList;
    }

    public void addSkill(SkillEntity skill) {
        repository.addSkillToDb(skill);
    }

}
