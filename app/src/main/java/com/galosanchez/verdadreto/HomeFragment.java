package com.galosanchez.verdadreto;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class HomeFragment extends Fragment {

    private Funciones funciones;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        getActivity().setTheme(R.style.Theme_VerdadOReto);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageViewSettings = view.findViewById(R.id.imageViewSettings);
        ImageView imageViewHome = view.findViewById(R.id.imageViewHome);
        ImageView imageViewAbout = view.findViewById(R.id.imageViewAbout);
        TextView textViewPlay = view.findViewById(R.id.textViewPlay);
        funciones = new Funciones(getActivity());

        // animar textViewPlay
        funciones.animationShowHide(1, 0.15f,1500,textViewPlay);

        // acción al presionar el texto de Continuar
        textViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playGame(view);
            }
        });

        // acción al presionar la imagen de inicio
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playGame(view);
            }
        });

        // acción al presionar el ícono de configuraciones
        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("PREVIOUS_FRAGMENT_SETTINGS",1);
                // navegar hacia aboutFragment
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_settingsFragment);
            }
        });

        // Acción al presionar el ícono de acerca_aplicacion
        imageViewAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navegar hacia aboutFragment
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_aboutFragment);
            }
        });
    }

    // Método que cambia al fragmento de jugadores
    private void playGame(View view){
        // activar sonido
        funciones.soundButton();
        // navegar hacia playersFragment
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_playersFragment);
    }

}