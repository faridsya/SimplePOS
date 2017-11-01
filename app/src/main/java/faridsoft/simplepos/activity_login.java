package faridsoft.simplepos;

/**
 * Created by faridya on 9/29/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import java.util.ArrayList;

import static faridsoft.simplepos.DataHelper.md5;

public class activity_login extends AppCompatActivity {
    private SharedPreferences sharedpreferences;
    private ProgressDialog spinner;
    DataHelper dbcenter;
    Cursor cursor;
    SQLiteDatabase db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sharedpreferences = getSharedPreferences("sesi", Context.MODE_PRIVATE);
        dbcenter = new DataHelper(this);
        db = dbcenter.getReadableDatabase();

    }


    public void onklik(View v)
    {

        EditText txtuser = (EditText) findViewById(R.id.username);
        EditText txtpasword = (EditText) findViewById(R.id.password);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtpasword.getWindowToken(), 0);

        //String vnis=txtnis.getText().toString().replaceAll(" ","%20");
        String vuser=txtuser.getText().toString();
        String vpass=txtpasword.getText().toString();

        cursor = db.rawQuery("select * from t_pengguna where c_user='"+ vuser +"' and c_password='"+md5(vpass)+"'", null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            Intent i = new Intent(getApplicationContext(), halamanutama.class);
            startActivity(i);
            finish();
        }
        else
            Toast.makeText(this, "Gagal login", Toast.LENGTH_SHORT).show();



    }
}