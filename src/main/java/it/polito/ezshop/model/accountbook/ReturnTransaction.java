package it.polito.ezshop.model.accountbook;

import it.polito.ezshop.model.inventory.ProductType;

public class ReturnTransaction {
    private int quantity;
    private ProductType product;
    private Integer idSaleTransaction;

    public ReturnTransaction(int quantity, ProductType product, Integer idSaleTransaction){
        this.quantity=quantity;
        this.product=product;
        this.idSaleTransaction=idSaleTransaction;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductType getProduct() {
        return product;
    }

    public void setProduct(ProductType product) {
        this.product = product;
    }

    public Integer getIdSaleTransaction() {
        return idSaleTransaction;
    }

    public void setIdSaleTransaction(Integer idSaleTransaction) {
        this.idSaleTransaction = idSaleTransaction;
    }

}
