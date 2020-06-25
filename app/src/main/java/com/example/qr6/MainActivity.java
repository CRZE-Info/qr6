package com.example.qr6;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
SurfaceView cameraPreview;
TextView textView;
BarcodeDetector barcodeDetector;
CameraSource cameraSource;
final int requestCameraPermissionID = 1001;
private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case requestCameraPermissionID:{
                //if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    //ActivityCompat.requestPermissions(MainActivity.this, new
                            //String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                //}
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                break;
            default:
        }
    }

    private void SetPermission(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,TextReader.class);
        startActivity(intent);
        SetPermission();
        cameraPreview =(SurfaceView)findViewById(R.id.SurfaceView);
        textView = (TextView)findViewById(R.id.textInQR);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(this,barcodeDetector)
                .setRequestedPreviewSize(640,480)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrcode = detections.getDetectedItems();
                if(qrcode.size()!=0){
                    textView.setText(qrcode.valueAt(0).displayValue);
                }
            }
        });
    }
}
