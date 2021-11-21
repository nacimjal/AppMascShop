package com.pjapp.appmascshop.ui.carrito;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pjapp.appmascshop.Adapters.CarritoAdapter;
import com.pjapp.appmascshop.DAO.DAOCarrito;
import com.pjapp.appmascshop.Model.CarritoModel;
import com.pjapp.appmascshop.R;

import java.util.ArrayList;
import java.util.List;

public class Carrito extends Fragment {
    RecyclerView recyclerProductosCarrito;
    TextView txtSubtotal,txtIgv,txtTotal;
    Button btnSolicitarPedido;

    //DAOCarrito daoCarrito = new DAOCarrito(getContext());
    List<CarritoModel> listaCarritoModel = new ArrayList<>();
    CarritoAdapter adapterList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carrito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        DAOCarrito daoCarrito = new DAOCarrito(view.getContext());
        daoCarrito.abrirDB();

        listaCarritoModel = daoCarrito.getCarrito();

        adapterList = new CarritoAdapter(getContext(),listaCarritoModel);
        recyclerProductosCarrito.setAdapter(adapterList);
        recyclerProductosCarrito.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void asignarReferencias(View view) {
        recyclerProductosCarrito = view.findViewById(R.id.recyclerProductosCarrito);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);
        txtIgv = view.findViewById(R.id.txtIgv);
        txtTotal = view.findViewById(R.id.txtTotal);

        btnSolicitarPedido = view.findViewById(R.id.btnSolicitarPedido);
        btnSolicitarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Solicitar pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}