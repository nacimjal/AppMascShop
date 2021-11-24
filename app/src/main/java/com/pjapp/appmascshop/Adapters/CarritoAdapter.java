package com.pjapp.appmascshop.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.pjapp.appmascshop.DAO.DAOCarrito;
import com.pjapp.appmascshop.MainActivity;
import com.pjapp.appmascshop.Model.CarritoModel;

import com.pjapp.appmascshop.R;
import com.pjapp.appmascshop.ui.DetalleProducto;
import com.pjapp.appmascshop.ui.carrito.Carrito;

import java.util.ArrayList;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.MyViewHolder> {
    private Context context;
    private List<CarritoModel> listaCarritoModel = new ArrayList<>();

    public CarritoAdapter(Context context,List<CarritoModel> lista){
        this.context = context;
        this.listaCarritoModel = lista;
    }

    @NonNull
    @Override
    public CarritoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.cardlist_producto_carrito,parent,false);
        return new CarritoAdapter.MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoAdapter.MyViewHolder holder, int position) {
        Double precio = listaCarritoModel.get(position).getPrecio();
        Integer cantidad = listaCarritoModel.get(position).getCantidad();
        Double importe = precio * cantidad;

        holder.lblProductoCarrito.setText(listaCarritoModel.get(position).getProducto()+"");
        holder.lblPrecioCarrito.setText(precio+"");
        holder.lblCantidadCarrito.setText(cantidad+"");
        holder.lblImporteCarrito.setText(importe+"");
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ventana = new AlertDialog.Builder(context);
                ventana.setTitle("Producto: " + listaCarritoModel.get(position).getProducto());

                final EditText inputCantidad = new EditText(ventana.getContext());
                inputCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputCantidad.setRawInputType(Configuration.KEYBOARD_12KEY);
                ventana.setView(inputCantidad);

                ventana.setMessage("Ingrese la cantidad a cambiar");
                ventana.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Editable value = inputCantidad.getText();

                        DAOCarrito daoCarrito = new DAOCarrito(context);
                        daoCarrito.abrirDB();

                        //Actualizamos la cantidad del producto
                        String respuesta = daoCarrito.modificarCantidadCarrito(listaCarritoModel.get(position).getIdProducto(),value.toString());

                        //Si la respuesta es correcta, refrescamos la pantalla
                        if (respuesta == "Actualizado"){

                            MainActivity activity = (MainActivity) view.getContext();
                            Fragment newFragment = new Carrito();
                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.nav_host_fragment_content_main,newFragment)
                                    .addToBackStack(null)
                                    .commit();

                        }else{
                            Toast.makeText(ventana.getContext(), "Error al actualizar la cantidad", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                ventana.setNegativeButton("Cancelar", null);
                ventana.create().show();

            }
        });

        holder.btnCleanCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ventana = new AlertDialog.Builder(context);
                ventana.setTitle("Mensaje");
                ventana.setMessage("¿Esta seguro de quitar este registro del carrito?");
                ventana.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DAOCarrito daoCarrito = new DAOCarrito(context);
                        daoCarrito.abrirDB();
                        String respuesta = daoCarrito.eliminarProductoCarrito(listaCarritoModel.get(position).getIdProducto());

                        AlertDialog.Builder v = new AlertDialog.Builder(context);
                        v.setTitle("Mensaje Informativo");
                        v.setMessage(respuesta);
                        v.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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
                        v.create().show();
                    }
                });
                ventana.setNegativeButton("No", null);
                ventana.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCarritoModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lblProductoCarrito,lblCantidadCarrito,lblPrecioCarrito,lblImporteCarrito;
        ImageButton btnCleanCarrito;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lblProductoCarrito = itemView.findViewById(R.id.lblProductoCarrito);
            lblCantidadCarrito = itemView.findViewById(R.id.lblCantidadCarrito);
            lblPrecioCarrito = itemView.findViewById(R.id.lblPrecioCarrito);
            lblImporteCarrito = itemView.findViewById(R.id.lblImporteCarrito);

            btnCleanCarrito = itemView.findViewById(R.id.btnCleanCarrito);
        }
    }
}
