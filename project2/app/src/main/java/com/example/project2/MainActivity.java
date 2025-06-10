//package com.example.project2;
//
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.Fragment;
//
//import com.example.project2.Fragment.FavoriteFragment;
//import com.example.project2.Fragment.HomeFragment;
//import com.example.project2.Fragment.SettingFragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//            int id = item.getItemId();
//
//            if (id == R.id.nav_home) {
//                selectedFragment = new HomeFragment();
//            } else if (id == R.id.nav_favorite) {
//                selectedFragment = new FavoriteFragment();
//            } else if (id == R.id.nav_setting) {
//                selectedFragment = new SettingFragment();
//            }
//
//
//            return loadFragment(selectedFragment);
//        });
//
//        // Load default HomeFragment
//        loadFragment(new HomeFragment());
//
//
//    }
//
//
//    private boolean loadFragment(Fragment fragment) {
//        if (fragment != null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }
//}
//
package com.example.project2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.project2.Fragment.FavoriteFragment;
import com.example.project2.Fragment.HomeFragment;
import com.example.project2.Fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme sebelum setContentView
        SettingFragment.applyThemeOnStart(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                // Clear fragment state karena user manually pindah
                SettingFragment.clearCurrentFragmentState(this);
            } else if (id == R.id.nav_favorite) {
                selectedFragment = new FavoriteFragment();
                // Clear fragment state karena user manually pindah
                SettingFragment.clearCurrentFragmentState(this);
            } else if (id == R.id.nav_setting) {
                selectedFragment = new SettingFragment();
            }

            return loadFragment(selectedFragment);
        });

        // Check if need to restore fragment state after theme change
        String currentFragment = SettingFragment.getCurrentFragment(this);
        if (currentFragment.equals("setting")) {
            // Load SettingFragment and set bottom nav selection
            loadFragment(new SettingFragment());
            bottomNav.setSelectedItemId(R.id.nav_setting);
            // Clear the state after restore
            SettingFragment.clearCurrentFragmentState(this);
        } else {
            // Load default HomeFragment
            loadFragment(new HomeFragment());
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}