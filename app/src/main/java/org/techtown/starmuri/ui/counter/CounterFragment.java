package org.techtown.starmuri.ui.counter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.starmuri.R;
import org.techtown.starmuri.link.UserObj;

import static android.content.ContentValues.TAG;

public class CounterFragment extends Fragment {


    private FirebaseAuth firebaseAuth;
    UserObj userObj;
    private FirebaseFirestore db;
    FirebaseUser user;

    private CounterViewModel counterViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userObj = new UserObj();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "db반영시간기다림");
            Log.d(TAG, "현재 사용자 인증됨.");
            String Cuid = user.getUid();
            DocumentReference docRef = db.collection("user_info").document("" + Cuid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userObj = documentSnapshot.toObject(UserObj.class);
                }
            });
            startLoading();

        } else {
            // No user is signed in
            Log.d(TAG, "NoUser");
        }

        counterViewModel =
                ViewModelProviders.of(this).get(CounterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_counter, container, false);
        final TextView textView = root.findViewById(R.id.text_counter);
        counterViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userObj.getG_code().equals("0000A")) {
                    Log.d(TAG, "현재 사용자 그룹없음.");
                } else {
                    Log.d(TAG, "현재 사용자 그룹있나?."+userObj.getG_code());
                }
            }
        }, 1000);

    }
}