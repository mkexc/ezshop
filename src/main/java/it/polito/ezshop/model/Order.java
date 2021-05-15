package it.polito.ezshop.model;

public class Order extends BalanceOperation implements it.polito.ezshop.data.Order{

    private Integer id;
    private String productCode;    // barcode of pro
    private double pricePerUnit;
    private Integer quantity;
    private String status;

    public Order(Integer id){
        super();
        this.id=id;
    }

    public Order(Integer id, String productCode, double pricePerUnit, Integer quantity, String status){
        super();
        this.id = id;
        this.productCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.status = status;
    }

    @Override
    public int getBalanceId(){
         return super.getBalanceId();
    }

    @Override
    public void setBalanceId(Integer balanceId){
        super.setBalanceId(balanceId);
    }

    @Override
    public String getProductCode() {
        return this.productCode;
    }

    @Override
    public void setProductCode(String productCode) {
        this.productCode=productCode;
    }

    @Override
    public double getPricePerUnit() {
        return this.pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit=pricePerUnit;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status=status;
    }

    @Override
    public Integer getOrderId() {
        return  this.id;
    }

    @Override
    public void setOrderId(Integer orderId) {
        this.id=orderId;
    }
}
