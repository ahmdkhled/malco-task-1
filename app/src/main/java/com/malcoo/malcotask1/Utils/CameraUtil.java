package com.malcoo.malcotask1.Utils;

import android.content.Context;
import android.util.Log;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraUtil {
    private  static CameraUtil cameraUtil;
    ImageCapture imageCapture =new  ImageCapture.Builder().build();
    private BarcodeScanner scanner;
    ImageAnalysis imageAnalysis;
    BarcodeAnalyzer.OnBarcodeScannedListener onBarcodeScannedListener;

    private static final String TAG = "CameraUtil";

    public static CameraUtil getInstance(){
        return cameraUtil==null?cameraUtil=new CameraUtil():cameraUtil;
    }

    public void startCamera(Context context, LifecycleOwner owner, PreviewView previewView){


            Preview preview = new Preview.Builder().build();
            ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);
            Executor cameraExecutor = ContextCompat.getMainExecutor(context);

            cameraProviderFuture.addListener(() -> {
                Log.d(TAG, "startCamera: ");
                ProcessCameraProvider cameraProvider = null;
                try {
                    cameraProvider = cameraProviderFuture.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "bug: "+e.getMessage());
                }
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                scanner =  BarcodeScanning.getClient();
                imageAnalysis = new ImageAnalysis.Builder()
                        .build();
                imageAnalysis.setAnalyzer(cameraExecutor,new BarcodeAnalyzer(scanner, onBarcodeScannedListener));

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        owner,
                        cameraSelector,
                        preview,
                        imageCapture,
                        imageAnalysis
                );



            }, cameraExecutor);



    }
    public void stopAnalyzer(){
        scanner.close();
        imageAnalysis.clearAnalyzer();
    }

    public void setOnBarcodeScannedListener(BarcodeAnalyzer.OnBarcodeScannedListener onBarcodeScannedListener) {
        this.onBarcodeScannedListener = onBarcodeScannedListener;
    }
}
