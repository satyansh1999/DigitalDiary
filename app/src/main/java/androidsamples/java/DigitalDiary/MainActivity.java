package androidsamples.java.DigitalDiary;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements MonthFragment.GroupCallbacks, DayFragment.Callbacks {
  private static final String TAG = "MainActivity";
  @SuppressLint("StaticFieldLeak")
  public static NavController navController;
  private static HashMap<Integer, String> monthMap = new HashMap<>();
  public static UUID grp = UUID.fromString("843e44ed-6e97-4930-858f-12ead8dea0bf");

  @RequiresApi(api = Build.VERSION_CODES.Q)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "Entered MainActivity");
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    setContentView(R.layout.activity_main);

    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    assert navHostFragment != null;
    navController = navHostFragment.getNavController();

    monthMap.put(0,"January");
    monthMap.put(1,"February");
    monthMap.put(2,"March");
    monthMap.put(3,"April");
    monthMap.put(4,"May");
    monthMap.put(5,"June");
    monthMap.put(6,"July");
    monthMap.put(7,"August");
    monthMap.put(8,"September");
    monthMap.put(9,"October");
    monthMap.put(10,"November");
    monthMap.put(11,"December");
  }

  @Override
  public void onEntrySelected(UUID entryId) {
    Log.d(TAG, "Entry selected: " + entryId);
    NavDirections action = DayFragmentDirections.actionDayFragmentToDayViewFragment(entryId);
    navController.navigate(action);
  }

  @Override
  public void onMonthAdded(UUID id, boolean edit) {
    Log.d(TAG, "Group added: " + id);
    NavDirections action = MonthFragmentDirections.monthAddedAction(id,edit);
    MainActivity.navController.navigate(action);
  }

  @Override
  public void onMonthSelected(UUID id) {
    Log.d(TAG, "Group selected: " + id);
    NavDirections action = MonthFragmentDirections.groupSelectedAction(id);
    navController.navigate(action);
  }

  public static String getSuperscript(int day){
    if(day == 1 || day == 21 || day == 31) return "st";
    if(day == 2 || day == 22) return "nd";
    if(day == 3 || day == 23) return "rd";
    return "th";
  }

  public static String getMonthYear(int month, int year){
    return monthMap.get(month) + ", " + year;
  }
}