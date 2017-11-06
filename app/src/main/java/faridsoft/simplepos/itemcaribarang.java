package faridsoft.simplepos;

import android.graphics.Bitmap;

public class itemcaribarang {
    private Bitmap image;
    private String title,nama,harga,stok,harga2,modal;

    public itemcaribarang(Bitmap image, String title, String nama, String harga,String harga2, String stok,String modal) {
        super();
        this.image = image;
        this.title = title;
        this.nama=nama;
        this.harga=harga;
        this.harga2=harga2;
        this.stok=stok;
        this.modal=modal;
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
    public String getharga2() {
        return harga2;
    }
    public String getModal(){return modal;};
    public void setTitle(String title) {
        this.title = title;
    }
}
