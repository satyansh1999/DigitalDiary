package androidsamples.java.Diary;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiaryRepository {
    private static final String DATABASE_NAME = "digital_diary_table";
    private static DiaryRepository sInstance;
    private final DiaryEntryDAO mDiaryEntryDao;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    private DiaryRepository(Context context) {
        DiaryRoomDatabase db
                = Room.databaseBuilder(context.getApplicationContext(),
                DiaryRoomDatabase.class,
                DATABASE_NAME).build();
        mDiaryEntryDao = db.journalEntryDao();
    }

    public static void init(Context context) {
        if (sInstance == null) sInstance = new DiaryRepository(context);
    }

    public static DiaryRepository getInstance() {
        if (sInstance == null)
            throw new IllegalStateException("Repository must be initialized");
        return sInstance;
    }

    public void insert(DiaryEntry entry) {
        mExecutor.execute(() -> mDiaryEntryDao.insert(entry));
    }

    public void update(DiaryEntry entry) {
        mExecutor.execute(() -> mDiaryEntryDao.update(entry));
    }

    public void delete(DiaryEntry entry) {
        mExecutor.execute(() -> mDiaryEntryDao.delete(entry));
    }

    public LiveData<DiaryEntry> getEntry(java.util.UUID id) {
        return mDiaryEntryDao.getEntry(id);
    }

    public LiveData<List<DiaryEntry>> getAllEntriesOfGroup(UUID grp) {
        return mDiaryEntryDao.getAllEntriesOfGroup(grp);
    }

    public LiveData<List<DiaryEntry>> getAllGroups() {
        return mDiaryEntryDao.getAllGroups(MainActivity.grp);
    }

    public void deleteGroup(UUID grp) {
        mExecutor.execute(() -> mDiaryEntryDao.deleteGroup(grp));
    }
}