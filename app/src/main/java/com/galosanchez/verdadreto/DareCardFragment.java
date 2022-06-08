package com.galosanchez.verdadreto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DareCardFragment extends Fragment {

    private String[] dateArray;
    private TextView textViewDare;
    private TextView textViewContinue;
    private NavController navController;

    public DareCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateArray = getResources().getStringArray(R.array.dare_array);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dare_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        textViewDare = view.findViewById(R.id.textViewDare);
        textViewContinue = view.findViewById(R.id.textViewContinue);
        textViewDare.setText(Funciones.getPhrase(dateArray));

        // acción al el texto para Continuar
        textViewContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // volver al fragment anterior
                Funciones.pressBack(navController);
            }
        });
    }

}