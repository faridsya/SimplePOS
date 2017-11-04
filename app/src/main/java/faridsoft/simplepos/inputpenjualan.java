package faridsoft.simplepos;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class inputpenjualan extends AppCompatActivity {
    DataHelper dbHelper;
    Button cmdcus, ton2;
    boolean edit;
    EditText txtkode, text2,txtcari;
    private ListView listView;
    private String idpelanggan;
    private ArrayAdapter<String> adapter;
    TextView txtjudul;
    RelativeLayout kotak;
    SharedPreferences sharedpreferences;
    ImageView panah,panah2,cmdcari;
    String[] daftar;
    ValueAnimator mAnimator;
    ImageView oto;
    Button cmdtgl;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter,tanggal;
    private String tanggalsesuai,tanggalindo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

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
        getSupportActionBar().setTitle("");
        cmdcari=(ImageView) findViewById(R.id.cari);
        oto = (ImageView) findViewById(R.id.oto);
        txtkode = (EditText) findViewById(R.id.txtkode);
        kotak = (RelativeLayout) findViewById(R.id.kotaksimpan);
        panah=(ImageView) findViewById(R.id.panah);
        panah2=(ImageView) findViewById(R.id.panah2);
        panah.setVisibility(View.VISIBLE);
        cmdtgl = (Button) findViewById(R.id.cmdtgl);
        cmdcus = (Button) findViewById(R.id.cmdcus);
        Calendar c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tanggalsesuai = tanggal.format(c.getTime());
        tanggalindo=dateFormatter.format(c.getTime());
        cmdtgl.setText(tanggalindo);

        cmdtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        panah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                panah.setVisibility(View.GONE);panah2.setVisibility(View.VISIBLE);
                //kotak.setVisibility(View.GONE);
                tutup();
            }
        });
        panah2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                panah2.setVisibility(View.GONE);panah.setVisibility(View.VISIBLE);
                //kotak.setVisibility(View.VISIBLE);
                buka();
            }
        });
        oto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edit) return;
                txtkode.setText(no_oto());

            }
        });
        cmdcus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(inputpenjualan.this, caridata.class);
                intent.putExtra("pilihan", "pelanggan");
                startActivityForResult(intent, 1);
            }
        });

        kotak.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        kotak.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        //kotak.setVisibility(View.GONE);

                        final int widthSpec =     View.MeasureSpec.makeMeasureSpec(
                                0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec
                                .makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                        kotak.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0,
                                kotak.getMeasuredHeight());
                        return true;
                    }
                });
        cmdcari.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(inputpenjualan.this, Datacaribarang.class);
                intent.putExtra("form", "penjualan");
                startActivityForResult(intent, 1);
            }
        });


    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
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
                 */tanggalindo=dateFormatter.format(newDate.getTime());
                cmdtgl.setText(tanggalindo);
                tanggalsesuai=tanggal.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    private String no_oto(){
        int j,n;
        String No;
        No="trx"+ tanggalindo.substring(8,10)+tanggalindo.substring(3,5)+tanggalindo.substring(0,2);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("Select c_idpenjualan from t_penjualan where c_idpenjualan like '"+ No + "%' order by c_idpenjualan desc", null);
        if (result.getCount()==0) No=No+"001";
        else{
            result.moveToFirst();
            String kode = result.getString(result.getColumnIndex("c_idpenjualan"));
            String kode2 = kode.substring(8,11);

            j=Integer.valueOf(kode2);
            n=j+1;
            //No=kode2;
            No=No+String.format("%03d", n);
        }

        return No;
    }
    public void simpandata(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(!edit) {
            if (!cekvalidasi(txtkode.getText().toString(), text2.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.duplikasikode, Toast.LENGTH_LONG).show();
                return;
            }
            db.execSQL("insert into t_satuan values('" +
                    txtkode.getText().toString() + "','" +

                    text2.getText().toString() + "')");
            Toast.makeText(getApplicationContext(), R.string.suksessimpan, Toast.LENGTH_LONG).show();

            txtkode.setText("");text2.setText("");txtkode.requestFocus();
        }
        else {

            Cursor result = db.rawQuery("select * from t_satuan where c_kodesatuan!='"+txtkode.getText().toString()+"' and c_satuan='"+text2.getText().toString()+"'", null);
            if(result.getCount()>0) {
                Toast.makeText(getApplicationContext(), R.string.duplikasinama, Toast.LENGTH_LONG).show();return;
            }
            else {
                db.execSQL("update t_satuan set c_satuan='"+text2.getText().toString()+"' where c_kodesatuan='"+txtkode.getText().toString()+"'");
                Toast.makeText(getApplicationContext(), R.string.suksesubah, Toast.LENGTH_LONG).show();
                edit=false;
                txtkode.setEnabled(true);

                txtkode.setText("");text2.setText("");txtkode.requestFocus();
            }


        }
    }
    public void isibarang() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> datakode = new ArrayList<>();
        ArrayList<String> datanama = new ArrayList<>();
        //Toast.makeText(getApplicationContext(), "aw", Toast.LENGTH_LONG).show();
        //return;
        // result.getCount();
        //daftar = new String[result.getCount()];




        listView = (ListView) findViewById(R.id.list_satuan);
        adapter = new ListSatuan(this,R.layout.datasatuan,datakode,datanama);
        listView.setAdapter(adapter);
        listView.setSelected(true);
        listView.setTextFilterEnabled(true);


    }

    private boolean cekvalidasi(String kode,String nama){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from t_satuan where c_kodesatuan='"+kode+"' or c_satuan='"+nama+"'", null);
        if(result.getCount()>0) return false; else return true;
    }

    public void buka() {
        // set Visible
        kotak.setVisibility(View.VISIBLE);

        mAnimator.start();
    }

    private void tutup() {
        int finalHeight = kotak.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                // Height=0, but it set visibility to GONE
                kotak.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }
    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new     ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = kotak
                        .getLayoutParams();
                layoutParams.height = value;
                kotak.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==44){
            String value = (String) data.getExtras().getString("kodekat");
            String value2 = (String) data.getExtras().getString("namakat");
            idpelanggan=value;
            cmdcus.setText(value2);
        }
        else if(resultCode==22){

            String value = (String) data.getExtras().getString("kode", "");
            String value2 = (String) data.getExtras().getString("nama", "");
            String value3 = (String) data.getExtras().getString("stok", "");
            Toast.makeText(getApplicationContext(), value2, Toast.LENGTH_LONG).show();
            showChangeLangDialog();
        }
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogbarang, null);
        dialogBuilder.setView(dialogView);

        //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Input");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_atas, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
