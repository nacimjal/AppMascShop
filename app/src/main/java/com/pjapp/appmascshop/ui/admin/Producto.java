package com.pjapp.appmascshop.ui.admin;

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
import com.pjapp.appmascshop.Adapters.ProductoAdapter;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.Categoria;
import com.pjapp.appmascshop.Model.Productos;
import com.pjapp.appmascshop.R;

import java.util.ArrayList;
import java.util.List;

public class Producto extends Fragment {

    RecyclerView recyclerProductosAdm;
    Button btnNewProducto;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Productos> listaProductos = new ArrayList<>();
    ProductoAdapter adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_producto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();
        listarDatos();
    }
    private void listarDatos(){
        //Nodo Categorias
        databaseReference.child("Productos").orderByChild("codigo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaProductos.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    Productos p = item.getValue(Productos.class);
                    listaProductos.add(p);
                }

                adaptador = new ProductoAdapter(getContext(),listaProductos);
                recyclerProductosAdm.setAdapter(adaptador);
                recyclerProductosAdm.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error => ", error.getMessage());
            }
        });
    }

    private void asignarReferencias(View view){
        recyclerProductosAdm = view.findViewById(R.id.recyclerProductosAdm);
        btnNewProducto = view.findViewById(R.id.btnNewProducto);
        btnNewProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                Fragment newFragment = new DetalleProductoAdm();
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