package com.pjapp.appmascshop.ui;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pjapp.appmascshop.Adapters.ProductoAdapter;
import com.pjapp.appmascshop.Adapters.ProductoCategoriaAdapter;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.Productos;
import com.pjapp.appmascshop.R;
import com.pjapp.appmascshop.ui.admin.DetalleProductoAdm;
import com.pjapp.appmascshop.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class DetalleProducto extends Fragment {

    ImageButton btnBackProd;
    TextView txtNombCategoria;
    RecyclerView recyclerProductosCat;

    String idCategoria;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Productos> listaProductos = new ArrayList<>();
    ProductoCategoriaAdapter adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_producto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();
        recuperarDatosEnviados();
        listarDatos();
    }

    private void recuperarDatosEnviados() {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            Toast.makeText(getContext(),"No hay datos para mostrar",Toast.LENGTH_SHORT).show();
            return;
        }else{

            idCategoria = datosRecuperados.getString("nombCategoria");
            txtNombCategoria.setText(datosRecuperados.getString("nombCategoria"));

        }
    }

    private void listarDatos(){

        databaseReference.child("Productos").orderByChild("categoria").equalTo(idCategoria).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaProductos.clear();
                int cont = 0;
                for(DataSnapshot item: snapshot.getChildren()){
                    cont++;
                    Productos p = item.getValue(Productos.class);
                    listaProductos.add(p);
                }

                if (cont<=0){
                    Toast.makeText(getContext(), "No hay productos para mostrar", Toast.LENGTH_SHORT).show();
                }

                adaptador = new ProductoCategoriaAdapter(getContext(),listaProductos);
                recyclerProductosCat.setAdapter(adaptador);
                recyclerProductosCat.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error => ", error.getMessage());
            }
        });
    }

    private void asignarReferencias(View view){
        recyclerProductosCat = view.findViewById(R.id.recyclerProductosCat);
        txtNombCategoria = view.findViewById(R.id.txtNombCategoria);
        btnBackProd = view.findViewById(R.id.btnBackProd);

        btnBackProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                Fragment newFragment = new HomeFragment();
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