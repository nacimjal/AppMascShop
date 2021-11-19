package com.pjapp.appmascshop.ui.admin;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.Categoria;
import com.pjapp.appmascshop.R;

import java.util.HashMap;
import java.util.UUID;


public class DetalleCategoriaProducto extends Fragment {
    EditText txtNombreCategoria;
    Button btnRegistrarCategoria;

    String nombCategoria,idCategoria;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Boolean registra = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_categoria_producto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        asignarReferencias(view);
        inicializarFirebase();
        verificarRegistraActualiza();
    }

    private void verificarRegistraActualiza() {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            Toast.makeText(getContext(),"No hay datos para mostrar",Toast.LENGTH_SHORT).show();
            return;
        }else{
            registra = false;
            idCategoria = datosRecuperados.getString("idCategoria");
            txtNombreCategoria.setText(datosRecuperados.getString("nombCategoria"));
            //nombCategoria = datosRecuperados.getString("nombCategoria");
        }
    }

    private void asignarReferencias(View view){
        txtNombreCategoria = view.findViewById(R.id.txtDireccionEntrega);
        btnRegistrarCategoria = view.findViewById(R.id.btnRegistrarDireccionEntrega);

        btnRegistrarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    if (registra){
                         registrarCategoria();
                    }else{
                        actualizarCategoria();
                    }

                }
            }
        });
    }

    private void registrarCategoria(){

        Categoria c = new Categoria();
        c.setIdCategoria(UUID.randomUUID().toString());
        c.setNombreCategoria(nombCategoria);

        databaseReference.child("Categorias").child(c.getIdCategoria()).setValue(c);
        AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
        ventana.setTitle("Mensaje informativo");
        ventana.setMessage("Categoría registrada correctamente");
        ventana.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MainActivity activity = (MainActivity) getContext();
                Fragment newFragment = new CategoriaProducto();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        ventana.create().show();
    }

    private void actualizarCategoria(){
        nombCategoria = txtNombreCategoria.getText().toString();

        HashMap map = new HashMap();
        map.put("nombreCategoria",nombCategoria);

        databaseReference.child("Categorias").child(idCategoria).updateChildren(map);
        AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
        ventana.setTitle("Mensaje informativo");
        ventana.setMessage("Categoría actualizada");
        ventana.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity activity = (MainActivity) getContext();
                Fragment newFragment = new CategoriaProducto();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        ventana.create().show();
    }

    private boolean validarCampos(){
        boolean resultado = true;

        nombCategoria = txtNombreCategoria.getText().toString();
        if (nombCategoria.equals("")){
            txtNombreCategoria.setError("Ingrese nombre de la categoría");
            resultado = false;
        }

        return resultado;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}