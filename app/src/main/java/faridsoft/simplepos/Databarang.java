package faridsoft.simplepos;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;


public class Databarang extends AppCompatActivity {
    GridView gridView;
    Listbarang gridAdapter;
    DataHelper dbHelper;
    Bitmap bitmap;
    EditText editsearch;
    ImageView barcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.databarang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Data barang");
        dbHelper = new DataHelper(this);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new Listbarang(this, getData(),20,10);
        gridView.setClickable(false);
        gridView.setAdapter(gridAdapter);
        editsearch = (EditText) findViewById(R.id.search);
        barcode=(ImageView) findViewById(R.id.barcode);
        barcode.setVisibility(View.VISIBLE);
        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                gridAdapter.filter(text);
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

        barcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Databarang.this, ScanActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    private ArrayList<itembarang> getData() {

        final ArrayList<itembarang> imageItems = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor result = db.rawQuery("select * from t_barang order by c_deskripsi", null);
        while(result.moveToNext()){
            String poto=result.getString(result.getColumnIndex("c_gambar"));
            if(poto.equals("")||(poto==null)||poto.equals("null")) {
                bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.simplepos);
            }
            else
            {
                byte[] imageAsBytes = Base64.decode(poto.getBytes(), Base64.DEFAULT);
               bitmap=BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
               // bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.simplepos);
            }
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String harga = formatter.format(Double.parseDouble(result.getString(result.getColumnIndex("c_hargajual1"))));
            imageItems.add(new itembarang(bitmap, result.getString(result.getColumnIndex("c_kodebrg")),result.getString(result.getColumnIndex("c_deskripsi")), harga, result.getString(result.getColumnIndex("c_stok"))));
        }
        return imageItems;
    }

    public void apdet(String kode){
        Intent intent = new Intent(this, inputbarang.class);
        intent.putExtra("kodebrg", kode);

        setResult(17, intent);
        finish();
    }
    public void onBackPressed() {

        setResult(Activity.RESULT_OK);
        finish();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==25){
            String value = (String) data.getExtras().getString("hasil");
            editsearch.setText(value);
        }
    }
}
