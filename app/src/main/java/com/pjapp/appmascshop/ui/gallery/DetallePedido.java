package com.pjapp.appmascshop.ui.gallery;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pjapp.appmascshop.Adapters.ProductoPedidoAdapter;
import com.pjapp.appmascshop.Model.Productos;
import com.pjapp.appmascshop.Model.Usuario;
import com.pjapp.appmascshop.R;

import java.util.ArrayList;
import java.util.List;


public class DetallePedido extends Fragment {
    TextView txtNumPedido_pd,txtCliente_dp,txtFechaEntrega_dp,txtEmail_dp,txtDireccion_dp,txtSubtotal_dp,txtIgv_dp,txtTotal_dp;
    RecyclerView recyclerProductos_dp;
    ImageView imgEvidenciaPago_dp;
    Button btnVerUbicacion_dp;

    String idPedido,idCliente,urlFotoEvidencia;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<com.pjapp.appmascshop.Model.DetallePedido> listaItemsPedido = new ArrayList<>();
    ProductoPedidoAdapter adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_pedido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        inicializarFirebase();
        obtenerDatosEnviados();
        obtenerDatosCliente();
        obtenerDetallePedido();
        cargarEvidencia();
    }

    private void cargarEvidencia() {
        //imgEvidenciaPago_dp

        Glide.with(getContext()).load(urlFotoEvidencia).centerCrop().into(imgEvidenciaPago_dp);
    }

    private void obtenerDetallePedido() {
        databaseReference.child("DetallePedido").orderByChild("idPedido").equalTo(idPedido).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaItemsPedido.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    com.pjapp.appmascshop.Model.DetallePedido dp = item.getValue(com.pjapp.appmascshop.Model.DetallePedido.class);
                    listaItemsPedido.add(dp);
                }

                adaptador = new ProductoPedidoAdapter(getContext(),listaItemsPedido);
                recyclerProductos_dp.setAdapter(adaptador);
                recyclerProductos_dp.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error","Desc: "+error);
            }
        });
    }

    private void obtenerDatosCliente() {

        databaseReference.child("Usuarios").orderByChild("id").equalTo(idCliente).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario u = snapshot.getChildren().iterator().next().getValue(Usuario.class);

                txtCliente_dp.setText(u.getNombres());
                txtEmail_dp.setText(u.getCorreo());
                txtDireccion_dp.setText(u.getDireccionEntrega());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error","Desc: "+error);
            }
        });


    }

    private void obtenerDatosEnviados() {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            Toast.makeText(getContext(),"No hay datos para mostrar",Toast.LENGTH_SHORT).show();
            return;
        }else{

            idPedido = datosRecuperados.getString("idPedido");
            idCliente = datosRecuperados.getString("idCliente");
            urlFotoEvidencia = datosRecuperados.getString("fotoEvidencia");

            txtNumPedido_pd.setText(datosRecuperados.getString("numPedido"));
            txtFechaEntrega_dp.setText(datosRecuperados.getString("fechaPedido"));
            txtSubtotal_dp.setText("S/ "+datosRecuperados.getString("subtotal"));
            txtIgv_dp.setText("S/ "+datosRecuperados.getString("igv"));
            txtTotal_dp.setText("S/ "+datosRecuperados.getString("total"));
        }
    }

    private void asignarReferencias(View view) {
        txtNumPedido_pd = view.findViewById(R.id.txtNumPedido_pd);
        txtCliente_dp = view.findViewById(R.id.txtCliente_dp);
        txtFechaEntrega_dp = view.findViewById(R.id.txtFechaEntrega_dp);
        txtEmail_dp = view.findViewById(R.id.txtEmail_dp);
        txtDireccion_dp = view.findViewById(R.id.txtDireccion_dp);
        txtSubtotal_dp = view.findViewById(R.id.txtSubtotal_dp);
        txtIgv_dp = view.findViewById(R.id.txtIgv_dp);
        txtTotal_dp = view.findViewById(R.id.txtTotal_dp);

        imgEvidenciaPago_dp =view.findViewById(R.id.imgEvidenciaPago_dp);

        recyclerProductos_dp = view.findViewById(R.id.recyclerProductos_dp);
        btnVerUbicacion_dp = view.findViewById(R.id.btnVerUbicacion_dp);

        btnVerUbicacion_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Ir a mapa", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}