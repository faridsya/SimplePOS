package faridsoft.simplepos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private SharedPreferences sharedpreferences;
    private boolean login;
    private final long startTime = 5 * 1000;
    private final long interval = 1 * 1000;
    public ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences("sesi", Context.MODE_PRIVATE);

        dapatversi();
        progres();


    }


    public void dapatversi(){
        String versionName = BuildConfig.VERSION_NAME;
        TextView code = (TextView) findViewById(R.id.txtcode);
        code.setText("Version "+versionName);
    }

    public void progres(){
        progressBar = (ProgressBar) findViewById(R.id.prog);
        progressBar.setMax(5000);
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 0, 5000);
        anim.setDuration(5000);
        progressBar.startAnimation(anim);

        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
        progressBar.setProgress(0);
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            login = sharedpreferences.getBoolean("login", false);
            if(!login) {
               // Intent i = new Intent(getApplicationContext(), activity_login.class);
                Intent i = new Intent(getApplicationContext(), halamanutama.class);
                startActivity(i);
                finish();
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            //progressStatus=maks-((int)millisUntilFinished)/50;
            // progressBar.setProgress(progressStatus);
            //Toast.makeText(getApplicationContext(), "Setting telah dipilih", Toast.LENGTH_SHORT).show();
        }
    }

}
