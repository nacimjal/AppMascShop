package com.pjapp.appmascshop.ui.carrito;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pjapp.appmascshop.DAO.DAOCarrito;
import com.pjapp.appmascshop.R;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.UUID;


public class ConfirmarPedido extends Fragment {
    TextView txtSubtotalPedConf,txtIgvPedConf,txtTotalPedConf;
    Button btnConfirmarPedido;
    ImageButton btnAbrirCamara;
    ImageView imgEvidenciaPed;

    String subtotal;

    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int REQUEST_IMAGE_CAMERA = 101;
    public String IMAGEN1;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirmar_pedido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        asignarReferencias(view);
        obtenerDatosEnviados();
        calcularSubtotales();

        limpiarCarrito();
    }

    private void limpiarCarrito() {
        DAOCarrito daoCarrito = new DAOCarrito(getContext());
        daoCarrito.abrirDB();
        String respuesta = daoCarrito.limpiarCarrito();
        Toast.makeText(getContext(), respuesta, Toast.LENGTH_SHORT).show();
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
        imgEvidenciaPed = view.findViewById(R.id.imgEvidenciaPed);

        btnAbrirCamara = view.findViewById(R.id.btnAbrirCamara);
        btnAbrirCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        abrirCamara();
                        IMAGEN1 = "FOTOPAGO";
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                    }
                } else {
                    abrirCamara();
                    IMAGEN1 = "FOTOPAGO";
                }
            }
        });

        btnConfirmarPedido = view.findViewById(R.id.btnConfirmarPedido);
        btnConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Confirmar pedido: "+subtotal, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(getContext(), "Se requiere dar permisos a la camara", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (IMAGEN1.equals("FOTOPAGO")){
                    imgEvidenciaPed.setImageBitmap(photo);
                }

                uploadImage(photo);
            }
        }
    }

    public void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://appmascshop.appspot.com/");
        StorageReference imagesRef = storageRef.child("evidencias/"+UUID.randomUUID().toString());

        UploadTask uploadTask = imagesRef.putBytes(data);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri urlImg = task.getResult();

                Toast.makeText(getContext(), "URL:" + urlImg, Toast.LENGTH_SHORT).show();

                System.out.println("URL => "+urlImg);
            }
        });

    }

    private void abrirCamara() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
            getActivity().startActivityFromFragment(ConfirmarPedido.this, cameraIntent, REQUEST_IMAGE_CAMERA);

        } else {
            Toast.makeText(getContext(), "No se puede abrir la camara", Toast.LENGTH_LONG).show();
        }
    }

}