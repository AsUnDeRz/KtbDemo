package th.co.itg.ktb_demo.Customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import th.co.itg.ktb_demo.ActivityMenu;
import th.co.itg.ktb_demo.R;

/**
 * Created by ToCHe on 3/9/2018 AD.
 */
public class ActivityCustomerList extends AppCompatActivity implements CustomerAdapter.CustomerListener {

    CustomerAdapter customerAdapter;
    RecyclerView rvCustomer;
    ArrayList<Customer> mockData = new ArrayList<>();
    TextView txtTotal,txtDate;
    String[] dayOfWeek = {"วันอาทิตย์","วันจันทร์","วันอังคาร","วันพุธ","วันพฤหัสบดี","วันศุกร์","วันเสาร์"};
    String[] months = {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฏาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันยาคม"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        txtTotal = findViewById(R.id.txtTotal);
        txtDate = findViewById(R.id.txtDate);
        initAdapter();
        initCurrentDate();
    }

    @SuppressLint("NewApi")
    void initCurrentDate(){
        Calendar calendar = BuddhistCalendar.getInstance();
        String dayOfW = (dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1]);
        String dayOfM = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = months[calendar.get(Calendar.MONTH)];
        String year = String.valueOf((calendar.get(Calendar.YEAR)+543));
        txtDate.setText(dayOfW+"ที่  "+dayOfM+"  "+month+"  "+year);

    }

    private void initAdapter(){
        mockData.add(new Customer("นายดำรงค์  ปคุณวานิช",
                "604/5   ซอยเอกมัย 28  ถนนสุขุมวิท 63  แขวงคลองตันเหนือ เขตวัฒนา กรุงเทพมหานคร 10110",
                "0899695461"));
        mockData.add(new Customer("นายสมบูรณ์  เอื้ออัชฌาสัย",
                "112/31  หมู่6  ซอยชินเขต1/5   ถนนงามวงศ์วาน  แขวงทุ่งสองห้อง   เขตหลักสี่ กรุงเทพมหานคร 10210",
                "0990223443"));
        mockData.add(new Customer("นางสาวสุกานดา  พลอยส่องแสง",
                "385/462   เตาปูนแมนชั่น C  ถนนเตชะวณิช  แขวงบางซื่อ เขตบางซื่อ กรุงเทพมหานคร 10800",
                "0875918475"));

        mockData.add(new Customer("นายเกียรติศักดิ์  กฤตยาวัฒนานนท์",
                "50/100   ซอยนวลจันทร์  ถนนนวมินทร์  แขวงคลองกุ่ม เขตบึงกุ่ม กรุงเทพมหานคร 10230",
                "0855490165"));
        mockData.add(new Customer("นางสาวปิยนันท์  เอกไพบูลย์",
                "80  หมู่บ้านชลนิเวศน์ ซอย 1  ถนนประชาชื่น  แขวงลาดยาว  เขตจตุจักร กรุงเทพมหานคร 10900",
                "08142561244"));
        mockData.add(new Customer("นางสาวอารีย์  ตัณฑ์สุทธิวงศ์",
                "85/1   ซอยลาดพร้าว 15  ถนนลาดพร้าว แขวงลาดยาว เขตจตุจักร กรุงเทพมหานคร 10900\t",
                "0990012343"));



        rvCustomer = findViewById(R.id.rvCustomer);
        customerAdapter = new CustomerAdapter(this);
        rvCustomer.setHasFixedSize(true);
        rvCustomer.setLayoutManager(new LinearLayoutManager(this));
        rvCustomer.setAdapter(customerAdapter);
        customerAdapter.updateCustomer(mockData);
        txtTotal.setText("ลูกค้าทั้งหมด  "+customerAdapter.customerList.size()+"  ราย");

    }

    @Override
    public void onClickCustomer(Customer data) {
        showSheetCustomer(data);
    }


    public void showSheetCustomer(Customer customer){
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_cutomer,null);
        RelativeLayout rootInformation = bottomSheetView.findViewById(R.id.rootInformation);
        RelativeLayout rootReceiveOtp = bottomSheetView.findViewById(R.id.rootReceiveotp);
        Button btnSendOtp = bottomSheetView.findViewById(R.id.btnSendOtp);
        Button btnConfirmOtp = bottomSheetView.findViewById(R.id.btnConfirmOtp);
        TextView txtname = bottomSheetView.findViewById(R.id.txtName);
        TextView txtAddress = bottomSheetView.findViewById(R.id.txtAddress);
        TextView txtMobile = bottomSheetView.findViewById(R.id.txtMobile);
        EditText edtOtp = bottomSheetView.findViewById(R.id.edtOtp);



        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        BottomSheetBehavior sheetDisableCard = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        if (sheetDisableCard.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetDialog.show();

        } else {
            bottomSheetDialog.dismiss();
        }

        txtname.setText(customer.name);
        txtMobile.setText(customer.mobile);
        txtAddress.setText(customer.address);

        PushDownAnim.setOnTouchPushDownAnim(btnSendOtp);
        PushDownAnim.setOnTouchPushDownAnim(btnConfirmOtp);

        btnSendOtp.setOnClickListener(view ->{
            rootReceiveOtp.setVisibility(View.VISIBLE);
            rootInformation.setVisibility(View.GONE);
        });

        btnConfirmOtp.setOnClickListener(view ->{
            if (edtOtp.getText().length() == 0){
                edtOtp.setError("กรุณากรอก OTP ");
            }else {
                startActivity(new Intent().setClass(ActivityCustomerList.this, ActivityMenu.class));
                bottomSheetDialog.dismiss();
            }

        });
    }

}
