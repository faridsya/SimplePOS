package faridsoft.simplepos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListKategori extends ArrayAdapter<String> {

    private Context activity;
    private List<String>kode,nama;
    private ArrayList<String> arraykode,arraynama;

    public ListKategori(Context context, int resource, List<String> kode, List<String> nama) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.datakategori, parent, false);
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

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView kode,nama;


    }


}