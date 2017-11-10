package faridsoft.simplepos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class PengaturanJual extends AppCompatActivity {
    Button cmdsimpan,cmdsimpantop;
    RadioGroup radioGroupNb;
    RadioButton radioButtonNb;
    EditText txtpajak,txtdiskon,txttop;
    private static Locale myLocale;
    SharedPreferences sharedpreferences;
    final fungsi2 f=new fungsi2();
    RadioButton rb1,rb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengaturanjual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtpajak=(EditText) findViewById(R.id.txtpersenpajak);
        txtdiskon=(EditText) findViewById(R.id.txtpersendiskon);
        txttop=(EditText) findViewById(R.id.txttop);

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
        cmdsimpantop = (Button) findViewById(R.id.cmdsimpantop);
        sharedpreferences = getSharedPreferences("sesi", MODE_PRIVATE);


        rb1 = (RadioButton) findViewById(R.id.radiojual1);
         rb2 = (RadioButton) findViewById(R.id.radiojual2);
        cmdsimpan=(Button) findViewById(R.id.cmdsimpan);
        dapatselected();
        dapatkodeawal();
        cmdsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("persenpajak", txtpajak.getText().toString());
                editor.putString("persendiskon", txtdiskon.getText().toString());

                editor.commit();
                f.pesan(getApplicationContext(),getString(R.string.suksessimpan));

            }
        });
        cmdsimpantop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("top", txttop.getText().toString());


                editor.commit();
                f.pesan(getApplicationContext(),getString(R.string.suksessimpan));

            }
        });
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

            case R.id.radiojual1:
                if(checked) {
                    editor = sharedpreferences.edit();
                    editor.putString("jenisharga", "satu");
                    editor.commit();

                    rb1.setChecked(true);

                }
                break;

            case R.id.radiojual2:
                if(checked) {
                    editor = sharedpreferences.edit();
                    editor.putString("jenisharga", "dua");
                    editor.commit();

                    rb2.setChecked(true);
                }
                break;


        }
    }

    private void dapatselected(){
        String language = sharedpreferences.getString("jenisharga", "satu");
        if( language.matches("satu"))  rb1.setChecked(true); else rb2.setChecked(true);
}
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, halamanutama.class);


        setResult(18, intent);
        finish();
    }

    private void dapatkodeawal(){
        String pajak=sharedpreferences.getString("persenpajak","10");
        String diskon=sharedpreferences.getString("persendiskon","0.0");
        String top=sharedpreferences.getString("top","30");
       txtpajak.setText(pajak);txtdiskon.setText(diskon);txttop.setText(top);

    }

}
