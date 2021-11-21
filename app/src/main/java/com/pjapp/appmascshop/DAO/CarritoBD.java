package com.pjapp.appmascshop.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarritoBD extends SQLiteOpenHelper {

    public CarritoBD(Context context){
        super(context,Constantes.NOMBRE_BD,null,Constantes.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query =
                "CREATE TABLE " + Constantes.NOMBRE_TABLA +
                        "(idProducto TEXT NOT NULL,"+
                        "producto TEXT NOT NULL,"+
                        "precio DOUBLE NOT NULL,"+
                        "cantidad INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
