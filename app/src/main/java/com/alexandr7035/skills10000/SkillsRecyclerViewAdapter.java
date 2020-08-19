package com.alexandr7035.skills10000;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alexandr7035.skills10000.data.SkillEntity;

import java.util.ArrayList;
import java.util.List;


public class SkillsRecyclerViewAdapter  extends RecyclerView.Adapter<SkillsRecyclerViewAdapter.ViewHolder> {

    private List<SkillEntity> items;
    private List<SkillEntity> selectedItems;
    private SkillClickListener skillClickListener;
    private SkillLongClickListener skillLongClickListener;

    public SkillsRecyclerViewAdapter() {
        this.items = new ArrayList<>();
        this.selectedItems = new ArrayList<>();
    }

    public void setItems(List<SkillEntity> items) {

        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean checkIfItemSelected(int position) {
        return selectedItems.contains(items.get(position));
    }

    public void selectItem(int position) {
        if (! checkIfItemSelected(position)) {
            selectedItems.add(items.get(position));
            notifyDataSetChanged();
        }
    }

    public void unselectItem(int position) {
        selectedItems.remove(items.get(position));
        notifyDataSetChanged();
    }

    public List<SkillEntity> getSelectedItems() {
        return selectedItems;
    }

    // Returns true if selectedItems list is not empty
    public boolean checkIfAnyItemSelected() {
        return selectedItems.size() > 0;
    }

    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public SkillsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_skill, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SkillsRecyclerViewAdapter.ViewHolder holder, int position) {


        holder.skillName.setText(items.get(position).getSkillName());
        holder.skillHours.setText(String.valueOf(items.get(position).getSkillHours()));
        holder.skill_id = items.get(position).getId();

        if (checkIfItemSelected(position)) {
            holder.itemView.setBackgroundResource(R.drawable.background_view_skill_selected);
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.background_view_skill);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public TextView skillName;
        public TextView skillHours;
        int skill_id;

        ViewHolder(View itemView) {
            super(itemView);

            skillName = itemView.findViewById(R.id.skillName);
            skillHours = itemView.findViewById(R.id.skillHours);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            skillClickListener.onSkillClick(skill_id, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            skillLongClickListener.onLongSkillClick(skill_id, getAdapterPosition());
            return true;
        }
    }

    public interface SkillClickListener {
        void onSkillClick(int skill_id, int position);
    }

    public interface SkillLongClickListener {
        void onLongSkillClick(int skill_id, int position);
    }


    // Allows to change click listener
    // For simple selection implementation
    public void setItemClickListener(SkillClickListener skillClickListener) {
        this.skillClickListener = skillClickListener;
    }

    public void setItemLongClickListener(SkillLongClickListener skillLongClickListener) {
        this.skillLongClickListener = skillLongClickListener;
    }

}
