package abc.ap.com.abcfashions.view;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.adapter.OrderLineItemAdapter;
import abc.ap.com.abcfashions.model.Order;
import abc.ap.com.abcfashions.model.OrderLine;
import abc.ap.com.abcfashions.model.Product;
import abc.ap.com.abcfashions.model.Stock;
import abc.ap.com.abcfashions.services.VolleySingletonController;
import abc.ap.com.abcfashions.services.VolleyUtils;


/**
 * Created by: Aparna Prasad
 */
public class FragmentCart extends Fragment
{

	private MainActivity activity;
	private ScannerActivity.barCodeCapturedListener listener;
	private int branchId=0;
	private Product product;
	private OrderLineItemAdapter adapt;
	private Order order;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_cart, container, false);
		activity = (MainActivity) getActivity();
		setHasOptionsMenu(true);

		Button  buttonScanner= (Button)view.findViewById(R.id.qr);
		ListView orderList = (ListView)view.findViewById(R.id.list_view);

		if(activity.getOrder() == null)
		activity.setOrder(new Order());

		this.order = activity.getOrder();

		branchId=activity.getBranchId();

		buttonScanner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				branchId=activity.getBranchId();

				if(branchId == 0)
				{
					Toast.makeText(getActivity(),"Cannot find the nearest branch",Toast.LENGTH_LONG).show();
				}
				else
				{
					ScannerActivity.registerListener(listener);

					Intent intent = new Intent(activity, ScannerActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});


		listener = new ScannerActivity.barCodeCapturedListener() {
			@Override public void onSuccess(String result) {

				final ProgressDialog progessDialog = new ProgressDialog(activity, R.style.AppThemeProgressDial);
				progessDialog.setIndeterminate(true);
				progessDialog.setCancelable(false);
				progessDialog.setMessage("Processing");
				progessDialog.show();

				ScannerActivity.unregisterListener();

				LinkedHashMap<String, String> queryParamCollection = new LinkedHashMap<>();
				queryParamCollection.put("userId", ""+App.LoggedInUser.getUserId());
				queryParamCollection.put("branchId", ""+branchId);
				queryParamCollection.put("qrCode", result);


				VolleyUtils.makeJsonObjectRequestGetSync(getActivity(), "getProduct.php", queryParamCollection, "product", new VolleySingletonController.VolleyResponseListener() {

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

							if (!new JsonParser().parse(res).isJsonNull())
							{
								JsonObject responseJson = ((JsonObject) new JsonParser().parse(res)).get("details").getAsJsonObject();
								JsonArray responseJsonArray = ((JsonObject) new JsonParser().parse(res)).get("availableStock").getAsJsonArray();

								Gson gson = new GsonBuilder().create();
								product = gson.fromJson(responseJson.toString(), Product.class);

								ArrayList<Stock> stocks= gson.fromJson(responseJsonArray.toString(), new TypeToken<ArrayList<Stock>>() {}.getType());
								product.setAvailableStock(stocks);

								showDialogWindow(1);

							}

						} catch (final Exception exception) {
							exception.printStackTrace();
							progessDialog.dismiss();
							App.setSnackbar(getView(), getResources().getString(R.string.internetCheckMsg));

						}

					}

				});


		}};


		adapt = new OrderLineItemAdapter(getActivity(), order.getOrderList());
		orderList.setAdapter(adapt);
		orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {

						TextView textviewInvoiceProductName = (TextView) view.findViewById(R.id.textviewInvoiceProductName);
						product = ((Product) textviewInvoiceProductName.getTag());
						showDialogWindow(2);
					}
		});


		return view;
	}


	private void showDialogWindow(int status)
	{
		// 1 - add
		// 2 - update
		final Dialog infoDialog = new Dialog(activity,R.style.AppThemeAlertMaterial);
		infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		infoDialog.setContentView(R.layout.dialog_info);
		((TextView) infoDialog.findViewById(R.id.name)).setText(product.getProductName());
		((TextView) infoDialog.findViewById(R.id.desc)).setText(product.getDescription());
		 final TextView price = ((TextView) infoDialog.findViewById(R.id.price));
		 price.setText("$"+App.convertDoubleToCurrency(product.getPrice()));

		Picasso.with(getActivity())
				.load(product.getImageUrl())
				.placeholder(R.drawable.noimage)
				.error(R.drawable.noimage)
				.into(((ImageView) infoDialog.findViewById(R.id.img)));

		final Spinner spinnerSize = (Spinner) infoDialog.findViewById(R.id.size);
		ArrayAdapter<Stock> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item,product.getAvailableStock());
		dataAdapter.setDropDownViewResource(R.layout.spinner_item);
		spinnerSize.setAdapter(dataAdapter);
		spinnerSize.setSelection(0);
		spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
											   View v, int position, long id) {
						price.setText("$"+App.convertDoubleToCurrency(((Stock)parent.getItemAtPosition(position)).getPrice()));

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		final EditText qtyEditText = ((EditText) infoDialog.findViewById(R.id.qty));
		Button btnAdd = ((Button) infoDialog.findViewById(R.id.btnAdd));
		Button btnRem = ((Button) infoDialog.findViewById(R.id.btnRemove));

		if(status == 1)
		{
			btnRem.setVisibility(View.GONE);
			btnAdd.setText("Add");
		}
		else
		{
			btnRem.setVisibility(View.VISIBLE);
			btnAdd.setText("Update");
		}

		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {

				if(qtyEditText.getText().toString().trim().isEmpty() || Integer.parseInt(qtyEditText.getText().toString()) == 0)
				{
					App.setSnackbar(view.getRootView(),"Enter Qty");
				}
				else if(product.getAvailableStock().size() == 0)
				{
					App.setSnackbar(view.getRootView(),"Out of stock");
				}
				else
				{

					final ProgressDialog progessDialog = new ProgressDialog(activity);
					progessDialog.setIndeterminate(true);
					progessDialog.setCancelable(false);
					progessDialog.setMessage("Validating stock");
					progessDialog.show();

					LinkedHashMap<String, String> queryParamCollection = new LinkedHashMap<>();
					queryParamCollection.put("userId", ""+App.LoggedInUser.getUserId());
					queryParamCollection.put("stockId", ""+((Stock) spinnerSize.getSelectedItem()).getStockId());
					queryParamCollection.put("orderQty", qtyEditText.getText().toString());

					VolleyUtils.makeJsonObjectRequestGetSync(getActivity(), "stockValidate.php", queryParamCollection, "stock", new VolleySingletonController.VolleyResponseListener() {

						@Override
						public void onError(String message) {
							try {
								progessDialog.dismiss();
								App.setSnackbar(view.getRootView(), getResources().getString(R.string.internetCheckMsg));
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

								if (resS.equals("200"))
								{
									infoDialog.dismiss();

									boolean exist=false;
									for (int i=0; i<order.getOrderList().size(); i++)
									{
										OrderLine orderLine = order.getOrderList().get(i);
										if(orderLine.getProduct().getProductId() == product.getProductId())
										{
											exist=true;
											orderLine.setQty(Integer.parseInt(qtyEditText.getText().toString()));
											orderLine.setStock(((Stock) spinnerSize.getSelectedItem()));
											break;
										}
									}

									if(!exist)
									{
										OrderLine orderLine = new OrderLine();
										orderLine.setProduct(product);
										orderLine.setQty(Integer.parseInt(qtyEditText.getText().toString()));
										orderLine.setStock(((Stock) spinnerSize.getSelectedItem()));

										order.getOrderList().add(orderLine);
									}
									adapt.notifyDataSetChanged();

								}
								else if(resS.equals("201"))
								{
									App.setSnackbar(view.getRootView(), "Out of stock");
								}

							} catch (final Exception exception) {
								exception.printStackTrace();
								App.setSnackbar(view.getRootView(), getResources().getString(R.string.internetCheckMsg));

							}

						}

					});

				}

			}
		});

		btnRem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				for (int i=0; i<order.getOrderList().size(); i++)
				{
					OrderLine orderLine = order.getOrderList().get(i);
					if(orderLine.getProduct().getProductId() == product.getProductId())
					{
						order.getOrderList().remove(orderLine);
						adapt.notifyDataSetChanged();
						break;
					}
				}
				infoDialog.dismiss();

			}
		});

		infoDialog.show();
	}

}


