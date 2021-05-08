package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.inventory.ProductType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EZShop implements EZShopInterface {
    private Connection conn;
    private User loggedUser;

    private List<ProductType> inventory = new ArrayList<ProductType>();
    private List<Customer> customerList = new ArrayList<Customer>();
    private List<User> userList = new ArrayList<User>();
    private List<BalanceOperation> balanceOperations = new ArrayList<BalanceOperation>();
    private List<Order> orderList = new ArrayList<>();
    private boolean isOrderListValid = false;


    public EZShop() throws SQLException, InvalidCustomerNameException {
        //assegnare user(solo riferimento no nuova istanza) che si logga a loggedUser
        //String sql="SELECT 'username' FROM user ";
        try {
            // db parameters
            String url = "jdbc:sqlite:ezshop_db.sqlite";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException  e) {
            System.out.println(e.getMessage());
        }
        //CREATE CUSTOMERLIST
        String sql="SELECT customerId, loyaltyCardId, customerName, points  FROM customer " ;
        try {

          PreparedStatement st = conn.prepareStatement(sql);
          ResultSet rs = st.executeQuery();
          while (rs.next()) {
                customerList.add( new Customer (rs.getInt("customerId"),
                                                rs.getString("loyaltyCardId"),
                                                rs.getString("customerName"),
                                                rs.getInt("points")
                                                )
                                );
            }
          System.out.println("Lista di customer:");
          System.out.println(customerList.toString());
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        //CREATE USERLIST
        sql="SELECT id, username, password, role, status  FROM user " ;
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
               userList.add ( new User   (   rs.getInt("id"),
                                            rs.getString("username"),
                                            rs.getString("password"),
                                            rs.getString("role"),
                                            rs.getBoolean("status")
                                         )
                            );
            }
            System.out.println("Lista di user:");
            System.out.println(userList.toString());
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        //CREATE INVENTORY
        sql="SELECT   FROM inventory " ;
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                userList.add( new User (    rs.getInt(""),
                                            rs.getString(""),
                                            rs.getString(""),
                                            rs.getString(""),
                                            rs.getBoolean("")
                                        )
                            );
            }
            System.out.println("Lista di productType:");
            System.out.println(inventory.toString());
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        return null;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        return null;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException{
        if (id.intValue()<=0 || id==null)
        {
            throw new InvalidUserIdException();
        }
        else if(loggedUser.getRole().equals("Administrator"))
        {
            //return userList.getUser(id);
            return null;
        }
        else
            throw new UnauthorizedException();
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        if (id.intValue()<=0 || id==null)
        {
            throw new InvalidUserIdException();
        }
        else if (!role.equals("Administrator") || !role.equals("ShopManager") || !role.equals("Cashier"))
        {
            throw new InvalidRoleException();
        }
        else if(loggedUser.getRole().equals("Administrator"))
        {
            //return userList.updateUserRights(id,role);
            return true;
        }
        else
            throw new UnauthorizedException();
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        return null;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();


        inventory.createProductType(description,productCode,pricePerUnit,note);

        return getProductTypeByBarCode(productCode).getId();
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        boolean res =inventory.updateProduct(id, newDescription, newCode, newPrice, newNote);

        return res;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        if(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();

        boolean res = inventory.deleteProductType(id);

        return res;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        if(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))
            throw new UnauthorizedException();
        //List<ProductType> res = inventory.listAllProductTypes();
        return null;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        return null;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return null;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
}
