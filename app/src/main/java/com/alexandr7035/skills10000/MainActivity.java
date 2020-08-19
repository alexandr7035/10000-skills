package com.alexandr7035.skills10000;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandr7035.skills10000.data.SkillEntity;
import com.alexandr7035.skills10000.viewmodel.MainViewModel;
import com.alexandr7035.skills10000.viewmodel.MainViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SkillsRecyclerViewAdapter recyclerViewAdapter;
    private MainViewModel viewModel;
    private LiveData<List<SkillEntity>> skillsListLiveData;

    private MutableLiveData<List<SkillEntity>> selectedSkillsLData;

    private TextView titleView;

    private FloatingActionButton addSkillBtn;
    private FloatingActionButton deleteSkillsBtn;

    private DefaultClickListener defaultClickListener;
    private SelectionClickListener selectionClickListener;

    private Vibrator vibrator;

    private LinearLayout deletedSkillsBar;

    private final String LOG_TAG = "DEBUG_10000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Click listeners for recyclerview items
        selectionClickListener = new SelectionClickListener();
        defaultClickListener = new DefaultClickListener();

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new SkillsRecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        // Set default click listeners
        recyclerViewAdapter.setItemClickListener(defaultClickListener);
        recyclerViewAdapter.setItemLongClickListener(defaultClickListener);

        // Views
        titleView = findViewById(R.id.toolbarTitleView);
        addSkillBtn = findViewById(R.id.addSkillBtn);
        deleteSkillsBtn = findViewById(R.id.deleteSkillsBtn);
        deleteSkillsBtn.hide();
        deletedSkillsBar = findViewById(R.id.deletedSkillsBar);

        // ViewModel & LiveData
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(this.getApplication())).get(MainViewModel.class);
        skillsListLiveData = viewModel.getSkills();

        selectedSkillsLData = viewModel.getSelectedSkillsLData();

        // Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Update RecyclerView when skills' list changed
        skillsListLiveData.observe(this, new Observer<List<SkillEntity>>() {
            @Override
            public void onChanged(@Nullable List<SkillEntity> skills) {

                if (skills != null) {
                        recyclerViewAdapter.setItems(skills);
                    }
            }
        });


        // Obsrver for selected items
        selectedSkillsLData.observe(this, new Observer<List<SkillEntity>>() {
            @Override
            public void onChanged(@Nullable List<SkillEntity> skills) {

                if (skills != null) {

                    if (! skills.isEmpty()) {
                        //Log.d(LOG_TAG, "some items are selected: " + skills);

                        addSkillBtn.hide();
                        deleteSkillsBtn.show();

                        recyclerViewAdapter.setItemClickListener(selectionClickListener);
                        recyclerViewAdapter.setItemLongClickListener(selectionClickListener);


                    } else {
                        //Log.d(LOG_TAG, "no items selected now");

                        deleteSkillsBtn.hide();
                        addSkillBtn.show();

                        recyclerViewAdapter.setItemClickListener(defaultClickListener);
                        recyclerViewAdapter.setItemLongClickListener(defaultClickListener);

                    }

                }
            }
        });

    }


    public void btnAddSkill(View v) {
        //Log.d(LOG_TAG, "add skill btn pressed");

        // Creating "new skill" dialog
       AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

       // Use custom layout for dialog
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_create_skill, null);
        builder.setView(dialogView);

        // Disable canceling on touching outside
        builder.setCancelable(false);

        // Make final to use in inner class
        final AlertDialog dialog = builder.create();

        // Create button (textview)
        TextView createBtn = dialogView.findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get skill name
                final EditText skillNameField = dialogView.findViewById(R.id.skillNameField);
                // Trim the sring
                String skillName = skillNameField.getText().toString().trim();

                // Don't allow empty name
                if (! skillName.equals("") ) {
                    // Create skill
                    SkillEntity newSkill = new SkillEntity(skillName);
                    viewModel.addSkill(newSkill);

                    dialog.dismiss();
                }
                else {

                    // Set red border to edittext
                    skillNameField.setBackgroundResource(R.drawable.background_new_skill_dialog_input_invalid);

                    // Change to default after 1s
                    new CountDownTimer(1000, 30) {

                        @Override
                        public void onTick(long arg0) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onFinish() {
                            skillNameField.setBackgroundResource(R.drawable.background_new_skill_dialog_input);
                        }
                    }.start();

                    // Set red border to edittext
                    skillNameField.setBackgroundResource(R.drawable.background_new_skill_dialog_input_invalid);

                    vibrator.vibrate(500);
                }

            }
        });

        // Cancel button (textview)
        TextView cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        // Cancel dialog on back key pressed
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface di, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // Cancel dialog
                    dialog.dismiss();
                }
                return false;
            }
        });
        

        // Show dialog
        dialog.show();
    }


    // Shows dialog to delete skills
    public void btnDeleteSkill(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Use custom layout for dialog
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_delete_skills, null);
        builder.setView(dialogView);

        final TextView deleteTextView = dialogView.findViewById(R.id.deleteTextView);
        deleteTextView.setText(Html.fromHtml(getString(R.string.delete_skill_dialog_text,
                                recyclerViewAdapter.getSelectedItems().size())));

        // Disable canceling on touching outside
        builder.setCancelable(false);

        // Make final to use in inner class
        final AlertDialog dialog = builder.create();

        // Create button (textview)
        TextView deleteBtn = dialogView.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Delete selected skills
                for (SkillEntity skill : recyclerViewAdapter.getSelectedItems()) {
                    viewModel.deleteSkill(skill);
                }

                // Prepare custom bar (show after deleting)
                TextView deletedSkillsBarTextView = deletedSkillsBar.findViewById(R.id.deletedSkillsBarTextView);
                deletedSkillsBarTextView.setText(getString(R.string.delete_skills_bar_text,
                        recyclerViewAdapter.getSelectedItems().size()));

                // Clear selection
                recyclerViewAdapter.clearSelection();
                selectedSkillsLData.setValue(recyclerViewAdapter.getSelectedItems());

                dialog.dismiss();

                vibrator.vibrate(100);

                // Make deletedSkillsBar visible for 3 seconds and then hide
                deletedSkillsBar.setVisibility(View.VISIBLE);
                new CountDownTimer(3000, 30) {

                    @Override
                    public void onTick(long arg0) {

                    }

                    @Override
                    public void onFinish() {
                        deletedSkillsBar.setVisibility(View.GONE);
                    }
                }.start();


            }
        });

        // Create button (textview)
        TextView camcelBtn = dialogView.findViewById(R.id.cancelBtn);
        camcelBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                recyclerViewAdapter.clearSelection();
                selectedSkillsLData.setValue(recyclerViewAdapter.getSelectedItems());
                dialog.dismiss();
            }
        });


        // Cancel dialog on back key pressed
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface di, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // Clear selection
                    recyclerViewAdapter.clearSelection();
                    selectedSkillsLData.setValue(recyclerViewAdapter.getSelectedItems());
                    // Cancel dialog
                    dialog.dismiss();
                }
                return false;
            }
        });


        dialog.show();

    }


    // Override onBackPressed method
    // Clear selection if any item selected
    @Override
    public void onBackPressed() {

        if ( recyclerViewAdapter.checkIfAnyItemSelected()) {
            recyclerViewAdapter.clearSelection();
            selectedSkillsLData.setValue(recyclerViewAdapter.getSelectedItems());
        }
        else {
            super.onBackPressed();
        }
    }


    // Default click listener for recyclerview items
    class DefaultClickListener implements SkillsRecyclerViewAdapter.SkillClickListener,
                                             SkillsRecyclerViewAdapter.SkillLongClickListener {


        @Override
        public void onSkillClick(int skill_id, int position) {
            //Log.d(LOG_TAG, "clicked position " + position + " id " + skill_id);

            Intent intent = new Intent(MainActivity.this, SkillActivity.class);
            intent.putExtra("REQUESTED_SKILL", skill_id);
            startActivity(intent);
        }

        @Override
        public void onLongSkillClick(int skill_id, int position) {
            //Log.d(LOG_TAG, "clicked (LONG) position " + position + " id " + skill_id);

            recyclerViewAdapter.selectItem(position);
            selectedSkillsLData.postValue(recyclerViewAdapter.getSelectedItems());

            //Log.d(LOG_TAG, selectedSkillsLData.getValue().toString());
        }
    }


    // Set if at least one item in RecyclerView is selected
    // Replaced by default click listener when no items selected
    class SelectionClickListener implements SkillsRecyclerViewAdapter.SkillClickListener,
                                             SkillsRecyclerViewAdapter.SkillLongClickListener {


        @Override
        public void onSkillClick(int skill_id, int position) {
            //Log.d(LOG_TAG, "SELECTED_CL: click item " + position + " skill_id " + skill_id);

            // Select item if not selected
            // Else unselect

            if (recyclerViewAdapter.checkIfItemSelected(position)) {
                recyclerViewAdapter.unselectItem(position);
            }
            else {
                recyclerViewAdapter.selectItem(position);
            }

            // Update livedata
            selectedSkillsLData.setValue(recyclerViewAdapter.getSelectedItems());

        }

        @Override
        public void onLongSkillClick(int skill_id, int position) {
            //Log.d(LOG_TAG, "SELECTED_CL: LONG click item " + position + " skill_id " + skill_id);

            // Do nothing
            // May be changed later
        }
    }


}
