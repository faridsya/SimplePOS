package faridsoft.simplepos;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.format;

public class Datasesuai extends AppCompatActivity implements AbsListView.OnScrollListener {
    DataHelper dbHelper;
    ListView listView;
    String[] daftar;
    Listsesuai adapter;
    private ProgressBar progressBar;
    private Handler mHandler;
    EditText editsearch;
    View footer;
    ArrayList<itemsesuai> arraylist = new ArrayList<itemsesuai>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datasesuai);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Data Penyesuaian");
        dbHelper = new DataHelper(this);
         footer = getLayoutInflater().inflate(R.layout.next, null);
        progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
        datasupplier("");
        mHandler = new Handler();

        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
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

    }

    public void datasupplier(String nama) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor result = db.rawQuery("select s.*,c_deskripsi,strftime('%d-%m-%Y', c_tanggal) tgl from t_sesuai s join t_barang b on s.c_kodebrg=b.c_kodebrg where c_deskripsi like '%"+nama+"%' order by c_tanggal desc,id desc", null);
        //Toast.makeText(getApplicationContext(), "aw", Toast.LENGTH_LONG).show();
        //return;
        // result.getCount();
        daftar = new String[result.getCount()];
        arraylist.clear();
        int i=0;
        while(result.moveToNext()){
            String tgl;


            itemsesuai wp = new  itemsesuai (result.getInt(result.getColumnIndex("id")), result.getString(result.getColumnIndex("c_kodebrg")),
                    result.getString(result.getColumnIndex("tgl")), result.getString(result.getColumnIndex("c_deskripsi")),result.getString(result.getColumnIndex("c_jumlah")));
            // Binds all strings into an array
            arraylist.add(wp);

            i++;
        }

        listView = (ListView) findViewById(R.id.listview);
        listView.addFooterView(footer);
        adapter = new Listsesuai(this,arraylist,20,10);
        listView.setAdapter(adapter);
        listView.setSelected(true);
        listView.setScrollContainer(false);
        listView.setTextFilterEnabled(true);
        listView.setOnScrollListener(this); //listen for a scroll movement to the bottom
        progressBar.setVisibility((20 < arraylist.size())? View.VISIBLE : View.GONE);



    }

    public void apdet(int kode){
        Intent intent = new Intent(this, inputsesuai.class);
        intent.putExtra("id", kode);

        setResult(23, intent);
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
