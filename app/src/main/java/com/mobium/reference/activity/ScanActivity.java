package com.mobium.reference.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 *  on 10.05.15.
 * http://mobiumapps.com/
 * Activity сканера штрихкодов
 */

public class ScanActivity extends Activity implements ZBarScannerView.ResultHandler {
    public static final String SCAN_RESULT = "SCAN_RESULT";


    private ZBarScannerView mScannerView;
    private ArrayList<Integer> mSelectedIndices;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);   // Programmatically initialize the scanner view
        setupFormats();
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(this);

    }


    public void setupFormats() {
        List<BarcodeFormat> formats = BarcodeFormat.ALL_FORMATS;
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent intent = getIntent();
        intent.putExtra(SCAN_RESULT, rawResult.getContents());
        setResult(RESULT_OK, intent);
        finish();
    }
}
