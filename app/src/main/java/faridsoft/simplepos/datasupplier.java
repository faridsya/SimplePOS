package faridsoft.simplepos;

import android.widget.ImageView;

public class datasupplier {
    private String kode;
    private ImageView imageView;
    private String nama,alamat,telp;


    public datasupplier(String kode, String nama,String alamt,String telp) {
        this.kode = kode;
        this.nama = nama;
        this.alamat=alamt;
        this.telp=telp;


    }

    public String getkode() {
        return this.kode;
    }
    public String getnama() {
        return this.nama;
    }
    public String getalamat() {
        return this.alamat;
    }
    public String gettelp() {
        return this.telp;
    }



}