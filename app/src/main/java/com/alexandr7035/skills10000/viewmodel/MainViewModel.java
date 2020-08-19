package com.alexandr7035.skills10000.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexandr7035.skills10000.data.SkillEntity;
import com.alexandr7035.skills10000.data.SkillsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends ViewModel {

    private SkillsRepository repository;
    private LiveData<List<SkillEntity>> skillsList;
    private MutableLiveData<List<SkillEntity>> selectedSkillsLData;


    public MainViewModel(Application application) {
        repository = new SkillsRepository(application);
        skillsList = repository.getSkillsFromDb();

        selectedSkillsLData = new MutableLiveData<List<SkillEntity>>(new ArrayList<SkillEntity>());

    }

    public LiveData<List<SkillEntity>> getSkills() {
        return skillsList;
    }

    public void addSkill(SkillEntity skill) {
        repository.addSkillToDb(skill);
    }

    public void deleteSkill(SkillEntity skill) {
        repository.deleteSkillFromDb(skill);
    }

    public MutableLiveData<List<SkillEntity>> getSelectedSkillsLData() {
        return selectedSkillsLData;
    }

    public SkillEntity getSkill(int skill_id) {

        SkillEntity skill = null;

        try {
            skill = repository.getSkillFromDb(skill_id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return skill;
    }
}
