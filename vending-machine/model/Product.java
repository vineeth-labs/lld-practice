package model;

public class Product {
    private String name;
    private Integer id;
    private Integer price;
    private Integer quantity;
    public Product(String name, int id, int price) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
