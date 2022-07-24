package androidsamples.java.DigitalDiary;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "diary_table")
public class DiaryEntry {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    public UUID mUid;

    @ColumnInfo(name = "group")
    private UUID mGroup;

    @ColumnInfo(name = "text")
    private String mText;

    @ColumnInfo(name = "desc")
    private String mDesc;

    @ColumnInfo(name = "day")
    private int mDay;

    @ColumnInfo(name = "month")
    private int mMonth;

    @ColumnInfo(name = "year")
    private int mYear;


    @SuppressLint("SimpleDateFormat")
    public DiaryEntry() {
        mUid = UUID.randomUUID();
        mText = "";
        mDesc = "";
        mGroup = UUID.fromString("843e44ed-6e97-4930-858f-12ead8dea0bf");
        mDay = 1;
        mMonth = 0;
        mYear = 2021;
    }

    public void setText(String t){
        this.mText = t;
    }
    public String getText(){
        return this.mText;
    }

    public void setId(UUID id){
        this.mUid = id;
    }
    public UUID getUid(){
        return this.mUid;
    }

    public void setGroup(UUID d){
        this.mGroup = d;
    }
    public UUID getGroup(){
        return this.mGroup;
    }

    public String getDesc() {
        return mDesc;
    }
    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public int getDay() {
        return mDay;
    }
    public void setDay(int mDay) {
        this.mDay = mDay;
    }

    public int getMonth() {
        return mMonth;
    }
    public void setMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getYear() {
        return mYear;
    }
    public void setYear(int mYear) {
        this.mYear = mYear;
    }
}
