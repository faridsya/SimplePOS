package faridsoft.simplepos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.DatePicker;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static faridsoft.simplepos.R.string.sesuai;


public class inputsesuai extends AppCompatActivity {
    ImageView barcode,cari;
    public EditText txtkode,txtnama,txtjum,txtket;
    DataHelper dbHelper;
    Double stokskrg,jumsesuai;
    private DatePickerDialog datePickerDialog;
    public boolean edit;
    Button cmdtgl,cmdsimpan,cmddata,cmdbatal;
   int kodeedit;
    TextView txtstok;

    private SimpleDateFormat dateFormatter,tanggal;
    private String tanggalsesuai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputsesuai);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        edit=false;
        stokskrg=0.0;
        setSupportActionBar(toolbar);
        barcode=(ImageView) findViewById(R.id.barcode);
        cari=(ImageView) findViewById(R.id.cari);
        txtkode=(EditText) findViewById(R.id.txtkode);
        txtnama=(EditText) findViewById(R.id.txtnama);
        txtjum=(EditText) findViewById(R.id.txtjum);
        txtket=(EditText) findViewById(R.id.txtket);
        txtstok=(TextView) findViewById(R.id.txtstok);
        cmdtgl = (Button) findViewById(R.id.cmdtgl);
        cmddata=(Button) findViewById(R.id.cmddata);
        cmdsimpan=(Button) findViewById(R.id.cmdsimpan);
        cmdbatal=(Button) findViewById(R.id.cmdbatal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dbHelper = new DataHelper(this);
        getSupportActionBar().setTitle(R.string.sesuai);

        Calendar c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tanggalsesuai = tanggal.format(c.getTime());
        cmdtgl.setText(dateFormatter.format(c.getTime()));
        txtjum.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
               // txtstok.setText("");

                String text = txtjum.getText().toString().toLowerCase(Locale.getDefault());
                //if (text.matches("")) return;
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

                Double jum=text.matches("")||text.matches("-") ? 0 : Double.parseDouble(text);
                Double stokbaru=edit==true ? (stokskrg-jumsesuai+jum) :(stokskrg+jum);
                if(stokbaru<0){
                    Toast.makeText(getApplicationContext(), R.string.stokkurang, Toast.LENGTH_LONG).show();
                    txtjum.setText("");return;
                }
                String s=String.valueOf(stokbaru);
                s = s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");

               //txtstok.setText("");
                txtstok.setText(s);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        cari.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(inputsesuai.this, Datacaribarang.class);
                intent.putExtra("form", "sesuai");
                startActivityForResult(intent, 1);
            }
        });
        cmdtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });




        cmdsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(txtkode.getText().toString().matches("")||txtnama.getText().toString().matches("")||txtjum.getText().toString().matches("")||
                       tanggalsesuai.matches("")){
                    Toast.makeText(getApplicationContext(), "Data belum lengkap!", Toast.LENGTH_LONG).show();
                    return;
                }
                simpandata();


            }
        });

        cmddata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(inputsesuai.this, Datasesuai.class);
                //intent.putExtra("someData", "Here is some data");
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });


        cmdbatal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                kosong();
            }
        });
    }



    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                cmdtgl.setText(dateFormatter.format(newDate.getTime()));
                tanggalsesuai=tanggal.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }


    public void simpandata(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        if(txtjum.getText().toString().matches("0")||txtjum.getText().toString().matches("")|| txtjum.getText().toString().matches("")|| txtkode.getText().toString().matches("")||tanggalsesuai.matches(""))
        {

        }
        if(!edit) {

            db.execSQL("insert into t_sesuai(c_tanggal,c_kodebrg,c_jumlah,c_alasan) values('" +

                    tanggalsesuai + "','" +
                    txtkode.getText().toString() + "','" +
                    txtjum.getText().toString() + "','" +
                    txtket.getText().toString() + "')");
            Toast.makeText(getApplicationContext(), "Data berhasil disimpan!", Toast.LENGTH_LONG).show();

            kosong();
        }

            else {
            //db.execSQL("delete from t_sesuai where id='"+ kodeedit +"'");


            db.execSQL("update t_sesuai set c_jumlah='" +  txtjum.getText().toString() + "',c_alasan='" +txtket.getText().toString() + "',c_tanggal='" +tanggalsesuai +"' where id='" + kodeedit +"'");
                Toast.makeText(getApplicationContext(), "Data berhasil diubah!", Toast.LENGTH_LONG).show();
                kosong();



        }
    }
    private void dapatdata(int kode){

        kodeedit=kode;

        edit=true;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select s.*,c_deskripsi,c_stok,strftime('%d-%m-%Y', c_tanggal) tgl from t_sesuai s join t_barang b on s.c_kodebrg=b.c_kodebrg where id='"+ kode +"'", null);
        result.moveToFirst();
        txtstok.setText("0");
        jumsesuai=Double.parseDouble(result.getString(result.getColumnIndex("c_jumlah")));
        stokskrg= Double.parseDouble(result.getString(result.getColumnIndex("c_stok")));
        txtjum.setText(result.getString(result.getColumnIndex("c_jumlah")));
        txtstok.setText(result.getString(result.getColumnIndex("c_stok")));
        txtnama.setText(result.getString(result.getColumnIndex("c_deskripsi")));
        txtket.setText(result.getString(result.getColumnIndex("c_alasan")));
        txtkode.setText(result.getString(result.getColumnIndex("c_kodebrg")));
        tanggalsesuai=result.getString(result.getColumnIndex("c_tanggal"));
        cmdtgl.setText(result.getString(result.getColumnIndex("tgl")));

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if(resultCode==11){

                String value = (String) data.getExtras().getString("kodekat", "");
                String value2 = (String) data.getExtras().getString("namakat", "");

                txtkode.setText(value2);
            }
        else if(resultCode==22){

            String value = (String) data.getExtras().getString("kode", "");
            String value2 = (String) data.getExtras().getString("nama", "");
            String value3 = (String) data.getExtras().getString("stok", "");
            stokskrg=Double.parseDouble(value3);
            txtkode.setText(value);txtnama.setText(value2);txtstok.setText(value3);
            txtjum.requestFocus();
        }
        else if(resultCode==23){
             int value = (int) data.getExtras().getInt("id");
             dapatdata(value);
         }

         else if(resultCode==25){
             String value = (String) data.getExtras().getString("hasil");
             txtkode.setText(value);
         }

    }





    private void kosong(){
        txtkode.setText("");txtnama.setText("");txtjum.setText("");txtket.setText("");txtstok.setText("");
        edit=false;
    }





}
