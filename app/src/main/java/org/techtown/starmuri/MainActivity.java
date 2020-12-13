package org.techtown.starmuri;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.techtown.starmuri.link.BookObj;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {
    Intent intent_from_login;
    BookObj this_week;
    FragmentTransaction fragmentTransaction;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //로그인 액티비티에서 인텐트로 넘겨준 BookObj 클래스 객체를 받는 부분
        intent_from_login = getIntent();
        this_week = (BookObj) intent_from_login.getSerializableExtra("OBJECT");
        Log.d(TAG, "인텐트로 잘 받아졌다면 책 제목을 : "+this_week.getBook());

        //프래그먼트에 전달해줄 번들 생성.
        final Bundle bundle = new Bundle();
        bundle.putString("book", this_week.getBook());
        bundle.putString("topic", this_week.getTopic());
        bundle.putString("bookcode", this_week.getBookcode());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_option, R.id.navigation_counter,
                R.id.navigation_group)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // 홈프래그먼트에 번들 전달.
        navController.navigate(R.id.navigation_home, bundle);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        //로그아웃 메소드
        /*@Override
        public void onClick(View view) {
            if(view == buttonLogout)
                firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }*/
    }

}

