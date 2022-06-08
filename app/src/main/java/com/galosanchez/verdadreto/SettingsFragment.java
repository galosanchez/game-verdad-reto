package com.galosanchez.verdadreto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private NavController navController;
    private SwitchCompat switchSound;
    private SwitchCompat switchVibration;
    private Intent intent;
    private Button button;
    private ImageView imageViewiIdiom;
    private boolean stateExpandeIcon;
    private View layoutLanguages;
    private View layoutLanguagesOptions;
    private ImageView imageViewFlagSelect;
    private TextView textViewIdiom;
    private View layout_spanish;
    private View layout_english;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intent = getActivity().getIntent();
        navController = Navigation.findNavController(view);
        ImageView imageViewBack = view.findViewById(R.id.imageViewBack);
        switchSound = view.findViewById(R.id.switchSound);
        switchVibration = view.findViewById(R.id.switchVibration);
        button = view.findViewById(R.id.button);
        imageViewiIdiom = view.findViewById(R.id.imageViewiIdiom);
        layoutLanguages = view.findViewById(R.id.layoutLanguages);
        layoutLanguagesOptions = view.findViewById(R.id.layoutLanguagesOptions);
        imageViewFlagSelect = view.findViewById(R.id.imageViewFlagSelect);
        textViewIdiom = view.findViewById(R.id.textViewIdiom);
        layout_spanish = view.findViewById(R.id.layout_spanish);
        layout_english = view.findViewById(R.id.layout_english);

        loadInstanceState();

        // Combrobar de que pantalla llama a la pantalla de configuraciones (Inicio o Ruleta)
        int FRAGMENT_PREVIOUS = intent.getIntExtra("PREVIOUS_FRAGMENT_SETTINGS", 0);
        if (FRAGMENT_PREVIOUS==1){

            button.setText(R.string.text_button_exit);
            // Acción al presionar el boton Salir
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Salir de la Aplicación
                    getActivity().finish();
                }
            });
        }else {
            button.setText(R.string.text_button_home);
            // Acción al presionar el boton inicio
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Cambiar a la pantalla Inicio
                    Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_homeFragment);
                }
            });
        }

        // acción al cambiar estado de switch de Sonido
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                intent.putExtra("SETTINGS_SOUND",switchSound.isChecked());
            }
        });

        // acción al cambiar estado de switch de Vibración
        switchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                intent.putExtra("SETTINGS_VIBRATION",switchVibration.isChecked());
            }
        });

        // acción al presionar el icono back
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Emula Up o back
                Funciones.pressBack(navController);
            }
        });

        // acción al presionar la sección de idioma
        layoutLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!stateExpandeIcon){
                    imageViewiIdiom.animate().rotation(180f).start();
                    imageViewFlagSelect.setVisibility(View.INVISIBLE);
                    textViewIdiom.setVisibility(View.INVISIBLE);
                    layoutLanguagesOptions.setVisibility(View.VISIBLE);
                    stateExpandeIcon = true;
                }else{
                    imageViewiIdiom.animate().rotation(0f).start();
                    imageViewFlagSelect.setVisibility(View.VISIBLE);
                    textViewIdiom.setVisibility(View.VISIBLE);
                    layoutLanguagesOptions.setVisibility(View.GONE);
                    stateExpandeIcon = false;
                }
            }
        });

        // acción al presionar la opción del idioma español
        layout_spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Locale.getDefault().getLanguage().equals("es")){
                    imageViewiIdiom.animate().rotation(0f).start();
                    imageViewFlagSelect.setVisibility(View.VISIBLE);
                    textViewIdiom.setVisibility(View.VISIBLE);
                    layoutLanguagesOptions.setVisibility(View.GONE);
                    stateExpandeIcon = false;
                    return;
                }
                LocaleHelper.setLocale(getContext(),"es");
                Funciones.pressBack(navController);
            }
        });

        // acción al presionar la opción del idioma inglés
        layout_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Locale.getDefault().getLanguage().equals("en")){
                    imageViewiIdiom.animate().rotation(0f).start();
                    imageViewFlagSelect.setVisibility(View.VISIBLE);
                    textViewIdiom.setVisibility(View.VISIBLE);
                    layoutLanguagesOptions.setVisibility(View.GONE);
                    stateExpandeIcon = false;
                    return;
                }
                LocaleHelper.setLocale(getContext(),"en");
                Funciones.pressBack(navController);
            }
        });

    }

    // Método para cargar la información de configuraciónes
    private void loadInstanceState(){
        if (intent.getBooleanExtra("SETTINGS_SOUND", true)){
            switchSound.setChecked(true);
        }else {
            switchSound.setChecked(false);
        }
        if (intent.getBooleanExtra("SETTINGS_VIBRATION", true)){
            switchVibration.setChecked(true);
        }else{
            switchVibration.setChecked(false);
        }

        if (Locale.getDefault().getLanguage().equals("en")){
            textViewIdiom.setText(R.string.text_language_english);
            imageViewFlagSelect.setImageResource(R.mipmap.ic_flag_english);
        }else{
            textViewIdiom.setText(R.string.text_language_spanish);
            imageViewFlagSelect.setImageResource(R.mipmap.ic_flag_spanish);
        }
    }

}