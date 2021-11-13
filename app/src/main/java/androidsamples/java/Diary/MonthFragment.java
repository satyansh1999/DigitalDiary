package androidsamples.java.Diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

public class MonthFragment extends Fragment{
    private static final String TAG = "MonthFragment";
    private AppViewModel mAppViewModel;
    private GroupCallbacks mCallbacks = null;
    @SuppressLint("StaticFieldLeak")

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setHasOptionsMenu(true);
        mAppViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        FloatingActionButton fab = view.findViewById(R.id.btn_add_month);
        fab.setOnClickListener(this::addNewMonth);

        RecyclerView entriesList = view.findViewById(R.id.recyclerViewGroups);
        entriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        EntryListAdapter adapter = new EntryListAdapter(getActivity());
        entriesList.setAdapter(adapter);

        mAppViewModel.getAllGroups().observe(requireActivity(), adapter::setEntries);
        return view;
    }

    public void addNewMonth(View view) {
        mCallbacks.onMonthAdded(UUID.randomUUID(), false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (GroupCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    interface GroupCallbacks {
        void onMonthAdded(UUID id, boolean edit);
        void onMonthSelected(UUID id);
    }

    private class EntryViewHolder extends RecyclerView.ViewHolder {
        private DiaryEntry mEntry;
        private final TextView mTxtTitle;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtTitle = itemView.findViewById(R.id.txt_item_title_group);
            itemView.setOnClickListener(this::launchJournalEntryFragment);

            itemView.findViewById(R.id.edit_group).setOnClickListener( v ->{
                NavDirections action = MonthFragmentDirections.monthAddedAction(mEntry.getUid(),true);
                MainActivity.navController.navigate(action);
            });
        }

        private void launchJournalEntryFragment(View v) {
            mCallbacks.onMonthSelected(mEntry.getUid());
        }

        void bind(DiaryEntry entry) {
            mEntry = entry;
            this.mTxtTitle.setText(MainActivity.getMonthYear(mEntry.getMonth(),mEntry.getYear()));
        }
    }

    private class EntryListAdapter extends RecyclerView.Adapter<EntryViewHolder> {
        private final LayoutInflater mInflater;
        private List<DiaryEntry> mGroups;

        public EntryListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.fragment_entry_month, parent, false);
            return new EntryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
            if (mGroups != null) {
                DiaryEntry current = mGroups.get(position);
                holder.bind(current);
            }
        }

        @Override
        public int getItemCount() {
            return (mGroups == null) ? 0 : mGroups.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setEntries(List<DiaryEntry> entries) {
            mGroups = entries;
            notifyDataSetChanged();
        }
    }

}
