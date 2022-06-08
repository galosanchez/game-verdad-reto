package com.galosanchez.verdadreto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataPreference();
        Funciones.hideSystemUI(this);
        setContentView(R.layout.activity_main);
        //Bloquear rotación de pantalla vertical
        Funciones.setOrientationPortrait(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // pantalla completa
            Funciones.hideSystemUI(this);
        }
    }

    // Método para guardar variables de configuración ( Sonido, Vibración, Idioma )
    private void saveDataPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("KEY_SETTINGS",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SETTINGS_SOUND",getIntent().getBooleanExtra("SETTINGS_SOUND",true));
        editor.putBoolean("SETTINGS_VIBRATION",getIntent().getBooleanExtra("SETTINGS_VIBRATION",true));
        editor.putString("SETTINGS_LANGUAGE",Locale.getDefault().getLanguage());
        editor.commit();

    }

    // Método para obtener variables de configuración ( Sonido, Vibración, Idioma )
    private void loadDataPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("KEY_SETTINGS",Context.MODE_PRIVATE);
        getIntent().putExtra("SETTINGS_SOUND",sharedPreferences.getBoolean("SETTINGS_SOUND",true));
        getIntent().putExtra("SETTINGS_VIBRATION",sharedPreferences.getBoolean("SETTINGS_VIBRATION",true));
        String language = sharedPreferences.getString("SETTINGS_LANGUAGE","default");
        // Asignar idioma
        setLanguage(language);
    }

    // Método para asignar el idioma de la aplicación
    private void setLanguage(String selectdLanguage){
        String language;
        // Asigna el idioma con la locacion por defecto sino asigna con la variable guardada
        if (selectdLanguage.equals("default")){
            if (Locale.getDefault().getLanguage().equals("en")){
                language = "en";
            }else {
                language = "es";
            }
        }else {
            if (selectdLanguage.equals("en")){
                language = "en";
            }else{
                language = "es";
            }
        }
        LocaleHelper.setLocale(this,language);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDataPreference();
    }
}