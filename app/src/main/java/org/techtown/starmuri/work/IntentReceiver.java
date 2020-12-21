package org.techtown.starmuri.work;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class IntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            Log.d(TAG, "알람실행됨");
            SharedPreferences sharedPref = context.getSharedPreferences("IFile",MODE_PRIVATE);
            int re = sharedPref.getInt("index",0);
            re++;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("index_",re);
            editor.commit();
        }
        else{
            // Set the alarm here.
            Log.d(TAG, "알람실행됨");
            SharedPreferences sharedPref = context.getSharedPreferences("IFile",MODE_PRIVATE);
            int re = sharedPref.getInt("index",0);
            re++;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("index_",re);
            editor.commit();
        }


    }

}