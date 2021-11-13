package androidsamples.java.Diary;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.UUID;

public class AppViewModel extends ViewModel {
    private final DiaryRepository mRepository;
    private final MutableLiveData<UUID> entryIdLiveData = new MutableLiveData<>();

    public AppViewModel() {
        mRepository = DiaryRepository.getInstance();
    }

    public void insert(DiaryEntry entry) {
        mRepository.insert(entry);
    }
    public void update(DiaryEntry entry) {
        mRepository.update(entry);
    }
    public void delete(DiaryEntry entry) {
        mRepository.delete(entry);
    }

    public LiveData<List<DiaryEntry>> getAllGroups() {
        return mRepository.getAllGroups();
    }
    public LiveData<List<DiaryEntry>> getAllEntriesOfGroup(UUID grp) {
        return mRepository.getAllEntriesOfGroup(grp);
    }

    public void loadEntry(UUID entryId) {
        entryIdLiveData.setValue(entryId);
    }
    public LiveData<DiaryEntry> getEntryLiveData() {
        return Transformations.switchMap(entryIdLiveData, mRepository::getEntry);
    }

    public void deleteGroup(UUID grp) {
        mRepository.deleteGroup(grp);
    }
}
