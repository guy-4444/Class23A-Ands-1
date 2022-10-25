package com.guy.class23a_ands_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private MaterialTextView info;
    private MaterialButton refresh;
    private MaterialButton request;

    private final int REQUEST_CODE_PERMISSION_CONTACTS = 900;
    private final int REQUEST_CODE_PERMISSION_CAMERA = 901;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }



    private void refresh() {


        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        String str = "Contacts = " + result;
        str += "\nShould Show Request Permission Rationale:\n" + ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS);
        info.setText(str);
    }


    private void requestContacts() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PERMISSION_CONTACTS);
    }

    private void requestCamera() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CONTACTS: {
                Log.d("pttt", "REQUEST_CODE_PERMISSION_CONTACTS");

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    refresh();
                    Toast.makeText(MainActivity.this, "Contacts ok", Toast.LENGTH_SHORT).show();
                } else {
                    //requestContacts();
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_CODE_PERMISSION_CAMERA: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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



































