package faridsoft.simplepos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class inputPelanggan extends AppCompatActivity {
    DataHelper dbHelper;
    public boolean edit;
    EditText text1, text2,text3,text4,txtcari;
    private ListView listView;
    Button ton1, ton2,ton3;
    public static inputPelanggan ma;
    TextView txtjudul;
    ImageView oto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pelanggan);



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
        getSupportActionBar().setTitle(R.string.inputpelanggan);
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
                Intent intent = new Intent(inputPelanggan.this, Pelanggan.class);
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
        int j,n;
        String No;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("Select c_idpelanggan from t_pelanggan where c_idpelanggan like 'cus%' order by c_idpelanggan desc", null);
        if (result.getCount()==0) No="cus0001";
        else{
            result.moveToFirst();
            String kode = result.getString(result.getColumnIndex("c_idpelanggan"));
            String kode2 = kode.substring(3,7);

            j=Integer.valueOf(kode2);
            n=j+1;
            //No=kode2;
            No="cus"+String.format("%04d", n);
        }

        return No;
    }
    public void simpandata(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(!edit) {
            if (!cekvalidasi(text1.getText().toString(), text2.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Kode atau nama pelanggan sudah ada!", Toast.LENGTH_LONG).show();
                return;
            }
            db.execSQL("insert into t_pelanggan values('" +
                    text1.getText().toString() + "','" +
                    text2.getText().toString() + "','" +
                    text3.getText().toString() + "','" +

                    text4.getText().toString() + "',0)");
            Toast.makeText(getApplicationContext(), "Data berhasil disimpan!", Toast.LENGTH_LONG).show();

            text1.setText("");text2.setText("");text3.setText("");text4.setText("");text1.requestFocus();
        }
        else {

            Cursor result = db.rawQuery("select * from t_pelanggan where c_idpelanggan!='"+text1.getText().toString()+"' and c_pelanggan='"+text2.getText().toString()+"'", null);
            if(result.getCount()>0) {
                Toast.makeText(getApplicationContext(), "Nama pelanggan sudah ada!", Toast.LENGTH_LONG).show();return;
            }
            else {
                db.execSQL("update t_pelanggan set c_pelanggan='"+text2.getText().toString()+"',c_alamat='"+text3.getText().toString()+"',c_telp='"+text4.getText().toString()+"' where c_idpelanggan='"+text1.getText().toString()+"'");
                Toast.makeText(getApplicationContext(), "Data berhasil diubah!", Toast.LENGTH_LONG).show();

                //text1.setEnabled(true);

            }


        }
    }


    private void dapatdata(String kode){

        text1.setText(kode);
        text1.setEnabled(false);
        edit=true;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from t_pelanggan where c_idpelanggan= '"+kode+"'", null);
        result.moveToFirst();
        text1.setText(result.getString(result.getColumnIndex("c_idpelanggan")));
        text2.setText(result.getString(result.getColumnIndex("c_pelanggan")));
        text3.setText(result.getString(result.getColumnIndex("c_alamat")));
        text4.setText(result.getString(result.getColumnIndex("c_telp")));

    }
    private boolean cekvalidasi(String kode,String nama){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from t_pelanggan where c_idpelanggan='"+kode+"' or c_pelanggan='"+nama+"'", null);
        if(result.getCount()>0) return false; else return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            String value = (String) data.getExtras().getString("idpel");
            dapatdata(value);
        }
    }
}
