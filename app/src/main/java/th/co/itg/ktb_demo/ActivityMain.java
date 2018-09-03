package th.co.itg.ktb_demo;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.Utils;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ActivityMain extends AppCompatActivity
        implements FileAdapter.FileListener,
        OnMapReadyCallback, Service.ServiceListener{

    private final String TAG = ActivityMain.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    EditText edtPrefixTh,edtFirstNameTh,edtLastNameTh,
            edtPrefixEn,edtFirstNameEn,edtLastNameEn,
            edtIdNumber,edtExpireDate,edtBirthDate,
            edtAge,edtGender,
            edtRate,edtLoan,edtTerm,edtStatus,
            edtAddress,edtProvince,edtArea,edtSubArea,
            edtPostalCode,edtPhone;
    ImageView correctSignature,correctName,correctMap,correctInformation,imgKtb,correctImage;
    CardView btnClearSignature,btnClearAddress,btnClearInfomation,
            btnClearName,btnSave,btnScan,btnClearImage,btnSelectImage;
    SignaturePad mSignaturePad;
    RelativeLayout layoutName,layoutInfo,layoutMap,layoutSignature,layoutImage,
                imgRotName,imgRotInfo,imgRotMap,imgRotSignature,imgRotImage;
    ExpandableLayout expenableName,expenableInfo,expanableImage,
            expenableMap,expenableSignature;
    RecyclerView rvFile;

    private ProgressDialog progressDialog;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private ArrayList<ExpandableLayout> expandableLayoutArrayList = new ArrayList<>();
    private ArrayList<EditText> listEdtName = new ArrayList<>();
    private ArrayList<EditText> listEdtInfo = new ArrayList<>();
    private ArrayList<EditText> listEdtAddress = new ArrayList<>();
    private ArrayList<AlbumFile> albumFiles = new ArrayList<>();
    private String package_name = "th.co.firstpayment.thaiidender";
    private String class_name = "th.co.firstpayment.thaiidender.activity.MainActivity";
    private FileAdapter fileAdapter;
    private Address add;
    String yearBorn;
    String monthBorn;
    String dayBorn;
    String issue;
    String base64Signature;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private GoogleMap mMap;
    private Service service;
    private File fileSignature;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);
        initUI();
        initExpenable();
        initFileAdapter();
        setListenerExpenable();
        setListenerSignaturePad();
        setListEdt();
        setListenerEdt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupGoogleClient();
    }

    private void initUI(){
        btnSave = findViewById(R.id.btnSave);
        btnScan = findViewById(R.id.btnScan);
        mSignaturePad = findViewById(R.id.signature_pad);
        btnClearSignature = findViewById(R.id.btnClearSignature);
        btnClearInfomation = findViewById(R.id.btnClearInfomation);
        btnClearName = findViewById(R.id.btnClearName);
        btnClearImage = findViewById(R.id.btnClearImage);
        btnSelectImage = findViewById(R.id.btnSelectFile);
        layoutName = findViewById(R.id.layoutName);
        layoutInfo = findViewById(R.id.layoutInfo);
        layoutMap = findViewById(R.id.layoutMap);
        layoutSignature = findViewById(R.id.layoutSignature);
        layoutImage = findViewById(R.id.layoutImage);
        correctSignature = findViewById(R.id.correctSignature);
        correctMap = findViewById(R.id.correctMap);
        correctInformation = findViewById(R.id.correctInfomation);
        correctName = findViewById(R.id.correctName);
        correctImage = findViewById(R.id.correctImage);
        imgRotName = findViewById(R.id.imgRotateName);
        imgRotInfo = findViewById(R.id.imgRotateInfo);
        imgRotMap = findViewById(R.id.imgRotateMap);
        imgRotSignature = findViewById(R.id.imgRotateSignature);
        imgRotImage = findViewById(R.id.imgRotateImage);
        edtPrefixTh = findViewById(R.id.edtPrefixTh);
        edtFirstNameTh = findViewById(R.id.edtFirstnameTh);
        edtLastNameTh = findViewById(R.id.edtLastnameTh);
        edtPrefixEn = findViewById(R.id.edtPrefixEn);
        edtFirstNameEn = findViewById(R.id.edtFirstnameEn);
        edtLastNameEn = findViewById(R.id.edtLastnameEn);
        edtIdNumber = findViewById(R.id.edtIdnumber);
        edtExpireDate = findViewById(R.id.edtExpiredate);
        edtBirthDate = findViewById(R.id.edtBirthdate);
        edtAge = findViewById(R.id.edtAge);
        edtGender = findViewById(R.id.edtGender);
        edtRate = findViewById(R.id.edtRate);
        edtLoan = findViewById(R.id.edtLoan);
        edtTerm = findViewById(R.id.edtTerm);
        edtStatus = findViewById(R.id.edtStatus);
        edtAddress = findViewById(R.id.edtAddress);
        edtProvince = findViewById(R.id.edtProvince);
        edtArea = findViewById(R.id.edtArea);
        edtSubArea = findViewById(R.id.edtSubArea);
        edtPostalCode = findViewById(R.id.edtPostalCode);
        edtPhone = findViewById(R.id.edtPhone);
        imgKtb = findViewById(R.id.imgKtb);
        rvFile = findViewById(R.id.rvFile);

        PushDownAniamtion(btnClearSignature);
        PushDownAniamtion(btnClearName);
        PushDownAniamtion(btnClearInfomation);
        PushDownAniamtion(btnSave);
        PushDownAniamtion(btnScan);
        PushDownAniamtion(btnClearImage);
        PushDownAniamtion(btnSelectImage);

        imgKtb.setOnClickListener(v -> {
            mockData();
        });
    }

    private void initExpenable(){
        for (int i = 0; i < 3; i++) {
            expandState.append(i, false);
        }
        expenableName = findViewById(R.id.expenableName);
        expenableInfo = findViewById(R.id.expenableInfo);
        expenableMap = findViewById(R.id.expenableMap);
        expenableSignature = findViewById(R.id.expenableSignature);
        expanableImage = findViewById(R.id.expenableImage);
        expandableLayoutArrayList.add(expenableName);
        expandableLayoutArrayList.add(expenableInfo);
        expandableLayoutArrayList.add(expenableMap);
        expandableLayoutArrayList.add(expanableImage);
        expandableLayoutArrayList.add(expenableSignature);

        layoutName.setOnClickListener(view -> collapseItem(expenableName));
        layoutInfo.setOnClickListener(view -> collapseItem(expenableInfo));
        layoutMap.setOnClickListener(view -> collapseItem(expenableMap));
        layoutSignature.setOnClickListener(view -> collapseItem(expenableSignature));
        layoutImage.setOnClickListener(view -> collapseItem(expanableImage));
        new Handler().postDelayed(() -> {
            collapseItem(expenableName);
        },500);
    }

    private void setListEdt(){
        listEdtName.add(edtPrefixTh);
        listEdtName.add(edtFirstNameTh);
        listEdtName.add(edtLastNameTh);
        listEdtName.add(edtPrefixEn);
        listEdtName.add(edtFirstNameEn);
        listEdtName.add(edtLastNameEn);
        listEdtName.add(edtIdNumber);
        listEdtName.add(edtExpireDate);
        listEdtName.add(edtBirthDate);
        listEdtName.add(edtAge);
        listEdtName.add(edtGender);

        listEdtAddress.add(edtAddress);
        listEdtAddress.add(edtProvince);
        listEdtAddress.add(edtArea);
        listEdtAddress.add(edtSubArea);
        listEdtAddress.add(edtPostalCode);

        //listEdtInfo.add(edtPhone);
        listEdtInfo.add(edtRate);
        listEdtInfo.add(edtLoan);
        listEdtInfo.add(edtTerm);
        //listEdtInfo.add(edtStatus);
    }

    private void initFileAdapter(){
        fileAdapter = new FileAdapter(this);
        rvFile.setHasFixedSize(true);
        rvFile.setLayoutManager(new GridLayoutManager(this,4));
        rvFile.setAdapter(fileAdapter);


    }

    private void setListenerSignaturePad(){
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {}

            @Override
            public void onSigned() {
                correctSignature.setVisibility(View.VISIBLE);
                //base64Signature = encodeImage(mSignaturePad.getTransparentSignatureBitmap());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mSignaturePad.getTransparentSignatureBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                fileSignature = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                try {
                    FileOutputStream fo = new FileOutputStream(fileSignature);
                    fo.write(bytes.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClear() {
                correctSignature.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void setListenerExpenable(){
        expenableName.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                rotateAnimation(imgRotName,0,true,0f,180f);
            }

            @Override
            public void onPreClose() {
                rotateAnimation(imgRotName,0,false,180f,0f);
            }
        });
        expenableInfo.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                rotateAnimation(imgRotInfo,1,true,0f,180f);
            }

            @Override
            public void onPreClose() {
                rotateAnimation(imgRotInfo,1,false,180f,0f);
            }
        });
        expenableMap.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
                mapFragment.getMapAsync(ActivityMain.this);
                rotateAnimation(imgRotMap,2,true,0f,180f);
            }

            @Override
            public void onPreClose() {
                rotateAnimation(imgRotMap,2,false,180f,0f);
            }
        });
        expenableSignature.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                rotateAnimation(imgRotSignature,3,true,0f,180f);
            }

            @Override
            public void onPreClose() {
                rotateAnimation(imgRotSignature,3,false,180f,0f);
            }
        });
        expanableImage.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                rotateAnimation(imgRotImage,3,true,0f,180f);
            }

            @Override
            public void onPreClose() {
                rotateAnimation(imgRotImage,3,false,180f,0f);
            }
        });

    }

    private void rotateAnimation(View view,Integer position,Boolean status,Float from,Float to){
        createRotateAnimator(view, from, to).start();
        expandState.put(position, status);
    }

    private void collapseItem(ExpandableLayout expanItem){
        expanItem.toggle();
        /*
        for(ExpandableLayout expandableLayout:expandableLayoutArrayList){
            if(expandableLayout.isExpanded() && expanItem != expandableLayout){
                expandableLayout.collapse();
            }
        }
        */
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
                        case R.id.btnClearSignature:
                            clearSignature();
                            break;
                        case R.id.btnClearAddress:
                            clearAddress();
                            break;
                        case R.id.btnClearInfomation:
                            clearInformation();
                            break;
                        case  R.id.btnClearName:
                            clearName();
                            clearAddress();
                            break;
                        case R.id.btnScan:
                            scanCardId();
                            break;
                        case R.id.btnSave:
                            saveData();
                            break;
                        case R.id.btnSelectFile:
                            showAlbum();
                            break;
                        case R.id.btnClearImage:
                            fileAdapter.clearImage();
                            setCorrectImageVisible(false);
                            break;

                    }
                });
    }

    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if(data != null){
                setResultByScanCard(data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("Can't write external");
                }
            }
        }
    }
    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    private void setResultByScanCard(Intent data){
        String id = data.getStringExtra("ID_NUMBER");
        String prefixThai = data.getStringExtra("PREFIX_THAI");
        String prefixEng = data.getStringExtra("PREFIX_ENG");
        String thaiName = data.getStringExtra("THAI_NAME");
        String thaiLastName = data.getStringExtra("THAI_LASTNAME");
        String engName = data.getStringExtra("ENG_NAME");
        String engLastName = data.getStringExtra("ENG_LASTNAME");
        yearBorn = data.getStringExtra("YEAR_BORN");
        monthBorn = data.getStringExtra("MONTH_BORN");
        dayBorn = data.getStringExtra("DAY_BORN");
        String sex = data.getStringExtra("SEX");
        //String address ="35/151    ถนนประดิษฐ์มนูธรรม แขวงนวลจันทร์ เขตบึงกุ่ม กรุงเทพมหานคร";
        String address = data.getStringExtra("ADDRESS");
        issue = data.getStringExtra("ISSUE");
        String expire = data.getStringExtra("EXPIRE");

        edtPrefixTh.setText(prefixThai);
        edtFirstNameTh.setText(thaiName);
        edtLastNameTh.setText(thaiLastName);
        edtPrefixEn.setText(prefixEng);
        edtFirstNameEn.setText(engName);
        edtLastNameEn.setText(engLastName);
        edtIdNumber.setText(id);
        edtExpireDate.setText(expire);
        edtBirthDate.setText(dayBorn+"/"+monthBorn+"/"+yearBorn);
        edtAge.setText(getAge(Integer.valueOf(yearBorn),Integer.valueOf(monthBorn),Integer.valueOf(dayBorn)));
        edtGender.setText(sex);

        add = new Address(address);
        Log.d(TAG,"address ["+add.getAddress()+"] subArea ["+add.getSubArea()+"] area ["+add.getArea()+"] province ["+add.getProvince()+"]");

        edtAddress.setText(add.getAddress());
        edtSubArea.setText(add.getSubArea());
        edtArea.setText(add.getArea());
        edtProvince.setText(add.getProvince());
    }

    private void clearInformation(){
        edtRate.getEditableText().clear();
        edtLoan.getEditableText().clear();
        edtTerm.getEditableText().clear();
        edtStatus.getEditableText().clear();
        edtPhone.getEditableText().clear();

    }
    private void clearAddress(){
        edtAddress.getEditableText().clear();
        edtProvince.getEditableText().clear();
        edtArea.getEditableText().clear();
        edtSubArea.getEditableText().clear();
        edtPostalCode.getEditableText().clear();
    }
    private void clearName(){
        edtPrefixTh.getEditableText().clear();
        edtFirstNameTh.getEditableText().clear();
        edtLastNameTh.getEditableText().clear();
        edtPrefixEn.getEditableText().clear();
        edtFirstNameEn.getEditableText().clear();
        edtLastNameEn.getEditableText().clear();
        edtIdNumber.getEditableText().clear();
        edtExpireDate.getEditableText().clear();
        edtBirthDate.getEditableText().clear();
        edtAge.getEditableText().clear();
        edtGender.getEditableText().clear();

    }

    private void clearSignature(){
        mSignaturePad.clear();
    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);


        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)+543;

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            //age--;
        }

        Integer ageInt = age;
        return ageInt.toString();
    }

    private void saveData(){
        if(correctName.getVisibility() == View.VISIBLE &&
                correctInformation.getVisibility() == View.VISIBLE &&
                correctSignature.getVisibility() == View.VISIBLE &&
                correctImage.getVisibility() == View.VISIBLE){
            showProgressDialog();
            sendData();
            //new Handler().postDelayed(this::hideProgressDialog,2000);


        }else{
            showToast("กรุณากรอกข้อมูลให้ครบถ้วน");
        }
    }

    private  String encodeImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return  Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", "โปรดรอสักครู่ ", false);
        progressDialog.setOnDismissListener(dialog -> showDialog());
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
        fileAdapter.clearImage();
        if (fileSignature.isFile()){
            fileSignature.delete();
        }
    }

    private void setListenerEdt(){
        for(EditText edt:listEdtInfo){
            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                   checkCorrectInfo(listEdtInfo,correctInformation);
                }
            });
        }
        for(EditText edt:listEdtName){
            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    checkCorrectInfo(listEdtName,correctName);
                }
            });
        }
        for(EditText edt:listEdtAddress){
            edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    checkCorrectInfo(listEdtAddress,correctName);
                }
            });
        }
    }

    private void checkCorrectInfo(ArrayList<EditText> listedt,View correctView){
        Boolean isCorrect =false;
        for(EditText edt:listedt){
            isCorrect = !edt.getEditableText().toString().isEmpty();
            if(!isCorrect){
                break;
            }
        }
        if(isCorrect){
            correctView.setVisibility(View.VISIBLE);
        }else{
            correctView.setVisibility(View.INVISIBLE);
        }
    }


    private  void mockData(){

        edtPrefixTh.setText("ชาย");
        edtFirstNameTh.setText("เกียรติชัย");
        edtLastNameTh.setText("รีรมย์");
        edtPrefixEn.setText("Mr");
        edtFirstNameEn.setText("Kiadtichai");
        edtLastNameEn.setText("Reerom");
        edtIdNumber.setText("1410100281219");
        edtExpireDate.setText("20/11/2567");
        edtBirthDate.setText("21/11/2536");
        edtAge.setText(getAge(2536,11,21));
        edtGender.setText("ชาย");

        edtAddress.setText("55 หมู่ที่ 20");
        edtSubArea.setText("ขามเรียง");
        edtArea.setText("กันทรวิชัย");
        edtProvince.setText("มหาสารคาม");
        edtPostalCode.setText("44000");
        edtStatus.setText("โสด");
        edtPhone.setText("0880657145");

        edtRate.setText("2");
        edtLoan.setText("400000");
        edtTerm.setText("48");


        /*
        String address ="35/151    ถนนประดิษฐ์มนูธรรม แขวงนวลจันทร์ เขตบึงกุ่ม กรุงเทพมหานคร";
        //String address ="55/150 บ้านหนองน้ำ ตำบลหนองบัว อำเภอเมือง จังหวัดอุดรธานี";

        Address add = new Address(address);
        Log.d(TAG,"address ["+add.getAddress()+"] subArea ["+add.getSubArea()+"] area ["+add.getArea()+"] province ["+add.getProvince()+"]");

        edtAddress.setText(add.getAddress());
        edtSubArea.setText(add.getSubArea());
        edtArea.setText(add.getArea());
        edtProvince.setText(add.getProvince());
        */
    }

    private void showDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("บันทึกข้อมูลสำเร็จ");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "ตกลง",
                (dialog, id) -> {
                    dialog.cancel();
                    finish();
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }




    private void showAlbum(){
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .build());
        Widget customize =
                Widget.newDarkBuilder(this)
                .title("เลือกรูปภาพ") // Title.
                .statusBarColor(ContextCompat.getColor(this,R.color.colorPrimary)) // StatusBar color.
                .toolBarColor(ContextCompat.getColor(this,R.color.colorPrimary)) // Toolbar color.
                .navigationBarColor(Color.WHITE) // Virtual NavigationBar color of Android5.0+.
                .mediaItemCheckSelector(Color.BLUE, Color.GREEN) // Image or video selection box.
                .bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
                .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
                                .setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
                                .build()
                )
                .build();


        /*
        Album.image(this) // Image selection.
                .multipleChoice()
                .widget(customize)
                .camera(true)
                .columnCount(3)
                .selectCount(20)
                .checkedList(albumFiles)
                .onResult(result -> { ;
                    this.runOnUiThread(() -> {
                        fileAdapter.updateFiles(result);
                    });
                })
                .onCancel(result -> {
                    System.out.println("onCancel");
                })
                .start();
                */
        Album.camera(this) // Camera function.
                .image() // Take Picture.
                .onResult(result -> {
                    System.out.println(result);
                    this.runOnUiThread(() -> {
                        fileAdapter.addFile(result);
                        setCorrectImageVisible(true);
                    });

                })
                .onCancel(result -> {
                })
                .start();
    }

    private void setCorrectImageVisible(Boolean data){
        if(data){
            correctImage.setVisibility(View.VISIBLE);
        }else{
            correctImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClickFile() {

    }


    private void sendData(){
        String url = "";
        JSONObject json = new JSONObject();
        //JSONArray mJSONArray = new JSONArray(Arrays.asList(mString));
        //JSONArray mJSONArray = new JSONArray(fileAdapter.base64Files);
        try {
            json.put("idCard",edtIdNumber.getText().toString());
            json.put("prefixThai",edtPrefixTh.getText().toString());
            json.put("prefixEng",edtPrefixEn.getText().toString());
            json.put("nameTh",edtFirstNameTh.getText().toString());
            json.put("lastnameTh",edtLastNameTh.getText().toString());
            json.put("nameEn",edtFirstNameEn.getText().toString());
            json.put("lastnameEn",edtLastNameEn.getText().toString());
            json.put("year",yearBorn);
            json.put("month",monthBorn);
            json.put("day",dayBorn);
            json.put("issueDate",issue);
            json.put("expireDate",edtExpireDate.getText().toString());
            json.put("addressNo",add.getAddress());
            json.put("tambon",add.getSubArea());
            json.put("amphur",add.getArea());
            json.put("province",add.getProvince().trim());
            json.put("loan",edtLoan.getText().toString());
            json.put("rate",edtRate.getText().toString());
            json.put("term",edtTerm.getText().toString());
            //json.put("imageSignature_filename",base64Signature);
            json.put("latitude",mLocation.getLatitude());
            json.put("longtitude",mLocation.getLongitude());
            System.out.println(json);

            service = new Service(this,getApplicationContext(),edtIdNumber.getText().toString(),
                    json,fileAdapter.filePaths);
            service.uploadSignature(fileSignature);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("MissingPermission")
    private void setupGoogleClient(){
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null){
                    for (Location location:locationResult.getLocations()){
                        mLocation = location;
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(mLocationRequest,locationCallback,null);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        @SuppressLint("MissingPermission") Task<Location> task = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        task.addOnCompleteListener(it -> {
            if (it.isComplete()){
                mLocation = it.getResult();
                handleNewLocation(it.getResult());
            }
        });

    }

    private void handleNewLocation(Location location) {
        mLocation = location;
        Float zoomLevel = 14f;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        MarkerOptions current = new MarkerOptions()
                .position(latLng);
        mMap.addMarker(current);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }


    @Override
    public void onSaveCaseComplete() {
        hideProgressDialog();
    }


}
