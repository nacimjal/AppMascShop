package com.pjapp.appmascshop.ui.carrito;

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
import android.widget.TextView;
import android.widget.Toast;

import com.pjapp.appmascshop.Adapters.CarritoAdapter;
import com.pjapp.appmascshop.DAO.DAOCarrito;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.CarritoModel;
import com.pjapp.appmascshop.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Carrito extends Fragment {
    RecyclerView recyclerProductosCarrito;
    TextView txtSubtotal,txtIgv,txtTotal;
    Button btnSolicitarPedido;

    Double subtotal;

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
                Toast.makeText(getContext(), "subt: "+subtotal, Toast.LENGTH_SHORT).show();

                MainActivity activity = (MainActivity) view.getContext();
                Fragment newFragment = new ConfirmarPedido();
                Bundle envData = new Bundle();
                envData.putString("subtotalPedido",subtotal+"");

                newFragment.setArguments(envData);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}