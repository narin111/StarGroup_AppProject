package org.techtown.starmuri.Write_ac;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.starmuri.MainActivity;
import org.techtown.starmuri.R;
import java.util.HashMap;
import java.util.Map;

import org.techtown.starmuri.google.LoginActivity;
import org.techtown.starmuri.link.UserObj;
import org.techtown.starmuri.ui.home.HomeFragment;

public class WritingActivity extends Activity {
    private Button finish_button;
    private EditText op_context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        finish_button = findViewById(R.id.finish_writing);
        op_context = findViewById(R.id.opinion_context);
        textView = findViewById(R.id.textView2);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        final SharedPreferences sharedPref = getSharedPreferences("sFile",MODE_PRIVATE);
        final String string = sharedPref.getString("topic","asd");
        textView.setText(string+"\n 주의! 수정은 불가능합니다.");
        finish_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    System.out.println("등록 버튼 클릭");
                    final String Contexts = op_context.getText().toString();
                    CollectionReference uidRef = db.collection("user_info");
                    String Cuid = user.getUid();
                    op_context.setEnabled(false);
                    String code = sharedPref.getString("bookcode","asd");
                    Map<String, Object> op1 = new HashMap<>();
                    op1.put("opinion", Contexts);
                    op1.put("bookcode", code);
                    uidRef.document(""+Cuid).collection("op").document(""+code).set(op1);
                    inputMethodManager.hideSoftInputFromWindow(op_context.getWindowToken(), 0);
                    Toast toast = Toast.makeText(getApplicationContext(),"느낀점 작성 완료", Toast.LENGTH_SHORT);
                    toast.show();
                    finish_button.setClickable(false);
                }
            });
    }

    //수정 버튼 추가해두기.

    @Override
    public void onPause() {
        super.onPause();
        savestate();
    }
    @Override
    public void onResume() {
        super.onResume();
        restorestate();
    }
    private long backPtime;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPtime < 1500) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            Intent intent_to_main = new Intent(getBaseContext(), MainActivity.class);  // Intent 선언
            startActivity(intent_to_main);   // Intent 시작
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            return;
        }
        backPtime = System.currentTimeMillis();
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 돌아갑니다..", Toast.LENGTH_SHORT).show();
    }
    public void savestate(){
        SharedPreferences sharedPref = getSharedPreferences("Wr_AcFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        final String Contexts = op_context.getText().toString();
        editor.putString("OP",Contexts);
        editor.commit();
    }
    public void restorestate(){
        SharedPreferences sharedPref = getSharedPreferences("Wr_AcFile",MODE_PRIVATE);
        if ((sharedPref != null) && (sharedPref.contains("OP"))){
            final String Contexts = sharedPref.getString("OP","");
            op_context.setText(Contexts);
            finish_button.setClickable(false);
            op_context.setEnabled(false);
        }
    }
}
