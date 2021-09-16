package com.example.tc_robots.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import com.example.tc_robots.databinding.ActivityMainBinding;
import com.example.tc_robots.uihelpers.CustomListAdapter;

public class ScannerScreenActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int CAMERA_REQUEST_CODE = 1000;

    private CodeScanner scanner;
    private ScannerScreenViewModel viewModel;
    private ListAdapter adapter;

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(ScannerScreenViewModel.class);
        checkForCameraPermission();
        initUiElements();
    }


    private void initCodeScanner() {
        CodeScannerView scannerView = binding.scannerView;
        if (scanner == null) {
            scanner = new CodeScanner(this, scannerView);
        }
        scanner.setAutoFocusEnabled(true);
        scanner.setScanMode(ScanMode.CONTINUOUS);
        scanner.setDecodeCallback(result -> runOnUiThread(() -> doOnScan(result.getText())));
        scannerView.setOnClickListener(view ->
                scanner.startPreview());
    }

    private void initUiElements() {
        binding.btnContinueorder.setOnClickListener(this);
        //ToDo ApiCall get available articles
        viewModel.fetchArticlesInStock();


        viewModel.getOrderedArticles().observe(this, articles -> {
            if (adapter == null) {
                adapter = new CustomListAdapter(ScannerScreenActivity.this, articles);
            }
            binding.listview.setAdapter(adapter);
        });

        viewModel.isScannerBlocked().observe(this, isScannerBlocked -> {
            if (isScannerBlocked) {
                scanner.stopPreview();
            } else {
                scanner.startPreview();
            }
        });
        viewModel.getErrorCode().observe(this, errorCode -> {
            Toast.makeText(this, "Error occurred: " + errorCode, Toast.LENGTH_LONG).show();
        });
    }

    private void doOnScan(String result) {
        viewModel.refreshScanner();
        //ToDo check if article is in available articles
        // else Toast unknown article
        viewModel.addIfInStockArticle(result);
        viewModel.checkForErrorCode(result);
    }

    @Override
    public void onClick(View view) {
        goToNextActivity();
    }

    private void goToNextActivity() {
        Intent i = new Intent(ScannerScreenActivity.this,OrderScreenActivity.class);
        startActivity(i);
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