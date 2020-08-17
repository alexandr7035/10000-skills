package com.example.a10000skills;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a10000skills.data.SkillEntity;
import com.example.a10000skills.viewmodel.SkillViewModel;
import com.example.a10000skills.viewmodel.SkillViewModelFactory;

import java.util.List;

public class SkillActivity extends AppCompatActivity
                            implements Toolbar.OnMenuItemClickListener {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView skillHoursView;
    private EditText editTitleView;

    private SkillViewModel viewModel;

    private int skill_id;

    private final String LOG_TAG = "DEBUG_10000";

    private LiveData<SkillEntity> skill;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        // Views
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        editTitleView = findViewById(R.id.toolbarEditTitleView);
        toolbar.inflateMenu(R.menu.toolbar_menu_skill_activity);
        toolbar.setOnMenuItemClickListener(this);
        skillHoursView = findViewById(R.id.skillHoursView);


        // Close activity on navigation btn click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get skill id
        Intent intent = getIntent();
        skill_id = intent.getIntExtra("REQUESTED_SKILL", 0);
        Log.d(LOG_TAG, "requested skill " + skill_id);

        // ViewModel & Livedata
        viewModel = new ViewModelProvider(this, new SkillViewModelFactory(this.getApplication(), skill_id)).get(SkillViewModel.class);
        skill = viewModel.getSkillLData();

        // Update UI when skill changed
        skill.observe(this, new Observer<SkillEntity>() {
            @Override
            public void onChanged(@Nullable SkillEntity skill) {
                        skillHoursView.setText(String.valueOf(skill.getSkillHours()));
                        toolbarTitle.setText(skill.getSkillName());
                    }
        });

        // Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }


    public void onIncreaseHoursBtnClick(View v) {
        SkillEntity skill_data;
        skill_data = skill.getValue();
        long hours = skill_data.getSkillHours() + 1;
        skill_data.setSkillHours(hours);
        viewModel.updateSkill(skill_data);
    }

    public void onDecreaseHoursBtnClick(View v) {
        SkillEntity skill_data;
        skill_data = skill.getValue();

        if (skill_data.getSkillHours() == 0) {
            // Hours can't be lower than 0
            vibrator.vibrate(100);
        }
        else {
            long hours = skill_data.getSkillHours() - 1;
            skill_data.setSkillHours(hours);
            viewModel.updateSkill(skill_data);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_title_edit_skill:

                SkillEntity skill_data;
                skill_data = skill.getValue();

                if (toolbarTitle.getVisibility() == View.VISIBLE) {

                    toolbarTitle.setVisibility(View.GONE);
                    editTitleView.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.ic_checkmark);

                    editTitleView.setText(skill_data.getSkillName());

                }
                else {
                    toolbarTitle.setVisibility(View.VISIBLE);
                    editTitleView.setVisibility(View.GONE);
                    item.setIcon(R.drawable.ic_edit);

                    // Update skill's name
                    skill_data.setSkillName(editTitleView.getText().toString());
                    viewModel.updateSkill(skill_data);

                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
