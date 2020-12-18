package org.techtown.starmuri.ui.group;

import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import org.techtown.starmuri.Dialogs.CD_for_g_code;
import org.techtown.starmuri.Dialogs.Custom_Dialog;
import org.techtown.starmuri.Dialogs.Custom_Dialog_2;
import org.techtown.starmuri.Dialogs.Dialogs;
import org.techtown.starmuri.R;
import org.techtown.starmuri.link.GroupObj;
import org.techtown.starmuri.link.UserObj;
import org.techtown.starmuri.ui.group.makegroup.MakeGroupActivity;
import java.util.HashMap;
import java.util.Map;


import static android.content.ContentValues.TAG;

public class GroupFragment extends Fragment {

    private GroupViewModel groupViewModel;
    private FirebaseAuth firebaseAuth;
    UserObj userObj;
    GroupObj groupObj;
    private FirebaseFirestore db, db2;
    FirebaseUser user;
    private Button group_search_button,group_make_button,withdrawal,show_code;
    private TextView group_name,Mnumber,Mlist;
    private View view;
    Map<String, Object> op1;
    Dialogs dialogs;
    AppCompatDialog progressDialog;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userObj = new UserObj();
        groupObj = new GroupObj();
        groupViewModel =
                ViewModelProviders.of(this).get(GroupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_group, container, false);
        group_name = root.findViewById(R.id.group_name_text);
        Mnumber = root.findViewById(R.id.member_number);
        Mlist = root.findViewById(R.id.member_list);

        view = root.findViewById(R.id.list_g);
        group_search_button = root.findViewById(R.id.group_search);
        group_make_button = root.findViewById(R.id.group_make);
        withdrawal = root.findViewById(R.id.withdrawal);
        show_code = root.findViewById(R.id.show_code);
        op1 = new HashMap<>();
        dialogs = new Dialogs();
        final Custom_Dialog custom_dialog;
        custom_dialog = new Custom_Dialog(this);

        final Custom_Dialog_2 custom_dialog2;
        custom_dialog2 = new Custom_Dialog_2(this);

        final CD_for_g_code CD;
        CD = new CD_for_g_code(this);

        if (user != null) {
            // User is signed in
            dialogs.progressON(getActivity(),"흐트러진 파일들 정리하는중...");
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
        group_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("그룹 검색 버튼 클릭");
                custom_dialog.Go_Dialog();
            }
        });
        show_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("그룹 초대 버튼 클릭");
                CD.Go_Dialog(userObj);
            }
        });
        group_make_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클릭");
                Intent intent = new Intent(getContext(), MakeGroupActivity.class);// Intent 선언
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);   // Intent 시작
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클릭");
                custom_dialog2.Go_Dialog2();
            }
        });
        /*final TextView textView = root.findViewById(R.id.text_group);
        groupViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userObj.getG_code().equals("0000A")) {
                    dialogs.progressOFF();
                    Log.d(TAG, "현재 사용자 그룹없음.");
                    group_name.setText("별무리에 속해 보세요~");
                    view.setVisibility(View.INVISIBLE);
                    group_search_button.setVisibility(View.VISIBLE);
                    group_make_button.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "현재 사용자 그룹있음."+userObj.getG_code());
                    view.setVisibility(View.VISIBLE);
                    Mnumber.setVisibility(View.VISIBLE);
                    Mlist.setVisibility(View.VISIBLE);
                    group_search_button.setVisibility(View.GONE);
                    group_make_button.setVisibility(View.GONE);
                    withdrawal.setVisibility(View.VISIBLE);
                    show_code.setVisibility(View.VISIBLE);
                    startDBLoading();
                }
            }
        }, 1000);

    }
    private void startDBLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "디비로딩.");
                db.collection("user_info").whereEqualTo("g_code", ""+userObj.getG_code())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int i = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                    Mnumber.append(""+(i+1)+".\n");
                                    Mlist.append(""+document.get("name")+"\n");
                                    Log.d(TAG, document.getId() + " => " + document.get("name"));
                                    i++;
                                }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                db2.collection("group_code").document("" +userObj.getG_code())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.get("group_name"));
                                        group_name.setText(""+document.get("group_name"));
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                dialogs.progressOFF();
        }
    }, 1000);
    }

}