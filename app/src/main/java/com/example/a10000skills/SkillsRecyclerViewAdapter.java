package com.example.a10000skills;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a10000skills.data.SkillEntity;

import java.util.ArrayList;
import java.util.List;


public class SkillsRecyclerViewAdapter  extends RecyclerView.Adapter<SkillsRecyclerViewAdapter.ViewHolder> {

    private List<SkillEntity> items;
    private SkillClickListener skillClickListener;

    public SkillsRecyclerViewAdapter(SkillClickListener skillClickListener) {
        this.items = new ArrayList<>();
        this.skillClickListener = skillClickListener;
    }

    public void setItems(List<SkillEntity> items) {

        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public SkillsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_skill, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SkillsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.skillName.setText(items.get(position).getSkillName());
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView skillName;
        int skill_id;

        ViewHolder(View itemView) {
            super(itemView);

            skillName = itemView.findViewById(R.id.skillName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            skillClickListener.onChatClick(skill_id, getAdapterPosition());
        }
    }

    public interface SkillClickListener {
        void onChatClick(int skill_id, int position);
    }

}
