package it.polito.ezshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import it.polito.ezshop.exceptions.*;

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
    private boolean isOrderListUpdated = false;
    private boolean isBalanceOperationUpdated = false;
    private boolean isCustomerListUpdated = false;
    private boolean isUserListUpdated = false;
    private boolean isInventoryUpdated = false;


    public EZShop() throws SQLException, InvalidCustomerNameException {
        // open db connection
        try {
            // db parameters
            String url = "jdbc:sqlite:ezshop_db.sqlite";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void reset() {
        // return to  base state: balance zero, no transactions, no products
    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        // TODO dobbiamo controllare che ci sia un user loggato? Nell'interface non lo chiede
        // username not null, not empty
        if(username == null || username.equals(""))
            throw new InvalidUsernameException();

        // password not null, not empty
        if(password == null || password.equals("")){
            throw new InvalidPasswordException();
        }

        // role not null, not empty, not Administrator&&ShopManager&&Cashier
        if(role == null || role.equals("") || (!role.equals("Administrator") && !role.equals("ShopManager") && !role.equals("Cashier"))){
            throw new InvalidRoleException();
        }

        // check if username is already present
        String sql = "SELECT username FROM user WHERE username=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if(rs.getString("username").equals(username)) {
                // product already present
                return -1;
            }
        } catch (SQLException e) {
            // problems with db connection
            return -1;
        }

        // insert the new user
        String sql2 = "INSERT INTO user(username, password, role) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, role);
            st.executeUpdate();
            conn.commit();
            isUserListUpdated = false;
            return st.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        // check role of the user (only administrator)
        if(loggedUser == null || !loggedUser.getRole().equals("Administrator")){
            throw new UnauthorizedException();
        }

        // id not null, not <= 0
        if(id == null || id <= 0){
            throw new InvalidUserIdException();
        }

        // delete user using id
        String sql="DELETE FROM user WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, id);
            st.executeUpdate();
            conn.commit();
            isUserListUpdated = false;
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        // check role of the user (only administrator)
        if (!loggedUser.getRole().equals("Administrator")) {
            throw new UnauthorizedException();
        }

        // if cached userList is not updated, download from db
        if(!isUserListUpdated) {
            String sql = "SELECT id, password, role, username FROM user";
            List<User> list = null;
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    list.add(new it.polito.ezshop.model.userlist.User(rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    ));
                }
                userList = list;
                isUserListUpdated = true;
                return userList;
            } catch (SQLException e) {
                return null;
            }
        }
        else
            return userList;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        // check role of the user (only administrator)
        if (!loggedUser.getRole().equals("Administrator")) {
            throw new UnauthorizedException();
        }

        // id not null, not <= 0
        if (id == null || id <= 0) {
            throw new InvalidUserIdException();
        }

        String sql = "SELECT id, password, role, username FROM user WHERE id=?";
        User user=null;
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                user = new it.polito.ezshop.model.userlist.User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        // check role of the user (only administrator)
        if(loggedUser.getRole().equals("Administrator")) {
            throw new UnauthorizedException();
        }

        // id not null, not <= 0
        if (id == null || id <= 0) {
            throw new InvalidUserIdException();
        }

        // role is Administrator||ShopManager||Cashier
        if (!role.equals("Administrator") && !role.equals("ShopManager") && !role.equals("Cashier")) {
            throw new InvalidRoleException();
        }

        String sql = "UPDATE user SET role=? WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, role);
            st.setInt(2, id);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        // there is already a logged user
        if(loggedUser!=null)
            return null;

        String sql = "SELECT id, password, role, username FROM user WHERE username=?";
        User user = null;
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            // TODO mettere classe User dentro il package data chiamandola UserImpl?
            // TODO cancellare classi "lista"
            user = new it.polito.ezshop.model.userlist.User(rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
            );

            // check password
            if(rs.getString("password").equals(password))
                return user;
            else
                throw new InvalidPasswordException();
        } catch (SQLException e) {
            throw new InvalidUsernameException();
        }
    }

    @Override
    public boolean logout() {
        if (loggedUser!=null)
            loggedUser = null;
        else
            return false;
        return true;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        // check role of the user (only administrator and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // producType not null, not empty, 12<productCode<14
        if(productCode==null||productCode.equals(""))
            throw  new InvalidProductCodeException();
        try{
            Integer c= Integer.parseInt(productCode);
            if(productCode.length()>14||productCode.length()<12){
                throw  new InvalidProductCodeException();
            }
        }catch (NumberFormatException e){
            throw  new InvalidProductCodeException();
        }

        // description not null, not empty
        if(description==null||description.equals("")){
            throw new InvalidProductDescriptionException();
        }

        // pricePerUnit not <=0
        if(pricePerUnit<=0){
            throw new InvalidPricePerUnitException();
        }

        // check if productCode is already present
        String sql = "SELECT productCode FROM ProductType WHERE productCode=?";
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, productCode);
            ResultSet rs = st.executeQuery();

            if(rs.getString("productCode").equals(productCode)) {
                // product already present
                return -1;
            }
        } catch (SQLException e) {
            // problems with db connection
            return -1;
        }

        // insert the new productType
        String sql2="INSERT INTO ProductType(productCode, description, pricePerUnit, quantity, discountRate, notes, position) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setString(1, productCode);
            st.setString(2, description);
            st.setDouble(3, pricePerUnit);
            st.setInt(4, 0);
            st.setDouble(5,0.0);
            st.setString(6,note);
            st.setString(7,"");
            st.executeUpdate();
            return st.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        // check role of the user (only administrator and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
           throw new UnauthorizedException();

        // check id of the product (not <=0)
        if(id==null||id<=0)
            throw new InvalidProductIdException();

        // producType not null, not empty, 12<productCode<14
        if(newCode==null||newCode.equals(""))
            throw  new InvalidProductCodeException();
        try{
            Integer c= Integer.parseInt(newCode);
            if(newCode.length()>14||newCode.length()<12){
                throw  new InvalidProductCodeException();
            }
        }catch (NumberFormatException e){
            throw  new InvalidProductCodeException();
        }

        // description not null, not empty
        if(newDescription==null||newDescription.equals("")){
            throw new InvalidProductDescriptionException();
        }

        // pricePerUnit not <=0
        if(newPrice<=0){
            throw new InvalidPricePerUnitException();
        }

        // check if productCode is already present
        String sql = "SELECT productCode FROM ProductType WHERE productCode=?";
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newCode);
            ResultSet rs = st.executeQuery();

            if(rs.getString("productCode").equals(newCode)) {
                // product already present
                return false;
            }
        } catch (SQLException e) {
            // problems with db connection
            return false;
        }

        String sql2 = "UPDATE ProductType SET productCode=?, description=?, pricePerUnit=?, notes=? WHERE id=?";

        try {

            PreparedStatement st = conn.prepareStatement(sql2);
            st.setString(1, newCode);
            st.setString(2, newDescription);
            st.setDouble(3, newPrice);
            st.setString(4, newNote);
            st.setInt(5, id);
            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        // check role of the user (only administrator and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // check id of the product (not <=0)
        if(id==null||id<=0)
            throw new InvalidProductIdException();

        String sql="DELETE FROM ProductType WHERE id=? " ;
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1,id);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        List<ProductType> list = new ArrayList<>();
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager")&&(!loggedUser.getRole().equals("Cashier")))))
            throw new UnauthorizedException();

        String sql="SELECT * FROM ProductType" ;
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                list.add(new it.polito.ezshop.model.inventory.ProductType(
                                            rs.getInt("id"),
                                            rs.getString("productCode"),
                                            rs.getString("description"),
                                            rs.getDouble("pricePerUnit"),
                                            rs.getInt("quantity"),
                                            rs.getDouble("discountRate"),
                                            rs.getString("notes"),
                                            rs.getString("position")
                ));

            }

            return list;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // check if productCode is already present
        String sql = "SELECT productCode FROM ProductType WHERE productCode=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, barCode);
            ResultSet rs = st.executeQuery();

            if(rs.getString("productCode").equals(barCode)) {
                // product already present
                return null;
            }
        } catch (SQLException e) {
            // problems with db connection
            return null;
        }

        ProductType product= null;
        String sql5 = "SELECT * FROM ProductType WHERE productCode=?";
        try {

            PreparedStatement st = conn.prepareStatement(sql5);
            st.setString(1, barCode);
            ResultSet rs = st.executeQuery();

            product= new it.polito.ezshop.model.inventory.ProductType(
                    rs.getInt("id"),
                    rs.getString("productCode"),
                    rs.getString("description"),
                    rs.getDouble("pricePerUnit"),
                    rs.getInt("quantity"),
                    rs.getDouble("discountRate"),
                    rs.getString("notes"),
                    rs.getString("position")
            );
            return product;
        } catch (SQLException e) {
            // problems with db connection
            return null;
        }
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        List<ProductType> list= new ArrayList<ProductType>();
        String sql6 = "SELECT * FROM ProductType WHERE description=?";
        try {

            PreparedStatement st = conn.prepareStatement(sql6);
            st.setString(1, description);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                list.add(new it.polito.ezshop.model.inventory.ProductType(
                        rs.getInt("id"),
                        rs.getString("productCode"),
                        rs.getString("description"),
                        rs.getDouble("pricePerUnit"),
                        rs.getInt("quantity"),
                        rs.getDouble("discountRate"),
                        rs.getString("notes"),
                        rs.getString("position")
                        )
                );
            }

            return list;
        } catch (SQLException e) {
            // problems with db connection
            return null;
        }
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // check id of the product (not <=0)
        if(productId==null||productId<=0)
            throw new InvalidProductIdException();

        // query the product quantity
        String sql="SELECT quantity, position FROM productType WHERE id=? " ;
        int quantity;
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1,productId);
            ResultSet rs = st.executeQuery();
            quantity = rs.getInt("quantity");
            if(rs.getString("position").equals("")){
                return false;
            }
        } catch (SQLException e) {
            return false;
        }

        // updating the value to quantity
        quantity= quantity+toBeAdded;

        String sql2="UPDATE productType SET quantity=? WHERE id=? " ;
        try {
            PreparedStatement st = conn.prepareStatement(sql2);

            st.setInt(1,quantity);
            st.setInt(2,productId);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if(newPos==null){
            newPos="";
        }
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();
        // check id of the product (not <=0)
        if(productId==null||productId<=0)
            throw new InvalidProductIdException();
        // check position format (number-string-number)
        if(!newPos.equals("") && !newPos.matches("[0-9]+-[a-zA-Z]+-[0-9]+")){
            throw new InvalidLocationException();
        }

        // query for checking the uniqueness of position
        String sql="SELECT position FROM productType WHERE position=? " ;

        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setString(1,newPos);
            ResultSet rs = st.executeQuery();

            if(rs.getString("position").equals(newPos)){
                return false;
            }
        } catch (SQLException e) {
            return false;
        }

        String sql2="UPDATE productType SET position=? WHERE id=? " ;
        try {
            PreparedStatement st = conn.prepareStatement(sql2);

            st.setString(1,newPos);
            st.setInt(2,productId);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }


    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();
        //check if the product exist
        ProductType product = this.getProductTypeByBarCode(productCode);
        if(product==null)
            return -1;
        //check quantity is not <=0
        if(quantity<=0)
            throw new InvalidQuantityException();
        //check pricePerUnit is not <=0
        if(pricePerUnit<=0)
            throw new InvalidPricePerUnitException();

        // insert the new productType
        String sql="INSERT INTO order(productCode, pricePerUnit, quantity, status) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, productCode);
            st.setDouble(2, pricePerUnit);
            st.setInt(3, quantity);
            st.setString(4,"ISSUED");
            st.executeUpdate();
            return st.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();
        //check if the product exist
        ProductType product = this.getProductTypeByBarCode(productCode);
        if(product==null)
            return -1;
        //check quantity is not <=0
        if(quantity<=0)
            throw new InvalidQuantityException();
        //check pricePerUnit is not <=0
        if(pricePerUnit<=0)
            throw new InvalidPricePerUnitException();

        // insert the new productType
        String sql="INSERT INTO order(productCode, pricePerUnit, quantity, status) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, productCode);
            st.setDouble(2, pricePerUnit);
            st.setInt(3, quantity);
            st.setString(4,"PAYED");
            Boolean out = this.recordBalanceUpdate(-pricePerUnit*quantity);
            if(!out){
                return -1;
            }
            st.executeUpdate();
            return st.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();
        if(orderId==null||orderId<=0){
            throw new InvalidOrderIdException();
        }

        String sql2="SELECT quantity, pricePerUnit, status FROM order WHERE id=? " ;
        String actualStatus;
        double toBeAdded=0.0;

        try {
            PreparedStatement st = conn.prepareStatement(sql2);

            st.setInt(1,orderId);
            ResultSet rs = st.executeQuery();
            actualStatus = rs.getString("status");
            toBeAdded=-rs.getDouble("pricePerUnit")*rs.getInt("quantity");
        } catch (SQLException e) {
            return false;
        }

        if(actualStatus.equals("PAYED"))
            return false;

        String sql3="UPDATE order SET status=? WHERE id=? " ;
        try {
            PreparedStatement st = conn.prepareStatement(sql3);

            st.setString(1,"PAYED");
            st.setInt(2,orderId);
            recordBalanceUpdate(toBeAdded);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }



    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();
        if(orderId==null||orderId<=0){
            throw new InvalidOrderIdException();
        }



        String sql="SELECT quantity, productCode, status FROM order WHERE id=? " ;
        int quantity=0;
        String productCode;
        ProductType product;
        String actualStatus;
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1,orderId);
            ResultSet rs = st.executeQuery();
            quantity = rs.getInt("quantity");
            productCode= rs.getString("productCode");
            actualStatus = rs.getString("status");
            product=this.getProductTypeByBarCode(productCode);
        } catch (Exception e) {
            return false;
        }

        if(product.getLocation().equals("")){
            throw new InvalidLocationException();
        }
        if(actualStatus.equals("COMPLETED"))
            return false;


        try {
            this.updateQuantity(product.getId(), quantity);
        }catch(Exception e){
            return false;
        }

        String sql3="UPDATE order SET status=? WHERE id=? " ;
        try {
            PreparedStatement st = conn.prepareStatement(sql3);

            st.setString(1,"COMPLETED");
            st.setInt(2,orderId);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser==null || (!loggedUser.getRole().equals("Administrator")&&(!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        String sql3="SELECT * FROM order" ;
        try {
            PreparedStatement st = conn.prepareStatement(sql3);

            ResultSet rs = st.executeQuery();
            while(rs.next()){
                this.orderList.add( new it.polito.ezshop.model.accountbook.Order(
                                                rs.getInt("id"),
                                                rs.getString("productCode"),
                                                rs.getDouble("pricePerUnit"),
                                                rs.getInt("quantity"),
                                                rs.getString("status")
                ));
            }
            return orderList;
        } catch (SQLException e) {
            return null;
        }

    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        this.isCustomerListUpdated = false;
        if(!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier"))
            throw new UnauthorizedException();
        else if (customerName==null || customerName.isEmpty())
            throw new InvalidCustomerNameException();
        else
        {
            String sql = "INSERT INTO customer(customerName, loyaltyCardId, points) VALUES (?, ?, ?)";
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                st.setString(1,customerName);
                st.setString(2,"");
                st.setInt(3,0);
                st.executeUpdate();
                conn.commit();
                return st.getGeneratedKeys().getInt(1);
            } catch (SQLException e) {
                return -1;
            }
        }
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        this.isCustomerListUpdated = false;
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else if (newCustomerName==null || newCustomerName.isEmpty())
            throw new InvalidCustomerNameException();
        else if (newCustomerCard==null || newCustomerCard.length()!=10 || !newCustomerCard.matches("[0-9]+") ) {
            throw new InvalidCustomerCardException();
        }
        else if ( id== null || id<=0) {
            throw new InvalidCustomerIdException();
        }
        else {
            //SQL
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        this.isCustomerListUpdated = false;
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else if ( id== null || id<=0) {
            throw new InvalidCustomerIdException();
        }
        else {
            String sql = "DELETE FROM customer WHERE id=?";
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1,id);
                st.executeUpdate();
                return true;
            }
            catch (SQLException e)
            {
                return false;
            }
        }
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else if ( id== null || id<=0) {
            throw new InvalidCustomerIdException();
        }
        else {
            // Da sistemare con join Customer loyaltyCard
            try {
                String sql = "SELECT * FROM customer WHERE id=?";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1,id);
                ResultSet rs = st.executeQuery();
                return new it.polito.ezshop.model.customerlist.Customer(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)
                );
            } catch (SQLException e)
            {
                return null;
            }
        }
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else if (this.isCustomerListUpdated)
            return this.customerList;
        else {
            this.customerList.clear();
            try {
                String sql = "SELECT * FROM customer";
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    customerList.add(new it.polito.ezshop.model.customerlist.Customer(
                       rs.getInt(1),
                       rs.getString(2),
                       rs.getString(3),
                       rs.getInt(4)
                    ));
                }
                this.isCustomerListUpdated=true;

            } catch (SQLException e) {
                //return null; ???
            }
            return this.customerList;

        }

    }

    @Override
    public String createCard() throws UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else{
            try{
                String sql = "INSERT INTO loyaltyCard(points) VALUES (?)";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1,0);
                st.executeUpdate();
                conn.commit();
                return st.getGeneratedKeys().getString("id");
            }catch (SQLException e)
            {
                return new String();
            }
        }
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else if (customerCard==null || customerCard.length()!=10 || !customerCard.matches("[0-9]+") ) {
            throw new InvalidCustomerCardException();
        }
        else if ( customerId== null || customerId<=0) {
            throw new InvalidCustomerIdException();
        }
        else
        {
            // SQL
        }
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else if (customerCard==null || customerCard.length()!=10 || !customerCard.matches("[0-9]+") ) {
            throw new InvalidCustomerCardException();
        }
        else {
            try {
                String sql = "UPDATE customer SET points = points + ? WHERE loyaltyCardId=? ";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1,pointsToBeAdded);

            } catch(SQLException e)
            {

            }
        }
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
