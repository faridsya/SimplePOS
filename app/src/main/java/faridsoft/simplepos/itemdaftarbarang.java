package faridsoft.simplepos;

import android.widget.ImageView;

public class itemdaftarbarang {
    int ids;
    private String kodebrg,namabrg,jum;
    private String total;
    private ImageView imageView;



    public itemdaftarbarang(String kodebrg, String namabrg, String jum,String total) {

        this.kodebrg = kodebrg;
        this.namabrg=namabrg;
        this.jum=jum;
        this.total=total;


    }


    public String getKodebrg() {
        return this.kodebrg;
    }
    public String getNamabrg() {
        return this.namabrg;
    }

    public String getJum() {
        return this.jum;
    }
    public String getTotal() {
        return this.total;
    }



}