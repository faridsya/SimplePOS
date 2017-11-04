package faridsoft.simplepos;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;


public class Datacaribarang extends AppCompatActivity {
    GridView gridView;
    Listcaribarang gridAdapter;
    DataHelper dbHelper;
    Bitmap bitmap;
    EditText editsearch;
    ImageView barcode;
    String form;
    TextView txtkode,txtnama,txtstok;
    String kodekat,namakat,stok;
    private int jenis;
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
        gridAdapter = new Listcaribarang(this, getData(),20,10);
        gridView.setClickable(false);
        gridView.setAdapter(gridAdapter);
        editsearch = (EditText) findViewById(R.id.search);
        barcode=(ImageView) findViewById(R.id.barcode);
        barcode.setVisibility(View.VISIBLE);


        form = getIntent().getExtras().getString("form");
        gridView.setOnItemClickListener(new klikbarang());


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
                Intent intent = new Intent(Datacaribarang.this, ScanActivity.class);
                startActivityForResult(intent, 1);
            }
        });



    }

    public class klikbarang implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            //final String kodekat = daftar[position]; //.getItemAtPosition(arg2).toString();
            //final String namakat = daftar2[position]; //.getItemAtPosition(arg2).toString();


           txtkode = (TextView) view.findViewById(R.id.txtkode);
            txtnama = (TextView) view.findViewById(R.id.txtnama);
             txtstok = (TextView) view.findViewById(R.id.txtstok);

            kodekat = txtkode.getText().toString();
             namakat = txtnama.getText().toString();
             stok = txtstok.getText().toString();
            //Toast.makeText(getApplicationContext(), str + " is pressed " + position, Toast.LENGTH_SHORT).show();


            form = getIntent().getExtras().getString("form");

            switch (form) {
                case "sesuai":
                    bukasesuai();
                    break;
                case "penjualan":
                    bukapenjualan();
                    break;
            }

            //Toast.makeText(getApplicationContext(),kodekat, Toast.LENGTH_SHORT).show();

        }
    }

    private void bukasesuai(){
        Intent intent = new Intent(getApplicationContext(), inputsesuai.class);
        intent.putExtra("kode", kodekat);
        intent.putExtra("nama", namakat);
        intent.putExtra("stok", stok);
        setResult(22, intent);
        finish();
    }
    private void bukapenjualan(){
        Intent intent = new Intent(getApplicationContext(), inputpenjualan.class);
        intent.putExtra("kode", kodekat);
        intent.putExtra("nama", namakat);
        intent.putExtra("stok", stok);
        setResult(22, intent);
        finish();
    }
    private ArrayList<itemcaribarang> getData() {

        final ArrayList<itemcaribarang> imageItems = new ArrayList<>();

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
            String harga2 = formatter.format(Double.parseDouble(result.getString(result.getColumnIndex("c_hargajual2"))));
            imageItems.add(new itemcaribarang(bitmap, result.getString(result.getColumnIndex("c_kodebrg")),result.getString(result.getColumnIndex("c_deskripsi")), harga,harga2, result.getString(result.getColumnIndex("c_stok"))));
        }
        return imageItems;
    }

    public void apdet(String kode){
        Intent intent = new Intent(this, inputSupplier.class);
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
