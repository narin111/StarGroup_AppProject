package org.techtown.starmuri.ui.group.makegroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.techtown.starmuri.R;

public class MakeGroupActivity extends Activity{
    private Button Yes,No;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makegroup);


        No = findViewById(R.id.button9);
        Yes = findViewById(R.id.button10);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("승낙");
            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("거절");
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
