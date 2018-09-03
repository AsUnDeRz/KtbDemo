package th.co.itg.ktb_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

import com.thekhaeng.pushdownanim.PushDownAnim;

/**
 * Created by ToCHe on 27/8/2018 AD.
 */
public class ActivityMenu extends AppCompatActivity {

    CardView btnOpenMap , btnCreateCase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnCreateCase = findViewById(R.id.btnCreateCase);
        btnOpenMap = findViewById(R.id.btnOpenMap);

        PushDownAniamtion(btnCreateCase);
        PushDownAniamtion(btnOpenMap);
    }

    private void PushDownAniamtion(View view){
        PushDownAnim.setOnTouchPushDownAnim(view)
                .setOnClickListener(view1 -> {
                    switch (view.getId()) {
                        case R.id.btnCreateCase:
                            startActivity(new Intent().setClass(ActivityMenu.this,ActivityMain.class));
                            break;
                        case R.id.btnOpenMap:
                            startActivity(new Intent().setClass(ActivityMenu.this,ActivityMap.class));
                            break;
                    }
                });
    }
}
