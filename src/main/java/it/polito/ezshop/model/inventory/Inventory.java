package it.polito.ezshop.model.inventory;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private  ArrayList<ProductType> list = new ArrayList<>();

    public Inventory(){

    }

    public Integer createProductType(String description, String productCode, double pricePerUnit, String note){
        return 0;
    }

    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote){
        return true;
    }

    public boolean deleteProductType(Integer id){
        return true;
    }

    public List<ProductType> listAllProductTypes(){
        return list;
    }

    public ProductType getProductTypeByBarcode(String barCode){
        return null;
    }

    public List<ProductType> getProductTypesByDescription(String description){
        return null;
    }

    public boolean updateQuantity(Integer productId, int toBeAdded){
        return true;
    }

    public boolean updatePosition(Integer productId, String newPos){
        return true;
    }

    private void savePersistent(){}
}
