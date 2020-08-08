package com.example.a10000skills.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "skills")
public class SkillEntity  {

    public SkillEntity(String name) {

        this.name = name;
        this.hours = 0;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "hours")
    public long hours;

    // Getters
    public int getId() {
        return id;
    }

    public String getSkillName() {
        return this.name;
    }

    public long getSkillHours() {
        return this.hours;
    }


    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSkillName(String name) {
        this.name = name;
    }

    public void setSkillHours(long hours) {
        this.hours = hours;
    }

}