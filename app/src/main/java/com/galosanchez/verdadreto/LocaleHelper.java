package com.galosanchez.verdadreto;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleHelper {

    // Método utilizado para establecer el idioma en tiempo de ejecución
    public static void setLocale(Context context, String language) {
        updateResources(context, language);

    }

    // Método actualiza el lenguage de la aplicaciónthe creada
    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

}