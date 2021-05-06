package it.polito.ezshop.model.inventory;

import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;

public class ProductType implements it.polito.ezshop.data.ProductType{
    private Integer id;
    private String productCode;
    private String description;
    private double pricePerUnit;
    private Integer quantity;
    private double discountRate;
    private String notes;
    private Position position;

    public ProductType(Integer id, String productCode, String description, double pricePerUnit, Integer quantity, double discountRate, String notes, Position position) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {


        if(pricePerUnit<=0){
            throw new InvalidPricePerUnitException("The price isn't valid");
        }
        if(description==null || description.equals("")){
            throw new InvalidProductDescriptionException("The description isn't valid");
        }
        if(id==null || id<0 || !Integer.TYPE.isInstance(id) ){
            throw new InvalidProductCodeException("The product code isn't valid");
        }




        this.id = id;
        this.productCode = productCode;
        this.description = description;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.notes = notes;
        this.position = position;
    }

    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity=quantity;
    }

    @Override
    public String getLocation() {
        return this.position.toString();
    }

    @Override
    public void setLocation(String location) {
        this.position = new Position(location);
    }

    @Override
    public String getNote() {
        return this.notes;
    }

    @Override
    public void setNote(String note) {
        this.notes=notes;
    }

    @Override
    public String getProductDescription() {
        return this.description;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.description=productDescription;
    }

    @Override
    public String getBarCode() {
        return this.productCode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.productCode=barCode;
    }

    @Override
    public Double getPricePerUnit() {
        return this.pricePerUnit;
    }

    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit=pricePerUnit;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    public double getDiscountRate(){
        return this.discountRate;
    }

    public void setDiscountRate(double discountRate){
        this.discountRate=discountRate;
    }

    public Position getPosition() {
        return position;
    }
}
