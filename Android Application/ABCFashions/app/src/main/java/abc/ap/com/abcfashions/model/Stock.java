package abc.ap.com.abcfashions.model;

/**
 * Created by Aparna Prasad on 12/18/2017.
 */

public class Stock {

    private int stockId;
    private String sizeName;
    private double price;

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return sizeName;
    }
}
