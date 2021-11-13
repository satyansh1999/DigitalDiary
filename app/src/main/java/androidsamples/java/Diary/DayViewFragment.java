package androidsamples.java.Diary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import java.util.UUID;

public class DayViewFragment extends Fragment {
    private static final String TAG = "DayViewFragment";
    private TextView mText, mDesc, mTitle, mTitle2, mTitle3;
    private AppViewModel appViewModel;
    private DiaryEntry mEntry;
    private UUID entryId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        entryId = DayViewFragmentArgs.fromBundle(getArguments()).getId();
        Log.d(TAG, "Loading entry: " + entryId);
        appViewModel.loadEntry(entryId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_view, container, false);
        mText = view.findViewById(R.id.diary_item_view_text);
        mDesc = view.findViewById(R.id.diary_item_view_desc);
        mTitle = view.findViewById(R.id.diary_item_view_title);
        mTitle2 = view.findViewById(R.id.diary_item_view_title2);
        mTitle3 = view.findViewById(R.id.diary_item_view_title3);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appViewModel.getEntryLiveData().observe(requireActivity(),
                entry -> {
                    this.mEntry = entry;
                    if (entry != null) updateUI();
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_day_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_entry) {
            Log.d(TAG, "Edit button clicked");
            NavDirections action = DayViewFragmentDirections.actionDayViewFragmentToDayEditFragment(entryId, true, mEntry.getGroup());
            MainActivity.navController.navigate(action);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        mText.setText(mEntry.getText());
        mDesc.setText(mEntry.getDesc());
        mTitle.setText(String.valueOf(mEntry.getDay()));
        mTitle2.setText(MainActivity.getSuperscript(mEntry.getDay()));
        mTitle3.setText(MainActivity.getMonthYear(mEntry.getMonth(),mEntry.getYear()));
    }
}

