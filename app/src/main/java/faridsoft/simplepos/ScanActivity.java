package faridsoft.simplepos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    String form;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            form= null;
        } else {
            form= extras.getString("form");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent intent;
        // Do something with the result here
       // Log.v("tag", rawResult.getText()); // Prints scan results
       // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        if(form=="inputsesuai") {
             intent = new Intent(getApplicationContext(), inputsesuai.class);
        }
        else if(form=="inputbarang") {
            intent = new Intent(getApplicationContext(), inputbarang.class);
        }
        else if(form=="penjualan") {
            intent = new Intent(getApplicationContext(), inputpenjualan.class);
        }
        else
            intent = new Intent(getApplicationContext(), inputbarang.class);

        intent.putExtra("hasil", rawResult.getText());

        setResult(25, intent);
        finish();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}