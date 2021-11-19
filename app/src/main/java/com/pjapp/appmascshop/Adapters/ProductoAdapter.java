package com.pjapp.appmascshop.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.Productos;
import com.pjapp.appmascshop.R;
import com.pjapp.appmascshop.ui.admin.DetalleCategoriaProducto;
import com.pjapp.appmascshop.ui.admin.DetalleProductoAdm;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.MyViewHolder> {
    private Context context;
    private List<Productos> listaProductos = new ArrayList<>();

    public ProductoAdapter(Context context,List<Productos> lista){
        this.context = context;
        this.listaProductos = lista;
    }

    @NonNull
    @Override
    public ProductoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.cardlist_producto_adm,parent,false);
        return new ProductoAdapter.MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.MyViewHolder holder, int position) {
        holder.txtCodProdCard.setText(listaProductos.get(position).getCodigo()+"");
        holder.txtNombProdCard.setText(listaProductos.get(position).getNombre()+"");
        holder.txtCategProdCard.setText(listaProductos.get(position).getCategoria()+"");
        holder.txtPrecProdCard.setText(listaProductos.get(position).getPrecio()+"");

        holder.btnEditarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                Fragment newFragment = new DetalleProductoAdm();
                Bundle envData = new Bundle();
                envData.putString("idProducto",listaProductos.get(position).getIdProducto()+"");
                envData.putString("codigoProducto",listaProductos.get(position).getCodigo()+"");
                envData.putString("nombreProducto",listaProductos.get(position).getNombre()+"");
                envData.putString("nombCategoria",listaProductos.get(position).getCategoria()+"");
                envData.putString("descripcionProducto",listaProductos.get(position).getDescripcion()+"");
                envData.putString("precioProducto",listaProductos.get(position).getPrecio()+"");

                newFragment.setArguments(envData);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        holder.btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar(listaProductos.get(position).getIdProducto());
            }
        });
    }

    private void eliminar(String id){
        AlertDialog.Builder ventana = new AlertDialog.Builder(context);
        ventana.setTitle("Confirmación");
        ventana.setMessage("¿Esta seguro de eliminar este registro?");
        ventana.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase firebaseDatabase;
                DatabaseReference databaseReference;
                FirebaseApp.initializeApp(context);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();

                databaseReference.child("Productos").child(id).removeValue();
            }
        });
        ventana.setNegativeButton("No",null);
        ventana.create().show();
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodProdCard,txtNombProdCard,txtCategProdCard,txtPrecProdCard;
        ImageButton btnEliminarProducto,btnEditarProducto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodProdCard = itemView.findViewById(R.id.txtCodProdCard);
            txtNombProdCard = itemView.findViewById(R.id.txtNombProdCard);
            txtCategProdCard = itemView.findViewById(R.id.txtCategProdCard);
            txtPrecProdCard = itemView.findViewById(R.id.txtPrecProdCard);

            btnEditarProducto = itemView.findViewById(R.id.btnEditarProducto);
            btnEliminarProducto = itemView.findViewById(R.id.btnEliminarProducto);
        }
    }

}
