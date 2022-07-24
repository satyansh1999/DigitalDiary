package androidsamples.java.DigitalDiary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 */
public class DayFragment extends Fragment {
  private static final String TAG = "EntryListFragment";
  private AppViewModel mAppViewModel;
  private Callbacks mCallbacks = null;
  private UUID group;
  private TextView tv;
  private DiaryEntry mEntry;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    mAppViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    group = DayFragmentArgs.fromBundle(getArguments()).getGroup();
    mAppViewModel.loadEntry(group);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_day, container, false);

    tv = view.findViewById(R.id.txt_grp);

    FloatingActionButton fab = view.findViewById(R.id.btn_add_day);
    fab.setOnClickListener(this::addNewEntry);

    RecyclerView entriesList = view.findViewById(R.id.recyclerView);
    entriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
    EntryListAdapter adapter = new EntryListAdapter(getActivity());
    entriesList.setAdapter(adapter);

    mAppViewModel.getAllEntriesOfGroup(group).observe(requireActivity(), adapter::setEntries);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mAppViewModel.getEntryLiveData().observe(requireActivity(),
            entry -> {
              this.mEntry = entry;
              if(entry != null) tv.setText(MainActivity.getMonthYear(mEntry.getMonth(), mEntry.getYear()));
            });
  }

  public void addNewEntry(View view) {
    NavDirections action = DayFragmentDirections.addEntryAction(UUID.randomUUID(), false, group);
    MainActivity.navController.navigate(action);
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    mCallbacks = (Callbacks) context;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_entry_list, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.menu_delete_group) {
      Log.d(TAG, "Group Delete button clicked");

      AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
      alert.setTitle("Delete Folder");
      alert.setMessage("Are you sure you want to delete " + MainActivity.getMonthYear(mEntry.getMonth(),mEntry.getYear()) + "?");
      alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
        mAppViewModel.deleteGroup(group);
        Toast.makeText(getContext(), group + " deleted", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
      });
      alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
      alert.show();
    }
    return super.onOptionsItemSelected(item);
  }

  interface Callbacks {
    void onEntrySelected(UUID id);
  }

  private class EntryViewHolder extends RecyclerView.ViewHolder {
    private DiaryEntry mEntry;
    private final TextView mTxtTitle;
    private final TextView mSuper;
    private final TextView mMonthYear;
    private final TextView mDesc;
    private final TextView mText;
    private final LinearLayout mDel;

    public EntryViewHolder(@NonNull View itemView) {
      super(itemView);

      mTxtTitle = itemView.findViewById(R.id.diary_item_title);
      mSuper = itemView.findViewById(R.id.diary_item_title2);
      mMonthYear = itemView.findViewById(R.id.diary_item_title3);
      mDesc = itemView.findViewById(R.id.diary_item_desc);
      mText = itemView.findViewById(R.id.diary_item_text);
      mDel = itemView.findViewById(R.id.diary_item_delete);

      itemView.setOnClickListener(this::launchJournalEntryFragment);
      mDel.setOnClickListener(this:: deleteView);
    }

    private void launchJournalEntryFragment(View v) {
      mCallbacks.onEntrySelected(mEntry.getUid());
    }

    private void deleteView(View v) {
      AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
      alert.setTitle("Delete Journal Entry");
      alert.setMessage("Are you sure you want to delete?");
      alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
        mAppViewModel.delete(mEntry);
        Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
      });
      alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
      alert.show();
    }

    @SuppressLint("SetTextI18n")
    void bind(DiaryEntry entry) {
      mEntry = entry;
      this.mTxtTitle.setText(String.valueOf(mEntry.getDay()));
      this.mSuper.setText(MainActivity.getSuperscript(mEntry.getDay()));
      this.mMonthYear.setText(MainActivity.getMonthYear(mEntry.getMonth(),mEntry.getYear()));
      String desc = mEntry.getDesc();
      String text = mEntry.getText();
      if(desc.length() > 100) this.mDesc.setText(mEntry.getDesc().substring(0,100)+"...");
      else this.mDesc.setText(mEntry.getDesc());
      if(text.length() > 350) this.mText.setText(mEntry.getText().substring(0,350)+"...");
      else this.mText.setText(mEntry.getText());
    }
  }

  private class EntryListAdapter extends RecyclerView.Adapter<EntryViewHolder> {
    private final LayoutInflater mInflater;
    private List<DiaryEntry> mEntries;

    public EntryListAdapter(Context context) {
      mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemView = mInflater.inflate(R.layout.fragment_entry, parent, false);
      return new EntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
      if (mEntries != null) {
        DiaryEntry current = mEntries.get(position);
        holder.bind(current);
      }
    }

    @Override
    public int getItemCount() {
      return (mEntries == null) ? 0 : mEntries.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEntries(List<DiaryEntry> entries) {
      mEntries = entries;
      Collections.sort(mEntries,new entryCompare());
      notifyDataSetChanged();
    }
  }
}
