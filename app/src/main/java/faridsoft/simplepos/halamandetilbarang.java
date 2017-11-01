package faridsoft.simplepos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class halamandetilbarang extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    SQLiteDatabase myDB= null;
    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamandetilbarang);
        dbHelper = new DataHelper(this);
        Intent intent = getIntent();
        final String kode = intent.getStringExtra("kode");
       // final ImageView detilurl = intent.getStringExtra("iddetilheaderurl");
        Bitmap bitmap = getIntent().getParcelableExtra("image");
        ImageView pic = (ImageView) findViewById(R.id.pic);

        pic.setImageBitmap(bitmap);
        /*Picasso.with(this)
                .load()
                //.placeholder(R.drawable.progress)
                .into(pic);*/
        initToolbar();
        TextView judul=(TextView) findViewById(R.id.txtjudul);
        judul.setText(kode);
        coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinator);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.maincollapsing);
        collapsingToolbarLayout.setTitle(kode);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("select b.*,c_supplier,c_satuan,c_namakategori from t_barang b join t_kategori g on b.c_kodekategori=g.c_kode join t_satuan s on b.c_kodesatuan=s.c_kodesatuan left join t_supplier p on b.c_idsupplier=p.c_idsupplier where b.c_kodebrg='"+ kode +"' order by c_deskripsi", null);

        c.moveToFirst();
        String ket = c.getString(c.getColumnIndex("c_deskripsi"));
        judul.setText(ket);

        TextView txtkode=(TextView) findViewById(R.id.txtkode);
        txtkode.setText("Kode\u0009\u0009\u0009\u0009\u0009:\u0009 "+kode);

        TextView txtkat=(TextView) findViewById(R.id.txtkat);
        txtkat.setText("Kategori\u0009\u0009\u0009\u0009:\u0009 "+c.getString(c.getColumnIndex("c_namakategori")));

        TextView txtstn=(TextView) findViewById(R.id.txtstn);
        txtstn.setText("Satuan\u0009\u0009\u0009\u0009:\u0009 "+c.getString(c.getColumnIndex("c_satuan")));
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String harga = formatter.format(Double.parseDouble(c.getString(c.getColumnIndex("c_hargabeli"))));


        TextView txtmodal=(TextView) findViewById(R.id.txtmodal);
        txtmodal.setText("Harga beli\u0009\u0009\u0009:\u0009 "+ harga);
        String hargajual = formatter.format(Double.parseDouble(c.getString(c.getColumnIndex("c_hargajual1"))));
        TextView txtjual1=(TextView) findViewById(R.id.txtjual1);
        txtjual1.setText("Harga jual1\u0009\u0009:\u0009 "+hargajual);

        String hargajual2 = formatter.format(Double.parseDouble(c.getString(c.getColumnIndex("c_hargajual2"))));
        TextView txtjual2=(TextView) findViewById(R.id.txtjual2);
        txtjual2.setText("Harga jual2\u0009\u0009:\u0009 "+hargajual2);

        TextView txtstok=(TextView) findViewById(R.id.txtstok);
        txtstok.setText("Stok\u0009\u0009\u0009\u0009\u0009:\u0009 "+c.getString(c.getColumnIndex("c_stok")));

        TextView txtstokmin=(TextView) findViewById(R.id.txtstokmin);
        txtstokmin.setText("Stok minimal\u0009:\u0009 "+c.getString(c.getColumnIndex("c_stokmin")));
        TextView txtsup=(TextView) findViewById(R.id.txtsup);
        txtsup.setText("Supplier\u0009\u0009\u0009\u0009:\u0009 "+c.getString(c.getColumnIndex("c_supplier")));

    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
