package org.techtown.starmuri.Dialogs;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.starmuri.R;
import org.techtown.starmuri.link.UserObj;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class Custom_Dialog_2 {
        private Fragment F;
        private FirebaseFirestore db;
        private FirebaseUser user;
        private UserObj userObj;
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
            userObj = new UserObj();
            DocumentReference docRef = db.collection("user_info").document("" + user.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userObj = documentSnapshot.toObject(UserObj.class);
                }
            });

            /*public void deleteAtPath(String path) {
                Map<String, Object> data = new HashMap<>();
                data.put("path", path);

                HttpsCallableReference deleteFn =
                        FirebaseFunctions.getInstance().getHttpsCallable("recursiveDelete");
                deleteFn.call(data)
                        .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                            @Override
                            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                                // Delete Success
                                // ...
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Delete failed
                                // ...
                            }
                        });
            }*/

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

                                        //여기서도 멤버의 uid문서 삭제하도록 수정 필요
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
    public void Read_king(){
            //유저오브젝트의 그룹코드 가져와서
            //그룹코드 컬렉션에 그 코드이름으로 된 문서가 있겠찌?
            //그 하위 멤버 컬렉션에는 유아이디로 된 문서들이 있을테고.
            //문서를 걍 다 가져오자 오브젝트로.
            //그럼 그 문서에서 KING 필드 값을 가져와.
            //그룹장이 그룹을 탈퇴하면 그냥 그룹이 공중분해가 되도록 하는것이지..
            //유저인포에서 그룹코드가 같은사람의 문서에서 그룹코드 다 디폴트값으로 바꿈으로서
            //공중분해 시킬 수 있을것.
    }


}
