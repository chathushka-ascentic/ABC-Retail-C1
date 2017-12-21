package abc.ap.com.abcfashions.model;

/**
 * Created by Aparna Prasad on 12/17/2017.
 */

public class OrderLine {

    private Product product;
    private int qty;
    private Stock stock;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
