package faridsoft.simplepos;

import android.widget.ImageView;

public class itempiutang {

    private String tgl,trx,tgljt,piutang,terbayar,nama;




    public itempiutang(String tgl, String trx, String tgljt, String piutang,String terbayar,String nama) {
        this.tgl = tgl;
        this.trx = trx;
        this.tgljt=tgljt;
        this.piutang=piutang;
        this.terbayar=terbayar;
        this.nama=nama;

    }

    public String getTgl() {
        return this.tgl;
    }
    public String getNama() {
        return this.nama;
    }
    public String getTrx() {
        return this.trx;
    }
    public String getTgljt() {
        return this.tgljt;
    }
    public String getPiutang() {
        return this.piutang;
    }
    public String getTerbayar() {
        return this.terbayar;
    }



}