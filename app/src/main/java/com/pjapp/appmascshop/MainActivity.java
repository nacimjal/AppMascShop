package com.pjapp.appmascshop;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.pjapp.appmascshop.databinding.ActivityMainBinding;
import com.pjapp.appmascshop.ui.carrito.Carrito;
import com.pjapp.appmascshop.ui.slideshow.SlideshowFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    NavigationView navigationView;


    String rolUsuario,idUsuarioGeneral,direccionEntrega,nombreUserLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);

        //Cargamos datos del usuario logueado en el menu
        TextView userLogin = headerView.findViewById(R.id.textUsuarioLogeado);
        TextView correoLogin = headerView.findViewById(R.id.textCorreoLogeado);
        userLogin.setText(getIntent().getStringExtra("userLogeado"));
        correoLogin.setText(getIntent().getStringExtra("correoUser"));
        rolUsuario = getIntent().getStringExtra("rolUsuario");
        idUsuarioGeneral = getIntent().getStringExtra("idUserLogin");
        direccionEntrega =  getIntent().getStringExtra("direccionEntrega");
        nombreUserLogeado = getIntent().getStringExtra("userLogeado");


        //Guardamos IdUsuario logeado en SharedPreferences temporalmente
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idUsuarioGeneral",idUsuarioGeneral);
        editor.putString("direccionEntrega",direccionEntrega);
        editor.putString("nombreUserLogeado",nombreUserLogeado);
        editor.putString("rolUsuario",rolUsuario);
        editor.commit();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_categoria,R.id.nav_producto)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        permisosMenu();

    }

    private void permisosMenu() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        //Toast.makeText(this, "Rol: "+rolUsuario, Toast.LENGTH_SHORT).show();

        if(rolUsuario.equals("Adm")){
            nav_Menu.findItem(R.id.nav_home).setVisible(false);
            nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
        }else{
            nav_Menu.findItem(R.id.nav_categoria).setVisible(false);
            nav_Menu.findItem(R.id.nav_producto).setVisible(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.nav_gallery);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_carrito:
                if(rolUsuario.equals("Adm")){
                    Toast.makeText(this, "No tiene acceso a esta opción", Toast.LENGTH_SHORT).show();
                }else{
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.nav_host_fragment_content_main,new Carrito());
                    ft.commit();
                }
                break;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}