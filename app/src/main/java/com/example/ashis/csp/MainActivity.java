package com.example.ashis.csp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends AppCompatActivity {
    private Button mUploadBtn;
    private ImageView mImageView;
    public static final int CAMERA_REQUEST_CODE=1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private static final int PERMISSION_REQUEST_CODE = 200;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(android.Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });

        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        mUploadBtn=(Button)findViewById(R.id.upload);
        mImageView=(ImageView) findViewById(R.id.imageView);
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);


            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==CAMERA_REQUEST_CODE&&resultCode==RESULT_OK){

            mProgress.setMessage("uploading image...");
            mProgress.show();
            uri=data.getData();
            StorageReference filePath=mStorage.child("Photos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this,"Uploading complete....", Toast.LENGTH_LONG).show();

                }
            });




        }
    }






}
