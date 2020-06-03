package generator.nex.rexx.userregistration.Model;

/**
 * Created by rexx on 29-04-2018.
 */

public class Order {

    private int Id;
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;


    public Order() {
    }

    public Order(String productId, String productName, String quantity, String price, String discount) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public Order(int Id, String productId, String productName, String quantity, String price, String discount) {
        this.Id = Id;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public int getID() {
        return Id;
    }

    public void setID(int Id) {
        this.Id = Id;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
