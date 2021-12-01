package com.pjapp.appmascshop.ui.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pjapp.appmascshop.R;

import java.util.List;


public class MapaClientePedido extends Fragment {

    FloatingActionButton btnMiubicacion;
    TextView txtDireccionCliente;
    String dDireccion;
    //Para el mapa
    SupportMapFragment supportMapFragment;
    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    SearchView svDireccion;

    double latitudActual = 0, longitudActual = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapa_cliente_pedido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            Toast.makeText(getContext(),"No hay datos para mostrar",Toast.LENGTH_SHORT).show();
            return;
        }
        dDireccion = datosRecuperados.getString("direccionCliente");

        referenciaMiUbicacion(view);
        asignarReferenciasMapa(view);
    }

    private void referenciaMiUbicacion(View view) {
        txtDireccionCliente = view.findViewById(R.id.txtDireccionCliente);
        txtDireccionCliente.setText(dDireccion);

        btnMiubicacion = view.findViewById(R.id.btnMiubicacion);
        btnMiubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUbicacionActual();
            }
        });
    }

    private void asignarReferenciasMapa(View view) {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.idMapaFragment);

        getUbicacionCliente();
        svDireccion = view.findViewById(R.id.idSvUbicacion);
        svDireccion.setTag(dDireccion);
        svDireccion.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    String direccion = svDireccion.getQuery().toString();
                    List<Address> listaDirecciones = null;

                    if (direccion != null || !direccion.equals("")){
                        Geocoder geocoder = new Geocoder(getContext());
                        listaDirecciones = geocoder.getFromLocationName(direccion,1);

                        Address address = listaDirecciones.get(0);
                        LatLng posicion = new LatLng(address.getLatitude(),address.getLongitude());

                        Log.i("posicion","Lati y Long: "+posicion);

                        mMap.addMarker(new MarkerOptions().position(posicion).title(direccion));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion,15));
                    }

                }catch (Exception e){
                    Toast.makeText(getContext(),"No encontramos la dirección ingresada",Toast.LENGTH_SHORT).show();
                    Log.i("Error","Error mapa: "+e.getMessage());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    private void getUbicacionCliente(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }else{
            try {
                String direccionCliente = dDireccion;

                List<Address> listaDirecciones = null;

                if (direccionCliente != null || !direccionCliente.equals("")){
                    Geocoder geocoder = new Geocoder(getContext());
                    listaDirecciones = geocoder.getFromLocationName(direccionCliente,1);

                    Address address = listaDirecciones.get(0);
                    latitudActual = address.getLatitude();
                    longitudActual = address.getLongitude();

                    Log.i("Mapa","Lati: "+latitudActual);
                    Log.i("Mapa","Long: "+longitudActual);

                    //Sincronizar con el mapa
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            LatLng posicionActual = new LatLng(latitudActual, longitudActual);
                            mMap.addMarker(new MarkerOptions()
                                    .position(posicionActual)
                                    .title("Ubicación del cliente")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            );
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,16));
                        }
                    });

                }else{
                    Toast.makeText(getContext(), "No se encontró la ubicación del cliente", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Toast.makeText(getContext(),"No encontramos la dirección ingresada",Toast.LENGTH_SHORT).show();
                Log.i("Error","Error mapa: "+e.getMessage());
            }
        }
    }

    private void getUbicacionActual() {
        //Verificar permiso
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }else {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            Task<Location> tarea = fusedLocationProviderClient.getLastLocation();

            tarea.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        latitudActual = location.getLatitude();
                        longitudActual = location.getLongitude();

                        //Sincronizar con el mapa
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                mMap = googleMap;
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                LatLng posicionActual = new LatLng(latitudActual, longitudActual);
                                mMap.addMarker(new MarkerOptions()
                                        .position(posicionActual)
                                        .title("Posicion Actual")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                );
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,16));
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUbicacionActual();
            }
        }
    }

    @Override
    public void onDestroyView() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.idMapaFragment);
        if (f!=null)
            getChildFragmentManager().beginTransaction()
                    .remove(f).commitAllowingStateLoss();
        super.onDestroyView();
    }
}