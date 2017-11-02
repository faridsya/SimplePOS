package faridsoft.simplepos;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class inputKategori extends AppCompatActivity {
    DataHelper dbHelper;
    Button ton1, ton2;
    public boolean edit;
    EditText text1, text2,txtcari;
    private ListView listView;
    private String userChoosenTask;
    private ArrayAdapter<String> adapter;
    ValueAnimator mAnimator;
    RelativeLayout kotak;
    ImageView panah,panah2;
    String[] daftar;
    ImageView oto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_kategori);
        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.txtkode);
        text2 = (EditText) findViewById(R.id.txtnama);
        edit=false;
        ton1 = (Button) findViewById(R.id.cmdsimpan);
        ton2 = (Button) findViewById(R.id.cmdbatal);
        kotak = (RelativeLayout) findViewById(R.id.kotaksimpan);
        EditText inputSearch;
        oto = (ImageView) findViewById(R.id.oto);
        oto.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        panah=(ImageView) findViewById(R.id.panah);
        panah2=(ImageView) findViewById(R.id.panah2);
        panah.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(R.string.inputkategori);
        datakategori("");
        oto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edit) return;
                text1.setText(no_oto());
                text2.requestFocus();
                //startActivity(intent);
            }
        });
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
                text1.setText("");text2.setText("");text1.requestFocus(); edit=false;
                text1.setEnabled(true);
            }
        });
        panah.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                panah.setVisibility(View.GONE);panah2.setVisibility(View.VISIBLE);
        tutup();
            }
        });
        panah2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                panah2.setVisibility(View.GONE);panah.setVisibility(View.VISIBLE);
                buka();
            }
        });

        inputSearch = (EditText) findViewById(R.id.search);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                datakategori(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                inputKategori.this.adapter.getFilter().filter(arg0);
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
    }
    private String no_oto(){
        int j,n;
        String No;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("Select c_kode from t_kategori where c_kode like 'kat%' order by c_kode desc", null);
        if (result.getCount()==0) No="kat0001";
        else{
            result.moveToFirst();
            String kode = result.getString(result.getColumnIndex("c_kode"));
            String kode2 = kode.substring(3,7);

            j=Integer.valueOf(kode2);
            n=j+1;
            //No=kode2;
            No="kat"+String.format("%04d", n);
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
        db.execSQL("insert into t_kategori values('" +
                text1.getText().toString() + "','" +

                text2.getText().toString() + "')");
        Toast.makeText(getApplicationContext(), R.string.suksessimpan, Toast.LENGTH_LONG).show();
        datakategori("");
        text1.setText("");text2.setText("");text1.requestFocus();
    }
    else {

        Cursor result = db.rawQuery("select * from t_kategori where c_kode!='"+text1.getText().toString()+"' and c_namakategori='"+text2.getText().toString()+"'", null);
        if(result.getCount()>0) {
            Toast.makeText(getApplicationContext(), R.string.duplikasinama, Toast.LENGTH_LONG).show();return;
        }
        else {
            db.execSQL("update t_kategori set c_namakategori='"+text2.getText().toString()+"' where c_kode='"+text1.getText().toString()+"'");
            Toast.makeText(getApplicationContext(), R.string.suksesubah, Toast.LENGTH_LONG).show();
            edit=false;
            text1.setEnabled(true);
            datakategori("");
            text1.setText("");text2.setText("");text1.requestFocus();
        }


    }
}
    public void datakategori(String nama) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> datakode = new ArrayList<>();
        ArrayList<String> datanama = new ArrayList<>();
        Cursor result = db.rawQuery("select * from t_kategori where c_namakategori like '%"+nama+"%'"+" order by c_namakategori", null);

       // result.getCount();
        daftar = new String[result.getCount()];

        int i=0;
        while(result.moveToNext()){
            datakode.add(result.getString(result.getColumnIndex("c_kode")));
            datanama.add(result.getString(result.getColumnIndex("c_namakategori")));
            daftar[i]=result.getString(result.getColumnIndex("c_kode"));


            i++;
        }

        listView = (ListView) findViewById(R.id.list_kategori);
        adapter = new ListKategori(this,R.layout.datakategori,datakode,datanama);
        listView.setAdapter(adapter);
        listView.setSelected(true);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String kodekat = daftar[arg2]; //.getItemAtPosition(arg2).toString();

                final CharSequence[] dialogitem = { getString(R.string.ubah), getString(R.string.hapus)};
                AlertDialog.Builder builder = new AlertDialog.Builder(inputKategori.this);
                builder.setTitle(R.string.pilih);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item){

                            case 0 :
                                panah2.setVisibility(View.GONE);panah.setVisibility(View.VISIBLE);
                                if(kotak.getVisibility()== View.GONE) buka();
                                SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                                Cursor result = db1.rawQuery("select * from t_kategori where c_kode = '"+kodekat+"'", null);
                                result.moveToFirst();
                                text1.setText(result.getString(result.getColumnIndex("c_kode")));
                                text2.setText(result.getString(result.getColumnIndex("c_namakategori")));
                                text2.requestFocus();
                                text1.setEnabled(false);
                                edit=true;
                                int pos = text2.getText().length();
                                text2.setSelection(pos);
                                break;
                            case 1 :
                                new AlertDialog.Builder(inputKategori.this)
                                        .setTitle(R.string.hapus)
                                        .setMessage(R.string.yakin)
                                        .setNegativeButton(android.R.string.no, null)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                                fungsi2 f=new fungsi2();
                                                if (f.hapuskategori(kodekat,getApplicationContext())) {
                                                    f.pesan(getApplicationContext(),getString(R.string.invalidhapus));
                                                    return;
                                                }
                                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                db.execSQL("delete from t_kategori where c_kode = '"+kodekat+"'");
                                                Toast.makeText(getApplicationContext(), R.string.sukses, Toast.LENGTH_LONG).show();
                                                datakategori("");
                                                txtcari = (EditText) findViewById(R.id.search);
                                                txtcari.setText("");

                                            }
                                        }).create().show();

                                break;
                        }
                    }
                });
                builder.create().show();
            }});

    }

    private boolean cekvalidasi(String kode,String nama){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from t_kategori where c_kode='"+kode+"' or c_namakategori='"+nama+"'", null);
        if(result.getCount()>0) return false; else return true;
    }

    private void buka() {
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
}
