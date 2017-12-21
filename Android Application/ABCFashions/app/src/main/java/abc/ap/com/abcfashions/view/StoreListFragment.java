package abc.ap.com.abcfashions.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.adapter.RecyclerStoreAdapter;
import abc.ap.com.abcfashions.model.Branch;
import abc.ap.com.abcfashions.services.DataService;
import abc.ap.com.abcfashions.utils.RunTimePermissionChecker;

/**
 * Created by Aparna Prasad
 */
public class StoreListFragment extends Fragment {
    private ArrayList<Branch> branchArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.store_recycler_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Store Locator");

        try {
            DataService branchService = new DataService(getActivity().getApplicationContext());

            branchArrayList.clear();
            branchArrayList = branchService.getList();

            RecyclerStoreAdapter adapter = new RecyclerStoreAdapter(getContext(), getActivity(), this, branchArrayList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }


    public static void callAction(String No, MainActivity activity, StoreListFragment fragment) {
        if (new RunTimePermissionChecker(activity, fragment).managePermission(190)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            try {
                callIntent.setData(Uri.parse("tel:".concat(No)));
                activity.startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 190: {
                if ((Build.VERSION.SDK_INT >= 23)) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                        //never ask again, allow
                        if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            //Log.i("kkk", "pp");
                        } else {
                            new RunTimePermissionChecker(getActivity(), this).permissionDenied();
                        }
                    }
                    else
                    {
                        //deny
                    }
                }
                break;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
