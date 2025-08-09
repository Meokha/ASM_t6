package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.appcompat.app.AlertDialog; // Thêm import AlertDialog
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    Intent intent;
    Bundle bundle;
    TextView tvUsername;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        navigationView = findViewById(R.id.nav_view);

        intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            String username = bundle.getString("USER_ACCOUNT", "");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setupViewPager();
        Menu menu = navigationView.getMenu();
        MenuItem itemLogout = menu.findItem(R.id.menu_logout);

        // === SỬA LẠI LOGIC ĐĂNG XUẤT CHO AN TOÀN HƠN ===
        itemLogout.setOnMenuItemClickListener(item -> {
            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("LogOut")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("LogOut", (dialog, which) -> {
                        // Xóa dữ liệu đăng nhập đã lưu
                        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        prefs.edit().clear().apply();

                        // Quay lại trang Login và xóa các màn hình cũ
                        Intent loginIntent = new Intent(MainMenuActivity.this, LoginActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
        // =======================================================

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                viewPager2.setCurrentItem(0);
            } else if (item.getItemId() == R.id.menu_expenses) {
                viewPager2.setCurrentItem(1);
            } else if (item.getItemId() == R.id.menu_budgets) {
                viewPager2.setCurrentItem(2);
            } else if (item.getItemId() == R.id.menu_report) {
                viewPager2.setCurrentItem(3);
            } else if (item.getItemId() == R.id.menu_settings) {
                viewPager2.setCurrentItem(4);
            }
            return true;
        });

        // Gọi hàm yêu cầu quyền thông báo
        requestNotificationPermission();
    }

    // === THÊM CÁC HÀM XỬ LÝ QUYỀN THÔNG BÁO VÀO ĐÂY ===
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notifications enabled!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You will not receive spending notifications.", Toast.LENGTH_LONG).show();
            }
        }
    }
    // ===============================================================

    private void setupViewPager() {
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                } else if (position == 1) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_expenses).setChecked(true);
                } else if (position == 2) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_budgets).setChecked(true);
                } else if (position == 3) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_report).setChecked(true);
                } else if (position == 4) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_settings).setChecked(true);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_home) {
            viewPager2.setCurrentItem(0);
        } else if (item.getItemId() == R.id.menu_expenses) {
            viewPager2.setCurrentItem(1);
        } else if (item.getItemId() == R.id.menu_budgets) {
            viewPager2.setCurrentItem(2);
        } else if (item.getItemId() == R.id.menu_report) {
            viewPager2.setCurrentItem(3);
        } else if (item.getItemId() == R.id.menu_settings) {
            viewPager2.setCurrentItem(4);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}