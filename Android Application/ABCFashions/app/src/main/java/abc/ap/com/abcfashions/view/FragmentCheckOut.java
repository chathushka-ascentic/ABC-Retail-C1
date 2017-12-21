package abc.ap.com.abcfashions.view;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Order;
import abc.ap.com.abcfashions.model.OrderLine;
import abc.ap.com.abcfashions.services.VolleySingletonController;
import abc.ap.com.abcfashions.services.VolleyUtils;

/**
 * Created by: Aparna Prasad
 */

public class FragmentCheckOut extends Fragment
{

	private Order order;
	private int branchId=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_checkout, container, false);

		TextView date = (TextView) view.findViewById(R.id.datee);
		TextView totalT = (TextView) view.findViewById(R.id.total);
		TextView nof = (TextView) view.findViewById(R.id.nof);
		RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.payby);
		Button checkout = (Button) view.findViewById(R.id.checkOut);


		final MainActivity activity = (MainActivity) getActivity();

		if(activity.getOrder() == null)
		activity.setOrder(new Order());

		this.order = activity.getOrder();

		branchId=activity.getBranchId();

		double total=0;
		for (int i=0; i<order.getOrderList().size(); i++)
		{
			OrderLine orderLine = order.getOrderList().get(i);
			total = total + (orderLine.getQty() * orderLine.getStock().getPrice());
		}

		order.setOrderTotal(total);
		totalT.setText(App.convertDoubleToCurrency(order.getOrderTotal()));
		nof.setText(String.valueOf(order.getOrderList().size()));
		date.setText(App.dateFormat.format(new Date()));

		order.setDate(date.getText().toString());
		order.setPaidBy(1);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				RadioButton rb=(RadioButton) view.findViewById(checkedId);
				order.setPaidBy(Integer.parseInt(rb.getTag().toString()));
			}
		});


		checkout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				branchId=activity.getBranchId();

				if(branchId == 0)
				{
					Toast.makeText(getActivity(),"Cannot find the nearest branch",Toast.LENGTH_LONG).show();
				}
				else {
					try {
						final ProgressDialog progessDialog = new ProgressDialog(activity);
						progessDialog.setIndeterminate(true);
						progessDialog.setCancelable(false);
						progessDialog.setMessage("Processing");
						progessDialog.show();

						Gson gson = new Gson();
						String userJson = gson.toJson(order);
						JSONObject jsonObject = new JSONObject(userJson);
						jsonObject.put("userId", "" + App.LoggedInUser.getUserId());
						jsonObject.put("branchId", "" + branchId);

						JSONObject orderJsonObject1 = new JSONObject().put("OrderInsertRequest", jsonObject);

						VolleyUtils.makeJsonObjectRequestPOST(getActivity(), "insertOrder.php", orderJsonObject1, "order", new VolleySingletonController.VolleyResponseListener() {

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

									JsonObject jobject = ((JsonObject) new JsonParser().parse(res)).getAsJsonObject();
									String resS = jobject.get("status").getAsString();
									if (resS.equals("200")) {
										App.setSnackbar(getView(), getResources().getString(R.string.thankyou));

										activity.setOrder(null);
										activity.getTabLayout().getTabAt(0).select();

									} else {
										App.setSnackbar(getView(), getResources().getString(R.string.internetCheckMsg));
									}

								} catch (final Exception exception) {
									exception.printStackTrace();
									progessDialog.dismiss();
									App.setSnackbar(getView(), getResources().getString(R.string.internetCheckMsg));

								}

							}

						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return view;
	}

}


