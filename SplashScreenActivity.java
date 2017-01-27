package com.example.torre.circuloapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    public static final int segundos = 6;
    public static final int delay = 2;
    public static final int milisegundos = segundos * 1000;
    ProgressBar barrita;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        barrita = (ProgressBar) findViewById(R.id.pbbarrita);
        barrita.setMax(maximoProgreso());
        empezar();
        //Se configura la pantalla en Vertical
        // Simulate a long loading process on application startup.
    }

    public void empezar() {
        new CountDownTimer(milisegundos, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                barrita.setProgress(establecerProgreso(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    public int establecerProgreso(long miliseconds) {
        return (int) ((milisegundos - miliseconds) / 1000);
    }

    public int maximoProgreso(){
        return segundos - delay;
    }

}
