package com.malcoo.malcotask1.Utils;

import android.annotation.SuppressLint;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.common.InputImage;

@SuppressLint("UnsafeOptInUsageError")

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {

    BarcodeScanner scanner;
    OnBarcodeScannedListener onBarcodeScannedListener;
    private static final String TAG = "BarcodeAnalyzer";

    public BarcodeAnalyzer(BarcodeScanner scanner, OnBarcodeScannedListener onBarcodeScannedListener) {
        this.scanner = scanner;
        this.onBarcodeScannedListener = onBarcodeScannedListener;
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        if (!barcodes.isEmpty()){
                            String value=barcodes.get(0).getRawValue();
                            if (value!=null&&!value.equals(CameraUtil.lastValue)){
                                onBarcodeScannedListener.oBarcodeScanned(barcodes.get(0));
                                CameraUtil.lastValue=value;
                            }
                        }
                    })
                    .addOnCompleteListener(task -> imageProxy.close()) ;
        }
    }

    public interface OnBarcodeScannedListener {
        void oBarcodeScanned(Barcode barcode);
    }
}
