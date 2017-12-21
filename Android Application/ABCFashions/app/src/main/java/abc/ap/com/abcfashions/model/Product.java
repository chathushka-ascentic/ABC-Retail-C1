package abc.ap.com.abcfashions.model;

import java.util.ArrayList;

/**
 * Created by Aparna Prasad on 12/17/2017.
 */

public class Product {

    private int productId;
    private String productName;
    private String description;
    private String categoryName;
    private String imageUrl;
    private double price;
    private ArrayList<Stock> availableStock = new ArrayList<>();


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Stock> getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(ArrayList<Stock> availableStock) {
        this.availableStock = availableStock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
