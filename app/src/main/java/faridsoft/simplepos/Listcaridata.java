package faridsoft.simplepos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Listcaridata extends BaseAdapter {
    // Declare Variables
    DataHelper dbHelper;
    private int count;
    private int stepNumber;
    private int startCount;
    Context mContext;
    LayoutInflater inflater;
    private List<datasupplier> datatemenlist = null;
    private ArrayList<datasupplier> arraylist;
    private int posisi;
    public Listcaridata(Context context, List<datasupplier> datasupplier, int startCount, int stepNumber) {
        this.mContext = context;
        this.startCount = Math.min(startCount, datasupplier.size()); //don't try to show more views than we have
        this.count = this.startCount;
        this.stepNumber = stepNumber;
        this.datatemenlist = datasupplier;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<datasupplier>();
        this.arraylist.addAll(datatemenlist);
    }
    public List<datasupplier> getdaftar() {
        return this.datatemenlist;
    }
    public class ViewHolder {
        TextView kode;ImageView imageView;
        TextView nama,alamat,telp;

    }

    @Override
    public int getCount() {
        return datatemenlist.size();
    }

    @Override
    public datasupplier getItem(int position) {
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
            view = inflater.inflate(R.layout.listcaridata, null);
            // Locate the TextViews in listview_item.xml
            holder.kode = (TextView) view.findViewById(R.id.txtkode);
            holder.nama = (TextView) view.findViewById(R.id.txtnama);
            holder.alamat = (TextView) view.findViewById(R.id.txtalamat);
            holder.telp = (TextView) view.findViewById(R.id.txttelp);
            holder.imageView=(ImageView) view.findViewById(R.id.image_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.kode.setText(datatemenlist.get(position).getkode());
        holder.nama.setText(datatemenlist.get(position).getnama());
        holder.alamat.setText(datatemenlist.get(position).getalamat());
        holder.telp.setText(datatemenlist.get(position).gettelp());

        String firstLetter = String.valueOf(datatemenlist.get(position).getnama().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(datatemenlist.get(position));
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px
        holder.imageView.setImageDrawable(drawable);

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
            for (datasupplier wp : arraylist)
            {
                if (wp.getnama().toLowerCase(Locale.getDefault()).contains(charText))
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