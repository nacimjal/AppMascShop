package com.pjapp.appmascshop.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pjapp.appmascshop.DAO.DAOCarrito;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.CarritoModel;
import com.pjapp.appmascshop.Model.Productos;
import com.pjapp.appmascshop.R;

import java.util.ArrayList;
import java.util.List;

public class ProductoCategoriaAdapter extends RecyclerView.Adapter<ProductoCategoriaAdapter.MyViewHolder> {
    private Context context;
    private List<Productos> listaProductos = new ArrayList<>();

    public ProductoCategoriaAdapter(Context context,List<Productos> lista){
        this.context = context;
        this.listaProductos = lista;
    }

    @NonNull
    @Override
    public ProductoCategoriaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.cardlist_detalle_productos,parent,false);
        return new ProductoCategoriaAdapter.MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoCategoriaAdapter.MyViewHolder holder, int position) {
        holder.txtNombProducto.setText(listaProductos.get(position).getCodigo()+" - " + listaProductos.get(position).getNombre());
        holder.txtDescripcionProducto.setText(listaProductos.get(position).getDescripcion()+"");
        holder.txtPrecioProducto.setText(listaProductos.get(position).getPrecio()+"");
        
        holder.btnAddCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String respuesta;
                //Agregamos Item al carrito
                DAOCarrito daoCarrito = new DAOCarrito(view.getContext());
                daoCarrito.abrirDB();

                //Verificamos si existe el producto en el carrito
                Boolean rsp = daoCarrito.verificarProducto(listaProductos.get(position).getIdProducto());
                if(rsp){
                    Toast.makeText(view.getContext(), "El producto ya existe en el carrito", Toast.LENGTH_SHORT).show();
                }else{
                    CarritoModel itemProd = new CarritoModel(
                            listaProductos.get(position).getIdProducto(),
                            listaProductos.get(position).getCodigo()+" - "+listaProductos.get(position).getNombre(),
                            listaProductos.get(position).getPrecio(),
                            1
                    );
                    respuesta = daoCarrito.agregarCarrito(itemProd);

                    //Mostramos alerta
                    AlertDialog.Builder ventana = new AlertDialog.Builder(context);
                    ventana.setTitle("Mensaje informativo");
                    ventana.setMessage(respuesta);
                    ventana.setPositiveButton("Ir al carrito", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity activity = (MainActivity) view.getContext();
                            Fragment newFragment = new com.pjapp.appmascshop.ui.carrito.Carrito();
                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.nav_host_fragment_content_main,newFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                    ventana.setNegativeButton("Continuar comprando",null);
                    ventana.create().show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombProducto,txtDescripcionProducto,txtPrecioProducto;
        ImageButton btnAddCarrito;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombProducto = itemView.findViewById(R.id.txtNombProducto);
            txtDescripcionProducto = itemView.findViewById(R.id.txtDescripcionProducto);
            txtPrecioProducto = itemView.findViewById(R.id.txtPrecioProducto);

            btnAddCarrito = itemView.findViewById(R.id.btnCleanCarrito);
        }
    }
}
