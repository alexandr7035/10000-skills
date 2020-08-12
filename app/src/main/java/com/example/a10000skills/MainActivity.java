package com.example.a10000skills;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    private TextView titleView;

    private final String LOG_TAG = "DEBUG_10000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new SkillsRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        // Divider for items
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // Views
        titleView = findViewById(R.id.toolbarTitleView);

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
                        titleView.setText(getString(R.string.maim_title, skills.size()));
                    }
                }
            }
        });

    }

    @Override
    public void onChatClick(int skill_id, int position) {
        Log.d(LOG_TAG, "clicked position " + position + " id " + skill_id);

        Intent intent = new Intent(this, SkillActivity.class);
        intent.putExtra("REQUESTED_SKILL", skill_id);
        startActivity(intent);
    }

    public void btnAddSkill(View v) {
        Log.d(LOG_TAG, "add skill btn pressed");
        viewModel.addSkill(new SkillEntity("skill1"));
    }
}
