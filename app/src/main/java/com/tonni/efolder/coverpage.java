package com.tonni.efolder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class coverpage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coverpage);



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = new Intent(coverpage.this, signUP.class);
                startActivity(intent);

                finish(); //This closes current activity
            }
        }, 2500); //It means 4 seconds
    }

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2500);
//                    Intent intent=new Intent(getApplicationContext(),signUP.class);
//
//                    startActivity(intent);
//                }catch (Exception e){
//
//                }
//            }
//        });


//    }
}
