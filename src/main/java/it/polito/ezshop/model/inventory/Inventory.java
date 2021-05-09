package it.polito.ezshop.model.inventory;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private  ArrayList<ProductType> list;
    private Integer id;

    public Inventory(){
        list= new ArrayList<ProductType>();
        id=0;
    }

    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {

        Integer quantity= 0;
        double discountRate=0.0;
        ProductType product= getProductTypeByBarcode(productCode);
        if(product!=null){
            return -1;
        }
        product = new ProductType(id, productCode, description, pricePerUnit, quantity, discountRate, note, null);
        id++;
        list.add(product);



        return product.getId();
    }

    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException{
        ProductType productType= getProductTypeById(id);
        if(productType==null){
            return false;
        }
        if(id==null || id<=0 ){
            throw new InvalidProductIdException("Product's id isn't valid");
        }

        boolean clean = deleteProductType(id);
        //productType = new ProductType(id, newCode, newDescription, newPrice, productType.getQuantity(), productType.getDiscountRate(), newNote, productType.getPosition());


        return true;
    }

    public boolean deleteProductType(Integer id) throws InvalidProductIdException{
        ProductType productType= getProductTypeById(id);
        if(productType==null){
            return false;
        }
        if(id==null || id<=0 ){
            throw new InvalidProductIdException("Product's id isn't valid");
        }

        list.remove(productType);
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

    private ProductType getProductTypeById(Integer id){
        for(ProductType p: list){
            if(p.getId()==id){
                return p;
            }
        }
        return null;
    }
}
