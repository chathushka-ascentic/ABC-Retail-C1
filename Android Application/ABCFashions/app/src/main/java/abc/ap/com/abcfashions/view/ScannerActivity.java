package abc.ap.com.abcfashions.view;


import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.zxing.Result;

import abc.ap.com.abcfashions.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by: Aparna Prasad
 */

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);

        mScannerView = new ZXingScannerView(ScannerActivity.this);
        LinearLayout scanner_layout = (LinearLayout) findViewById(R.id.activity_scan);

        mScannerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        scanner_layout.addView(mScannerView);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        if (rawResult != null)
        {
            String format = rawResult.getBarcodeFormat().toString();
            String result = rawResult.getText();
            //Log.i("barcode_format",format);
            //Log.i("barcode_result",result);

            if(mListener != null) {
                mListener.onSuccess(result);
                finish();
            }
        }

    }


    ///////////listener
    interface barCodeCapturedListener {
        void onSuccess(String result);
    }

    private static barCodeCapturedListener mListener = null;

    public static void registerListener(barCodeCapturedListener listener) {
        mListener = listener;
    }

    public static void unregisterListener() {
        mListener = null;
    }
}
