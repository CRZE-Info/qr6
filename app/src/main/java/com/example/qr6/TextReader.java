package com.example.qr6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReader extends AppCompatActivity {
String TAG = "TEXTREADER";
CameraSource mCameraSource;
SurfaceView mCameraView;
final int requestCameraPermissionID = 1001;
TextView textView;
ArrayList<String> CadN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reader);
        mCameraView = (SurfaceView) findViewById(R.id.surfaceView);
        textView = (TextView) findViewById(R.id.text_view);
        CadN=new ArrayList<String>();
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational()){
            Log.d(TAG,"Нихера этот рекогнайзер не создался");
        }else{
            mCameraSource= new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .build();

            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try {
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(TextReader.this, new String[]{Manifest.permission.CAMERA},requestCameraPermissionID);
                        return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size()!=0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                getCadN(stringBuilder.toString());
                                if(CadN.size()!=0){
                                    textView.setText(android.text.TextUtils.join("\n",CadN));
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void startCameraSource(){

    }

    private void getCadN(String line){
        String pattern = "\\b\\d{2}(:)\\d{2}(:)\\d{3,7}(:)\\d{1,6}\\b";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        while (m.find( )) {
            String StringCadN = m.group(0).replace(" ","");
            if(!CadN.contains(StringCadN)){
                CadN.add(StringCadN);
            }
            System.out.println("Found value: " + m.group(0).replace(" ","") );
        }
    }
}
