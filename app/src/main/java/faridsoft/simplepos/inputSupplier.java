package faridsoft.simplepos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class inputSupplier extends AppCompatActivity {
    DataHelper dbHelper;
    public boolean edit;
    EditText text1, text2,text3,text4,txtcari;
    private ListView listView;
    Button ton1, ton2,ton3;
    public static inputSupplier ma;
    TextView txtjudul;
    ImageView oto;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_supplier);
        dbHelper = new DataHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Input Supplier");
        sharedpreferences = getSharedPreferences("sesi", MODE_PRIVATE);
        oto = (ImageView) findViewById(R.id.oto);
        oto.setVisibility(View.VISIBLE);
        ton1 = (Button) findViewById(R.id.cmdsimpan);
        ton2 = (Button) findViewById(R.id.cmdbatal);
        ton3 = (Button) findViewById(R.id.cmddata);
        text1 = (EditText) findViewById(R.id.txtkode);
        text2 = (EditText) findViewById(R.id.txtnama);
        text3 = (EditText) findViewById(R.id.txtalamat);
        text4 = (EditText) findViewById(R.id.txttelp);

         edit=false;


        ton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(text1.getText().toString().matches("")||text2.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Data belum lengkap!", Toast.LENGTH_LONG).show();
                    return;
                }
                simpandata();


            }
        });
        ton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                text1.setText("");text2.setText("");text3.setText("");text4.setText("");text1.requestFocus(); edit=false;
                text1.setEnabled(true);
            }
        });
        ton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //startActivity(new Intent(inputSupplier.this, Supplier.class));
                Intent intent = new Intent(inputSupplier.this, Supplier.class);
                //intent.putExtra("someData", "Here is some data");
                startActivityForResult(intent, 1);
            }
        });

        oto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edit) return;
                text1.setText(no_oto());
                text2.requestFocus();
                //startActivity(intent);
            }
        });
    }


    private String no_oto(){
        int j,n,pjg;
        String No;
        String kodebrg=sharedpreferences.getString("awalkodesup","sup");
        pjg=kodebrg.length();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("Select c_idsupplier from t_supplier where c_idsupplier like '"+ kodebrg + "%' order by c_idsupplier desc", null);
        if (result.getCount()==0) No=kodebrg+"0001";
        else{
            result.moveToFirst();
            String kode = result.getString(result.getColumnIndex("c_idsupplier"));
            String kode2 = kode.substring(pjg,(pjg+4));

            j=Integer.valueOf(kode2);
            n=j+1;
            //No=kode2;
            No=kodebrg+String.format("%04d", n);
        }

        return No;
    }
    public void simpandata(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(!edit) {
            if (!cekvalidasi(text1.getText().toString(), text2.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.duplikasikode, Toast.LENGTH_LONG).show();
                return;
            }
            db.execSQL("insert into t_supplier values('" +
                    text1.getText().toString() + "','" +
                    text2.getText().toString() + "','" +
                    text3.getText().toString() + "','" +

                    text4.getText().toString() + "',0)");
            Toast.makeText(getApplicationContext(), R.string.suksessimpan, Toast.LENGTH_LONG).show();

            text1.setText("");text2.setText("");text3.setText("");text4.setText("");text1.requestFocus();
        }
        else {

            Cursor result = db.rawQuery("select * from t_supplier where c_idsupplier!='"+text1.getText().toString()+"' and c_supplier='"+text2.getText().toString()+"'", null);
            if(result.getCount()>0) {
                Toast.makeText(getApplicationContext(), R.string.duplikasinama, Toast.LENGTH_LONG).show();return;
            }
            else {
                db.execSQL("update t_supplier set c_supplier='"+text2.getText().toString()+"',c_alamat='"+text3.getText().toString()+"',c_telp='"+text4.getText().toString()+"' where c_idsupplier='"+text1.getText().toString()+"'");
                Toast.makeText(getApplicationContext(), R.string.suksesubah, Toast.LENGTH_LONG).show();

                //text1.setEnabled(true);

            }


        }
    }


    private void dapatdata(String kode){

        text1.setText(kode);
        text1.setEnabled(false);
        edit=true;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from t_supplier where c_idsupplier= '"+kode+"'", null);
        result.moveToFirst();
        text1.setText(result.getString(result.getColumnIndex("c_idsupplier")));
        text2.setText(result.getString(result.getColumnIndex("c_supplier")));
        text3.setText(result.getString(result.getColumnIndex("c_alamat")));
        text4.setText(result.getString(result.getColumnIndex("c_telp")));

    }
    private boolean cekvalidasi(String kode,String nama){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from t_supplier where c_idsupplier='"+kode+"' or c_supplier='"+nama+"'", null);
        if(result.getCount()>0) return false; else return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            String value = (String) data.getExtras().getString("idsup");
            dapatdata(value);
        }
    }
}
