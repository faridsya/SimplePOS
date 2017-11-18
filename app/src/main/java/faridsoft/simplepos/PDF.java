package faridsoft.simplepos;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
public class PDF extends AppCompatActivity {
    private static String FILE = Environment.getExternalStorageDirectory()+ File.separator+"Simpanpdf1.pdf";
    private static Font Font18bold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font Font12 = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addTitlePage(document);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        viewPdf();
    }

    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // menambah baris kosong
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Judul 1", Font18bold));

        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                "kalimat 1",
                Font12));
        preface.add(new Paragraph(
                "kalimat 2",
                Font12));
        preface.add(new Paragraph(
                "kalima 3",
                Font12));

        document.add(preface);
        // membuka halaman baru
        document.newPage();
        preface = new Paragraph();

        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Judul 2", Font18bold));

        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                "kalimat 1",
                Font12));
        preface.add(new Paragraph(
                "kalimat 2",
                Font12));
        preface.add(new Paragraph(
                "kalima 3",
                Font12));
        document.add(preface);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void viewPdf(){
        try {
            File myFile = new File(FILE);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void emailNote()
    {
        File myFile = new File(FILE);
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT,"judul");
        email.putExtra(Intent.EXTRA_TEXT,"isi");
        Uri uri = Uri.parse(myFile.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        startActivity(email);
    }
}
