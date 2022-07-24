package androidsamples.java.DigitalDiary;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {DiaryEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DiaryTypeConverters.class)
public abstract class DiaryRoomDatabase extends RoomDatabase {
    public abstract DiaryEntryDAO journalEntryDao();
}
