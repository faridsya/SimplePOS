package faridsoft.simplepos;

import android.widget.ImageView;

public class itemsesuai {
    int ids;
    private String kodebrg,tgl,namabrg,jum;
    private ImageView imageView;



    public itemsesuai(int ids, String kodebrg, String tgl, String namabrg,String jum) {
        this.ids = ids;
        this.kodebrg = kodebrg;
        this.tgl=tgl;
        this.namabrg=namabrg;
        this.jum=jum;


    }

    public int getid() {
        return this.ids;
    }
    public String getKodebrg() {
        return this.kodebrg;
    }
    public String getNamabrg() {
        return this.namabrg;
    }
    public String getTgl() {
        return this.tgl;
    }
    public String getJum() {
        return this.jum;
    }



}