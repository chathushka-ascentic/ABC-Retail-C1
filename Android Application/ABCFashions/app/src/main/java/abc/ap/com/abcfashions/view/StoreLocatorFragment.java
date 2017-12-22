package abc.ap.com.abcfashions.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import abc.ap.com.abcfashions.R;


/**
 * Created by: Aparna Prasad
 */
public class StoreLocatorFragment extends Fragment implements OnMapReadyCallback
{

    private GoogleMap googleMap;
    private LatLng point;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.store_locate, container, false);

        SupportMapFragment fm = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Store Locator");

        Bundle args = getArguments();
        if (getArguments() != null) {
            point = ((LatLng)args.getParcelable("point"));
        }

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMp) {

        googleMap = googleMp;
        googleMap.setLocationSource(null);

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setAllGesturesEnabled(true);

        googleMap.addMarker(new MarkerOptions().position(point).anchor(0.5f, 0.5f));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 18f));


    }
}
