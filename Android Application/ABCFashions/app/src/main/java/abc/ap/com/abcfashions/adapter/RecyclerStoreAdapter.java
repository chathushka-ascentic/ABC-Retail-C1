package abc.ap.com.abcfashions.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Branch;
import abc.ap.com.abcfashions.view.MainActivity;
import abc.ap.com.abcfashions.view.StoreListFragment;
import abc.ap.com.abcfashions.view.StoreLocatorFragment;


/**
 * Created by Aparna Prasad
 */
public class RecyclerStoreAdapter extends RecyclerView.Adapter<RecyclerStoreAdapter.RecyclerVehicleViewHolder> {

    private LayoutInflater inflater;
    private StoreListFragment storeListFragment;
    private ArrayList<Branch> branchArrayList;
    private Activity activity;


    public RecyclerStoreAdapter(Context context,  FragmentActivity activity, StoreListFragment storeListFragment, ArrayList<Branch> InList) {
        this.branchArrayList = InList;
        this.storeListFragment=storeListFragment;
        inflater = LayoutInflater.from(context);
        this.activity=activity;
    }



    static class RecyclerVehicleViewHolder extends RecyclerView.ViewHolder
    {
        TextView branchName,address,phone;
        ImageButton contact,loc;
        CardView cardView;

        RecyclerVehicleViewHolder(View v) {
            super(v);
            branchName= (TextView) itemView.findViewById(R.id.branchName);
            address= (TextView) itemView.findViewById(R.id.address);
            phone= (TextView) itemView.findViewById(R.id.phoneText);
            contact = (ImageButton) itemView.findViewById(R.id.phone);
            loc = (ImageButton)itemView.findViewById(R.id.location);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public RecyclerVehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.store_card_view, parent, false);
        return new RecyclerVehicleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerVehicleViewHolder holder, int position) {
        Branch branch = branchArrayList.get(position);


        holder.branchName.setText(branch.getBranchName());
        holder.address.setText(branch.getAddress());
        holder.phone.setText(branch.getContactNo());
        holder.branchName.setTag(branch);

        holder.loc.setTag(new LatLng(branch.getLatitude(),branch.getLongitude()));
        holder.contact.setTag(branch.getContactNo());


        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StoreListFragment.callAction(holder.contact.getTag().toString(),(MainActivity)activity,storeListFragment);

            }});

        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StoreLocatorFragment storeLocatorFragment = new StoreLocatorFragment();
                Bundle args = new Bundle();
                args.putParcelable("point", (LatLng) holder.loc.getTag());
                storeLocatorFragment.setArguments(args);
                storeListFragment.getFragmentManager().beginTransaction().replace(R.id.frame, storeLocatorFragment, storeLocatorFragment.getClass().getSimpleName()).addToBackStack(storeLocatorFragment.getClass().getSimpleName()).commit();

            }});

        holder.cardView.setTag(holder);

    }

    @Override
    public int getItemCount() {
        if (branchArrayList.size() > 0) {
            return branchArrayList.size();

        } else return 0;
    }


}
