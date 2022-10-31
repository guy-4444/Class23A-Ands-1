package com.guy.class23a_ands_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_Contacts extends AppCompatActivity {

    private MaterialTextView info;
    private MaterialButton refresh;
    private MaterialButton request;

    private final int REQUEST_CODE_PERMISSION_CONTACTS = 900;
    private final int REQUEST_CODE_PERMISSION_CAMERA = 901;
    private static final int MANUALLY_CONTACTS_PERMISSION_REQUEST_CODE = 124;

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


    private void requestContacts() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PERMISSION_CONTACTS);
    }

    private void requestCamera() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);
    }

    private void requestPermissionWithRationaleCheck() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Activity_Contacts.this, Manifest.permission.READ_CONTACTS)) {
            Log.d("pttt", "shouldShowRequestPermissionRationale = true");
            // Show user description for what we need the permission

            String message = "permission description for approve";
            AlertDialog alertDialog =
                    new AlertDialog.Builder(Activity_Contacts.this)
                            .setMessage(message)
                            .setPositiveButton(getString(android.R.string.ok),
                                    (dialog, which) -> {
                                        requestContacts();
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
                new AlertDialog.Builder(Activity_Contacts.this)
                        .setMessage(message)
                        .setPositiveButton(getString(android.R.string.ok),
                                (dialog, which) -> {
                                    openSettingsManually();
                                    dialog.cancel();
                                }).show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void openSettingsManually() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, MANUALLY_CONTACTS_PERMISSION_REQUEST_CODE);
    }

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
        Toast.makeText(Activity_Contacts.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CONTACTS: {
                Log.d("pttt", "REQUEST_CODE_PERMISSION_CONTACTS");
                boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
                if (result) {
                    getContacts();
                    return;
                }
                requestPermissionWithRationaleCheck();
                return;
            }
            case REQUEST_CODE_PERMISSION_CAMERA: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(Activity_Contacts.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void findViews() {
        info = findViewById(R.id.info);
        refresh = findViewById(R.id.refresh);
        request = findViewById(R.id.request);
    }

    private void initViews() {
        refresh.setOnClickListener(v -> refresh());
        request.setOnClickListener(v -> requestContacts());
    }
}



































