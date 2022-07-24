package androidsamples.java.DigitalDiary;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import java.util.Calendar;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayEditFragment # newInstance} factory method to
 * create an instance of this fragment.
 */

public class DayEditFragment extends Fragment {
  private static final String TAG = "EntryDetailsFragment";
  private EditText mText, mDesc;
  private Button mEditDate;
  private AppViewModel appViewModel;
  private DiaryEntry mEntry;
  private boolean edit;
  private UUID group, entryId;
  public static int day, month, year;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    entryId = DayEditFragmentArgs.fromBundle(getArguments()).getEntryId();
    edit = DayEditFragmentArgs.fromBundle(getArguments()).getEdit();
    group = DayEditFragmentArgs.fromBundle(getArguments()).getGroup();
    Log.d(TAG, "Loading entry: " + entryId);

    if(edit) appViewModel.loadEntry(entryId);
    else {
      mEntry = new DiaryEntry();
      entryId = mEntry.getUid();
      mEntry.setGroup(group);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_day_edit, container, false);

    mText = view.findViewById(R.id.edit_text);
    mEditDate = view.findViewById(R.id.btn_entry_date);
    mDesc = view.findViewById(R.id.edit_desc);
    view.findViewById(R.id.btn_save).setOnClickListener(this::saveEntry);

    mEditDate.setOnClickListener(v -> {
      NavDirections action = DayEditFragmentDirections.datePickerAction(edit);
      MainActivity.navController.navigate(action);
    });
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if(edit) {
      appViewModel.getEntryLiveData().observe(requireActivity(),
              entry -> {
                this.mEntry = entry;
                if (entry != null) updateUI();
              });
    }
    else{
      Calendar cal = Calendar.getInstance();
      day = cal.get(Calendar.DAY_OF_MONTH);
      month = cal.get(Calendar.MONTH);
      year = cal.get(Calendar.YEAR);
      mEditDate.setText(day+"-"+(month+1)+"-"+year);
    }
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_entry_detail, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_delete_entry) {
      Log.d(TAG, "Delete button clicked");
      if(edit) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Delete Journal Entry");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
          appViewModel.delete(mEntry);
          Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
          requireActivity().onBackPressed();
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
        alert.show();
      }
      if(!edit)
        requireActivity().onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  private void updateUI() {
    mText.setText(mEntry.getText());
    mDesc.setText(mEntry.getDesc());
    year = mEntry.getYear();
    month = mEntry.getMonth();
    day = mEntry.getDay();
    mEditDate.setText(day+"-"+(month+1)+"-"+year);
  }

  private void saveEntry(View v) {
    Log.d(TAG, "Save button clicked");
    mEntry.setText(mText.getText().toString());
    mEntry.setDesc(mDesc.getText().toString());
    mEntry.setDay(day);
    mEntry.setMonth(month);
    mEntry.setYear(year);
    if (edit) appViewModel.update(mEntry);
    else appViewModel.insert(mEntry);
    Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
    requireActivity().onBackPressed();
  }
}

