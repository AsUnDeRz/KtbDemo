package th.co.itg.ktb_demo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.SupportMapFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import th.co.itg.ktb_demo.Customer.ActivityCustomerList;

/**
 * Created by ToCHe on 24/8/2018 AD.
 */
public class ActivityLogin extends AppCompatActivity {

    Button btnScanner;
    private String package_name = "th.co.firstpayment.thaiidender";
    private String class_name = "th.co.firstpayment.thaiidender.activity.MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnScanner = findViewById(R.id.button);
        PushDownAniamtion(btnScanner);
        PushDownAniamtion(findViewById(R.id.logoLogin));

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if(data != null){
                startActivity(new Intent().setClass(ActivityLogin.this,ActivityCustomerList.class));
                finish();
            }
        }
    }


    public void scanCardId(){
        Intent intent = new Intent();
        intent.putExtra("CALL_MODE", true);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(package_name, class_name));
        try {
            startActivityForResult(intent, 123);
        } catch (Exception e) {

        }
    }

    private void PushDownAniamtion(View view){
        PushDownAnim.setOnTouchPushDownAnim(view)
                .setOnClickListener(view1 -> {
                    switch (view.getId()){
                        case R.id.button:
                            scanCardId();
                            break;
                        case R.id.logoLogin:
                            Dexter.withActivity(this)
                                    .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                                    .withListener(new MultiplePermissionsListener() {
                                        @Override
                                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                                            if (report != null) {
                                                if(!report.isAnyPermissionPermanentlyDenied()) {
                                                    startActivity(new Intent().setClass(ActivityLogin.this,ActivityMap.class));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                        }
                                    }).check();
                            break;
                    }
                });
    }


}
