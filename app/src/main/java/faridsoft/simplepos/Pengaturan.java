package faridsoft.simplepos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

import static faridsoft.simplepos.R.id.radiobarat;
import static faridsoft.simplepos.R.id.radioina;

public class Pengaturan extends AppCompatActivity {
    Button cmdsimpan;
    RadioGroup radioGroupNb;
    RadioButton radioButtonNb;

    private static Locale myLocale;
    SharedPreferences sharedpreferences;
    RadioButton rb1,rb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(R.string.pengaturan);
        cmdsimpan = (Button) findViewById(R.id.cmdsimpan);
        sharedpreferences = getSharedPreferences("bahasa", MODE_PRIVATE);


        rb1 = (RadioButton) findViewById(R.id.radioina);
         rb2 = (RadioButton) findViewById(R.id.radiobarat);
        dapatselected();

    }


    public void onRadioButtonClicked(View v)
    {
        //require to import the RadioButton class

        SharedPreferences.Editor editor;
        //is the current radio button now checked?
        boolean  checked = ((RadioButton) v).isChecked();

        //now check which radio button is selected
        //android switch statement
        switch(v.getId()){

            case R.id.radioina:
                if(checked) {
                    editor = sharedpreferences.edit();
                    editor.putString("bahasa", "in");
                    editor.commit();
                    fungsi2 f = new fungsi2();
                    f.changeLocale(Pengaturan.this, "in");
                    rb1.setChecked(true);

                }
                break;

            case R.id.radiobarat:
                if(checked) {
                    editor = sharedpreferences.edit();
                    editor.putString("bahasa", "en");
                    editor.commit();
                    fungsi2 g = new fungsi2();
                    g.changeLocale(Pengaturan.this, "en");
                    rb2.setChecked(true);
                }
                break;


        }
    }

    private void dapatselected(){
        sharedpreferences = getSharedPreferences("bahasa", MODE_PRIVATE);
        String language = sharedpreferences.getString("bahasa", "en");
     if( language.matches("en")||language.matches(""))  rb2.setChecked(true); else rb1.setChecked(true);
}
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, halamanutama.class);


        setResult(18, intent);
        finish();
    }

}
