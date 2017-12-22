package abc.ap.com.abcfashions.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.ap.com.abcfashions.R;

/**
 * Created by: Aparna Prasad
 */

public class FragmentOrder extends Fragment
{



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_add_order, container, false);
		final MainActivity activity = (MainActivity) getActivity();
		setHasOptionsMenu(true);


		final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

		tabLayout.addTab(tabLayout.newTab().setText(activity.getResources().getString(R.string.cart)), 0, false);
		tabLayout.addTab(tabLayout.newTab().setText(activity.getResources().getString(R.string.checkOut)), 1, false);

		((MainActivity)getActivity()).getSupportActionBar().setTitle("Shop");

		activity.setTabLayout(tabLayout);

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

			@Override
			public void onTabSelected(TabLayout.Tab tab) {


				if(tab.getText().equals(activity.getResources().getString(R.string.cart)))
				{
					MainActivity activity = (MainActivity) getActivity();
					activity.getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutInvoicePlaceHolder, new FragmentCart()).commit();
				}
				else if (tab.getText().equals(activity.getResources().getString(R.string.checkOut)))
				{
					if (activity.getOrder() == null)
					{
						App.showToast("Add items to cart",activity);
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								activity.getTabLayout().getTabAt(0).select();
							}
						});
					}
					else
					{
						activity.getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutInvoicePlaceHolder, new FragmentCheckOut()).commit();
					}
				}

			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				//Log.i("tabunselected", "tabunselected" + tab.getPosition());
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				//Log.i("tabreselected", "tabreselected" + tab.getPosition());

			}
		});



		activity.getTabLayout().getTabAt(0).select();


		return view;
	}

}


