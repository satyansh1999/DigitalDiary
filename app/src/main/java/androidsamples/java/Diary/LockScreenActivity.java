package androidsamples.java.Diary;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LockScreenActivity extends AppCompatActivity {
    private String str = "";
    private final String PASSWORD = "52463";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        Button bt1,bt2,bt3,bt4,bt5,bt6;
        bt1 = this.findViewById(R.id.lock_btn1);
        bt2 = this.findViewById(R.id.lock_btn2);
        bt3 = this.findViewById(R.id.lock_btn3);
        bt4 = this.findViewById(R.id.lock_btn4);
        bt5 = this.findViewById(R.id.lock_btn5);
        bt6 = this.findViewById(R.id.lock_btn6);

        bt1.setOnClickListener(v -> {
            str += "1";
        });
        bt2.setOnClickListener(v -> {
            str += "2";
        });
        bt3.setOnClickListener(v -> {
            str += "3";
        });
        bt4.setOnClickListener(v -> {
            str += "4";
        });
        bt5.setOnClickListener(v -> {
            str += "5";
        });
        bt6.setOnClickListener(v -> {
            str += "6";
        });
        this.findViewById(R.id.lock_check).setOnClickListener(v -> {
            if(str.equals(PASSWORD)){
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            else{
                count++;
                str = "";
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                if(count > 2) {
                    this.finish();
                    System.exit(0);
                }
            }
        });
    }
}