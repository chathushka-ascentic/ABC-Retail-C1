package abc.ap.com.abcfashions.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.OrderLine;
import abc.ap.com.abcfashions.view.App;

/**
 * Created by Aparna Prasad
 */
public class OrderLineItemAdapter extends ArrayAdapter<OrderLine>{

	private final Context context;
	private final ArrayList<OrderLine> invoiceLineItemCollection;

	public OrderLineItemAdapter(Context context, ArrayList<OrderLine> invoiceLineItemCollection) {
		super(context, R.layout.listview_item_invoice_line_item, invoiceLineItemCollection);
		this.context = context;
		this.invoiceLineItemCollection = invoiceLineItemCollection;
		
	}
	
	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View rowView, ViewGroup parent) {
		

		OrderLine orderLine = invoiceLineItemCollection.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rowView = inflater.inflate(R.layout.listview_item_invoice_line_item, parent, false);

		TextView textviewInvoiceProductName = (TextView) rowView.findViewById(R.id.textviewInvoiceProductName);
		textviewInvoiceProductName.setText(orderLine.getProduct().getProductName());
		textviewInvoiceProductName.setTag(orderLine.getProduct());

		TextView textviewInvoiceProductCode = (TextView) rowView.findViewById(R.id.textviewInvoiceProductCode);
		textviewInvoiceProductCode.setText("Size-"+orderLine.getStock().getSizeName());

		TextView textviewInvoiceUnitPrice = (TextView) rowView.findViewById(R.id.textviewInvoiceUnitPrice);
		textviewInvoiceUnitPrice.setText(App.convertDoubleToCurrency(orderLine.getStock().getPrice()));

		TextView textviewInvoiceNumberOfItems = (TextView) rowView.findViewById(R.id.textviewInvoiceNumberOfItems);
		textviewInvoiceNumberOfItems.setText(String.valueOf(orderLine.getQty()));

		TextView textviewInvoiceTotal = (TextView) rowView.findViewById(R.id.textviewInvoiceTotal);
		textviewInvoiceTotal.setText(App.convertDoubleToCurrency(orderLine.getQty() * orderLine.getStock().getPrice()));


		return rowView;
	}
	
}
