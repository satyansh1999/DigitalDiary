package androidsamples.java.DigitalDiary;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

@Dao
public interface DiaryEntryDAO {
    @Insert
    void insert(DiaryEntry entry);

    @Update
    void update(DiaryEntry entry);

    @Delete
    void delete(DiaryEntry entry);

    @Query("SELECT * from diary_table WHERE id=(:id)")
    LiveData<DiaryEntry> getEntry(java.util.UUID id);

    @Query("SELECT * from diary_table where `group`=(:group)")
    LiveData<List<DiaryEntry>> getAllEntriesOfGroup(UUID group);

    @Query("SELECT * From diary_table where `group`=(:grp)")
    LiveData<List<DiaryEntry>> getAllGroups(UUID grp);

    @Query("Delete from diary_table where `group`=(:group) or `id`=(:group)")
    void deleteGroup(UUID group);

    @Query("SELECT * from diary_table")
    LiveData<List<DiaryEntry>> getAllEntries();

    @Query("Delete from diary_table")
    void deleteAll();
}
