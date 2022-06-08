package com.galosanchez.verdadreto;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class RouletteFragment extends Fragment {

    private NavController navController;
    private ArrayList<Competitor> competitors;
    private ImageView imageViewBottle;
    private ImageView imageViewRoulette;
    private ImageView imageViewSettings;
    private TextView textViewPlayer;
    private Button buttonTruth;
    private Button buttonDare;
    private Funciones funciones;
    private Intent intent;
    private Bundle saveState = new Bundle();
    private boolean animateBottle;


    public RouletteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Funciones.pressBack(navController);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_roulette, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intent = getActivity().getIntent();
        loadListCompetitors();
        navController = Navigation.findNavController(view);
        imageViewBottle = view.findViewById(R.id.imageViewBottle);
        imageViewSettings = view.findViewById(R.id.imageViewSettings);
        textViewPlayer = view.findViewById(R.id.textViewPlayer);
        imageViewRoulette = view.findViewById(R.id.imageViewRoulette);
        buttonTruth = view.findViewById(R.id.buttonTruth);
        buttonDare = view.findViewById(R.id.buttonDare);
        funciones= new Funciones(getActivity());

        // cargar el angulo a la imagen Bottle si ya a sido girada
        loadInstanceState();

        // asignar la imagen de la ruleta
        selectImageRoulette(competitors.size());

        // acción al presionar la imagen de la botella
        imageViewBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinRoulette();
            }
        });

        // acción al presionar el boton Verdad
        buttonTruth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveState.putInt("CARD_TYPE",0);
                saveState.putString("NAME_PLAYER",null);
                openFragmentCard(true,view);
            }
        });

        // acción al presionar el boton Reto
        buttonDare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveState.putInt("CARD_TYPE",0);
                saveState.putString("NAME_PLAYER",null);
                openFragmentCard(false, view);
            }
        });

        // acción al presionar el ícono de configuraciones
        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("PREVIOUS_FRAGMENT_SETTINGS",2);
                Navigation.findNavController(view).navigate(R.id.action_rouletteFragment_to_settingsFragment);
            }
        });

    }

    // Método para cargar los datos guardados de la configuración
    private void loadInstanceState() {
        RotateAnimation rotar = new RotateAnimation(angulo%360,angulo%360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotar.setFillAfter(true);
        rotar.setDuration(0);
        imageViewBottle.startAnimation(rotar);

        if (!animateBottle){
            // animar imagen de la botella
            funciones.animationShowHide(1, 0.40f,600,imageViewBottle);
            animateBottle = true;
        }

        // asignar estado guardado a los componentes
        if (saveState.getInt("CARD_TYPE",0) == 1){
            setStyleButtonSelected(buttonTruth, R.color.truth);
        }
        if (saveState.getInt("CARD_TYPE",0) == 2){
            setStyleButtonSelected(buttonDare, R.color.dare);
        }

        if (saveState.getString("NAME_PLAYER",null) != null){
            textViewPlayer.setText(saveState.getString("NAME_PLAYER"));
        }

    }

    // Método para abrir el fragmento de la targeta
    private void openFragmentCard(boolean cardType, View view) {
        restablecer = false;
        if (cardType){
            Navigation.findNavController(view).navigate(R.id.action_rouletteFragment_to_truthCardFragment);
            setStyleDisable(buttonTruth);
        }else{
            Navigation.findNavController(view).navigate(R.id.action_rouletteFragment_to_dareCardFragment);
            setStyleDisable(buttonDare);
        }
    }

    // Método para asignar la imagen de la ruleta dependiendo del número de jugadores
    private void selectImageRoulette(int numberPlayers) {
        int image = 0;
        switch (numberPlayers){
            case 2:
                image = R.mipmap.img_roulette_2users;
                break;
            case 3:
                image = R.mipmap.img_roulette_3users;
                break;
            case 4:
                image = R.mipmap.img_roulette_4users;
                break;
            case 5:
                image = R.mipmap.img_roulette_5users;
                break;
            case 6:
                image = R.mipmap.img_roulette_6users;
                break;
            case 7:
                image = R.mipmap.img_roulette_7users;
                break;
            case 8:
                image = R.mipmap.img_roulette_8users;
                break;
            default:
                //image = 0;
        }
        if (image != 0){
            imageViewRoulette.setImageResource(image);
        }
    }

    private Random random = new Random();
    private boolean restablecer;
    private int angulo = 0;

    // Metodo para rotar la botella
    private void spinRoulette(){
        if(restablecer)
            return;

        restablecer = true;
        int aux = angulo % 360;
        angulo = random.nextInt(3600)+360;
        RotateAnimation rotar = new RotateAnimation(aux,angulo,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotar.setFillAfter(true);
        rotar.setDuration(3600);
        rotar.setInterpolator(new AccelerateDecelerateInterpolator());
        imageViewBottle.startAnimation(rotar);


        rotar.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

                // Vibrar dispositivo
                funciones.vibrate();

                // obtener el participante seleccionado
                int playerSelected = findPlayerSelected(angulo % 360, competitors.size());
                if (playerSelected<1)
                    return;

                // asignar en nombre del participante al textViewPlayer
                textViewPlayer.setText(competitors.get(playerSelected-1).getName());
                saveState.putString("NAME_PLAYER",competitors.get(playerSelected-1).getName());

                // obtener una opcion al azal true o false
                if (opcionRandom()){
                    saveState.putInt("CARD_TYPE",1);
                    setStyleButtonSelected(buttonTruth, R.color.truth);

                }else{
                    saveState.putInt("CARD_TYPE",2);
                    setStyleButtonSelected(buttonDare, R.color.dare);
                }

            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }


    // Método para cambiar estilo de boton seleccionado
    private void setStyleButtonSelected(Button button, int color){
        button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(color)));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        button.setEnabled(true);
    }

    // Método para cambiar estilo de boton desabilitado
    private void setStyleDisable(Button button){
        button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_700)));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_38));
        button.setEnabled(false);
    }

    // Método para obtener al jugador dependiendo del angulo de la imagen de la botella
    private int findPlayerSelected(int anguloSelected, int numberPlayers){
        int selected = 0;
        switch (numberPlayers){
            case 2 :
                if (anguloSelected <= 270)
                    selected = 1;
                else
                    selected = 2;
                break;
            case 3:
                if (anguloSelected <= 120){
                    selected = 2;
                }else if (anguloSelected <= 240){
                    selected = 3;
                }else{
                    selected = 1;
                }
                break;
            case 4:
                if (anguloSelected <= 90){
                    selected = 2;
                }else if (anguloSelected <= 180){
                    selected = 3;
                }else if(anguloSelected <= 270){
                    selected = 4;
                }else{
                    selected = 1;
                }
                break;
            case 5:
                if (anguloSelected <= 72){
                    selected = 2;
                }else if (anguloSelected <= 144){
                    selected = 3;
                }else if(anguloSelected <= 216){
                    selected = 4;
                }else if(anguloSelected <= 287){
                    selected = 5;
                }else{
                    selected = 1;
                }
                break;
            case 6:
                if (anguloSelected <= 60){
                    selected = 2;
                }else if (anguloSelected <= 120){
                    selected = 3;
                }else if(anguloSelected <= 180){
                    selected = 4;
                }else if(anguloSelected <= 240){
                    selected = 5;
                }else if(anguloSelected <= 300){
                    selected = 6;
                }else{
                    selected = 1;
                }
                break;
            case 7:
                if (anguloSelected <= 51){
                    selected = 2;
                }else if (anguloSelected <= 103){
                    selected = 3;
                }else if(anguloSelected <= 154){
                    selected = 4;
                }else if(anguloSelected <= 205){
                    selected = 5;
                }else if(anguloSelected <= 257){
                    selected = 6;
                }else if(anguloSelected <= 308){
                    selected = 7;
                }else{
                    selected = 1;
                }
                break;
            case 8:
                if (anguloSelected <= 45){
                    selected = 2;
                }else if (anguloSelected <= 90){
                    selected = 3;
                }else if(anguloSelected <= 135){
                    selected = 4;
                }else if(anguloSelected <= 180){
                    selected = 5;
                }else if(anguloSelected <= 225){
                    selected = 6;
                }else if(anguloSelected <= 270){
                    selected = 7;
                }else if(anguloSelected <= 315){
                    selected = 8;
                }else{
                    selected = 1;
                }
                break;
            default:
                //selected = 0;
        }
        return selected;
    }

    // Método para ontener un booleano al azar
    private boolean opcionRandom(){
        Random random = new Random();
        int num = random.nextInt();
        System.out.println(num);
        if (num % 2 == 0) {
            return true;
        }
        return false;
    }

    // Método para recuperar la lista de jugadores ingresados en el caso que exista
    private void loadListCompetitors() {
        intent =getActivity().getIntent();
        if (intent.getSerializableExtra("LIST_COMPETITORS") != null){
            competitors = (ArrayList<Competitor>) intent.getSerializableExtra("LIST_COMPETITORS");
        }

    }

}