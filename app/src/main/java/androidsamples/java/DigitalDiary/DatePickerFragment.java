package androidsamples.java.DigitalDiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class DatePickerFragment extends DialogFragment {
  private Calendar calendar;
  private Button mEditDate;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    calendar = Calendar.getInstance();
    mEditDate = requireActivity().findViewById(R.id.btn_entry_date);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // TODO implement the method
    return new DatePickerDialog(requireContext(), (dp, y, m, d) -> {
      DayEditFragment.day = d;
      DayEditFragment.month = m;
      DayEditFragment.year = y;

      String str = d + "-" + (m+1) + "-" + y;
      mEditDate.setText(str);
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
  }
}
