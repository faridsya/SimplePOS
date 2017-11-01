package faridsoft.simplepos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.id.list;

public class Listbarang extends BaseAdapter {
    // Declare Variables
    DataHelper dbHelper;
    private int count;
    private int stepNumber;
    private int startCount;
    Context mContext;
    LayoutInflater inflater;
    private List<itembarang> datatemenlist = null;
    private ArrayList<itembarang> arraylist;
    private int posisi;
    public Listbarang(Context context, List<itembarang> itembarang, int startCount, int stepNumber) {
        this.mContext = context;
        this.startCount = Math.min(startCount, itembarang.size()); //don't try to show more views than we have
        this.count = this.startCount;
        this.stepNumber = stepNumber;
        this.datatemenlist = itembarang;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<itembarang>();
        this.arraylist.addAll(datatemenlist);
    }
    public List<itembarang> getdaftar() {
        return this.datatemenlist;
    }
    public class ViewHolder {
        TextView imageTitle,deskripsi,harga,stok;
        ImageView image;

    }

    @Override
    public int getCount() {
        return datatemenlist.size();
    }

    @Override
    public itembarang getItem(int position) {
        return datatemenlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        final ListView lis;
        this.posisi=position;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.grid_item_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.imageTitle = (TextView) view.findViewById(R.id.txtkode);
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.deskripsi = (TextView) view.findViewById(R.id.txtnama);
            holder.stok  = (TextView) view.findViewById(R.id.txtstok);
            holder.harga = (TextView) view.findViewById(R.id.txtharga);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        itembarang item = datatemenlist.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.deskripsi.setText(item.getNama());
        holder.image.setImageBitmap(item.getImage());
        holder.stok.setText("Stok\u0009\u0009: "+item.getstok());
        holder.harga.setText("Harga\u0009: "+item.getharga());


        ImageView imageClick = (ImageView) view
                .findViewById(R.id.image);

        ImageView iconmenu = (ImageView) view
                .findViewById(R.id.iconmenu);

        try {


            imageClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    itembarang item = datatemenlist.get(position);

                    Intent intent = new Intent(mContext, detailgambar.class);
                    ImageView imageView = (ImageView) v.findViewById(R.id.image);

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("title", item.getNama()).
                            putExtra("image", item.getImage());
                    // Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                    //Start details activity
                    mContext.startActivity(intent);

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }




            iconmenu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    switch (v.getId()) {
                        case R.id.iconmenu:

                            PopupMenu popup = new PopupMenu(mContext, v);
                            popup.getMenuInflater().inflate(R.menu.menubarang,
                                    popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    final String kodekat = datatemenlist.get(position).getTitle();
                                    switch (item.getItemId()) {
                                        case R.id.detail:

                                            Intent ii=new Intent(mContext, halamandetilbarang.class);
                                            ii.putExtra("kode",datatemenlist.get(position).getTitle());
                                            ii.putExtra("image", datatemenlist.get(position).getImage());
                                            mContext.startActivity(ii);
                                            break;
                                        case R.id.update:

                                            ((Databarang)mContext).apdet(datatemenlist.get(position).getTitle());
                                            break;
                                        case R.id.hapus:
                                            new AlertDialog.Builder(mContext)
                                                    .setTitle(R.string.hapus)
                                                    .setMessage(R.string.yakin)
                                                    .setNegativeButton(android.R.string.no, null)
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                        public void onClick(DialogInterface arg0, int arg1) {
                                                            dbHelper = new DataHelper(mContext);
                                                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                                                            db.execSQL("delete from t_barang where c_kodebrg = '"+kodekat+"'");
                                                            Toast.makeText(mContext, R.string.sukses, Toast.LENGTH_LONG).show();
                                                            datatemenlist.remove(position);

                                                            if(mContext instanceof Databarang){
                                                                ((Databarang)mContext).gridAdapter.notifyDataSetChanged();
                                                                ((Databarang)mContext).gridView.startLayoutAnimation();
                                                            }


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


        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        datatemenlist.clear();
        if (charText.length() == 0) {
            datatemenlist.addAll(arraylist);
        }
        else
        {
            for (itembarang wp : arraylist)
            {
                if (wp.getNama().toLowerCase(Locale.getDefault()).contains(charText)||wp.getTitle().toLowerCase(Locale.getDefault()).matches(charText))
                {
                    datatemenlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean showMore(){
        if(count == datatemenlist.size()) {
            return true;
        }else{
            count = Math.min(count + stepNumber, datatemenlist.size()); //don't go past the end
            notifyDataSetChanged(); //the count size has changed, so notify the super of the change
            return endReached();
        }
    }

    /**
     * @return true if then entire data set is being displayed, false otherwise
     */
    public boolean endReached(){
        return count == datatemenlist.size();
    }

    /**
     * Sets the ListView back to its initial count number
     */
    public void reset(){
        count = startCount;
        notifyDataSetChanged();
    }

}