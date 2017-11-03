package faridsoft.simplepos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class caridata extends AppCompatActivity implements AbsListView.OnScrollListener {
    DataHelper dbHelper;
    private ListView listView;
    String[] daftar;
    String[] daftar2;
    Listcaridata adapter;
    private ProgressBar progressBar;
    private Handler mHandler;
    private int jenis;
    EditText editsearch;
    View footer;
    ArrayList<datasupplier> arraylist = new ArrayList<datasupplier>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caridata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dbHelper = new DataHelper(this);
         footer = getLayoutInflater().inflate(R.layout.next, null);
        progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);


        String value = getIntent().getExtras().getString("pilihan");

        switch (value) {
            case "kategori":
                datakategori("");
                getSupportActionBar().setTitle(R.string.carikategori);
                jenis=11;
                break;
            case "satuan":
                datasatuan("");
                getSupportActionBar().setTitle(R.string.carisatuan);
                jenis=12;
                break;
            case "supplier":
                datasupplier("");
                getSupportActionBar().setTitle(R.string.carisupplier);
                jenis=13;
                break;
            case "pelanggan":
                datasupplier("");
                getSupportActionBar().setTitle(R.string.caripelanggan);
                jenis=44;
                break;

        }
        listView = (ListView) findViewById(R.id.listview);
        listView.addFooterView(footer);
        adapter = new Listcaridata(this,arraylist,20,10);
        listView.setAdapter(adapter);
        listView.setSelected(true);
        listView.setScrollContainer(false);
        listView.setTextFilterEnabled(true);
        listView.setOnScrollListener(this); //listen for a scroll movement to the bottom
        progressBar.setVisibility((20 < arraylist.size())? View.VISIBLE : View.GONE);

        listView.setOnItemClickListener(new klikkategori());




        mHandler = new Handler();

        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
                //datakategori(text);


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method studatakategori(arg0.toString());

            }
        });

    }


    public class klikkategori implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            //final String kodekat = daftar[position]; //.getItemAtPosition(arg2).toString();
            //final String namakat = daftar2[position]; //.getItemAtPosition(arg2).toString();


            TextView txtkode = (TextView) view.findViewById(R.id.txtkode);
            TextView txtnama = (TextView) view.findViewById(R.id.txtnama);

            String kodekat = txtkode.getText().toString();
            String namakat = txtnama.getText().toString();
            //Toast.makeText(getApplicationContext(), str + " is pressed " + position, Toast.LENGTH_SHORT).show();

           Intent intent = new Intent(getApplicationContext(), inputbarang.class);
            intent.putExtra("kodekat", kodekat);
            intent.putExtra("namakat", namakat);

            setResult(jenis, intent);
            finish();
            //Toast.makeText(getApplicationContext(),kodekat, Toast.LENGTH_SHORT).show();

        }
    }
    public void datasupplier(String nama) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor result = db.rawQuery("select * from t_supplier where c_supplier like '%"+nama+"%' order by c_supplier", null);
        //Toast.makeText(getApplicationContext(), "aw", Toast.LENGTH_LONG).show();
        //return;
        // result.getCount();
        daftar = new String[result.getCount()];
        arraylist.clear();
        int i=0;
        while(result.moveToNext()){

            daftar[i]=result.getString(result.getColumnIndex("c_idsupplier"));

            datasupplier wp = new datasupplier(result.getString(result.getColumnIndex("c_idsupplier")), result.getString(result.getColumnIndex("c_supplier")),
                    result.getString(result.getColumnIndex("c_alamat")), result.getString(result.getColumnIndex("c_telp")));
            // Binds all strings into an array
            arraylist.add(wp);

            i++;
        }

    }


    public void datapelanggan(String nama) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor result = db.rawQuery("select * from t_pelanggan where c_supplier like '%"+nama+"%' order by c_supplier", null);

        daftar = new String[result.getCount()];
        arraylist.clear();
        int i=0;
        while(result.moveToNext()){

            daftar[i]=result.getString(result.getColumnIndex("c_idsupplier"));

            datasupplier wp = new datasupplier(result.getString(result.getColumnIndex("c_idsupplier")), result.getString(result.getColumnIndex("c_supplier")),
                    result.getString(result.getColumnIndex("c_alamat")), result.getString(result.getColumnIndex("c_telp")));
            // Binds all strings into an array
            arraylist.add(wp);

            i++;
        }

    }


    public void onBackPressed() {

        setResult(Activity.RESULT_OK);
        finish();


    }
    public void datakategori(String nama) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor result = db.rawQuery("select * from t_kategori where c_namakategori like '%"+nama+"%' order by c_namakategori", null);
        //Toast.makeText(getApplicationContext(), "aw", Toast.LENGTH_LONG).show();
        //return;
        // result.getCount();


        arraylist.clear();
        int i=0;
        while(result.moveToNext()){


            datasupplier wp = new datasupplier(result.getString(result.getColumnIndex("c_kode")), result.getString(result.getColumnIndex("c_namakategori")),
                    "","");
            // Binds all strings into an array
            arraylist.add(wp);

            i++;
        }



    }

    public void datasatuan(String nama) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor result = db.rawQuery("select * from t_satuan where c_satuan like '%"+nama+"%' order by c_satuan", null);
        //Toast.makeText(getApplicationContext(), "aw", Toast.LENGTH_LONG).show();
        //return;
        // result.getCount();


        arraylist.clear();
        int i=0;
        while(result.moveToNext()){


            datasupplier wp = new datasupplier(result.getString(result.getColumnIndex("c_kodesatuan")), result.getString(result.getColumnIndex("c_satuan")),
                    "","");
            // Binds all strings into an array
            arraylist.add(wp);

            i++;
        }



    }


    public void apdet(String kode){
        Intent intent = new Intent(this, inputSupplier.class);
        intent.putExtra("idsup", kode);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem + visibleItemCount == totalItemCount && !adapter.endReached() && !hasCallback){ //check if we've reached the bottom
            mHandler.postDelayed(showMore, 300);
            hasCallback = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private boolean hasCallback;
    private Runnable showMore = new Runnable(){
        public void run(){
            boolean noMoreToShow = adapter.showMore(); //show more views and find out if
            progressBar.setVisibility(noMoreToShow? View.GONE : View.VISIBLE);
            hasCallback = false;
        }
    };
}
