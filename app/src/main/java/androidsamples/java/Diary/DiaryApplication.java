package androidsamples.java.Diary;

import android.app.Application;

public class DiaryApplication extends Application {
  @java.lang.Override
  public void onCreate() {
    super.onCreate();
    DiaryRepository.init(this);
  }
}
