package com.pjapp.appmascshop.ui.carrito;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.R;

import java.text.DecimalFormat;


public class ConfirmarPedido extends Fragment {
    TextView txtSubtotalPedConf,txtIgvPedConf,txtTotalPedConf;
    Button btnConfirmarPedido;
    ImageView imgEvidenciaPed;

    String subtotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirmar_pedido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        obtenerDatosEnviados();
        calcularSubtotales();
    }

    private void calcularSubtotales() {

        Double sumSubtotal = Double.parseDouble(subtotal);
        Double igv = 0.0;
        Double total = 0.0;

        igv = sumSubtotal * 0.18;
        total = sumSubtotal + igv;

        DecimalFormat df = new DecimalFormat("#.00");

        txtSubtotalPedConf.setText("S/ "+df.format(sumSubtotal));
        txtIgvPedConf.setText("S/ "+df.format(igv));
        txtTotalPedConf.setText("S/ "+df.format(total));

    }

    private void obtenerDatosEnviados() {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            Toast.makeText(getContext(),"No hay datos para mostrar",Toast.LENGTH_SHORT).show();
            return;
        }else{
            subtotal = datosRecuperados.getString("subtotalPedido");
        }
    }

    private void asignarReferencias(View view) {

        txtSubtotalPedConf = view.findViewById(R.id.txtSubtotalPedConf);
        txtIgvPedConf = view.findViewById(R.id.txtIgvPedConf);
        txtTotalPedConf = view.findViewById(R.id.txtTotalPedConf);

        btnConfirmarPedido = view.findViewById(R.id.btnConfirmarPedido);
        btnConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Confirmar pedido: "+subtotal, Toast.LENGTH_SHORT).show();
            }
        });
    }
}