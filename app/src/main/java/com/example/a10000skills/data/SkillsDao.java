package com.example.a10000skills.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SkillsDao {

    @Query("SELECT COUNT(*) FROM skills")
    int getSkillsCount();

    @Query("SELECT * FROM skills ORDER BY id")
    LiveData<List<SkillEntity>> getSkills();

    @Query("SELECT * FROM skills WHERE id = (:id)")
    SkillEntity getSkillById(int id);

    @Query("SELECT * FROM skills WHERE id = (:id)")
    LiveData<SkillEntity> getSkillById_LD(int id);

    @Insert
    void insert(SkillEntity skillEntity);

    @Update
    void updateSkill(SkillEntity skillEntity);

    @Delete
    void deleteSkill(SkillEntity skillEntity);

}
