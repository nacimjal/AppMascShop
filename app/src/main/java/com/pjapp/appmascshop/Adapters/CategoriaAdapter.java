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
import com.pjapp.appmascshop.Model.Categoria;
import com.pjapp.appmascshop.R;
import com.pjapp.appmascshop.ui.admin.DetalleCategoriaProducto;

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder> {
    private Context context;
    private List<Categoria> listaCategoria = new ArrayList<>();

    public CategoriaAdapter(Context context,List<Categoria> lista){
        this.context = context;
        this.listaCategoria = lista;
    }

    @NonNull
    @Override
    public CategoriaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.cardlist_categoria_producto,parent,false);
        return new MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.MyViewHolder holder, int position) {
        holder.textCategoriaProducto.setText(listaCategoria.get(position).getNombreCategoria()+"");

        holder.btnEditarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                Fragment newFragment = new DetalleCategoriaProducto();
                Bundle envData = new Bundle();
                envData.putString("idCategoria",listaCategoria.get(position).getIdCategoria()+"");
                envData.putString("nombCategoria",listaCategoria.get(position).getNombreCategoria()+"");

                newFragment.setArguments(envData);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.btnEliminarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar(listaCategoria.get(position).getIdCategoria());
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

                databaseReference.child("Categorias").child(id).removeValue();

            }
        });
        ventana.setNegativeButton("No",null);
        ventana.create().show();
    }

    @Override
    public int getItemCount() {
        return listaCategoria.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textCategoriaProducto;
        ImageButton btnEliminarCategoria,btnEditarCategoria;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoriaProducto = itemView.findViewById(R.id.textCategoriaProducto);
            btnEditarCategoria = itemView.findViewById(R.id.btnEditarProducto);
            btnEliminarCategoria = itemView.findViewById(R.id.btnCleanCarrito);
        }
    }
}
