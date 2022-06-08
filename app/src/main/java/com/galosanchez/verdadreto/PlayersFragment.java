package com.galosanchez.verdadreto;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayersFragment extends Fragment {

    private NavController navController;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Competitor> competitors = new ArrayList<Competitor>();
    private ArrayList<Competitor> listCompetitorsOriginal = new ArrayList<Competitor>();
    private String nameCompetitorDefault;
    private FloatingActionButton floatingButtonAdd;
    private FloatingActionButton floatingButtonRemove;
    private Funciones funciones;


    public PlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack(navController);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_players, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListCompetitors();
        AdapterRecyclerView(view);
        navController = Navigation.findNavController(view);
        ImageView imageViewBack = view.findViewById(R.id.imageViewBack);
        floatingButtonAdd = view.findViewById(R.id.floatingButtonAdd);
        floatingButtonRemove = view.findViewById(R.id.floatingButtonRemove);
        Button button_next = view.findViewById(R.id.button_next);
        funciones = new Funciones(getActivity());



        // acción al presionar el boton de agregar jugador
        floatingButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCompetitor();
            }
        });

        // acción al presionar el boton de remover jugador
        floatingButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCompetitor();
            }
        });

        // acción al presionar el boton siguiente
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funciones.soundButton();
                saveDataIntent();
                // pantalla completa
                Funciones.hideSystemUI(getActivity());
                Navigation.findNavController(view).navigate(R.id.action_playersFragment_to_rouletteFragment);
            }
        });

        // acción al presionar el ícono Atras
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Emula Up o back
                goBack(navController);
            }
        });
    }

    // Método para construir el RecyclerView
    private void AdapterRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerViewCompetitors);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AdapterCompetitor(competitors, R.layout.layout_edit_user, new AdapterCompetitor.OnItemClickListener() {
            @Override
            public void onItemClick(Competitor competitor, int position) {
                Toast.makeText(getActivity(), competitor.getName() + " - " + position , Toast.LENGTH_SHORT).show();
            }
        }, new AdapterCompetitor.OnEditTextTouchListener() {
            @Override
            public void onEditTextTouch() {
                hideSystemUINavegation();
            }
        }, new AdapterCompetitor.OnAfterTextChangedListener() {
            @Override
            public void onAfterTextChanged(Competitor competitor, int position, String cadena) {
                if (cadena.isEmpty()){
                    competitors.get(position).setName(nameCompetitorDefault+(position+1));
                    return;
                }
                competitors.get(position).setName(cadena);
            }
        });

        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    // Método para agregar un nuevo jugador
    private void addCompetitor(){
        int position = competitors.size();
        if (position >= 8){
            Toast.makeText(getActivity(), R.string.text_max_players, Toast.LENGTH_LONG).show();
            return;
        }
        Competitor competitor = listCompetitorsOriginal.get(0);
        listCompetitorsOriginal.remove(0);
        competitor.setName(nameCompetitorDefault+(position+1));
        competitors.add(position,competitor);
        mAdapter.notifyItemInserted(position);
        //mLayoutManager.scrollToPosition();
    }

    // Método para remover un jugador
    private void removeCompetitor(){
        int position = competitors.size();
        if (position <= 2){
            Toast.makeText(getActivity(), R.string.text_min_players, Toast.LENGTH_LONG).show();
            return;
        }
        position--;
        listCompetitorsOriginal.add(0,competitors.get(position));
        competitors.remove(position);
        mAdapter.notifyItemRemoved(position);
    }


    // Método que cargo los datos de competidores por defecto
    private void loadListCompetitors() {
        // crear competidores por defecto
        nameCompetitorDefault = getString( R.string.player_name_default);
        listCompetitorsOriginal.add(new Competitor("",R.color.competitor_1));
        listCompetitorsOriginal.add(new Competitor("",R.color.competitor_2));
        listCompetitorsOriginal.add(new Competitor("",R.color.competitor_3));
        listCompetitorsOriginal.add(new Competitor("",R.color.competitor_4));
        listCompetitorsOriginal.add(new Competitor("",R.color.competitor_5));
        listCompetitorsOriginal.add(new Competitor("",R.color.competitor_6));
        listCompetitorsOriginal.add(new Competitor("",R.color.competitor_7));
        listCompetitorsOriginal.add(new Competitor("",R.color.competitor_8));

        Intent intent =getActivity().getIntent();
        // verificar si existe competidores ya existentes
        if (intent.getSerializableExtra("LIST_COMPETITORS") != null){
            ArrayList<Competitor> listCompetitors = (ArrayList<Competitor>) intent.getSerializableExtra("LIST_COMPETITORS");
            competitors= listCompetitors;
            for (int x = 0; x<listCompetitors.size(); x++){
                listCompetitorsOriginal.remove(0);
            }
        }else {
            listCompetitorsOriginal.remove(0);
            listCompetitorsOriginal.remove(0);
            competitors.add(new Competitor(nameCompetitorDefault+"1", R.color.competitor_1));
            competitors.add(new Competitor(nameCompetitorDefault+"2", R.color.competitor_2));
        }

    }

    // Método para cambiar el modo de pantalla completa (aparece la barra inferiores del sistema)
    private void hideSystemUINavegation() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // Método para volver a la anterior fragmento grardando la información de los jugadores
    private void goBack(NavController navController){
        Funciones.hideSystemUI(getActivity());
        saveDataIntent();
        Funciones.pressBack(navController);
    }

    // Método para guardar la información de la lista de jugadores
    private void saveDataIntent(){
        Intent intent =getActivity().getIntent();
        intent.putExtra("LIST_COMPETITORS", competitors);
        getActivity().setIntent(intent);
    }

}