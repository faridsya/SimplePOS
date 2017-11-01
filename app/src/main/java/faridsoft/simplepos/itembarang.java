package faridsoft.simplepos;

import android.graphics.Bitmap;

public class itembarang {
    private Bitmap image;
    private String title,nama,harga,stok;

    public itembarang(Bitmap image, String title,String nama,String harga,String stok) {
        super();
        this.image = image;
        this.title = title;
        this.nama=nama;
        this.harga=harga;
        this.stok=stok;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }
    public String getNama() {
        return nama;
    }
    public String getstok() {
        return stok;
    }
    public String getharga() {
        return harga;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
