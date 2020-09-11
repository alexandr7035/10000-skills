package com.alexandr7035.skills10000;

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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alexandr7035.skills10000.data.SkillEntity;
import com.alexandr7035.skills10000.data.SkillStatHelper;
import com.alexandr7035.skills10000.viewmodel.SkillViewModel;
import com.alexandr7035.skills10000.viewmodel.SkillViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class SkillActivity extends AppCompatActivity
                            implements Toolbar.OnMenuItemClickListener {

    private TextView toolbarTitle;
    private TextView skillHoursView;
    private EditText editTitleView;

    private SkillViewModel viewModel;

    private SkillStatHelper skillStatHelper;

    private final String LOG_TAG = "DEBUG_10000";

    private LiveData<SkillEntity> skill;

    private Vibrator vibrator;

    private JSONObject statJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        // Views
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        int skill_id = intent.getIntExtra("REQUESTED_SKILL", 0);
        //Log.d(LOG_TAG, "requested skill " + skill_id);

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

        skillStatHelper = new SkillStatHelper("skill_" + skill_id + ".csv", this);

        // fixme
        // Handle exception
        try {
            statJSON = new JSONObject(skillStatHelper.getStatFromFile());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d(LOG_TAG, "stat " + statJSON.toString());

    }


    public void onIncreaseHoursBtnClick(View v) {
        SkillEntity skill_data;
        skill_data = skill.getValue();
        long hours = skill_data.getSkillHours() + 1;
        skill_data.setSkillHours(hours);
        viewModel.updateSkill(skill_data);

        updateStat(1);

    }

    public void onDecreaseHoursBtnClick(View v) {
        SkillEntity skill_data;
        skill_data = skill.getValue();

        long curr_date = System.currentTimeMillis() / 1000;
        String curr_date_str = DateFormat.format("yyyyMMdd", curr_date*1000).toString();
        int today_hours = 0;

        try {
            today_hours = (Integer) statJSON.get(String.valueOf(curr_date_str));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d(LOG_TAG, "today hours: " + today_hours);


        if (skill_data.getSkillHours() == 0) {
            // Hours can't be lower than 0
            vibrator.vibrate(100);
        }

        if (today_hours == 0) {
            // Hours can't be lower than 0
            vibrator.vibrate(100);
        }

        else {
            long hours = skill_data.getSkillHours() - 1;
            skill_data.setSkillHours(hours);
            viewModel.updateSkill(skill_data);

            updateStat(-1);
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


    private void updateStat(int hoursChange) {

        long date = System.currentTimeMillis() / 1000;

        String dateStr = DateFormat.format("yyyyMMdd", date*1000).toString();

        Log.d(LOG_TAG, "update stat " + dateStr);

        try {
            if (! statJSON.has(dateStr)) {
                statJSON.put(dateStr, hoursChange);
            }
            else {

                int previousVal = (int) statJSON.get(dateStr);

                statJSON.remove(dateStr);
                statJSON.put(dateStr, previousVal + hoursChange);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "updated stat " + statJSON.toString());
        skillStatHelper.writeStatToFile(statJSON.toString());

    }

}
