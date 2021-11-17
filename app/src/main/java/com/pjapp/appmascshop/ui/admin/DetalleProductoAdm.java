package com.pjapp.appmascshop.ui.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pjapp.appmascshop.R;

public class DetalleProductoAdm extends Fragment {

    EditText textCodigoProducto,textNombreProducto,textDescProducto,textPrecProducto;
    Spinner spinnerCategorias;
    Button btnRegistrarProducto;

    String codigoProducto,nombreProducto,descProducto,precProducto,idCategoria;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Boolean registra = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_producto_adm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();
        //verificarRegistraActualiza();
    }

    private void asignarReferencias(View view){
        textCodigoProducto = view.findViewById(R.id.textCodigoProducto);
        textNombreProducto = view.findViewById(R.id.textNombreProducto);
        textDescProducto = view.findViewById(R.id.textDescProducto);
        textPrecProducto = view.findViewById(R.id.textPrecProducto);
        spinnerCategorias = view.findViewById(R.id.spinnerCategorias);

        btnRegistrarProducto = view.findViewById(R.id.btnRegistrarProducto);

        btnRegistrarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    if (registra){
                        registrarProducto();
                    }else{
                        actualizarProducto();
                    }
                }
            }
        });
    }

    private void registrarProducto(){
        Toast.makeText(getContext(), "Registrar producto", Toast.LENGTH_SHORT).show();
    }

    private void actualizarProducto(){
        Toast.makeText(getContext(), "Registrar producto", Toast.LENGTH_SHORT).show();
    }

    private boolean validarCampos(){
        boolean resultado = true;

        codigoProducto = textCodigoProducto.getText().toString();
        nombreProducto = textNombreProducto.getText().toString();
        descProducto = textDescProducto.getText().toString();
        precProducto = textPrecProducto.getText().toString();
        idCategoria = spinnerCategorias.getTransitionName();

        if (codigoProducto.equals("")){
            textCodigoProducto.setError("Ingrese código del producto");
            resultado = false;
        }
        if (nombreProducto.equals("")){
            textNombreProducto.setError("Ingrese nombre del producto");
            resultado = false;
        }
        if (descProducto.equals("")){
            textDescProducto.setError("Ingrese descripción del producto");
            resultado = false;
        }
        if (precProducto.equals("")){
            textPrecProducto.setError("Ingrese precio para el producto");
            resultado = false;
        }
        /*if (idCategoria.equals("")){
            Toast.makeText(getContext(), "Seleccione un categoría", Toast.LENGTH_SHORT).show();
            resultado = false;
        }*/

        return resultado;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}