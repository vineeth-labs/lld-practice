package model;

public class Product {
    String name;
    Integer id;
    Integer price;
    Integer quantity;
    public Product(String name, int id, double price) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.quantity = 1;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }
}
