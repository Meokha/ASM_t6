package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.adapters.ViewPageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    Intent intent;
    Bundle bundle;
    TextView tvUsername;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        navigationView = findViewById(R.id.nav_view);
       // tvUsername = findViewById(R.id.tvUsername);
        intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null){
            String username = bundle.getString("USER_ACCOUNT", "");
            // tvUsername.setText("Hi : " + username);
        }

        // xu ly hien thi Drawer menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setupViewPager();// goi ham
        Menu menu = navigationView.getMenu();
        MenuItem itemLogout = menu.findItem(R.id.menu_logout);
        // xu ly bam logout
        itemLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                // remove data ma Login ban sang
                if(bundle != null){
                    intent.removeExtra("USERNAME_ACCOUNT");
                    intent.removeExtra("ID_ACCOUNT");
                    intent.removeExtra("EMAIL_ACCOUNT");
                    intent.removeExtra("ROLE_ACCOUNT");
                }
                // quay lai trang login
                Intent login = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(login);
                finish(); // khong cho back tro lai
                return false;
            }
        });

        // xu ly click vao cac tab bottom navigation
        bottomNavigationView.setOnItemSelectedListener(item ->  {
            if (item.getItemId() == R.id.menu_home){
                viewPager2.setCurrentItem(0);
            }else if (item.getItemId() == R.id.menu_expenses){
                viewPager2.setCurrentItem(1);
            }else if (item.getItemId() == R.id.menu_budgets) {
                viewPager2.setCurrentItem(2);
            }else if (item.getItemId() == R.id.menu_report){
                viewPager2.setCurrentItem(3);
            }else if (item.getItemId() == R.id.menu_settings){
                viewPager2.setCurrentItem(4);
            }else {
                viewPager2.setCurrentItem(0);
            }
            return true;
        });
    }
    private void setupViewPager(){
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager2.setAdapter(adapter);
        //xu ly khi vuot man hinh chuyen tab
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0){
                    bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                }else if (position == 1){
                    bottomNavigationView.getMenu().findItem(R.id.menu_expenses).setChecked(true);
                }else if (position == 2){
                    bottomNavigationView.getMenu().findItem(R.id.menu_budgets).setChecked(true);
                }else if (position == 3){
                    bottomNavigationView.getMenu().findItem(R.id.menu_report).setChecked(true);
                }else if (position == 4){
                    bottomNavigationView.getMenu().findItem(R.id.menu_settings).setChecked(true);
                }else {
                    bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_home){
            viewPager2.setCurrentItem(0);
        }else if (item.getItemId() == R.id.menu_expenses){
            viewPager2.setCurrentItem(1);
        }else if (item.getItemId() == R.id.menu_budgets){
            viewPager2.setCurrentItem(2);
        }else if (item.getItemId() == R.id.menu_report){
            viewPager2.setCurrentItem(3);
        }else if (item.getItemId() == R.id.menu_settings){
            viewPager2.setCurrentItem(4);
        }
        drawerLayout.closeDrawer(GravityCompat.START); // dong menu lai
        return true;
    }
}
