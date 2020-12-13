package org.techtown.starmuri.Write_ac;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.starmuri.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class WritingActivity extends Activity {
    private FirebaseFirestore db;
    private Button finish_button;
    private EditText op_context;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        finish_button = findViewById(R.id.finish_writing);
        op_context = findViewById(R.id.opinion_context);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println("등록 버튼 클릭");
                String CUname = user.getDisplayName();
                String CUemail = user.getEmail();
                String Cuid = user.getUid();
                String Context = op_context.getText().toString();

                Map<String, Object> CUser = new HashMap<>();

            }
        });

    }
}
