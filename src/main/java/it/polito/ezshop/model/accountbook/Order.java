package it.polito.ezshop.model.accountbook;

public class Order extends it.polito.ezshop.model.accountbook.BalanceOperation implements it.polito.ezshop.data.Order {

    private Integer id;
    private String productId;
    private String supplier;
    private double pricePerUnit;
    private int quantity;
    private String status;

    @Override
    public Integer getBalanceId(){
        return new Integer(super.getBalanceId());
    }

    @Override
    public void setBalanceId(Integer balanceId){
        super.setBalanceId(balanceId);
    }

    @Override
    public String getProductCode() {
        return this.productId;
    }

    @Override
    public void setProductCode(String productCode) {
        this.productId=this.productId;
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
        this.id=id;
    }

    public String getSupplier(){
        return this.supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
