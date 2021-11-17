package com.pjapp.appmascshop.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pjapp.appmascshop.Adapters.CategoriaAdapter;
import com.pjapp.appmascshop.LoginActivity;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.Categoria;
import com.pjapp.appmascshop.R;
import com.pjapp.appmascshop.databinding.FragmentCategoriaProductoBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoriaProducto extends Fragment {


    RecyclerView recyclerCategorias;
    Button btnNewcategoria;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Categoria> listaCategorias = new ArrayList<>();
    CategoriaAdapter adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categoria_producto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();
        listarDatos();
    }

    private void listarDatos(){
        //Nodo Categorias
        databaseReference.child("Categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCategorias.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    //Reconstruimos el objeto categoria
                    Categoria c = item.getValue(Categoria.class);
                    listaCategorias.add(c);
                }

                adaptador = new CategoriaAdapter(getContext(),listaCategorias);
                recyclerCategorias.setAdapter(adaptador);
                recyclerCategorias.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error => ", error.getMessage());
            }
        });
    }

    private void asignarReferencias(View view){
        recyclerCategorias = view.findViewById(R.id.recyclerCategorias);
        btnNewcategoria = view.findViewById(R.id.btnNewcategoria);
        btnNewcategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                Fragment newFragment = new DetalleCategoriaProducto();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}