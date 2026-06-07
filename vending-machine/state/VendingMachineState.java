package state;

import model.Product;

public interface VendingMachineState {
    void selectProduct(Product product);

    void insertMoney(Integer money);

    void dispenseProduct();

    void cancelTransaction();
}