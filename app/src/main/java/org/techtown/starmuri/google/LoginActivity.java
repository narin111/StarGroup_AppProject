package org.techtown.starmuri.google;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.starmuri.Loading_ac.LoadingActivity;
import org.techtown.starmuri.MainActivity;
import org.techtown.starmuri.R;
import org.techtown.starmuri.link.BookObj;
import org.techtown.starmuri.ui.home.HomeFragment;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

//구글 로그인 액티비티
public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;
    Intent intent_to_Homefrag;
    BookObj this_week;
    String[] doc_list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        doc_list = new String[12];
        Log.d(TAG, "Oncreate 실행됨");
        // Set the dimensions of the sign-in button.
        Button signInButton = findViewById(R.id.sign_in_button);
        db = FirebaseFirestore.getInstance();
        //파이어베이스 Auth 객체
        firebaseAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //이부분에서 빨갛게 에러가 뜬다면 참고 -> https://www.inflearn.com/questions/7287
                //내 경우에는 무시하고 한번 실행했다 끄면 에러 사라졌음.
                .requestEmail()
                .requestProfile()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Onclick 실행됨");
                        signIn();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI2(account);
    }

    private void signIn() {
        Log.d(TAG, "Onsignin 실행됨");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //RC_SIGN_IN의 값
        // int: If >= 0, this code will be returned in onActivityResult() when the activity exits.
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Onactivityresult 실행됨");
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {   // Google Sign In was successful, authenticate with Firebase
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                //구글 파이어베이스로 값넘기기
                final FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어 객체
                //파이어베이스로 받은 구글사용자가 확인된 이용자의 값을 토큰으로 받음.
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential);
            }
            catch(ApiException e) {
                Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
                updateUI(null);
            }
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            Log.d(TAG, "handlesigninresult 실행됨");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    private void updateUI(GoogleSignInAccount account) {
        Log.d(TAG, "updateui 실행됨");
        if(account!=null){
            //이미 계정을 만들음
            startLoading();
            start_get_DB_Loading();
            start_make_BookObj_Loading();
        }
    }
    private void updateUI2(GoogleSignInAccount account) {
        Log.d(TAG, "updateui2 실행됨");
        if(account!=null){
            //이미 계정을 만들음
            start_get_DB_Loading();
            start_make_BookObj_Loading();
        }
    }
    private void database_write(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "이프에서 트루 넘어옴");
            String CUname = user.getDisplayName();
            String CUemail = user.getEmail();
            String Cuid = user.getUid();
            Map<String, Object> CUser = new HashMap<>();
            CUser.put("name", CUname);
            CUser.put("email", CUemail);
            db.collection("user_info").document(Cuid)
                    .set(CUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

        } else {
            // No user is signed in
            Log.d(TAG, "NoUser");
        }
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "db반영시간기다림");
                database_write();
            }
        }, 2000);
    }//로딩 메소드들 사이 밀리초 재설정 필요함. get_db<book_obj 인거 까진 알겠음.
    private void start_get_DB_Loading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "db가져오는 시간");
                db.collection("book_and_topic")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int i = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        doc_list[i] = document.getId();
                                        i++;
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        }, 2500);
    }
    private void start_make_BookObj_Loading() {
        this_week = new BookObj();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "객체 가져오는 시간");
                DocumentReference docRef = db.collection("book_and_topic").document(""+doc_list[0]);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        this_week = documentSnapshot.toObject(BookObj.class);
                        intent_to_Homefrag = new Intent(getBaseContext(), MainActivity.class);  // Intent 선언
                        intent_to_Homefrag.putExtra("OBJECT",this_week);
                        startActivity(intent_to_Homefrag);   // Intent 시작
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                    }
                });
            }
        }, 3500);
    }
}

