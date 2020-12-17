package org.techtown.starmuri;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class Custom_Dialog_2 {
        private Fragment F;
        private FirebaseFirestore db;
        private FirebaseUser user;
        public Custom_Dialog_2(Fragment fragment) {
            this.F = fragment;
        }
        public void Go_Dialog2(){
            db = FirebaseFirestore.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            final Dialog dialog = new Dialog(F.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_2);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);
            dialog.show();
            Button Yes = (Button) dialog.findViewById(R.id.Yes_Bt);
            Button No = (Button) dialog.findViewById(R.id.No_Bt);
            Yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "현재 사용자 인증됨.");
                        final String Cuid = user.getUid();
                        final Map<String, Object> g_code = new HashMap<>();
                        g_code.put("g_code","0000A");
                        db.collection("user_info").document("" + Cuid)
                                .update(g_code).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "성공");
                                        Toast.makeText(F.getContext(), "별 무리에서 빠져나왔습니다.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Intent intent = F.getActivity().getIntent();
                                        F.getActivity().finish();
                                        F.getActivity().startActivity(intent);
                                    }
                                });
                    } else {
                        // No user is signed in
                        Log.d(TAG, "NoUser");
                    }


                }
            });

            // 취소 버튼
            No.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    dialog.dismiss();
                    Toast.makeText(F.getContext(), "그만둡니다.", Toast.LENGTH_SHORT).show();
                }
            });

        }



}
