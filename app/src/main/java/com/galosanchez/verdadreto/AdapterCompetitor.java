package com.galosanchez.verdadreto;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCompetitor extends RecyclerView.Adapter<AdapterCompetitor.ViewHolder> {
    private ArrayList<Competitor> competitors;
    private int layout;
    private AdapterCompetitor.OnItemClickListener itemClickListener;
    private AdapterCompetitor.OnEditTextTouchListener editTextTouchListener;
    private AdapterCompetitor.OnAfterTextChangedListener afterTextChangedListener;

    public AdapterCompetitor(ArrayList<Competitor> competitors, int layout, OnItemClickListener itemClickListener, OnEditTextTouchListener editTextTouchListener, OnAfterTextChangedListener afterTextChangedListener) {
        this.competitors = competitors;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
        this.editTextTouchListener = editTextTouchListener;
        this.afterTextChangedListener = afterTextChangedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        AdapterCompetitor.ViewHolder viewHolder = new AdapterCompetitor.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCompetitor.ViewHolder holder, int position) {
        holder.bind(competitors.get(position),itemClickListener,editTextTouchListener, afterTextChangedListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageViewUser;
        public final EditText editTextNameUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewUser = itemView.findViewById(R.id.imageViewUser);
            this.editTextNameUser = itemView.findViewById(R.id.editTextNameUser);
        }

        public void bind(final Competitor competitor, final AdapterCompetitor.OnItemClickListener listenerItem, final AdapterCompetitor.OnEditTextTouchListener listenerEditTextTouch, final AdapterCompetitor.OnAfterTextChangedListener listenerAfterTextChanged){
            this.editTextNameUser.setText(competitor.getName());

            this.imageViewUser.setColorFilter(ContextCompat.getColor(itemView.getContext(), competitor.getColor()));

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerItem.onItemClick(competitor, getAdapterPosition());
                }
            });

            this.editTextNameUser.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    listenerEditTextTouch.onEditTextTouch();
                    return false;
                }
            });

            this.editTextNameUser.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    listenerAfterTextChanged.onAfterTextChanged(competitor,getAdapterPosition(),editable.toString());
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return competitors.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Competitor competitor, int position);
    }

    public interface OnEditTextTouchListener{
        void onEditTextTouch();
    }

    public interface OnAfterTextChangedListener{
        void onAfterTextChanged(Competitor competitor, int position, String cadena);
    }

}
