package com.pjapp.appmascshop.ui.admin;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.Productos;
import com.pjapp.appmascshop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DetalleProductoAdm extends Fragment {

    EditText textCodigoProducto,textNombreProducto,textDescProducto,textPrecProducto;
    Spinner spinnerCategorias;
    Button btnRegistrarProducto;

    String codigoProducto,nombreProducto,descProducto,precProducto,categoria,idProducto;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    final List<String> listCategSpinner = new ArrayList<>();

    Boolean registra = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_producto_adm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();
        cargarSpinnerCategoria("");
        verificarRegistraActualiza();
    }

    private void verificarRegistraActualiza() {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            Toast.makeText(getContext(),"No hay datos para mostrar",Toast.LENGTH_SHORT).show();
            return;
        }else{
            registra = false;
            idProducto = datosRecuperados.getString("idProducto");
            textCodigoProducto.setText(datosRecuperados.getString("codigoProducto"));
            textNombreProducto.setText(datosRecuperados.getString("nombreProducto"));
            textDescProducto.setText(datosRecuperados.getString("descripcionProducto"));
            textPrecProducto.setText(datosRecuperados.getString("precioProducto"));
            cargarSpinnerCategoria(datosRecuperados.getString("nombCategoria"));
            //nombCategoria = datosRecuperados.getString("nombCategoria");
        }
    }

    private void cargarSpinnerCategoria(String value){
        databaseReference.child("Categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCategSpinner.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    String nombCategoria = item.child("nombreCategoria").getValue(String.class);
                    if (nombCategoria!=null){
                        listCategSpinner.add(nombCategoria);
                    }
                }

                ArrayAdapter<String> categoriaAdapter =
                        new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,listCategSpinner);
                categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerCategorias.setAdapter(categoriaAdapter);
                spinnerCategorias.setSelection(categoriaAdapter.getPosition(value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error => ", error.getMessage());
            }
        });
    }

    private void asignarReferencias(View view){
        textCodigoProducto = view.findViewById(R.id.txtCodProdCard);
        textNombreProducto = view.findViewById(R.id.textNombreProducto);
        textDescProducto = view.findViewById(R.id.textDescProducto);
        textPrecProducto = view.findViewById(R.id.textPrecProducto);
        spinnerCategorias = view.findViewById(R.id.spinnerCategorias);

        btnRegistrarProducto = view.findViewById(R.id.btnRegistrarDireccionEntrega);

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
        //Toast.makeText(getContext(), "Registrar producto", Toast.LENGTH_SHORT).show();
        Productos p = new Productos();
        p.setIdProducto(UUID.randomUUID().toString());
        p.setCodigo(codigoProducto);
        p.setNombre(nombreProducto);
        p.setDescripcion(descProducto);
        p.setPrecio(Double.valueOf(precProducto).doubleValue());
        p.setCategoria(categoria);

        databaseReference.child("Productos").child(p.getIdProducto()).setValue(p);
        AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
        ventana.setTitle("Mensaje informativo");
        ventana.setMessage("Producto registrado correctamente");
        ventana.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MainActivity activity = (MainActivity) getContext();
                Fragment newFragment = new Producto();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        ventana.create().show();

    }

    private void actualizarProducto(){
        HashMap map = new HashMap();
        map.put("codigo",codigoProducto);
        map.put("nombre",nombreProducto);
        map.put("descripcion",descProducto);
        map.put("precio",Double.valueOf(precProducto).doubleValue());
        map.put("categoria",categoria);

        databaseReference.child("Productos").child(idProducto).updateChildren(map);
        AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
        ventana.setTitle("Mensaje informativo");
        ventana.setMessage("Producto actualizado");
        ventana.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity activity = (MainActivity) getContext();
                Fragment newFragment = new Producto();
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

        codigoProducto = textCodigoProducto.getText().toString();
        nombreProducto = textNombreProducto.getText().toString();
        descProducto = textDescProducto.getText().toString();
        precProducto = textPrecProducto.getText().toString();
        categoria = spinnerCategorias.getSelectedItem().toString();



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