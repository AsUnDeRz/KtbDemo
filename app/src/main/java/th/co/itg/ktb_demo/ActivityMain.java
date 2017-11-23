package th.co.itg.ktb_demo;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.github.aakira.expandablelayout.Utils;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class ActivityMain extends AppCompatActivity{

    private final String TAG = ActivityMain.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    EditText edtPrefixTh,edtFirstNameTh,edtLastNameTh,
            edtPrefixEn,edtFirstNameEn,edtLastNameEn,
            edtIdNumber,edtExpireDate,edtBirthDate,
            edtAge,edtGender,
            edtNational,edtCountryBorn,edtEducation,edtStatus,
            edtAddress,edtProvince,edtArea,edtSubArea,
            edtPostalCode,edtPhone;
    ImageView correctSignature,correctName,correctAddress,correctInformation,imgKtb;
    CardView btnClearSignature,btnClearAddress,btnClearInfomation,
            btnClearName,btnSave,btnScan;
    SignaturePad mSignaturePad;
    RelativeLayout layoutName,layoutInfo,layoutAddress,layoutSignature,
                imgRotName,imgRotInfo,imgRotAddress,imgRotSignature;
    ExpandableLayout expenableName,expenableInfo,
            expenableAddress,expenableSignature;
    private ProgressDialog progressDialog;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private ArrayList<ExpandableLayout> expandableLayoutArrayList = new ArrayList<>();
    private ArrayList<EditText> listEdtName = new ArrayList<>();
    private ArrayList<EditText> listEdtInfo = new ArrayList<>();
    private ArrayList<EditText> listEdtAddress = new ArrayList<>();
    private String package_name = "th.co.firstpayment.thaiidender";
    private String class_name = "th.co.firstpayment.thaiidender.activity.MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);
        initUI();
        initExpenable();
        setListenerExpenable();
        setListenerSignaturePad();
        setListEdt();
        setListenerEdt();

    }

    private void initUI(){
        btnSave = findViewById(R.id.btnSave);
        btnScan = findViewById(R.id.btnScan);
        mSignaturePad = findViewById(R.id.signature_pad);
        btnClearSignature = findViewById(R.id.btnClearSignature);
        btnClearAddress = findViewById(R.id.btnClearAddress);
        btnClearInfomation = findViewById(R.id.btnClearInfomation);
        btnClearName = findViewById(R.id.btnClearName);
        layoutName = findViewById(R.id.layoutName);
        layoutInfo = findViewById(R.id.layoutInfo);
        layoutAddress = findViewById(R.id.layoutAddress);
        layoutSignature = findViewById(R.id.layoutSignature);
        correctSignature = findViewById(R.id.correctSignature);
        correctAddress = findViewById(R.id.correctAddress);
        correctInformation = findViewById(R.id.correctInfomation);
        correctName = findViewById(R.id.correctName);
        imgRotName = findViewById(R.id.imgRotateName);
        imgRotInfo = findViewById(R.id.imgRotateInfo);
        imgRotAddress = findViewById(R.id.imgRotateAddress);
        imgRotSignature = findViewById(R.id.imgRotateSignature);
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
        edtNational = findViewById(R.id.edtNational);
        edtCountryBorn = findViewById(R.id.edtCountryBorn);
        edtEducation = findViewById(R.id.edtEducation);
        edtStatus = findViewById(R.id.edtStatus);
        edtAddress = findViewById(R.id.edtAddress);
        edtProvince = findViewById(R.id.edtProvince);
        edtArea = findViewById(R.id.edtArea);
        edtSubArea = findViewById(R.id.edtSubArea);
        edtPostalCode = findViewById(R.id.edtPostalCode);
        edtPhone = findViewById(R.id.edtPhone);
        imgKtb = findViewById(R.id.imgKtb);

        PushDownAniamtion(btnClearSignature);
        PushDownAniamtion(btnClearAddress);
        PushDownAniamtion(btnClearName);
        PushDownAniamtion(btnClearInfomation);
        PushDownAniamtion(btnSave);
        PushDownAniamtion(btnScan);

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
        expenableAddress = findViewById(R.id.expenableAddress);
        expenableSignature = findViewById(R.id.expenableSignature);
        expandableLayoutArrayList.add(expenableName);
        expandableLayoutArrayList.add(expenableInfo);
        expandableLayoutArrayList.add(expenableAddress);
        expandableLayoutArrayList.add(expenableSignature);

        layoutName.setOnClickListener(view -> collapseItem(expenableName));
        layoutInfo.setOnClickListener(view -> collapseItem(expenableInfo));
        layoutAddress.setOnClickListener(view -> collapseItem(expenableAddress));
        layoutSignature.setOnClickListener(view -> collapseItem(expenableSignature));
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

        listEdtInfo.add(edtPhone);
        listEdtInfo.add(edtNational);
        listEdtInfo.add(edtCountryBorn);
        listEdtInfo.add(edtEducation);
        listEdtInfo.add(edtStatus);
    }

    private void setListenerSignaturePad(){
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {}

            @Override
            public void onSigned() {
                correctSignature.setVisibility(View.VISIBLE);
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
        expenableAddress.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                rotateAnimation(imgRotAddress,2,true,0f,180f);
            }

            @Override
            public void onPreClose() {
                rotateAnimation(imgRotAddress,2,false,180f,0f);
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
                            break;
                        case R.id.btnScan:
                            scanCardId();
                            break;
                        case R.id.btnSave:
                            saveData();
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
        String yearBorn = data.getStringExtra("YEAR_BORN");
        String monthBorn = data.getStringExtra("MONTH_BORN");
        String dayBorn = data.getStringExtra("DAY_BORN");
        String sex = data.getStringExtra("SEX");
        String address = data.getStringExtra("ADDRESS");
        String issue = data.getStringExtra("ISSUE");
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

        int indexProvince = address.indexOf("จังหวัด");
        String province="";
        if(indexProvince == -1){
            String[] split = address.split(" ");
            province = split[split.length-1];
        }else{
            province = address.substring(indexProvince).substring(7);
        }
        int indexArea = address.indexOf("อำเภอ");
        if(indexArea == -1 ){
            indexArea = address.indexOf("เขต");
        }
        int indexSubArea = address.indexOf("ตำบล");
        if(indexSubArea == -1){
            indexSubArea = address.indexOf("แขวง");
        }
        String prefixAddress = address.substring(0,indexSubArea).trim();
        String prefixSubArea = address.substring(indexSubArea,address.substring(indexSubArea).lastIndexOf(""));
        String subArea = prefixSubArea.substring(0,prefixSubArea.indexOf(" ")).substring(4);
        String prefixArea = address.substring(indexArea);
        String area = prefixArea.substring(0,prefixArea.indexOf(" ")).substring(5);
        Log.d(TAG,subArea.substring(4)+"/"+area.substring(5)+"/"+province.substring(7));

        edtAddress.setText(prefixAddress);
        edtSubArea.setText(subArea);
        edtArea.setText(area);
        edtProvince.setText(province);
    }

    private void clearInformation(){
        edtNational.getEditableText().clear();
        edtCountryBorn.getEditableText().clear();
        edtEducation.getEditableText().clear();
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
                correctAddress.getVisibility() == View.VISIBLE &&
                correctSignature.getVisibility() == View.VISIBLE){
            showProgressDialog();
            new Handler().postDelayed(this::hideProgressDialog,2000);
        }else{
            showToast("กรุณากรอกข้อมูลให้ครบถ้วน");
        }
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", "โปรดรอสักครู่ ", false);
        progressDialog.setOnDismissListener(dialog -> showDialog());
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
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
                    checkCorrectInfo(listEdtAddress,correctAddress);
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
        /*
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
        edtNational.setText("ไทย");
        edtCountryBorn.setText("ไทย");
        edtEducation.setText("ปริญญาตรี");
        edtStatus.setText("โสด");
        edtPhone.setText("0880657145");
        */

        String address ="35/151    ถนนประดิษฐ์มนูธรรม แขวงนวลจันทร์ เขตบึงกุ่ม กรุงเทพมหานคร";
        //String address ="55/150 บ้านหนองน้ำ ตำบลหนองบัว อำเภอเมือง จังหวัดอุดรธานี";

        Address add = new Address(address);
        Log.d(TAG,"address ["+add.getAddress()+"] subArea ["+add.getSubArea()+"] area ["+add.getArea()+"] province ["+add.getProvince()+"]");

        edtAddress.setText(add.getAddress());
        edtSubArea.setText(add.getSubArea());
        edtArea.setText(add.getArea());
        edtProvince.setText(add.getProvince());
    }

    private void showDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("บันทึกข้อมูลสำเร็จ");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "ตกลง",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
