package abc.ap.com.abcfashions.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Offer;


/**
 * Created by Aparna Prasad
 */
public class RecyclerOfferAdapter extends RecyclerView.Adapter<RecyclerOfferAdapter.RecyclerVehicleViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Offer> ofArrayList;


    public RecyclerOfferAdapter(Context context,ArrayList<Offer> InList) {
        this.ofArrayList = InList;
        inflater = LayoutInflater.from(context);
    }



    static class RecyclerVehicleViewHolder extends RecyclerView.ViewHolder
    {
        TextView desc,desc1;
        CardView cardView;

        RecyclerVehicleViewHolder(View v) {
            super(v);
            desc= (TextView) itemView.findViewById(R.id.desc);
            desc1= (TextView) itemView.findViewById(R.id.desc1);
        }
    }

    @Override
    public RecyclerVehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.offer_card_view, parent, false);
        return new RecyclerVehicleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerVehicleViewHolder holder, int position) {
        Offer offer = ofArrayList.get(position);


        holder.desc.setText(offer.getDesc());
        holder.desc1.setText(offer.getDesc1());

    }

    @Override
    public int getItemCount() {
        if (ofArrayList.size() > 0) {
            return ofArrayList.size();

        } else return 0;
    }


}
