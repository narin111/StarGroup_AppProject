package org.techtown.starmuri;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.techtown.starmuri.Dialogs.Custom_Dialog_readme;
import org.techtown.starmuri.link.BookObj;


public class MainActivity extends AppCompatActivity {
    Intent intent_from_login;
    BookObj this_week;
    FragmentTransaction fragmentTransaction;
    private FirebaseAuth firebaseAuth;
    //private Button readme;


    Button book_title;
    //readme = root.findViewById(R.id.question);
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.helpmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Custom_Dialog_readme CDr = new Custom_Dialog_readme(this);
        CDr.callDialog();
        return super.onOptionsItemSelected(item);

    }

    public void ShowDialog(){

    }
//    show_code.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            System.out.println("그룹 초대 버튼 클릭");
//            CD.Go_Dialog(userObj);
//        }
//    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_option, R.id.navigation_counter,
                R.id.navigation_group)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }





}

