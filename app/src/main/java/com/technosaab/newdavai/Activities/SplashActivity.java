package com.technosaab.newdavai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.technosaab.newdavai.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



//                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
//            Toast.makeText(SplashActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
//        }else {
//
//        }


        Thread timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(SplashActivity.this , SelectLangActivity.class));
                    SplashActivity.this.finish();
                }
            }
        });
        timer.start();


    }
}
