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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
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
    Listdaftarbarang adapter;
    TextView txttotal;
    RelativeLayout kotak;
    SharedPreferences sharedpreferences;
    ImageView panah,panah2,cmdcari,cmdbayar;
    String[] daftar;
    private CheckBox cekpajak,cekdiskon;
    ValueAnimator mAnimator;
    ImageView oto,barcode;
    Button cmdtgl;
    Double pph=0.0;
    Double totaljual=0.0;
    Double diskonjual=0.0;

    Double bayarnya=0.0;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter,tanggal;
    private String tanggaljual,tanggalindo;
    ArrayList<String> daftarkode = new ArrayList<>();
    ArrayList<String> daftarnama = new ArrayList<>();
    ArrayList<Double> daftarjumlah = new ArrayList<>();
    ArrayList<Double> daftarharga = new ArrayList<>();
    ArrayList<Double> daftartotal= new ArrayList<>();
    ArrayList<Double> daftarmodal= new ArrayList<>();
    ArrayList<Double> daftarstok= new ArrayList<>();
    ArrayList<itemdaftarbarang> arraylist = new ArrayList<itemdaftarbarang>();
    DecimalFormat formatter = new DecimalFormat("#,###,###");
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
        txttotal = (TextView) findViewById(R.id.txttotal);
        sharedpreferences = getSharedPreferences("sesi", MODE_PRIVATE);
        kotak = (RelativeLayout) findViewById(R.id.kotaksimpan);
        panah=(ImageView) findViewById(R.id.panah);
        panah2=(ImageView) findViewById(R.id.panah2);
        cmdbayar=(ImageView) findViewById(R.id.bayar);
        panah.setVisibility(View.VISIBLE);
        cmdtgl = (Button) findViewById(R.id.cmdtgl);
        cmdcus = (Button) findViewById(R.id.cmdcus);
        Calendar c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tanggaljual = tanggal.format(c.getTime());
        tanggalindo=dateFormatter.format(c.getTime());
        barcode=(ImageView) findViewById(R.id.barcode);
        cmdtgl.setText(tanggalindo);
        arraylist.clear();
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
        cmdbayar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //panah.setVisibility(View.GONE);panah2.setVisibility(View.VISIBLE);
                //kotak.setVisibility(View.GONE);
                if (daftartotal.isEmpty()) return;
                pembayaran(totalharga());
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

        barcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(inputpenjualan.this, ScanActivity.class);
                intent.putExtra("form", "penjualan");
                startActivityForResult(intent, 1);
            }
        });

    }


    public double totalharga(){
        int i;
        double sum = 0;
        for(i = 0; i <daftartotal.size(); i++)
            sum += daftartotal.get(i);
        return sum;
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
                tanggaljual=tanggal.format(newDate.getTime());
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
            String kode2 = kode.substring(9,12);
            Toast.makeText(getApplicationContext(), kode2, Toast.LENGTH_LONG).show();
            j=Integer.valueOf(kode2);
            n=j+1;
            //No=kode2;
            No=No+String.format("%03d", n);
        }

        return No;
    }
    public void simpandata(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String top=sharedpreferences.getString("top","30");
        String user="admin";
        if(!edit) {
            if (!cekvalidasi(txtkode.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.duplikasikode, Toast.LENGTH_LONG).show();
                return;
            }
            db.execSQL("insert into t_penjualan values('" +
                    txtkode.getText().toString() + "','" +
                    tanggaljual + "','" +
                    idpelanggan + "','" +
                    totalharga() + "','" +
                    pph + "','" +
                    diskonjual + "','" +
                    grantotal(totalharga(),pph,diskonjual) + "','" +
                    bayarnya + "','" +
                    Integer.parseInt(top)  + "','" +
                    user + "')");
            Toast.makeText(getApplicationContext(), R.string.suksessimpan, Toast.LENGTH_LONG).show();

            kosong();
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
    public void kosong() {

    txtkode.setText("");
    cmdcus.setText(R.string.pilih);
    cmdtgl.setText(R.string.pilih);
    daftarmodal.clear();
    daftarjumlah.clear();
    daftarstok.clear();
    daftarharga.clear();
    daftarnama.clear();
    daftartotal.clear();
    daftarkode.clear();
    arraylist.clear();
    adapter.notifyDataSetChanged();
    listView.setAdapter(null);
    txttotal.setText("");
    idpelanggan="";
    tanggaljual = tanggal.format(Calendar.getInstance().getTime());
    }

    private boolean cekvalidasi(String kode){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from t_penjualan where c_idpenjualan='"+kode+"'", null);
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
            String value3 = (String) data.getExtras().getString("stok", "0");
            String value4 = (String) data.getExtras().getString("harga", "0");
            String value5 = (String) data.getExtras().getString("harga2", "0");
            String value6 = (String) data.getExtras().getString("modal", "0");
           //Toast.makeText(getApplicationContext(), value4, Toast.LENGTH_LONG).show();

            detilbarang(value,value2,value3,value4,value5,value6);
        }
        else if(resultCode==25){
            String value = (String) data.getExtras().getString("hasil");
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor result = db.rawQuery("select * from t_barang where c_kodebrg='"+value+"'", null);
            if (result.getCount()==0) {
                Toast.makeText(getApplicationContext(), R.string.takterdaftar , Toast.LENGTH_LONG).show();return;
            }
            result.moveToFirst();
            detilbarang(result.getString(result.getColumnIndex("c_kodebrg")),result.getString(result.getColumnIndex("c_deskripsi")),result.getString(result.getColumnIndex("c_stok")),
                    result.getString(result.getColumnIndex("c_hargajual1")),result.getString(result.getColumnIndex("c_hargajual2")),result.getString(result.getColumnIndex("c_hargabeli")));
            //txtkode.setText(value);
        }
    }

    public void detilbarang(final String kode, final String nama, final String stok, final String harga, final String harga2, final String modal) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogbarang, null);
        dialogBuilder.setView(dialogView);

        final TextView txtnama = (TextView) dialogView.findViewById(R.id.txtbarang);
        final EditText txtharga = (EditText) dialogView.findViewById(R.id.txtharga);
        final TextView txtstok = (TextView) dialogView.findViewById(R.id.txtstok);
        final TextView txtjum = (TextView) dialogView.findViewById(R.id.txtjum);

        txtnama.setText(nama);
        String language = sharedpreferences.getString("jenisharga", "satu");
        if( language.matches("satu"))   txtharga.setText(String.valueOf(harga)); else  txtharga.setText(String.valueOf(harga2));;

        txtstok.setText(stok);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               //Toast.makeText(getApplicationContext(), modal, Toast.LENGTH_LONG).show();

                Double ttl= null;Double vharga=null;Double vjum=null;Double vmodal=null;
                try {
                    ttl = NumberFormat.getInstance(Locale.getDefault()).parse(txtjum.getText().toString()).doubleValue()* NumberFormat.getInstance(Locale.getDefault()).parse(txtharga.getText().toString()).doubleValue();
                    vharga =  NumberFormat.getInstance(Locale.getDefault()).parse(txtharga.getText().toString()).doubleValue();
                    vjum =  NumberFormat.getInstance(Locale.getDefault()).parse(txtjum.getText().toString()).doubleValue();
                    vmodal =  NumberFormat.getInstance(Locale.getDefault()).parse(modal).doubleValue();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(!daftarkode.contains(kode)) {
                    daftarkode.add(kode);
                    daftarnama.add(nama);
                    daftarjumlah.add(vjum);
                    daftarharga.add(vharga);
                    daftartotal.add(ttl);
                    daftarmodal.add(vmodal);
                    daftarstok.add(Double.parseDouble(stok));
                    String hargatotal = formatter.format(ttl);
                    itemdaftarbarang wp = new  itemdaftarbarang (kode, nama,txtjum.getText().toString() + " x " + txtharga.getText().toString(),hargatotal);

                    arraylist.add(wp);

                    listView = (ListView) findViewById(R.id.list_satuan);
                    // listView.addFooterView(footer);
                    adapter = new Listdaftarbarang(inputpenjualan.this,arraylist ,20,10);
                    listView.setAdapter(adapter);



                }
                else
                {
                    int i=daftarkode.indexOf(kode);
                    daftarjumlah.set(i,(daftarjumlah.get(i)+vjum));
                    daftartotal.set(i,daftarharga.get(i)*daftarjumlah.get(i));
                    itemdaftarbarang wp = new  itemdaftarbarang (daftarkode.get(i), nama,formatter.format(daftarjumlah.get(i)) + " x " +formatter.format(daftarharga.get(i)) ,formatter.format(daftartotal.get(i)));
                    arraylist.set(i,wp);
                   adapter.notifyDataSetChanged();
                }
                //Toast.makeText(getApplicationContext(), nama, Toast.LENGTH_LONG).show();
                txttotal.setText(formatter.format(totalharga())); ;
            }
        });
        dialogBuilder.setNegativeButton(R.string.batal, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    public void detilbarangapdet(final String kode,final String nama,String stok,final String harga,final String harga2,final int posisi) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogbarang, null);
        dialogBuilder.setView(dialogView);

        final TextView txtnama = (TextView) dialogView.findViewById(R.id.txtbarang);
        final EditText txtharga = (EditText) dialogView.findViewById(R.id.txtharga);
        final TextView txtstok = (TextView) dialogView.findViewById(R.id.txtstok);
        final TextView txtjum = (TextView) dialogView.findViewById(R.id.txtjum);

        txtnama.setText(nama);
        txtharga.setText(String.valueOf(harga));
        txtstok.setText(String.valueOf(stok));
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Toast.makeText(getApplicationContext(), value4, Toast.LENGTH_LONG).show();

                Double ttl= null;Double vharga=null;Double vjum=null;
                try {
                    ttl = NumberFormat.getInstance(Locale.getDefault()).parse(txtjum.getText().toString()).doubleValue()* NumberFormat.getInstance(Locale.getDefault()).parse(txtharga.getText().toString()).doubleValue();
                    vharga =  NumberFormat.getInstance(Locale.getDefault()).parse(txtharga.getText().toString()).doubleValue();
                    vjum =  NumberFormat.getInstance(Locale.getDefault()).parse(txtjum.getText().toString()).doubleValue();

                } catch (ParseException e) {
                    e.printStackTrace();
                }



                    daftarjumlah.set(posisi,(vjum));
                    daftartotal.set(posisi,ttl);
                    daftarjumlah.set(posisi,vharga);

                    itemdaftarbarang wp = new  itemdaftarbarang (daftarkode.get(posisi), nama,formatter.format(vjum) + " x " +formatter.format(vharga) ,formatter.format(daftartotal.get(posisi)));
                    arraylist.set(posisi,wp);
                    adapter.notifyDataSetChanged();
                txttotal.setText(formatter.format(totalharga())); ;
                //Toast.makeText(getApplicationContext(), nama, Toast.LENGTH_LONG).show();

            }
        });
        dialogBuilder.setNegativeButton(R.string.batal, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private double grantotal(Double total,Double pajak,Double diskon){
        Double gran;
        gran=total+pajak-diskon;
        this.totaljual=gran;
        return gran;
    }
    public void pembayaran(final Double total) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogbayar, null);
        dialogBuilder.setView(dialogView);

        final TextView txttotal = (TextView) dialogView.findViewById(R.id.txttotal);
        cekpajak = (CheckBox) dialogView.findViewById(R.id.t744);
        cekdiskon = (CheckBox) dialogView.findViewById(R.id.t742);

        final EditText txtpajak = (EditText) dialogView.findViewById(R.id.txtpajak);
        final EditText txtdiskon = (EditText) dialogView.findViewById(R.id.txtdiskon);
        final EditText txtbayar = (EditText) dialogView.findViewById(R.id.txtbayar);
        final TextView tgran = (TextView) dialogView.findViewById(R.id.tgran);
        txtpajak.setEnabled(false);
        txtdiskon.setEnabled(false);
        String hargatotal = formatter.format(total);
        txttotal.setText(hargatotal);

        txtpajak.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {


            }

            @Override
            public void afterTextChanged(Editable arg0) {

                String text = txtpajak.getText().toString().toLowerCase(Locale.getDefault());
                String text2 = txtdiskon.getText().toString().toLowerCase(Locale.getDefault());
                Double jumdiskon= 0.0;Double jumpajak=0.0;
                try {
                    jumpajak =  NumberFormat.getInstance(Locale.getDefault()).parse(text).doubleValue();
                    jumdiskon =  NumberFormat.getInstance(Locale.getDefault()).parse(text2).doubleValue();
                    //Toast.makeText(getApplicationContext(), String.valueOf(bayar), Toast.LENGTH_LONG).show();
                    //diskonjual= NumberFormat.getInstance(Locale.getDefault()).parse(txtdiskon.getText().toString()).doubleValue();;
                } catch (ParseException e) {
                    e.printStackTrace();
                }


               // Double jumpajak=text.matches("")||text.matches("-") ? 0 : Double.parseDouble(text);
                tgran.setText(formatter.format(grantotal(total,jumpajak,jumdiskon)));
                pph=jumpajak;

            }
        });

        txtdiskon.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {


            }

            @Override
            public void afterTextChanged(Editable arg0) {

                String text = txtdiskon.getText().toString().toLowerCase(Locale.getDefault());
                String text2 = txtpajak.getText().toString().toLowerCase(Locale.getDefault());
                Double jumdiskon= 0.0;Double jumpajak=0.0;
                try {
                    jumdiskon =  NumberFormat.getInstance(Locale.getDefault()).parse(text).doubleValue();
                    jumpajak =  NumberFormat.getInstance(Locale.getDefault()).parse(text2).doubleValue();
                    //Toast.makeText(getApplicationContext(), String.valueOf(bayar), Toast.LENGTH_LONG).show();
                    //diskonjual= NumberFormat.getInstance(Locale.getDefault()).parse(txtdiskon.getText().toString()).doubleValue();;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                diskonjual=jumdiskon;
                // Double jumpajak=text.matches("")||text.matches("-") ? 0 : Double.parseDouble(text);
                tgran.setText(formatter.format(grantotal(total,jumpajak,jumdiskon)));

            }
        });
        cekpajak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    txtpajak.setEnabled(true);
                    String persenpajak = sharedpreferences.getString("persenpajak", "10.0");
                    Double pajak=Double.parseDouble(persenpajak)*0.01*total;
                    txtpajak.setText(formatter.format(pajak));
                    pph=pajak;

                }
                else  {
                    txtpajak.setText(""); txtpajak.setEnabled(false);

                    pph=0.0;
                }

            }
        });
        cekdiskon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    txtdiskon.setEnabled(true);
                    String persendiskon = sharedpreferences.getString("persendiskon", "0.0");
                    Double diskon=Double.parseDouble(persendiskon)*0.01*total;
                    txtdiskon.setText(formatter.format(diskon));
                    tgran.setText(formatter.format(grantotal(total,pph,diskon)));
                    diskonjual=diskon;
                }
                else  {
                    txtdiskon.setText("");txtdiskon.setEnabled(false);
                    diskonjual=0.0;
                    tgran.setText(formatter.format(grantotal(total,pph,0.0)));
                }

            }
        });
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Toast.makeText(getApplicationContext(), value4, Toast.LENGTH_LONG).show();

                Double bayar= null;
                try {
                    bayar =  NumberFormat.getInstance(Locale.getDefault()).parse(txtbayar.getText().toString()).doubleValue();
                    //Toast.makeText(getApplicationContext(), String.valueOf(bayar), Toast.LENGTH_LONG).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), String.valueOf(bayar), Toast.LENGTH_LONG).show();

                bayarnya=bayar;
                simpandata();

            }
        });
        dialogBuilder.setNegativeButton(R.string.batal, new DialogInterface.OnClickListener() {
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
        inflater.inflate(R.menu.menu_jual, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.atur) {
            startActivity(new Intent(this, PengaturanJual.class));

        } else if (item.getItemId() == R.id.bantu) {
            Toast.makeText(this, "Bantu dong", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, bantuan.class));
        }

        return true;
    }
}
