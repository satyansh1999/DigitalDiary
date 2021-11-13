package androidsamples.java.Diary;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.UUID;

public class MonthPicker extends Fragment {
    private Button jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec;
    private EditText mYear;
    private int year, month;
    private UUID entryId;
    private boolean edit;
    private DiaryEntry mEntry;
    private AppViewModel appViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        entryId = MonthPickerArgs.fromBundle(getArguments()).getId();
        edit = MonthPickerArgs.fromBundle(getArguments()).getEdit();
        appViewModel = new AppViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_picker, container, false);

        mYear = view.findViewById(R.id.month_picker_year);
        if(edit) {
            appViewModel.loadEntry(entryId);
        }
        year = Calendar.getInstance().get(Calendar.YEAR);
        mYear.setText(String.valueOf(year));

        jan = view.findViewById(R.id.btn_jan);
        feb = view.findViewById(R.id.btn_feb);
        mar = view.findViewById(R.id.btn_mar);
        apr = view.findViewById(R.id.btn_apr);
        may = view.findViewById(R.id.btn_may);
        jun = view.findViewById(R.id.btn_jun);
        jul = view.findViewById(R.id.btn_jul);
        aug = view.findViewById(R.id.btn_aug);
        sep = view.findViewById(R.id.btn_sep);
        oct = view.findViewById(R.id.btn_oct);
        nov = view.findViewById(R.id.btn_nov);
        dec = view.findViewById(R.id.btn_dec);

        jan.setOnClickListener(v -> {
            month = 0;
            btnClick();
        });
        feb.setOnClickListener(v -> {
            month = 1;
            btnClick();
        });
        mar.setOnClickListener(v -> {
            month = 2;
            btnClick();
        });
        apr.setOnClickListener(v -> {
            month = 3;
            btnClick();
        });
        may.setOnClickListener(v -> {
            month = 4;
            btnClick();
        });
        jun.setOnClickListener(v -> {
            month = 5;
            btnClick();
        });
        jul.setOnClickListener(v -> {
            month = 6;
            btnClick();
        });
        aug.setOnClickListener(v -> {
            month = 7;
            btnClick();
        });
        sep.setOnClickListener(v -> {
            month = 8;
            btnClick();
        });
        oct.setOnClickListener(v -> {
            month = 9;
            btnClick();
        });
        nov.setOnClickListener(v -> {
            month = 10;
            btnClick();
        });
        dec.setOnClickListener(v -> {
            month = 11;
            btnClick();
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
                    });
        }
    }

    private void btnClick(){
        if(!mYear.getText().toString().equals(""))
            year = Integer.parseInt(mYear.getText().toString());
        if(edit) {
            mEntry.setYear(year);
            mEntry.setMonth(month);
            appViewModel.update(mEntry);
        }
        else{
            mEntry = new DiaryEntry();
            mEntry.setYear(year);
            mEntry.setMonth(month);
            appViewModel.insert(mEntry);
        }
        requireActivity().onBackPressed();
    }
}
