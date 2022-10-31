package com.guy.class23a_ands_1;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_Camera extends AppCompatActivity {

    private MaterialTextView info;
    private MaterialButton refresh;
    private MaterialButton request;

    private final int REQUEST_CODE_PERMISSION_CONTACTS = 900;
    private final int REQUEST_CODE_PERMISSION_CAMERA = 901;
    private static final int MANUALLY_CONTACTS_PERMISSION_REQUEST_CODE = 124;


    ActivityResultCallback<Boolean> cameraPermissionCallBack = new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isGranted) {
            if (isGranted) {
                getContacts();
            } else {
                requestPermissionWithRationaleCheck();
            }
        }
    };
    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), cameraPermissionCallBack);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }



    private void refresh() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        String str = "Contacts permission= " + result;
        str += "\nShould Show Message= " + ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS);
        info.setText(str);
    }


    private void requestCamera() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    private void requestPermissionWithRationaleCheck() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Activity_Camera.this, Manifest.permission.READ_CONTACTS)) {
            Log.d("pttt", "shouldShowRequestPermissionRationale = true");
            // Show user description for what we need the permission

            String message = "permission description for approve";
            AlertDialog alertDialog =
                    new AlertDialog.Builder(Activity_Camera.this)
                            .setMessage(message)
                            .setPositiveButton(getString(android.R.string.ok),
                                    (dialog, which) -> {
                                        requestCamera();
                                        dialog.cancel();
                                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // disabled functions due to denied permissions
                                }
                            })
                            .show();
            alertDialog.setCanceledOnTouchOutside(true);
        } else {
            Log.d("pttt", "shouldShowRequestPermissionRationale = false");
            openPermissionSettingDialog();
        }
    }

    private void openPermissionSettingDialog() {
        String message = "Setting screen if user have permanently disable the permission by clicking Don't ask again checkbox.";
        AlertDialog alertDialog =
                new AlertDialog.Builder(Activity_Camera.this)
                        .setMessage(message)
                        .setPositiveButton(getString(android.R.string.ok),
                                (dialog, which) -> {
                                    openSettingsManually ();
                                    dialog.cancel();
                                }).show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void openSettingsManually() {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        manuallyPermissionResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> manuallyPermissionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getContacts();
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MANUALLY_CONTACTS_PERMISSION_REQUEST_CODE) {
            boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
            if (result) {
                getContacts();
                return;
            }
        }
    }

    private void getContacts() {
        Toast.makeText(Activity_Camera.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
    }

    private void findViews() {
        info = findViewById(R.id.info);
        refresh = findViewById(R.id.refresh);
        request = findViewById(R.id.request);
    }

    private void initViews() {
        refresh.setOnClickListener(v -> refresh());
        request.setOnClickListener(v -> requestCamera());
    }
}



































