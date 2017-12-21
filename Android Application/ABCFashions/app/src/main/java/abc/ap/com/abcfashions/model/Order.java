package abc.ap.com.abcfashions.model;

import java.util.ArrayList;

/**
 * Created by Aparna Prasad on 12/17/2017.
 */

public class Order {

    private int orderId;
    private double orderTotal;
    private String orderDate;
    private int paidBy;
    private ArrayList<OrderLine> orderList = new ArrayList<>();

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getDate() {
        return orderDate;
    }

    public void setDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(int paidBy) {
        this.paidBy = paidBy;
    }

    public ArrayList<OrderLine> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<OrderLine> orderList) {
        this.orderList = orderList;
    }
}
