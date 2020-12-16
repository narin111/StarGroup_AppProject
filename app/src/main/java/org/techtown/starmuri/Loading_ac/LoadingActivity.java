package org.techtown.starmuri.Loading_ac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import org.techtown.starmuri.MainActivity;
import org.techtown.starmuri.R;
import org.techtown.starmuri.google.LoginActivity;


public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent_to_login = new Intent(getBaseContext(), LoginActivity.class);  // Intent 선언
                startActivity(intent_to_login);   // Intent 시작
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        }, 4000);
    }
}