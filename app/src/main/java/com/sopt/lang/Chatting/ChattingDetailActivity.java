package com.sopt.lang.Chatting;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sopt.lang.R;

public class ChattingDetailActivity extends AppCompatActivity {

    ImageView follow_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_detail);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.x);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View view){
            onBackPressed();
        }
            });

        follow_btn = (ImageView)findViewById(R.id.follow_btn);
        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow_btn.setSelected(!follow_btn.isSelected());
                }
        });
    }
}
