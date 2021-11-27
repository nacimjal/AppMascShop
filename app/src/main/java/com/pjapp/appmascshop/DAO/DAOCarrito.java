package com.pjapp.appmascshop.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.pjapp.appmascshop.Model.CarritoModel;

import java.util.ArrayList;
import java.util.List;

public class DAOCarrito {
    CarritoBD carritoBD;
    SQLiteDatabase db;

    private Context context;

    public DAOCarrito(Context context){
        this.context = context;
        this.carritoBD = new CarritoBD(context);
    }
    //Permiso para abrir la bd
    public void abrirDB() {
        db = carritoBD.getWritableDatabase();
    }

    //Método agregar producto al carrito
    public  String agregarCarrito(CarritoModel carritoModel){
        String mensaje = "";
        try {
            ContentValues valores = new ContentValues();

            valores.put("idProducto", carritoModel.getIdProducto());
            valores.put("producto",carritoModel.getProducto());
            valores.put("precio", carritoModel.getPrecio());
            valores.put("cantidad", carritoModel.getCantidad());

            long resultado = db.insert(Constantes.NOMBRE_TABLA,null,valores);
            if (resultado == -1){
                mensaje =  "Error al agregar producto al carrito";
            }else{
                mensaje =  "Producto agregado al carrito";
            }

        }catch (Exception e){
            Log.d("=>",e.getMessage());
        }
        return mensaje;
    }

    //Metodo modificar cantidad del producto
    public String modificarCantidadCarrito(String id,String cantidad){
        String mensaje = "";
        try {
            ContentValues valores = new ContentValues();

            valores.put("cantidad", cantidad);

            long resultado = db.update(Constantes.NOMBRE_TABLA,valores,"idProducto='"+ id +"'",null);
            if (resultado == -1){
                mensaje =  "Error";
            }else{
                mensaje =  "Actualizado";
            }

        }catch (Exception e){
            Log.d("=>",e.getMessage());
        }
        return mensaje;
    }

    //Metodo eliminar producto del carrito
    public String eliminarProductoCarrito(String id){
        String mensaje = "";
        try {
            long resultado = db.delete(Constantes.NOMBRE_TABLA,"idProducto='"+id+"'",null);

            if (resultado == -1){
                mensaje =  "Error al eliminar el producto del carrito";
            }else{
                mensaje =  "Se eliminó correctamente el producto del carrito";
            }
        }catch (Exception e){
            Log.d("=>",e.getMessage());
        }

        return mensaje;
    }
    //Metodo limpiar carrito
    public String limpiarCarrito(){
        String mensaje = "";
        try {
            long resultado = db.delete(Constantes.NOMBRE_TABLA,null,null);

            if (resultado == -1){
                mensaje =  "Error al limpiar el carrito";
            }else{
                mensaje =  "Se limpio el carrito con éxito";
            }
        }catch (Exception e){
            Log.d("=>",e.getMessage());
        }

        return mensaje;
    }


    //Metodo listar vehiculos
    public List<CarritoModel> getCarrito(){
        List<CarritoModel> listaProductosCarritoModel = new ArrayList<>();
        try {
            Cursor c = db.rawQuery("SELECT * FROM "+Constantes.NOMBRE_TABLA,null);
            while (c.moveToNext()){
                listaProductosCarritoModel.add(new CarritoModel(
                        c.getString(0),
                        c.getString(1),
                        c.getDouble(2),
                        c.getInt(3)
                ));
            }
        }catch (Exception e){
            Log.d("=>",e.getMessage());
        }
        return listaProductosCarritoModel;
    }

    //Metodo listar vehiculos
    public Boolean verificarProducto(String id){
        boolean resultado = false;

        try {
            Cursor c = db.rawQuery("SELECT idProducto FROM "+Constantes.NOMBRE_TABLA+" WHERE idProducto='"+id+"'",null);
            if (c.moveToFirst()){
                resultado = true;
            }

        }catch (Exception e){
            Log.d("=>",e.getMessage());
        }

        return resultado;

    }
}
