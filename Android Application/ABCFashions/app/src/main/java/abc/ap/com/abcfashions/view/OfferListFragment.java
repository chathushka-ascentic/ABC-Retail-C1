package abc.ap.com.abcfashions.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.adapter.RecyclerOfferAdapter;
import abc.ap.com.abcfashions.model.Offer;
import abc.ap.com.abcfashions.services.VolleySingletonController;
import abc.ap.com.abcfashions.services.VolleyUtils;

/**
 * Created by Aparna Prasad
 */
public class OfferListFragment extends Fragment {

    private RecyclerOfferAdapter adapter;
    private ArrayList<Offer> offerArrayList = new ArrayList<>();
    int branchId=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.store_recycler_view, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Offers");

        final MainActivity activity = (MainActivity) getActivity();


        if(getArguments().getInt("branchId") != 0)
            branchId = getArguments().getInt("branchId");
        else
            branchId = activity.getBranchId();

        if(branchId == 0)
        {
            Toast.makeText(getActivity(),"Cannot find the nearest branch",Toast.LENGTH_LONG).show();
        }
        else {

            try {


                final ProgressDialog progessDialog = new ProgressDialog(activity, R.style.AppThemeProgressDial);
                progessDialog.setIndeterminate(true);
                progessDialog.setCancelable(false);
                progessDialog.setMessage("Processing");
                progessDialog.show();

                LinkedHashMap<String, String> queryParamCollection = new LinkedHashMap<>();
                queryParamCollection.put("userId", "" + App.LoggedInUser.getUserId());
                queryParamCollection.put("branchId", "" + branchId);

                VolleyUtils.makeJsonObjectRequestGetSync(getActivity(), "getoffers.php", queryParamCollection, "offers", new VolleySingletonController.VolleyResponseListener() {

                    @Override
                    public void onError(String message) {
                        try {
                            progessDialog.dismiss();
                            App.setSnackbar(getView(), getResources().getString(R.string.internetCheckMsg));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onResponse(String res) {
                        try {

                            progessDialog.dismiss();
                            offerArrayList.clear();

                            if (!new JsonParser().parse(res).isJsonNull()) {
                                JsonArray responseJsonArray = ((JsonObject) new JsonParser().parse(res)).get("getOffersResponse").getAsJsonArray();

                                Gson gson = new GsonBuilder().create();

                                offerArrayList = gson.fromJson(responseJsonArray.toString(), new TypeToken<ArrayList<Offer>>() {
                                }.getType());

                                adapter = new RecyclerOfferAdapter(getContext(), offerArrayList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (final Exception exception) {
                            exception.printStackTrace();
                            progessDialog.dismiss();
                            App.setSnackbar(getView(), getResources().getString(R.string.internetCheckMsg));

                        }

                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return view;
    }

}
