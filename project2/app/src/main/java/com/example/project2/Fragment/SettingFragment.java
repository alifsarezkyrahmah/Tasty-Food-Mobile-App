package com.example.project2.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.example.project2.R;

public class SettingFragment extends Fragment {

    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_THEME_MODE = "theme_mode";

    // Theme modes
    private static final int THEME_LIGHT = 0;
    private static final int THEME_DARK = 1;
    private static final int THEME_AUTO = 2;

    private RadioGroup radioGroupTheme;
    private RadioButton radioLight, radioDark, radioAuto;
    private TextView tvCurrentTheme, tvThemeTitle, tvThemeDescription;
    private ImageView ivThemeIcon;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        loadSettings();
        setupListeners();
        updateThemeDisplay();
    }

    private void initViews(View view) {
        radioGroupTheme = view.findViewById(R.id.radio_group_theme);
        radioLight = view.findViewById(R.id.radio_light_theme);
        radioDark = view.findViewById(R.id.radio_dark_theme);
        radioAuto = view.findViewById(R.id.radio_auto_theme);
        tvCurrentTheme = view.findViewById(R.id.tv_current_theme);
        tvThemeTitle = view.findViewById(R.id.tv_theme_title);
        tvThemeDescription = view.findViewById(R.id.tv_theme_description);
        ivThemeIcon = view.findViewById(R.id.iv_theme_icon);
    }

    private void loadSettings() {
        // Load theme mode setting
        int themeMode = sharedPreferences.getInt(KEY_THEME_MODE, THEME_AUTO);
        switch (themeMode) {
            case THEME_LIGHT:
                radioLight.setChecked(true);
                break;
            case THEME_DARK:
                radioDark.setChecked(true);
                break;
            case THEME_AUTO:
            default:
                radioAuto.setChecked(true);
                break;
        }
    }

    private void setupListeners() {
        // Radio Group Theme Listener
        radioGroupTheme.setOnCheckedChangeListener((group, checkedId) -> {
            int themeMode = THEME_AUTO;

            if (checkedId == R.id.radio_light_theme) {
                themeMode = THEME_LIGHT;
            } else if (checkedId == R.id.radio_dark_theme) {
                themeMode = THEME_DARK;
            } else if (checkedId == R.id.radio_auto_theme) {
                themeMode = THEME_AUTO;
            }

            saveThemeModeSetting(themeMode);
            applyThemeMode(themeMode);
            updateThemeDisplay();
        });
    }

    private void saveThemeModeSetting(int themeMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_THEME_MODE, themeMode);
        editor.apply();
    }

    private void applyThemeMode(int themeMode) {
        // Simpan current fragment state sebelum apply theme
        saveCurrentFragmentState();

        switch (themeMode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_AUTO:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    private void saveCurrentFragmentState() {
        // Simpan bahwa user sedang di SettingFragment
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_fragment", "setting");
        editor.apply();
    }

    private void updateThemeDisplay() {
        int currentThemeMode = sharedPreferences.getInt(KEY_THEME_MODE, THEME_AUTO);

        switch (currentThemeMode) {
            case THEME_LIGHT:
                ivThemeIcon.setImageResource(R.drawable.ic_day_mode);
                tvThemeTitle.setText("Mode Terang");
                tvThemeDescription.setText("Menggunakan tema terang untuk tampilan yang cerah");
                tvCurrentTheme.setText("Tema saat ini: Terang");
                break;
            case THEME_DARK:
                ivThemeIcon.setImageResource(R.drawable.ic_dark_mode);
                tvThemeTitle.setText("Mode Gelap");
                tvThemeDescription.setText("Menggunakan tema gelap untuk kenyamanan mata");
                tvCurrentTheme.setText("Tema saat ini: Gelap");
                break;
            case THEME_AUTO:
            default:
                ivThemeIcon.setImageResource(R.drawable.ic_auto_mode);
                tvThemeTitle.setText("Mode Otomatis");
                tvThemeDescription.setText("Mengikuti pengaturan sistem perangkat Anda");
                tvCurrentTheme.setText("Tema saat ini: Ikuti Sistem");
                break;
        }
    }

    // Method untuk mendapatkan current fragment (bisa dipanggil dari Activity lain)
    public static String getCurrentFragment(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("current_fragment", "home");
    }

    // Method untuk clear current fragment state
    public static void clearCurrentFragmentState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("current_fragment");
        editor.apply();
    }

    // Method untuk mendapatkan theme mode (bisa dipanggil dari Activity lain)
    public static int getThemeMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME_MODE, THEME_AUTO);
    }

    // Method untuk apply theme saat aplikasi start
    public static void applyThemeOnStart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int themeMode = prefs.getInt(KEY_THEME_MODE, THEME_AUTO);

        switch (themeMode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_AUTO:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}