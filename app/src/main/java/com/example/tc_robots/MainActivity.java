package com.example.tc_robots;

import android.Manifest;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    private static final int CAMERA_REQUEST_CODE = 1000;

    private CodeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        checkForCameraPermission();
    }

    private void initCodeScanner() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        if(scanner == null) {
            scanner = new CodeScanner(this, scannerView);
        }
        scanner.setDecodeCallback(result -> runOnUiThread(() ->
                Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show()));
        scannerView.setOnClickListener(view ->
                scanner.startPreview() );
    }

    @AfterPermissionGranted(CAMERA_REQUEST_CODE)
    private void checkForCameraPermission() {
        String[] permission = {Manifest.permission.CAMERA};
        if(!EasyPermissions.hasPermissions(this, permission)) {
            EasyPermissions.requestPermissions(this,"We need permissions to scan your barcodes!",CAMERA_REQUEST_CODE,permission);
        }
        else{
            initCodeScanner();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        checkForCameraPermission();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            checkForCameraPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(scanner != null) {
            scanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(scanner != null) {
            scanner.releaseResources();
        }
    }


}