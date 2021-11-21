package com.pjapp.appmascshop.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pjapp.appmascshop.Adapters.CategoriaAdapterUser;
import com.pjapp.appmascshop.Model.Categoria;
import com.pjapp.appmascshop.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    //private FragmentHomeBinding binding;

    RecyclerView recyclerCategoriaUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Categoria> listaCategorias = new ArrayList<>();
    CategoriaAdapterUser adaptador;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();
        listarDatos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
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

                adaptador = new CategoriaAdapterUser(getContext(),listaCategorias);
                recyclerCategoriaUser.setAdapter(adaptador);
                recyclerCategoriaUser.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error => ", error.getMessage());
            }
        });
    }

    private void asignarReferencias(View view){
        recyclerCategoriaUser = view.findViewById(R.id.recyclerProductosCat);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}