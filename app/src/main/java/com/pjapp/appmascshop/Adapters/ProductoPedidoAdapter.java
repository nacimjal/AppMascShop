package com.pjapp.appmascshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pjapp.appmascshop.Model.DetallePedido;
import com.pjapp.appmascshop.R;

import java.util.ArrayList;
import java.util.List;

public class ProductoPedidoAdapter extends RecyclerView.Adapter<ProductoPedidoAdapter.MyViewHolder>{
    private Context context;
    private List<DetallePedido> listaProductos = new ArrayList<>();

    public ProductoPedidoAdapter(Context context, List<DetallePedido> lista){
        this.context = context;
        this.listaProductos = lista;
    }

    @NonNull
    @Override
    public ProductoPedidoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.cardlist_producto_pedido,parent,false);
        return new ProductoPedidoAdapter.MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoPedidoAdapter.MyViewHolder holder, int position) {
        Double precio = listaProductos.get(position).getPrecio();
        Integer cantidad = listaProductos.get(position).getCantidad();
        Double importe = precio * cantidad;

        holder.txtProductodp.setText(listaProductos.get(position).getProducto()+"");
        holder.txtCantidaddp.setText(cantidad+"");
        holder.txtPreciodp.setText(precio+"");
        holder.txtImportedp.setText(importe+"");

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductodp,txtImportedp,txtPreciodp,txtCantidaddp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductodp = itemView.findViewById(R.id.txtProductodp);
            txtCantidaddp = itemView.findViewById(R.id.txtCantidaddp);
            txtPreciodp = itemView.findViewById(R.id.txtPreciodp);
            txtImportedp = itemView.findViewById(R.id.txtImportedp);

        }
    }
}
