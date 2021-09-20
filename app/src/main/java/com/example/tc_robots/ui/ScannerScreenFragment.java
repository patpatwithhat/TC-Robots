package com.example.tc_robots.ui;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.example.tc_robots.R;
import com.example.tc_robots.databinding.FragmentCodeScannerBinding;
import com.example.tc_robots.uihelpers.CustomListAdapterArticles;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ScannerScreenFragment extends Fragment implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int CAMERA_REQUEST_CODE = 1000;

    private CodeScanner scanner;
    private ScannerScreenViewModel viewModel;
    private ListAdapter adapter;

    FragmentCodeScannerBinding binding;

    public ScannerScreenFragment() {
        super(R.layout.fragment_code_scanner);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_code_scanner, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ScannerScreenViewModel.class);
        checkForCameraPermission();
        initUiElements();
    }


    private void initCodeScanner() {
        CodeScannerView scannerView = binding.scannerView;
        if (scanner == null) {
            scanner = new CodeScanner(requireContext(), scannerView);
        }
        scanner.setAutoFocusEnabled(true);
        scanner.setScanMode(ScanMode.CONTINUOUS);
        scanner.setDecodeCallback(result -> requireActivity().runOnUiThread(() -> doOnScan(result.getText())));
        scannerView.setOnClickListener(view ->
                scanner.startPreview());
    }

    private void initUiElements() {
        binding.btnContinueorder.setOnClickListener(this);
        //ToDo ApiCall get available articles
        viewModel.fetchArticlesInStock();


        viewModel.getOrderedArticles().observe(getViewLifecycleOwner(), articles -> {
            if (adapter == null) {
                adapter = new CustomListAdapterArticles(ScannerScreenFragment.this.requireContext(), articles);
            }
            binding.listview.setAdapter(adapter);
        });

        viewModel.isScannerBlocked().observe(getViewLifecycleOwner(), isScannerBlocked -> {
            if (isScannerBlocked) {
                scanner.stopPreview();
            } else {
                scanner.startPreview();
            }
        });
        viewModel.getErrorCode().observe(getViewLifecycleOwner(), errorCode -> {
            Toast.makeText(requireContext(), "Error occurred: " + errorCode, Toast.LENGTH_LONG).show();
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
        //  Intent i = new Intent(ScannerScreenFragment.this,OrderScreenActivity.class);
        // startActivity(i);
/*        Fragment fragment = new FragmentB();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();*/
    }

    @AfterPermissionGranted(CAMERA_REQUEST_CODE)
    private void checkForCameraPermission() {
        String[] permission = {Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(requireContext(), permission)) {
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
    public void onResume() {
        super.onResume();
        if (scanner != null) {
            scanner.startPreview();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (scanner != null) {
            scanner.releaseResources();
        }
    }

}