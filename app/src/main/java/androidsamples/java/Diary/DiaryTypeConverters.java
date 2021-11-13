package androidsamples.java.Diary;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.util.UUID;

public class DiaryTypeConverters {
    @TypeConverter
    public UUID toUUID(@NonNull String uuid) {
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public String fromUUID(@NonNull UUID uuid) {
        return uuid.toString();
    }
}