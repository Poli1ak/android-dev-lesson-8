package ru.mirea.panov.mirea_project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


import java.util.ArrayList;
import java.util.List;

import ru.mirea.panov.mirea_project.databinding.FragmentEstablishmentsBinding;

public class EstablishmentsFragment extends Fragment {

    private FragmentEstablishmentsBinding binding;
    private MapView mapView;
    private MyLocationNewOverlay locationNewOverlay;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private List<Establishment> establishments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        initEstablishments();
    }

    private void initEstablishments() {
        establishments = new ArrayList<>();
        establishments.add(new Establishment("Московский Кремль", "Исторический памятник", new GeoPoint(55.751244, 37.618423)));
        establishments.add(new Establishment("Балашиха", "Город в Московской области", new GeoPoint(55.796289, 37.938272)));
        establishments.add(new Establishment("Мытищи", "Город в Московской области", new GeoPoint(55.910464, 37.736370)));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEstablishmentsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mapView = binding.mapView;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(10.0);
        GeoPoint startPoint = new GeoPoint(55.751244, 37.618423); // Центр Москвы
        mapController.setCenter(startPoint);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(locationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);

        addMarkers();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EstablishmentsAdapter adapter = new EstablishmentsAdapter(establishments, this::onEstablishmentSelected);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void onEstablishmentSelected(Establishment establishment) {
        mapView.getController().setCenter(establishment.getGeoPoint());
    }

    private void addMarkers() {
        for (Establishment establishment : establishments) {
            Marker marker = new Marker(mapView);
            marker.setPosition(establishment.getGeoPoint());
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                Toast.makeText(getContext(), establishment.getDescription(), Toast.LENGTH_SHORT).show();
                return true;
            });
            marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
            marker.setTitle(establishment.getName());
            mapView.getOverlays().add(marker);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_PERMISSIONS_REQUEST_CODE);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            locationNewOverlay.enableMyLocation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }
}
