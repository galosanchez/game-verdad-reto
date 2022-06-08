package com.galosanchez.verdadreto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.navigation.NavController;

import java.util.Random;

public class Funciones {

    private Activity activityParent;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private Intent intent;

    public Funciones(Activity activityParent){
        this.activityParent = activityParent;
        this.intent = activityParent.getIntent();
    }

    // Método para vibrar el dispositivo
    public void vibrate(){
        if (!intent.getBooleanExtra("SETTINGS_VIBRATION",true)) return;
        vibrator = (Vibrator)activityParent.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    // Método para reproducir el sonido de un boton
    public void soundButton(){
        if (!intent.getBooleanExtra("SETTINGS_SOUND",true)) return;
        mediaPlayer = MediaPlayer.create(activityParent,R.raw.sound_button);
        mediaPlayer.start();
    }

    /*
    public void clearnMediaPlayer(){
        mediaPlayer.release();
        mediaPlayer = null;
    }
    */

    // Método para mostrar/ocultar una vista con animación
    public void animationShowHide(float startVisible, float endVisible, long duration, View view){
        Animation animation = new AlphaAnimation( startVisible,endVisible);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }

    // Método para cerrar Fragment
    public static void pressBack(NavController navController){
        //navController.popBackStack();
        navController.navigateUp();
    }

    // Método para obtener una frase al azar del arreglo de verdades o retos
    public static String getPhrase(String[] arrayPhrases){
        int min = 1;
        int max = arrayPhrases.length;
        Random random = new Random();
        int value = random.nextInt((max-1) + min) + min;
        return arrayPhrases[value-1];
    }

    // Método para asignar la orientacion vertical
    public static void setOrientationPortrait(Activity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // Método para habilitar el modo de pantalla completa
    public static void hideSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}
