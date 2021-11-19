package com.pjapp.appmascshop.ui.slideshow;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.Categoria;
import com.pjapp.appmascshop.Model.Usuario;
import com.pjapp.appmascshop.R;
import com.pjapp.appmascshop.databinding.FragmentSlideshowBinding;
import com.pjapp.appmascshop.ui.admin.CategoriaProducto;

import java.util.HashMap;

public class SlideshowFragment extends Fragment {
    EditText txtDireccionEntrega;
    Button btnRegistrarDireccionEntrega;

    String direccionEntrega,userIdLogin;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        //Obtenemos idUsuarioGeneral guardado en SharedPreferences(MainActivity)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userIdLogin = preferences.getString("idUsuarioGeneral","unknown");
        direccionEntrega = preferences.getString("direccionEntrega","Dirección no definida");

        return inflater.inflate(R.layout.fragment_slideshow, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();
        obtenerDireccionUser();
    }

    private void obtenerDireccionUser(){
        txtDireccionEntrega.setText(direccionEntrega);

        Log.d("TAG","user001: "+direccionEntrega);


    }

    private void asignarReferencias(View view){
        txtDireccionEntrega = view.findViewById(R.id.txtDireccionEntrega);
        btnRegistrarDireccionEntrega = view.findViewById(R.id.btnRegistrarDireccionEntrega);

        btnRegistrarDireccionEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDireccionEntrega(view);
            }
        });
    }

    private void actualizarDireccionEntrega(View view){
        direccionEntrega = txtDireccionEntrega.getText().toString();

        HashMap map = new HashMap();
        map.put("direccionEntrega",direccionEntrega);

        databaseReference.child("Usuarios").child(userIdLogin).updateChildren(map);
        AlertDialog.Builder ventana = new AlertDialog.Builder(getContext());
        ventana.setTitle("Mensaje informativo");
        ventana.setMessage("Su dirección de entrega se actualizó correctamente");
        ventana.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity activity = (MainActivity) getContext();
                Fragment newFragment = new SlideshowFragment();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        ventana.create().show();
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