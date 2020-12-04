package org.techtown.starmuri;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private View decorView;
    private int	uiOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //softkey and statusbar hide
        {decorView = getWindow().getDecorView();
            uiOption = getWindow().getDecorView().getSystemUiVisibility();
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
                uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility( uiOption );}

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
    //softkey and statusbar hide when restart
    @Override
    protected void onRestart() {
        super.onRestart();
        {decorView = getWindow().getDecorView();
            uiOption = getWindow().getDecorView().getSystemUiVisibility();
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
                uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            decorView.setSystemUiVisibility( uiOption );}
    }

}