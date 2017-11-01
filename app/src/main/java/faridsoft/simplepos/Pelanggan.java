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

import java.util.ArrayList;
import java.util.Locale;

public class Pelanggan extends AppCompatActivity implements AbsListView.OnScrollListener {
    DataHelper dbHelper;
    private ListView listView;
    String[] daftar;
    ListPelanggan adapter;
    private ProgressBar progressBar;
    private Handler mHandler;
    EditText editsearch;
    View footer;
    ArrayList<datasupplier> arraylist = new ArrayList<datasupplier>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Data Pelanggan");
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

        Cursor result = db.rawQuery("select * from t_pelanggan where c_pelanggan like '%"+nama+"%' order by c_pelanggan", null);
        //Toast.makeText(getApplicationContext(), "aw", Toast.LENGTH_LONG).show();
        //return;
        // result.getCount();
        daftar = new String[result.getCount()];
        arraylist.clear();
        int i=0;
        while(result.moveToNext()){

            daftar[i]=result.getString(result.getColumnIndex("c_idpelanggan"));

            datasupplier wp = new datasupplier(result.getString(result.getColumnIndex("c_idpelanggan")), result.getString(result.getColumnIndex("c_pelanggan")),
                    result.getString(result.getColumnIndex("c_alamat")), result.getString(result.getColumnIndex("c_telp")));
            // Binds all strings into an array
            arraylist.add(wp);

            i++;
        }

        listView = (ListView) findViewById(R.id.listview);
        listView.addFooterView(footer);
        adapter = new ListPelanggan(this,arraylist,20,10);
        listView.setAdapter(adapter);
        listView.setSelected(true);
        listView.setScrollContainer(false);
        listView.setTextFilterEnabled(true);
        listView.setOnScrollListener(this); //listen for a scroll movement to the bottom
        progressBar.setVisibility((20 < arraylist.size())? View.VISIBLE : View.GONE);



    }

    public void apdet(String kode){
        Intent intent = new Intent(this, inputPelanggan.class);
        intent.putExtra("idpel", kode);

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
