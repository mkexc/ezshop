package it.polito.ezshop.model.inventory;

public class ProductType implements it.polito.ezshop.data.ProductType{
    private Integer id;
    private String productCode;
    private String description;
    private double pricePerUnit;
    private Integer quantity;
    private String notes;
    private String location;

    public ProductType(Integer id, String productCode, String description, double pricePerUnit, Integer quantity, String notes, String location){
        this.id = id;
        this.productCode = productCode;
        this.description = description;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.notes = notes;
        this.location = location;
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
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getNote() {
        return this.notes;
    }

    @Override
    public void setNote(String note) {
        this.notes=note;
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
}
