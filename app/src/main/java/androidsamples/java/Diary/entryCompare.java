package androidsamples.java.Diary;

import java.util.Comparator;
import java.util.HashMap;

public class entryCompare implements Comparator<DiaryEntry> {
    HashMap<String,Integer> month = new HashMap<>();

    @Override
    public int compare(DiaryEntry o1, DiaryEntry o2) {
        int a2 = o1.getYear();
        int b2 = o2.getYear();
        if(a2 != b2) return a2 - b2;

        int a1 = o1.getMonth();
        int b1 = o2.getMonth();
        if(a1 != b1) return a1 - b1;

        int a = o1.getDay();
        int b = o2.getDay();
        return a - b;
    }
}