package com.pjapp.appmascshop.ui.gallery;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.pjapp.appmascshop.Adapters.PedidosAdapter;

import com.pjapp.appmascshop.Adapters.ProductoPedidoAdapter;
import com.pjapp.appmascshop.Model.Pedido;
import com.pjapp.appmascshop.Model.Usuario;
import com.pjapp.appmascshop.R;


import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    RecyclerView recyclerListaPedidosEntregas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Pedido> listaPedidos = new ArrayList<>();
    PedidosAdapter adaptador;

    String rolUsuario,idUsuario;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        rolUsuario = preferences.getString("rolUsuario","none");
        idUsuario = preferences.getString("idUsuarioGeneral","none");

        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();

        if(rolUsuario.equals("Adm")){
            listarDatosAdm();
        }else{
            listarDatosUser();
        }
    }

    private void listarDatosUser() {
        //Toast.makeText(getContext(), "UserId: "+ idUsuario, Toast.LENGTH_SHORT).show();
        databaseReference.child("Pedido").orderByChild("cliente").equalTo(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPedidos.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    //Reconstruimos el objeto pedido
                    Pedido p = item.getValue(Pedido.class);
                    listaPedidos.add(p);
                }

                adaptador = new PedidosAdapter(getContext(),listaPedidos);
                recyclerListaPedidosEntregas.setAdapter(adaptador);
                recyclerListaPedidosEntregas.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error","Desc: "+error);
            }
        });
    }

    private void listarDatosAdm(){
        databaseReference.child("Pedido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPedidos.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    //Reconstruimos el objeto pedido
                    Pedido p = item.getValue(Pedido.class);
                    listaPedidos.add(p);
                }

                adaptador = new PedidosAdapter(getContext(),listaPedidos);
                recyclerListaPedidosEntregas.setAdapter(adaptador);
                recyclerListaPedidosEntregas.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error => ", error.getMessage());
            }
        });
    }

    private void asignarReferencias(View view){
        recyclerListaPedidosEntregas = view.findViewById(R.id.recyclerListaPedidosEntregas);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}