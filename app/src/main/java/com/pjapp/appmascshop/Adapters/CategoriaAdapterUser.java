package com.pjapp.appmascshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pjapp.appmascshop.Model.Categoria;
import com.pjapp.appmascshop.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapterUser extends RecyclerView.Adapter<CategoriaAdapterUser.MyViewHolder>{
    private Context context;
    private List<Categoria> listaCategoria = new ArrayList<>();

    public CategoriaAdapterUser(Context context,List<Categoria> lista){
        this.context = context;
        this.listaCategoria = lista;
    }

    @NonNull
    @Override
    public CategoriaAdapterUser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.cardlist_productos,parent,false);
        return new CategoriaAdapterUser.MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapterUser.MyViewHolder holder, int position) {
        holder.txtNombCategoriaUser.setText(listaCategoria.get(position).getNombreCategoria()+"");
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Direcciona a productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCategoria.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombCategoriaUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombCategoriaUser = itemView.findViewById(R.id.txtNombCategoriaUser);

        }
    }
}
