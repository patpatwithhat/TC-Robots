package com.example.tc_robots.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.example.tc_robots.R;
import com.example.tc_robots.backend.Article;

import java.util.List;
import java.util.stream.Collectors;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ScannerScreen extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";
    private static final int CAMERA_REQUEST_CODE = 1000;

    private CodeScanner scanner;
    private ScannerScreenViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(ScannerScreenViewModel.class);
        checkForCameraPermission();
        updateUiAfterScan();
    }

    private void updateUiAfterScan() {
        viewModel.getArticles().observe(this, articles -> {
            TextView scanCounterView = findViewById(R.id.scanCounter);
            StringBuilder articleNames = new StringBuilder();
            for (Article article : articles) {
                articleNames.append(article.getName()).append(", ");
            }
            scanCounterView.setText(articleNames.toString());
        });
        viewModel.isScannerBlocked().observe(this, isScannerBlocked -> {
            if (isScannerBlocked) {
                scanner.stopPreview();
            } else {
                scanner.startPreview();
            }
        });
    }

    private void initCodeScanner() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        if (scanner == null) {
            scanner = new CodeScanner(this, scannerView);
        }
        scanner.setAutoFocusEnabled(true);
        scanner.setScanMode(ScanMode.CONTINUOUS);
        scanner.setDecodeCallback(result -> runOnUiThread(() -> doOnScan(result.getText())));
        scannerView.setOnClickListener(view ->
                scanner.startPreview());
    }

    private void doOnScan(String result) {
        viewModel.refreshScanner();
        viewModel.addArticle(result);

    }

    @AfterPermissionGranted(CAMERA_REQUEST_CODE)
    private void checkForCameraPermission() {
        String[] permission = {Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, permission)) {
            EasyPermissions.requestPermissions(this, "We need permissions to scan your barcodes!", CAMERA_REQUEST_CODE, permission);
        } else {
            initCodeScanner();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        checkForCameraPermission();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            checkForCameraPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (scanner != null) {
            scanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (scanner != null) {
            scanner.releaseResources();
        }
    }


}