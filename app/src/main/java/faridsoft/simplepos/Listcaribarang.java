package faridsoft.simplepos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Listcaribarang extends BaseAdapter {
    // Declare Variables
    DataHelper dbHelper;
    private int count;
    private int stepNumber;
    private int startCount;
    Context mContext;
    LayoutInflater inflater;
    private List<itemcaribarang> datatemenlist = null;
    private ArrayList<itemcaribarang> arraylist;
    private int posisi;
    public Listcaribarang(Context context, List<itemcaribarang> itemcaribarang, int startCount, int stepNumber) {
        this.mContext = context;
        this.startCount = Math.min(startCount, itemcaribarang.size()); //don't try to show more views than we have
        this.count = this.startCount;
        this.stepNumber = stepNumber;
        this.datatemenlist = itemcaribarang;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<itemcaribarang>();
        this.arraylist.addAll(datatemenlist);
    }
    public List<itemcaribarang> getdaftar() {
        return this.datatemenlist;
    }
    public class ViewHolder {
        TextView imageTitle,deskripsi,harga,stok,harga2;
        ImageView image;


    }

    @Override
    public int getCount() {
        return datatemenlist.size();
    }

    @Override
    public itemcaribarang getItem(int position) {
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
            view = inflater.inflate(R.layout.caribarang, null);
            // Locate the TextViews in listview_item.xml
            holder.imageTitle = (TextView) view.findViewById(R.id.txtkode);
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.deskripsi = (TextView) view.findViewById(R.id.txtnama);
            holder.stok  = (TextView) view.findViewById(R.id.txtstok);
            holder.harga = (TextView) view.findViewById(R.id.txtharga);
            holder.harga2 = (TextView) view.findViewById(R.id.txtharga2);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        itemcaribarang item = datatemenlist.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.deskripsi.setText(item.getNama());
        holder.image.setImageBitmap(item.getImage());
        holder.stok.setText(item.getstok());
        holder.harga.setText("Harga1\u0009: "+item.getharga());
        holder.harga2.setText("Harga2\u0009: "+item.getharga2());


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
            for (itemcaribarang wp : arraylist)
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