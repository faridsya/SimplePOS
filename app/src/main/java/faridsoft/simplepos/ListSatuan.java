package faridsoft.simplepos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListSatuan extends ArrayAdapter<String> {
    DataHelper dbHelper;
    private Context activity;
    private List<String>kode,nama;
    private ArrayList<String> arraykode,arraynama;
    private int posisi;

    public ListSatuan(Context context, int resource, List<String> kode, List<String> nama) {
        super(context, resource);
        this.activity = context;
        this.kode = kode;
        this.nama = nama;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return kode.size();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        this.posisi=position;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.datasatuan, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.kode=(TextView) convertView.findViewById(R.id.txtkode);

            holder.nama =(TextView) convertView.findViewById(R.id.txtnama);
            holder.imageView=(ImageView) convertView.findViewById(R.id.image_view);
            ;            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }


        holder.kode.setText(kode.get(position));
        holder.nama.setText(nama.get(position));

        //get first letter of each String item
        String firstLetter = String.valueOf(nama.get(position).charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(kode.get(position));
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px
        holder.imageView.setImageDrawable(drawable);

        //menu dot
        ImageView imageClick = (ImageView) convertView
                .findViewById(R.id.image_view2);
        try {


            imageClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {



                    switch (v.getId()) {
                        case R.id.image_view2:

                            PopupMenu popup = new PopupMenu(activity, v);
                            popup.getMenuInflater().inflate(R.menu.menusatuan,
                                    popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    final String kodekat = kode.get(position);
                                    switch (item.getItemId()) {
                                        case R.id.update:
                                            ImageView panah2 = (ImageView) ((inputsatuan)activity).findViewById(R.id.panah2);
                                            ImageView panah = (ImageView) ((inputsatuan)activity).findViewById(R.id.panah);
                                            RelativeLayout kotak=(RelativeLayout) ((inputsatuan)activity).findViewById(R.id.kotaksimpan);
                                            panah2.setVisibility(View.GONE);
                                            panah.setVisibility(View.VISIBLE);
                                            if(kotak.getVisibility()== View.GONE) ((inputsatuan)activity).buka();
                                            //kotak.setVisibility(View.VISIBLE);
                                            dbHelper = new DataHelper(activity);
                                            SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                                            Cursor result = db1.rawQuery("select * from t_satuan where c_kodesatuan = '"+kodekat+"'", null);
                                            result.moveToFirst();
                                            EditText text1=(EditText) ((inputsatuan)activity).findViewById(R.id.txtkode);
                                            EditText text2=(EditText) ((inputsatuan)activity).findViewById(R.id.txtnama);
                                            text1.setText(result.getString(result.getColumnIndex("c_kodesatuan")));
                                            text2.setText(result.getString(result.getColumnIndex("c_satuan")));
                                            text2.requestFocus();
                                            text1.setEnabled(false);
                                            ((inputsatuan)activity).edit=true;
                                            int pos = text2.getText().length();
                                            text2.setSelection(pos);

                                            //Toast.makeText(activity, "Update", Toast.LENGTH_LONG).show();

                                            break;
                                        case R.id.hapus:
                                            new AlertDialog.Builder(activity)
                                                    .setTitle(R.string.hapus)
                                                    .setMessage(R.string.yakin)
                                                    .setNegativeButton(android.R.string.no, null)
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                        public void onClick(DialogInterface arg0, int arg1) {
                                                            dbHelper = new DataHelper(activity);
                                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                            db.execSQL("delete from t_satuan where c_kodesatuan = '"+kodekat+"'");
                                                            Toast.makeText(activity, R.string.yakin, Toast.LENGTH_LONG).show();
                                                            if(activity instanceof inputsatuan){
                                                                ((inputsatuan)activity).datakategori("");
                                                            }
                                                            EditText txtcari = (EditText) ((inputsatuan)activity).findViewById(R.id.search);
                                                             txtcari.setText("");

                                                        }
                                                    }).create().show();


                                            break;

                                        default:
                                            break;
                                    }

                                    return true;
                                }
                            });

                            break;

                        default:
                            break;
                    }



                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView kode,nama;


    }


}