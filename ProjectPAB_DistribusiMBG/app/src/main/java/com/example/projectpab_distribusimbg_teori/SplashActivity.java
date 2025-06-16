package com.example.projectpab_distribusimbg_teori;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 4000;
    private final Handler handler = new Handler();

    private final String[] dotStates = {".", ". .", ". . ."};
    private final int delayDots = 700;

    private TextView loadingDots;
    private int dotIndex = 0;

    private final Runnable dotsRunnable = new Runnable() {
        @Override
        public void run() {
            loadingDots.setText(dotStates[dotIndex % dotStates.length]);
            dotIndex++;
            handler.postDelayed(this, delayDots);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logoSplash);
        loadingDots = findViewById(R.id.loadingDots);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        logo.startAnimation(anim);

        handler.post(dotsRunnable);

        handler.postDelayed(() -> {
            handler.removeCallbacks(dotsRunnable);

            startActivity(new Intent(SplashActivity.this, LandingPage.class));
            finish();
        }, SPLASH_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Pastikan handler stop callback biar gak memory leak
        handler.removeCallbacks(dotsRunnable);
    }
}
