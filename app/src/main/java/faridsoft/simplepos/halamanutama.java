package faridsoft.simplepos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class halamanutama extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private boolean menutst = false;
    private Toolbar toolbar;
    ImageView fotoku;
    private Uri picUri;
    SharedPreferences sharedpreferences;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    final int PIC_CROP = 2;
    View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_halamanutama);
        sharedpreferences = getSharedPreferences("sesi", MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menusamping();
        loadlogo();


    }

    public void loadlogo(){
        fotoku = (ImageView)header.findViewById(R.id.logotoko);
        String poto=sharedpreferences.getString("fotoku","");
        if(poto.equals("")) fotoku.setImageResource(R.drawable.simplepos);
        else loadgambar(poto);

        fotoku.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void loadgambar(String f){

        byte[] imageAsBytes = Base64.decode(f.getBytes(), Base64.DEFAULT);

        fotoku.setImageBitmap(getCircleBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));
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


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(halamanutama.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(halamanutama.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
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
    public void menusamping(){
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        header=navigationView.getHeaderView(0);
        TextView name=(TextView)header.findViewById(R.id.idtxtnama);

        name.setText("Nama toko");

        //Mengatur Navigasi View Item yang akan dipanggil untuk menangani item klik menu navigasi
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Memeriksa apakah item tersebut dalam keadaan dicek  atau tidak,
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Menutup  drawer item klik
                //drawerLayout.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()) {
                    // pilihan menu item navigasi akan menampilkan pesan toast klik kalian bisa menggantinya
                    //dengan intent activity
                    case R.id.navigation1:
                        Toast.makeText(getApplicationContext(), "Beranda Telah Dipilih", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation2:
                        navigationView.getMenu().findItem(R.id.mnbrang).setVisible(!menutst);
                        navigationView.getMenu().findItem(R.id.mnsatuan).setVisible(!menutst);
                        navigationView.getMenu().findItem(R.id.mnkategori).setVisible(!menutst);
                        navigationView.getMenu().findItem(R.id.mnsuplier).setVisible(!menutst);
                        navigationView.getMenu().findItem(R.id.mnpelanggan).setVisible(!menutst);
                        navigationView.getMenu().findItem(R.id.mnsesuai).setVisible(!menutst);
                        if(menutst)
                        {

                            ImageView imgFp = (ImageView) findViewById(R.id.panahtst);
                            imgFp.setImageResource(R.drawable.ic_panahbawah);
                            menutst=false;
                        }
                        else {

                            ImageView imgFp = (ImageView) findViewById(R.id.panahtst);
                            imgFp.setImageResource(R.drawable.ic_panahatas);
                            menutst=true;
                        }


                        return true;
                    case R.id.mnkategori:
                       startActivity(new Intent(halamanutama.this, inputKategori.class));
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.mnsuplier:
                        Intent ii=new Intent(halamanutama.this, inputSupplier.class);
                        startActivity(ii);
                        //startActivity(new Intent(halamanutama.this, inputSupplier.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.mnsesuai:
                        startActivity(new Intent(halamanutama.this, inputsesuai.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.mnpenjualan:
                        startActivity(new Intent(halamanutama.this, inputpenjualan.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.mnsatuan:
                        startActivity(new Intent(halamanutama.this, inputsatuan.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.mnbrang:
                        startActivity(new Intent(halamanutama.this, inputbarang.class));
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.mnpelanggan:
                        startActivity(new Intent(halamanutama.this, inputPelanggan.class));
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.navigation4:
                        //startActivity(new Intent(halamanutama.this, Pengaturan.class));
                        Intent intent = new Intent(halamanutama.this, Pengaturan.class);
                        //intent.putExtra("someData", "Here is some data");
                        startActivityForResult(intent, 1);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.navigation5:
                        Toast.makeText(getApplicationContext(), "About telah dipilih", Toast.LENGTH_SHORT).show();

                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Kesalahan Terjadi ", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Kode di sini akan merespons setelah drawer menutup disini kita biarkan kosong
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //  Kode di sini akan merespons setelah drawer terbuka disini kita biarkan kosong
                super.onDrawerOpened(drawerView);
            }
        };
        //Mensetting actionbarToggle untuk drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //memanggil synstate
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_atas, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
            {
                //onCaptureImageResult(data);
                picUri = data.getData();
                performCrop();
            }
            else
            {
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                Bitmap b = Bitmap.createScaledBitmap(thePic, 170, 140, false);
                simpangambar(b);
//retrieve a reference to the ImageView
                fotoku.setImageBitmap(getCircleBitmap(b));
//display the returned cropped image

            }

        }
        else if(resultCode==18){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap b = Bitmap.createScaledBitmap(bm, 170, 140, false);
        simpangambar(b);
        fotoku.setImageBitmap(getCircleBitmap(b));


    }
    public void simpangambar(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b,  Base64.DEFAULT);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("fotoku", encoded);
        editor.commit();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.atur) {
            Toast.makeText(this, "Atur dong", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.out) {
            Toast.makeText(this, "Out dong", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.bantu) {
           // startActivity(new Intent(this, bantuan.class));
        }

        return true;
    }
}
