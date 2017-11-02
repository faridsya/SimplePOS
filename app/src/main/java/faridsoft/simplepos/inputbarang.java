package faridsoft.simplepos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class inputbarang extends AppCompatActivity {
    ImageView barcode;
    public EditText txtkode,txtnama,txtmodal,txtjual1,txtjual2,txtstokmin;
    DataHelper dbHelper;
    ImageView gmbbrg,oto;
    public boolean edit;
    SharedPreferences sharedpreferences;
    Button cmdkategori,cmdsatuan,cmdsup,cmdsimpan,cmddata,cmdbatal;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 6, SELECT_FILE = 7;
    private Uri picUri;
    String kodekat,kodestn,kodesup,filefoto;
    final int PIC_CROP = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputbarang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        edit=false;
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences("sesi", MODE_PRIVATE);
        barcode=(ImageView) findViewById(R.id.barcode);
        txtkode=(EditText) findViewById(R.id.txtkode);
        txtnama=(EditText) findViewById(R.id.txtnama);
        txtmodal=(EditText) findViewById(R.id.txtmodal);

        txtjual1=(EditText) findViewById(R.id.txtjual1);
        txtjual2=(EditText) findViewById(R.id.txtjual2);
        txtstokmin=(EditText) findViewById(R.id.txtstokmin);
        cmdkategori=(Button) findViewById(R.id.cmdkat);
        cmdsatuan=(Button) findViewById(R.id.cmdsatuan);
        cmdsup=(Button) findViewById(R.id.cmdsup);
        cmddata=(Button) findViewById(R.id.cmddata);
        cmdsimpan=(Button) findViewById(R.id.cmdsimpan);
        cmdbatal=(Button) findViewById(R.id.cmdbatal);
        barcode.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dbHelper = new DataHelper(this);
        getSupportActionBar().setTitle(R.string.inputbarang);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        oto = (ImageView) findViewById(R.id.oto);
        oto.setVisibility(View.VISIBLE);
        gmbbrg = (ImageView) findViewById(R.id.gmbbrg);

        gmbbrg.setImageResource(R.drawable.simplepos);


        gmbbrg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edit) return;
                Intent intent = new Intent(inputbarang.this, ScanActivity.class);
                intent.putExtra("form", "inputbarang");
                startActivityForResult(intent, 1);
            }
        });

        cmdkategori.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(inputbarang.this, caridata.class);
                intent.putExtra("pilihan", "kategori");
                startActivityForResult(intent, 1);
            }
        });

        cmdsatuan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(inputbarang.this, caridata.class);
                intent.putExtra("pilihan", "satuan");
                startActivityForResult(intent, 1);
            }
        });
        cmdsup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(inputbarang.this, caridata.class);
                intent.putExtra("pilihan", "supplier");
                startActivityForResult(intent, 1);
            }
        });


        cmdsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(txtkode.getText().toString().matches("")||txtnama.getText().toString().matches("")||txtmodal.getText().toString().matches("")
                        ||txtjual1.getText().toString().matches("")||txtjual2.getText().toString().matches("")||kodekat.matches("")||kodestn.matches("")){
                    Toast.makeText(getApplicationContext(), R.string.belumlengkap, Toast.LENGTH_LONG).show();
                    return;
                }
                simpandata();


            }
        });

        cmddata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(inputbarang.this, Databarang.class);
                //intent.putExtra("someData", "Here is some data");
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });
        oto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edit) return;
                txtkode.setText(no_oto());
                txtnama.requestFocus();
                //startActivity(intent);
            }
        });

        cmdbatal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                kosong();
            }
        });
    }

    private void dapatdata(String kode){

        txtkode.setText(kode);
        txtkode.setEnabled(false);
        edit=true;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select b.*,c_supplier,c_satuan,c_namakategori from t_barang b join t_kategori g on b.c_kodekategori=g.c_kode join t_satuan s on b.c_kodesatuan=s.c_kodesatuan left join t_supplier p on b.c_idsupplier=p.c_idsupplier where b.c_kodebrg='"+ kode +"' order by c_deskripsi", null);
        result.moveToFirst();
        txtnama.setText(result.getString(result.getColumnIndex("c_deskripsi")));
        kodekat=result.getString(result.getColumnIndex("c_kodekategori"));
        kodestn=result.getString(result.getColumnIndex("c_kodesatuan"));
        kodesup=result.getString(result.getColumnIndex("c_idsupplier"));
        cmdkategori.setText(result.getString(result.getColumnIndex("c_namakategori")));
        cmdsatuan.setText(result.getString(result.getColumnIndex("c_satuan")));
        String sup=result.getString(result.getColumnIndex("c_idsupplier")).equals("null") ? "Pilih" : result.getString(result.getColumnIndex("c_supplier"));
        cmdsup.setText(sup);
        txtjual1.setText(result.getString(result.getColumnIndex("c_hargajual1")));
        txtjual2.setText(result.getString(result.getColumnIndex("c_hargajual2")));
        txtmodal.setText(result.getString(result.getColumnIndex("c_hargabeli")));
        txtstokmin.setText(result.getString(result.getColumnIndex("c_stokmin")));
        filefoto=result.getString(result.getColumnIndex("c_gambar"));

        if(filefoto.equals("null")) {

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.simplepos);
            gmbbrg.setImageBitmap(bitmap);
        }
        else
        {
            byte[] imageAsBytes = Base64.decode(filefoto.getBytes(), Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            gmbbrg.setImageBitmap(bitmap);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if(resultCode==11){

                String value = (String) data.getExtras().getString("kodekat", "");
                String value2 = (String) data.getExtras().getString("namakat", "");
                this.kodekat = value;
                cmdkategori.setText(value2);
            }
            //Toast.makeText(getApplicationContext(),value, Toast.LENGTH_SHORT).show();

        else if (resultCode==12){
                String value = (String) data.getExtras().getString("kodekat");
                String value2 = (String) data.getExtras().getString("namakat");
                kodestn=value;
                cmdsatuan.setText(value2);
                //Toast.makeText(getApplicationContext(),value, Toast.LENGTH_SHORT).show();
            }
        else if (resultCode==13){
            String value = (String) data.getExtras().getString("kodekat");
            String value2 = (String) data.getExtras().getString("namakat");
            cmdsup.setText(value2);
            kodesup=value;
            //Toast.makeText(getApplicationContext(),value, Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK)
        {
            //onCaptureImageResult(data);
            picUri = data.getData();
            performCrop();
        }
        else if (requestCode == SELECT_FILE)
            onSelectFromGalleryResult(data);
        else if (requestCode == PIC_CROP)
        {
            Bundle extras2 = data.getExtras();
//get the cropped bitmap
            Bitmap thePic = extras2.getParcelable("data");
            Bitmap b = Bitmap.createScaledBitmap(thePic, 200,200, false);
            //simpangambar(b);
//retrieve a reference to the ImageView
            gmbbrg.setImageBitmap(b);
            filefoto = gmb(b);


        }

         else if (resultCode==17)
         {
             String value = (String) data.getExtras().getString("kodebrg");
             dapatdata(value);
         }

         else if (resultCode==25)
         {
             String value = (String) data.getExtras().getString("hasil");
             txtkode.setText(value);txtnama.requestFocus();
         }


    }


    private String no_oto(){
        int j,n,pjg;
        String No;

        String kodebrg=sharedpreferences.getString("awalkodebarang","brg");
        pjg=kodebrg.length();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("Select c_kodebrg from t_barang where c_kodebrg like '"+kodebrg+"%' order by c_kodebrg desc", null);
        if (result.getCount()==0) No=kodebrg+"0001";
        else{
            result.moveToFirst();
            String kode = result.getString(result.getColumnIndex("c_kodebrg"));
            String kode2 = kode.substring(pjg,(pjg+4));

            j=Integer.valueOf(kode2);
            n=j+1;
        //No=kode2;
            No=kodebrg+String.format("%04d", n);
        }

       return No;
    }
    private boolean cekvalidasi(String kode,String nama){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from t_barang where c_kodebrg='"+kode+"' or c_deskripsi='"+nama+"'", null);
        if(result.getCount()>0) return false; else return true;
    }
    public void simpandata(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(!edit) {
            if (!cekvalidasi(txtkode.getText().toString(), txtnama.getText().toString())) {
                Toast.makeText(getApplicationContext(), getString(R.string.duplikasikode), Toast.LENGTH_LONG).show();
                return;
            }
            db.execSQL("insert into t_barang values('" +
                    txtkode.getText().toString() + "','" +
                    txtnama.getText().toString() + "','" +
                   kodekat + "','" +
                    kodestn + "','" +
                    txtmodal.getText().toString() + "','" +
                    txtjual1.getText().toString() + "','" +
                    txtjual2.getText().toString() + "','" +
                    0 + "','" +
                    txtstokmin.getText().toString() + "','" +
                    kodesup + "','" +
                    filefoto + "')");
            Toast.makeText(getApplicationContext(), R.string.suksessimpan, Toast.LENGTH_LONG).show();

            kosong();
        }
        else {

            Cursor result = db.rawQuery("select * from t_barang where c_kodebrg!='"+txtkode.getText().toString()+"' and c_deskripsi='"+txtnama.getText().toString()+"'", null);
            if(result.getCount()>0) {
                Toast.makeText(getApplicationContext(), R.string.duplikasinama, Toast.LENGTH_LONG).show();return;
            }
            else {
                db.execSQL("update t_barang set c_deskripsi='"+txtnama.getText().toString()+"',c_kodekategori='"+kodekat+"',c_kodesatuan='"+kodestn+"'," +
                        "c_hargabeli='"+txtmodal.getText().toString()+"',c_hargajual1='"+txtjual1.getText().toString()+"',c_hargajual2='"+txtjual2.getText().toString()+"'" +
                        ",c_stokmin='"+txtstokmin.getText().toString()+"',c_idsupplier='"+kodesup+"',c_gambar='"+filefoto+"' where c_kodebrg='"+ txtkode.getText().toString()+"'");
                Toast.makeText(getApplicationContext(), R.string.suksesubah, Toast.LENGTH_LONG).show();
                kosong();
            }


        }
    }

    private void kosong(){
        txtkode.setText("");txtnama.setText("");txtmodal.setText("");txtjual1.setText("");
        txtjual2.setText("");txtstokmin.setText("");kodekat="";kodesup="";kodestn="";filefoto="";
        txtkode.setEnabled(true);txtkode.requestFocus();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.simplepos);
        gmbbrg.setImageBitmap(bitmap);
        cmdsup.setText(R.string.pilih);cmdkategori.setText(R.string.pilih);cmdsatuan.setText(R.string.pilih);
        edit=false;
    }
    private String gmb(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, baos); //bm is the bitmap object
        byte[] bit = baos.toByteArray();
        String encoded = Base64.encodeToString(bit,  Base64.DEFAULT);
        return  encoded;
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Bitmap b = Bitmap.createScaledBitmap(bm, 200, 200, false);
                //simpangambar(b);
                gmbbrg.setImageBitmap(b);
                filefoto = gmb(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    private void performCrop(){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Waduh,HPmu tidak mndukung crop gambar!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void selectImage() {
        final CharSequence[] items = { getString(R.string.kamera), getString(R.string.galeri),
                getString(R.string.batal) };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(inputbarang.this);

                if (items[item].equals(getString(R.string.kamera))) {
                    userChoosenTask =getString(R.string.kamera);
                    if(result)
                        cameraIntent();

                } else if (items[item].equals(getString(R.string.galeri))) {
                    userChoosenTask =getString(R.string.galeri);
                    if(result)
                        galleryIntent();

                } else if (items[item].equals(getString(R.string.batal))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(getString(R.string.kamera)))
                        cameraIntent();
                    else if(userChoosenTask.equals(getString(R.string.galeri)))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
