package com.pjapp.appmascshop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pjapp.appmascshop.LoginActivity;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.Pedido;
import com.pjapp.appmascshop.Model.Usuario;
import com.pjapp.appmascshop.R;
import com.pjapp.appmascshop.ui.DetalleProducto;
import com.pjapp.appmascshop.ui.gallery.DetallePedido;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.MyViewHolder>{
    private Context context;
    private List<Pedido> listaPedidos = new ArrayList<>();
    private List<Usuario> listUsuarios = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public PedidosAdapter(Context context,List<Pedido> lista){
        this.context = context;
        this.listaPedidos = lista;
    }
    @NonNull
    @Override
    public PedidosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.cardlist_pedidos,parent,false);
        return new PedidosAdapter.MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosAdapter.MyViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.00");

        Double subtotal = listaPedidos.get(position).getSubtotal();
        Double igv = listaPedidos.get(position).getIgv();
        Double total = listaPedidos.get(position).getTotal();

        holder.txtNum_Pedido.setText(listaPedidos.get(position).getNumPedido());
        holder.txtFecha_pedido.setText(listaPedidos.get(position).getFechEmision());
        holder.txtCliente_pedido.setText(listaPedidos.get(position).getNombreCliente());
        holder.txtSubtotal_pedido.setText("Sub: S/ "+df.format(subtotal));
        holder.txtIgv_pedido.setText("Igv: S/ "+df.format(igv));
        holder.txtTotal_pedido.setText("Tot: S/ "+df.format(total));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                Fragment newFragment = new DetallePedido();
                Bundle envData = new Bundle();
                envData.putString("idPedido",listaPedidos.get(position).getIdPedido()+"");
                envData.putString("idCliente",listaPedidos.get(position).getCliente()+"");
                envData.putString("numPedido",listaPedidos.get(position).getNumPedido()+"");
                envData.putString("fechaPedido",listaPedidos.get(position).getFechEmision()+"");
                envData.putString("fotoEvidencia",listaPedidos.get(position).getEvidenciaPago()+"");

                envData.putString("subtotal",df.format(subtotal)+"");
                envData.putString("igv",df.format(igv)+"");
                envData.putString("total",df.format(total)+"");

                newFragment.setArguments(envData);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNum_Pedido,txtCliente_pedido,txtFecha_pedido,txtSubtotal_pedido,txtIgv_pedido,txtTotal_pedido;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNum_Pedido = itemView.findViewById(R.id.txtNum_Pedido);
            txtCliente_pedido = itemView.findViewById(R.id.txtCliente_pedido);
            txtFecha_pedido = itemView.findViewById(R.id.txtFecha_pedido);
            txtSubtotal_pedido = itemView.findViewById(R.id.txtSubtotal_pedido);
            txtIgv_pedido = itemView.findViewById(R.id.txtIgv_pedido);
            txtTotal_pedido = itemView.findViewById(R.id.txtTotal_pedido);

        }
    }
}
