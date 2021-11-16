package com.pjapp.appmascshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pjapp.appmascshop.Model.Usuario;

import java.util.UUID;

public class AccountActivity extends AppCompatActivity {

    EditText txtNombres,txtApellidos,txtCorreo,txtContrasenia,txtRepitContrasenia;
    CheckBox checkBoxTerminos;
    Button btnRegistrarUsuario;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //Para capturar los datos
    String nombres,apellidos,correo,contrasenia,repetirContrasenia,id,checkTerminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        asignarReferencias();
        inicializarFirebase();
    }

    private void asignarReferencias(){

        //Asignamos referencias a los campos del XML Activity_account
        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        txtRepitContrasenia = findViewById(R.id.txtRepitContrasenia);
        checkBoxTerminos = findViewById(R.id.checkBoxTerminos);

        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);

        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validarCampos()){
                    RegistrarUsuario();
                }

            }
        });
    }

    private void RegistrarUsuario(){
        //Seteamos los datos con el modelo Usuario
        Usuario u = new Usuario();
        u.setId(UUID.randomUUID().toString());
        u.setNombres(nombres);
        u.setApellidos(apellidos);
        u.setCorreo(correo);
        u.setContrasenia(contrasenia);
        u.setTerminos(checkTerminos);

        databaseReference.child("Usuarios").child(u.getId()).setValue(u);
        AlertDialog.Builder ventana = new AlertDialog.Builder(AccountActivity.this);
        ventana.setTitle("Mensaje informativo");
        ventana.setMessage("Usuario registrado correctamente");
        ventana.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AccountActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        ventana.create().show();

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private boolean validarCampos(){
        boolean resultado = true;

        nombres = txtNombres.getText().toString();
        apellidos = txtApellidos.getText().toString();
        correo = txtCorreo.getText().toString();
        contrasenia = txtContrasenia.getText().toString();
        repetirContrasenia = txtRepitContrasenia.getText().toString();
        checkTerminos = (checkBoxTerminos.isChecked() ? "Acepto términos" : "No acepto términos");

        if (nombres.equals("")){
            txtNombres.setError("Ingrese nombres");
            resultado = false;
        }
        if (apellidos.equals("")){
            txtApellidos.setError("Ingrese apellidos");
            resultado = false;
        }
        if (correo.equals("")){
            txtCorreo.setError("Ingrese correo válido");
            resultado = false;
        }
        if (contrasenia.equals("")){
            txtContrasenia.setError("Ingrese una contraseña");
            resultado = false;
        }
        if (!contrasenia.equals(repetirContrasenia)){
            txtRepitContrasenia.setError("Las contraseñas ingresadas no coinciden");
            resultado = false;
        }
        if (!checkBoxTerminos.isChecked()){
            Toast.makeText(getApplicationContext(), "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
            resultado = false;
        }

        return resultado;
    }
}