package model;

import java.util.Map;

public class Inventory {
    Map<Integer, Product> items;

    public Product getProduct(int productId) {
        return items.get(productId);
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
