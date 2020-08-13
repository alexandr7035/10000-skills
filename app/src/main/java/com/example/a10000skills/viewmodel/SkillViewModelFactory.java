package com.example.a10000skills.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SkillViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private int skill_id;

    public SkillViewModelFactory(Application application, int skill_id) {
        this.application = application;
        this.skill_id = skill_id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SkillViewModel(application, skill_id);
    }
}
