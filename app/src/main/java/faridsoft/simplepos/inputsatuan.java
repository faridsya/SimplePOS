package faridsoft.simplepos;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class inputsatuan extends AppCompatActivity {
    DataHelper dbHelper;
    Button ton1, ton2;
    boolean edit;
    EditText text1, text2,txtcari;
    private ListView listView;
    private String userChoosenTask;
    private ArrayAdapter<String> adapter;
    TextView txtjudul;
    RelativeLayout kotak;
    SharedPreferences sharedpreferences;
    ImageView panah,panah2;
    String[] daftar;
    ValueAnimator mAnimator;
    ImageView oto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputsatuan);

        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.txtkode);
        text2 = (EditText) findViewById(R.id.txtnama);
        edit=false;
        ton1 = (Button) findViewById(R.id.cmdsimpan);
        ton2 = (Button) findViewById(R.id.cmdbatal);
        kotak = (RelativeLayout) findViewById(R.id.kotaksimpan);
        oto = (ImageView) findViewById(R.id.oto);
        oto.setVisibility(View.VISIBLE);
        EditText inputSearch;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(R.string.inputsatuan);
        panah=(ImageView) findViewById(R.id.panah);
        panah2=(ImageView) findViewById(R.id.panah2);
        panah.setVisibility(View.VISIBLE);

        datakategori("");
        ton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(text1.getText().toString().matches("")||text2.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), R.string.belumlengkap, Toast.LENGTH_LONG).show();
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
                text1.setText(no_oto());
                text2.requestFocus();
                //startActivity(intent);
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
                inputsatuan.this.adapter.getFilter().filter(arg0);
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
        Cursor result = db.rawQuery("Select c_kodesatuan from t_satuan where c_kodesatuan like 'sat%' order by c_kodesatuan desc", null);
        if (result.getCount()==0) No="sat0001";
        else{
            result.moveToFirst();
            String kode = result.getString(result.getColumnIndex("c_kodesatuan"));
            String kode2 = kode.substring(3,7);

            j=Integer.valueOf(kode2);
            n=j+1;
            //No=kode2;
            No="sat"+String.format("%04d", n);
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
            db.execSQL("insert into t_satuan values('" +
                    text1.getText().toString() + "','" +

                    text2.getText().toString() + "')");
            Toast.makeText(getApplicationContext(), R.string.sukses, Toast.LENGTH_LONG).show();
            datakategori("");
            text1.setText("");text2.setText("");text1.requestFocus();
        }
        else {

            Cursor result = db.rawQuery("select * from t_satuan where c_kodesatuan!='"+text1.getText().toString()+"' and c_satuan='"+text2.getText().toString()+"'", null);
            if(result.getCount()>0) {
                Toast.makeText(getApplicationContext(), R.string.duplikasinama, Toast.LENGTH_LONG).show();return;
            }
            else {
                db.execSQL("update t_satuan set c_satuan='"+text2.getText().toString()+"' where c_kodesatuan='"+text1.getText().toString()+"'");
                Toast.makeText(getApplicationContext(), R.string.sukses, Toast.LENGTH_LONG).show();
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
        Cursor result = db.rawQuery("select * from t_satuan where c_satuan like '%"+nama+"%'"+" order by c_satuan", null);
        //Toast.makeText(getApplicationContext(), "aw", Toast.LENGTH_LONG).show();
        //return;
        // result.getCount();
        daftar = new String[result.getCount()];

        int i=0;
        while(result.moveToNext()){
            datakode.add(result.getString(result.getColumnIndex("c_kodesatuan")));
            datanama.add(result.getString(result.getColumnIndex("c_satuan")));
            daftar[i]=result.getString(result.getColumnIndex("c_kodesatuan"));


            i++;
        }

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
}
