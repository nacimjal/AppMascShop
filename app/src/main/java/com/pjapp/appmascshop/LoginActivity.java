package com.pjapp.appmascshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pjapp.appmascshop.Model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsuario,txtContrasenia;
    Button btnIniciarSesion,btnCrearCuenta;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String user,password;

    private List<Usuario> listUsuarios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        asignarReferencias();
        inicializarFirebase();

    }

    private void asignarReferencias() {

        //Asignamos referencias a los campos de formulario login
        txtUsuario = findViewById(R.id.txtUsuarioLogin);
        txtContrasenia = findViewById(R.id.txtContraseniaLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    iniciarSesion();
                }else{
                    Toast.makeText(LoginActivity.this, "No se pudierón validar los datos ingresados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarSesion(){

        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUsuarios.clear();

                //Reconstruimos el Objeto Usuario
                for (DataSnapshot item: snapshot.getChildren()){
                    Usuario useData = item.getValue(Usuario.class);
                    listUsuarios.add(useData);
                }
                int cont = 0;

                for (int i=0;i<listUsuarios.size();i++) {
                    //Validamos si usuario y contraseña son correctas
                    if((listUsuarios.get(i).getCorreo()).equals(user) && listUsuarios.get(i).getContrasenia().equals(password)){
                        cont++;

                        //System.out.println(listUsuarios.get(i).getCorreo());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userLogeado",listUsuarios.get(i).getNombres()+" "+listUsuarios.get(i).getApellidos());
                        intent.putExtra("correoUser",listUsuarios.get(i).getCorreo());
                        intent.putExtra("rolUsuario",listUsuarios.get(i).getTipoUsuario());
                        intent.putExtra("idUserLogin",listUsuarios.get(i).getId());
                        intent.putExtra("direccionEntrega",listUsuarios.get(i).getDireccionEntrega());

                        startActivity(intent);
                    }
                }

                if(cont<=0){
                    Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validarCampos(){
        boolean resultado = true;

        user = txtUsuario.getText().toString();
        password = txtContrasenia.getText().toString();

        if (user.equals("")){
            txtUsuario.setError("Ingrese su usuario (correo)");
            resultado = false;
        }
        if (password.equals("")){
            txtContrasenia.setError("Ingrese su contraseña");
            resultado = false;
        }

        return resultado;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }
}