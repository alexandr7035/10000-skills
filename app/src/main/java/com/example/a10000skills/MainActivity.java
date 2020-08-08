package com.example.a10000skills;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.a10000skills.data.SkillEntity;
import com.example.a10000skills.viewmodel.MainViewModel;
import com.example.a10000skills.viewmodel.MainViewModelFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements SkillsRecyclerViewAdapter.SkillClickListener {

    private RecyclerView recyclerView;
    private SkillsRecyclerViewAdapter recyclerViewAdapter;
    private MainViewModel viewModel;
    private LiveData<List<SkillEntity>> skillsListLiveData;

    private final String LOG_TAG = "DEBUG_10000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new SkillsRecyclerViewAdapter(this);

        recyclerView.setAdapter(recyclerViewAdapter);

        // Divider for items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // ViewModel & LiveData
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(this.getApplication())).get(MainViewModel.class);
        skillsListLiveData = viewModel.getSkills();


        // Update RecyclerView when chats are changed
        skillsListLiveData.observe(this, new Observer<List<SkillEntity>>() {
            @Override
            public void onChanged(@Nullable List<SkillEntity> skills) {

                if (skills != null) {

                    if (! skills.isEmpty()) {
                        recyclerViewAdapter.setItems(skills);
                    }
                }
            }
        });

    }

    @Override
    public void onChatClick(int skill_id, int position) {

    }

    public void btnAddSkill(View v) {
        Log.d(LOG_TAG, "add skill btn pressed");
        viewModel.addSkill(new SkillEntity("skill1"));
    }
}
