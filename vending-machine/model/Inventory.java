package model;

import java.util.Map;

public class Inventory {
    private Map<Integer, Product> items;

    public Inventory() {
        this.items = new java.util.HashMap<>();
    }

    public Inventory(Map<Integer, Product> items) {
        this.items = items == null ? new java.util.HashMap<>() : items;
    }

    public Map<Integer, Product> getItems() {
        return items;
    }

    public void putProduct(int productId, Product product) {
        if (this.items == null) {
            this.items = new java.util.HashMap<>();
        }
        this.items.put(productId, product);
    }

    public Product getProduct(int productId) {
        return items == null ? null : items.get(productId);
    }

    public void decreaseProduct(int productId) {
        Product product = getProduct(productId);
        if (product != null && product.getQuantity() > 0) {
            product.setQuantity(product.getQuantity() - 1);
        } else {
            throw new IllegalArgumentException("Product decrease failed");
        }
    }
}
