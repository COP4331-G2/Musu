package musuapp.com.musu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public static final String apiURL = "http://www.musuapp.com/API/API.php";
    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 0;
    private static int currentUserID;
    SharedPreferences access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent splash = getIntent();
        currentUserID = 0;

        if((MainActivity.currentUserID = splash.getIntExtra("userID", 0)) == -1) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.title_personal)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.title_latest)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.title_groups)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.title_settings)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void createNewPost(View view)
    {
        Intent intent = new Intent(this, CreateNewPost.class);
        intent.putExtra("userID", MainActivity.currentUserID);
        startActivity(intent);
    }

    public void logout(View view)
    {
        access = getSharedPreferences("Login", MODE_PRIVATE);

        SharedPreferences.Editor editor = access.edit();
        //editor.putString("username", "");
        //editor.putString("password", "");
        //editor.putInt("userID", -1);
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
    }

    // This method is called when the second activity finishes

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the LoginActivity with an OK result
        if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // get data from Intent
                MainActivity.currentUserID = data.getIntExtra("userID", 0);

                Log.e("currentUserID", String.valueOf(MainActivity.currentUserID));
            }
        }
    }
}