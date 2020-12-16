package org.techtown.starmuri.ui.dashboard;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.starmuri.R;
import org.techtown.starmuri.link.UserObj;
import org.techtown.starmuri.Dialogs;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Dialogs dialogs;
    private FirebaseAuth firebaseAuth;
    UserObj userObj;
    private FirebaseFirestore db,db2;
    FirebaseUser user;
    private String Cuid;
    String string1,string2;
    private String[] name_list;
    private String[] op_list;
    private View view1, view2,view3;
    int ocount, ncount;
    private TextView title,name1,name2,op1,op2;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        dialogs = new Dialogs();
        final SharedPreferences sharedPref = getActivity().getSharedPreferences("sFile",MODE_PRIVATE);
        string1 = sharedPref.getString("book","asd");
        string2 = sharedPref.getString("bookcode","asd");
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        title = root.findViewById(R.id.title_topic);
        title.setText(string1);
        name1 = root.findViewById(R.id.name1);
        name2 = root.findViewById(R.id.name2);
        op1 = root.findViewById(R.id.op1);
        op2 = root.findViewById(R.id.op2);
        userObj = new UserObj();
        name_list = new String[30];
        op_list = new String[30];
        view1 = root.findViewById(R.id.for_group);
        view2 = root.findViewById(R.id.for_solo);
        view3 = root.findViewById(R.id.for_button);
        if (user != null) {
            // User is signed in
            dialogs.progressON(this.getActivity(),"이웃 별 보는중...");
            Log.d(TAG, "db반영시간기다림");
            Log.d(TAG, "현재 사용자 인증됨.");
            Cuid = user.getUid();
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

        /*final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
                    Log.d(TAG, "현재 사용자 그룹없음.");
                    view1.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    startDBLoading_for_solo();

                } else {
                    Log.d(TAG, "현재 사용자 그룹있음."+userObj.getG_code());
                    startDBLoading();
                    startsetting();
                }
            }
        }, 1500);

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
                                    ncount = 0;
                                    ocount = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        name_list[ncount] = ""+document.get("name");
                                        String temp = document.getId();
                                        ncount++;
                                        Log.d(TAG, ""+ncount);
                                        // try{
                                        CollectionReference uidRef = db2.collection("user_info");
                                        uidRef.document(""+temp)
                                                .collection("op").document(string2)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                Log.d(TAG, "DocumentSnapshot data: " + document.get("opinion"));
                                                                op_list[ocount] = (""+document.get("opinion"));
                                                                Log.d(TAG, "" + ocount +"배열"+ op_list[ocount]);
                                                                ocount++;
                                                            } else {
                                                                Log.d(TAG, "No such document");
                                                            }
                                                        } else {
                                                            Log.d(TAG, "get failed with ", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                    //catch (Exception e) {
                                    //ocount++;
                                    //Log.d(TAG, "익셉션"+ocount);
                                    //}
                                    // }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        }, 1000);
    }

    private void startDBLoading_for_solo() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "디비로딩솔로.");
                db.collection("user_info").document(""+Cuid)
                        .collection("op").document(""+string2)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.get("opinion"));
                                        name1.setText(userObj.getname());
                                        op1.setText(""+document.get("opinion"));
                                        dialogs.progressOFF();
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
            }
        }, 1000);
    }
    private void startsetting() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogs.progressOFF();
                Log.d(TAG, "셋팅.");
                name1.setText("익명의 별똥별 "+(ncount-1));
                name2.setText("익명의 별똥별 "+(ncount));
                op1.setText(op_list[0]);
                op2.setText(op_list[1]);
            }
        }, 2000);
    }
}