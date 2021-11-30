package com.pjapp.appmascshop.ui.carrito;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pjapp.appmascshop.AccountActivity;
import com.pjapp.appmascshop.Adapters.CarritoAdapter;
import com.pjapp.appmascshop.DAO.DAOCarrito;
import com.pjapp.appmascshop.LoginActivity;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.CarritoModel;
import com.pjapp.appmascshop.Model.DetallePedido;
import com.pjapp.appmascshop.Model.Pedido;
import com.pjapp.appmascshop.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Carrito extends Fragment {
    RecyclerView recyclerProductosCarrito;
    TextView txtSubtotal,txtIgv,txtTotal;
    Button btnSolicitarPedido;

    Double subtotal,igvPed,totalPed;

    String userIdLogin,numPedido,idPedidoGen,nombreUserLogeado;

    List<CarritoModel> listaCarritoModel = new ArrayList<>();
    CarritoAdapter adapterList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Obtenemos idUsuarioGeneral guardado en SharedPreferences(MainActivity)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userIdLogin = preferences.getString("idUsuarioGeneral","unknown");
        nombreUserLogeado = preferences.getString("nombreUserLogeado","unknown");

        return inflater.inflate(R.layout.fragment_carrito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();

        DAOCarrito daoCarrito = new DAOCarrito(view.getContext());
        daoCarrito.abrirDB();

        listarProductosCarrito(daoCarrito);
        calcularSubtotales();

    }

    private void calcularSubtotales() {

        Double sumSubtotal = 0.0;
        Double igv = 0.0;
        Double total = 0.0;

        for (int i=0;i<listaCarritoModel.size();i++) {
            sumSubtotal += Double.valueOf(listaCarritoModel.get(i).getCantidad()) * listaCarritoModel.get(i).getPrecio();

            System.out.println(listaCarritoModel.get(i).getProducto());
        }

        subtotal = sumSubtotal;
        igv = subtotal * 0.18;
        total = subtotal + igv;
        Log.d("=>","Subtotal: "+ subtotal);

        DecimalFormat df = new DecimalFormat("#.00");

        txtSubtotal.setText("S/ "+df.format(subtotal));
        txtIgv.setText("S/ "+df.format(igv));
        txtTotal.setText("S/ "+df.format(total));

        igvPed = igv;
        totalPed = total;
    }

    private void listarProductosCarrito(DAOCarrito daoCarrito){
        listaCarritoModel = daoCarrito.getCarrito();

        adapterList = new CarritoAdapter(getContext(),listaCarritoModel);
        recyclerProductosCarrito.setAdapter(adapterList);
        recyclerProductosCarrito.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void asignarReferencias(View view) {
        recyclerProductosCarrito = view.findViewById(R.id.recyclerProductosCarrito);
        txtSubtotal = view.findViewById(R.id.txtSubtotalCarr);
        txtIgv = view.findViewById(R.id.txtIgvPedCarr);
        txtTotal = view.findViewById(R.id.txtTotalPedCarr);

        btnSolicitarPedido = view.findViewById(R.id.btnSolicitarPedido);
        btnSolicitarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "subt: "+subtotal, Toast.LENGTH_SHORT).show();

                if(subtotal>0){
                   // registrarPedido();

                    if(registrarPedido()){

                        MainActivity activity = (MainActivity) view.getContext();
                        Fragment newFragment = new ConfirmarPedido();
                        Bundle envData = new Bundle();
                        envData.putString("subtotalPedido",subtotal+"");
                        envData.putString("idPedidoGen",idPedidoGen+"");


                        newFragment.setArguments(envData);
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main,newFragment)
                                .addToBackStack(null)
                                .commit();
                    }else{
                        Toast.makeText(getContext(), "Error al registrar pedido", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getContext(), "No tiene productos en su carrito", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean registrarPedido(){
        boolean resultado = false;

        //Obtenemos Fecha y hora
        Date dt = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd hh:mma").format(dt);
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        int seconds = dt.getSeconds();
        String curTime = hours + "" + minutes + "" + seconds;

        //Generamos numero de pedido
        Random rand = new Random();
        int numPedRand = rand.nextInt(99)+1;
        numPedido = "PED00"+numPedRand+""+curTime;

        String idPedido = UUID.randomUUID().toString();
        idPedidoGen = idPedido;


        //GUARDAMOS PEDIDO

        Pedido p = new Pedido();
        p.setIdPedido(idPedido);
        p.setNumPedido(numPedido);
        p.setCliente(userIdLogin);
        p.setFechEmision(fecha);
        p.setSubtotal(subtotal);
        p.setIgv(igvPed);
        p.setTotal(totalPed);
        p.setEvidenciaPago("");
        p.setNombreCliente(nombreUserLogeado);

        databaseReference.child("Pedido").child(p.getIdPedido()).setValue(p);

        //RECORREMOS LISTA DE CARRITO Y GUARDAMOS
        for (int i=0;i<listaCarritoModel.size();i++) {
            DetallePedido dp = new DetallePedido();
            dp.setIdDetallePedido(UUID.randomUUID().toString());
            dp.setIdPedido(idPedido);
            dp.setProducto(listaCarritoModel.get(i).getProducto());
            dp.setIdProducto(listaCarritoModel.get(i).getIdProducto());
            dp.setPrecio(listaCarritoModel.get(i).getPrecio());
            dp.setCantidad(listaCarritoModel.get(i).getCantidad());

            databaseReference.child("DetallePedido").child(dp.getIdDetallePedido()).setValue(dp);
        }
        resultado = true;

        return resultado;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}