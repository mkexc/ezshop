package it.polito.ezshop.data;

import java.sql.*;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.CreditCard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.*;
import java.util.Date;

import static it.polito.ezshop.model.ProductType.validateProductCode;

public class EZShop implements EZShopInterface{
    private static Connection conn;
    private User loggedUser;

    private List<ProductType> inventory = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private List<BalanceOperation> balanceOperationList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private boolean isOrderListUpdated = false;
    private boolean isBalanceOperationUpdated = false;
    private boolean isCustomerListUpdated = false;
    private boolean isUserListUpdated = false;
    private boolean isInventoryUpdated = false;

    public EZShop()  {
        // open db connection
        try {
            // db parameters
            String url = "jdbc:sqlite:ezshop_db.sqlite";
            // create a connection to the database
            if(conn==null)
            {
                conn = DriverManager.getConnection(url);
                System.out.println("Connection to SQLite has been established.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection fail. Aborting...");
            System.exit(-1);
        }
    }

    @Override
    public void reset() {
        this.isOrderListUpdated = false;
        this.isBalanceOperationUpdated = false;
        this.isCustomerListUpdated = false;
        this.isUserListUpdated = false;
        this.isInventoryUpdated = false;
        
        try {
            String sql = "DELETE FROM balanceOperation WHERE true";
            PreparedStatement st = conn.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM saleTransaction WHERE true";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM returnTransaction WHERE true";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM 'order' WHERE true";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM productType WHERE true";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM productEntry WHERE true";
            st = conn.prepareStatement(sql);
            st.executeUpdate();

        } catch (SQLException ignored) {
            
        }
    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        // username not null, not empty
        if(username == null || username.equals(""))
            throw new InvalidUsernameException();

        // password not null, not empty
        if(password == null || password.equals("")){
            throw new InvalidPasswordException();
        }

        // role not null, not empty, not Administrator&&ShopManager&&Cashier
        if(role == null || role.isEmpty() || (!role.equals("Administrator") && !role.equals("ShopManager") && !role.equals("Cashier"))){
            throw new InvalidRoleException();
        }

//        // check if username is already present
//        String sql = "SELECT username FROM user WHERE username=?";
//        try {
//            PreparedStatement st = conn.prepareStatement(sql);
//            st.setString(1, username);
//            ResultSet rs = st.executeQuery();
//
//            if(rs.getString("username").equals(username)) {
//                // product already present
//                return -1;
//            }
//        } catch (SQLException e) {
//            // problems with db connection
//            return -1;
//        }

        // insert the new user
        String sql = "INSERT INTO user(username, password, role) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, role);

            int updatedRows = st.executeUpdate();
            if(updatedRows==0){
                return -1;
            }

            isUserListUpdated = false;

            return st.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {

            return -1;
        }
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        // check role of the user (only administrator)
        if(loggedUser == null || !loggedUser.getRole().equals("Administrator")) {
            throw new UnauthorizedException();
        }

        // id not null, not <= 0
        if(id == null || id <= 0) {
            throw new InvalidUserIdException();
        }

        // delete user using id
        String sql="DELETE FROM user WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, id);
            int deletedRows = st.executeUpdate();
            //conn.commit();

            if(deletedRows == 0)
                return false;

            isUserListUpdated = false;
            //st.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        // check role of the user (only administrator)
        if (loggedUser==null || !loggedUser.getRole().equals("Administrator")) {
            throw new UnauthorizedException();
        }

        // if cached userList is not updated, download from db
        if(!isUserListUpdated) {
            String sql = "SELECT id, password, role, username FROM user";
            List<User> list = new ArrayList<>();
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    list.add(new it.polito.ezshop.model.User(rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    ));
                }
                userList = list;
                isUserListUpdated = true;
                return userList;
            } catch (SQLException e) {
                // list empty if there are problems with db
                return list;
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
        User user;
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if(!rs.next())
                // no product with the given code
                return null;

            user = new it.polito.ezshop.model.User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
            );
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        // check role of the user (only administrator)
        if(loggedUser == null || !loggedUser.getRole().equals("Administrator")) {
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
            int updatedRows = st.executeUpdate();

            if(updatedRows == 0)
                return false;

            isUserListUpdated = false;
            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        // there is already a logged user
        if(loggedUser != null)
            return null;

        String sql = "SELECT id, password, role, username FROM user WHERE username=?";
        User user;
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            user = new it.polito.ezshop.model.User(rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
            );

            // check password
            if(rs.getString("password").equals(password)) {
                // do the login
                loggedUser = user;
                return user;
            }
            else
                throw new InvalidPasswordException();
        } catch (SQLException e) {
            throw new InvalidUsernameException();
        }
    }

    @Override
    public boolean logout() {
        if (loggedUser != null) {
            loggedUser = null;
            return true;
        }
        else
            return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        // check role of the user (only administrator and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // productCode not null, not empty
        if(productCode == null || productCode.equals(""))
            throw new InvalidProductCodeException();

        // check if productCode is valid
        if(!validateProductCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        // description not null, not empty
        if(description == null || description.equals("")){
            throw new InvalidProductDescriptionException();
        }

        // pricePerUnit not <= 0
        if(pricePerUnit <= 0){
            throw new InvalidPricePerUnitException();
        }

//        // check if productCode is already present
//        String sql = "SELECT productCode FROM ProductType WHERE productCode=?";
//        try {
//            PreparedStatement st = conn.prepareStatement(sql);
//            st.setString(1, productCode);
//            ResultSet rs = st.executeQuery();
//
//            if(rs.getString("productCode").equals(productCode)) {
//                // product already present
//                return -1;
//            }
//        } catch (SQLException e) {
//            // problems with db connection
//            return -1;
//        }

        // insert the new productType
        String sql="INSERT INTO productType(productCode, description, pricePerUnit, quantity, notes) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, productCode);
            st.setString(2, description);
            st.setDouble(3, pricePerUnit);
            st.setInt(4, 0);
            st.setString(5, note);

            int updatedRows = st.executeUpdate();
            //conn.commit();

            if(updatedRows == 0)
                return -1;
            
            // get Id generated in the db from row inserted
            isInventoryUpdated = false;
            return st.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            // product already present or db problem
            return -1;
        }
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        // check role of the user (only administrator and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
           throw new UnauthorizedException();

        // check id of the product (not <=0)
        if(id == null || id <= 0)
            throw new InvalidProductIdException();

        // description not null, not empty
        if(newDescription == null || newDescription.equals("")){
            throw new InvalidProductDescriptionException();
        }

        // pricePerUnit not <=0
        if(newPrice <= 0){
            throw new InvalidPricePerUnitException();
        }

        // barCode not null, not empty
        if(newCode == null || newCode.equals(""))
            throw  new InvalidProductCodeException();

        // check if newCode is valid
        if(!validateProductCode(newCode)) {
            throw new InvalidProductCodeException();
        }

        String sql = "UPDATE productType SET productCode=?, description=?, pricePerUnit=?, notes=? WHERE id=?";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newCode);
            st.setString(2, newDescription);
            st.setDouble(3, newPrice);
            st.setString(4, newNote);
            st.setInt(5, id);
            int updatedRows = st.executeUpdate();

            if(updatedRows == 0)
                // no product with the given id
                return false;

            isInventoryUpdated = false;
            return true;
        } catch (SQLException e) {
            // another product already has the new barcode provided or db problem
            return false;
        }
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        // check role of the user (only administrator and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // check id of the product (not <=0)
        if(id == null || id <= 0)
            throw new InvalidProductIdException();

        String sql="DELETE FROM ProductType WHERE id=?" ;
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,id);
            int deletedRows = st.executeUpdate();

            if(deletedRows == 0)
                // no product deleted
                return false;

            isInventoryUpdated = false;
            return true;
        } catch (SQLException e) {
            // db problem
            return false;
        }
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager") && (!loggedUser.getRole().equals("Cashier")))))
            throw new UnauthorizedException();

        if(!isInventoryUpdated) {
            String sql = "SELECT id, productCode, description, pricePerUnit, quantity, notes, position FROM productType";
            List<ProductType> list = new ArrayList<>();
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    list.add(new it.polito.ezshop.model.ProductType(
                            rs.getInt("id"),
                            rs.getString("productCode"),
                            rs.getString("description"),
                            rs.getDouble("pricePerUnit"),
                            rs.getInt("quantity"),
                            rs.getString("notes"),
                            rs.getString("position")
                    ));
                }
                inventory = list;
                isInventoryUpdated = true;
                return inventory;
            } catch (SQLException e) {
                // db problem
                return null;
            }
        }
        else
            return inventory;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // barCode not null, not empty
        if(barCode == null || barCode.equals(""))
            throw  new InvalidProductCodeException();

        // check if barCode is valid
        if(!validateProductCode(barCode)) {
            throw new InvalidProductCodeException();
        }

//        // check if productCode is already present
//        String sql = "SELECT productCode FROM productType WHERE productCode=?";
//        try {
//            PreparedStatement st = conn.prepareStatement(sql);
//            st.setString(1, barCode);
//            ResultSet rs = st.executeQuery();
//
//            if(rs.getString("productCode").equals(barCode)) {
//                // product already present
//                return null;
//            }
//        } catch (SQLException e) {
//            // problems with db connection
//            return null;
//        }

        ProductType product;
        String sql = "SELECT id, productCode, description, pricePerUnit, quantity, notes, position FROM productType WHERE productCode=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, barCode);
            ResultSet rs = st.executeQuery();

            if(!rs.isBeforeFirst())
                // no product with the given code
                return null;

            product = new it.polito.ezshop.model.ProductType(
                    rs.getInt("id"),
                    rs.getString("productCode"),
                    rs.getString("description"),
                    rs.getDouble("pricePerUnit"),
                    rs.getInt("quantity"),
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
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // null should be considered as the empty string
        if(description == null)
           description = "";
        
        String sql = "SELECT id, productCode, description, pricePerUnit, quantity, notes, position FROM ProductType WHERE description LIKE ?";
        List<ProductType> list = new ArrayList<>();
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, '%' + description + '%');
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                list.add(new it.polito.ezshop.model.ProductType(
                        rs.getInt("id"),
                        rs.getString("productCode"),
                        rs.getString("description"),
                        rs.getDouble("pricePerUnit"),
                        rs.getInt("quantity"),
                        rs.getString("notes"),
                        rs.getString("position")
                        )
                );
            }

            return list;
        } catch (SQLException e) {
            // problems with db connection
            return list;
        }
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // check id of the product (not <=0)
        if(productId == null || productId <= 0)
            throw new InvalidProductIdException();

//        // query the product quantity
//        String sql="SELECT quantity, position FROM productType WHERE id=? " ;
//        int quantity;
//        try {
//            PreparedStatement st = conn.prepareStatement(sql);
//
//            st.setInt(1,productId);
//            ResultSet rs = st.executeQuery();
//            quantity = rs.getInt("quantity");
//            if(rs.getString("position").equals("")){
//                return false;
//            }
//        } catch (SQLException e) {
//            return false;
//        }
//
//        // updating the value to quantity
//        quantity= quantity+toBeAdded;
        String sql="UPDATE productType SET quantity=quantity+? WHERE id=? AND position IS NOT NULL";
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1,toBeAdded);
            st.setInt(2,productId);
            int updatedRows = st.executeUpdate();

            if(updatedRows == 0)
                // quantity would be negative or productType has not an assigned location
                return false;

            isInventoryUpdated = false;
            return true;
        } catch (SQLException e) {
            // db problem
            return false;
        }
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        // if newPos is null, position should be empty
        if(newPos == null){
            newPos = "";
        }

        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // check id of the product (not <=0)
        if(productId == null || productId <= 0)
            throw new InvalidProductIdException();

        // check position format (number-string-number)
        if(!newPos.equals("") && !newPos.matches("[0-9]+-[a-zA-Z]+-[0-9]+")){
            throw new InvalidLocationException();
        }

//        // query for checking the uniqueness of position
//        String sql="SELECT position FROM productType WHERE position=?" ;
//
//        try {
//            PreparedStatement st = conn.prepareStatement(sql);
//
//            st.setString(1,newPos);
//            ResultSet rs = st.executeQuery();
//
//            if(rs.getString("position").equals(newPos)){
//                return false;
//            }
//        } catch (SQLException e) {
//            return false;
//        }

        String sql="UPDATE productType SET position=? WHERE id=?" ;
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setString(1,newPos);
            st.setInt(2,productId);
            int updatedRows = st.executeUpdate();

            if(updatedRows == 0)
                // productId not exist
                return false;

            isInventoryUpdated = false;
            return true;
        } catch (SQLException e) {
            // db problem or position not unique
            return false;
        }
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        //check if the product exist and if barcode is valid
        ProductType product = this.getProductTypeByBarCode(productCode);
        if(product == null)
            return -1;

        //check quantity is not <=0
        if(quantity <= 0)
            throw new InvalidQuantityException();

        //check pricePerUnit is not <=0
        if(pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        // insert the new productType
        String sql = "INSERT INTO 'order'(productCode, pricePerUnit, quantity, status) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, productCode);
            st.setDouble(2, pricePerUnit);
            st.setInt(3, quantity);
            st.setString(4,"ISSUED");
            int updatedRows = st.executeUpdate();

            if(updatedRows == 0)
                // cannot update order
                return -1;

            isOrderListUpdated = false;
            // new id of the order created
            return st.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            // db problem
            return -1;
        }
    }

    // issue order + pay order
    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        //check if the product exist
        ProductType product = this.getProductTypeByBarCode(productCode);
        if(product == null)
            return -1;

        //check quantity is not <=0
        if(quantity <= 0)
            throw new InvalidQuantityException();

        //check pricePerUnit is not <=0
        if(pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        // insert the new productType
        String sql = "INSERT INTO 'order'(productCode, pricePerUnit, quantity, status) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, productCode);
            st.setDouble(2, pricePerUnit);
            st.setInt(3, quantity);
            st.setString(4,"PAYED");
            int updatedRows = st.executeUpdate();

            if(updatedRows == 0)
                return -1;

            // record the order on the balance
            boolean out = this.recordBalanceUpdate(-pricePerUnit*quantity);
            if(!out) {
                // not enough balance to pay for order
                return -1;
            }

            isOrderListUpdated = false;
            return st.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            return -1;
        }
    }

    // TODO da checkare
    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // orderId not null, not <=0
        if(orderId == null || orderId <= 0){
            throw new InvalidOrderIdException();
        }

        String sql2 = "SELECT quantity, pricePerUnit, status FROM 'order' WHERE id=?" ;
        String actualStatus;
        double toBeAdded;

        try {
            PreparedStatement st = conn.prepareStatement(sql2);

            st.setInt(1, orderId);
            ResultSet rs = st.executeQuery();

            if(!rs.next())
                // no orderId found
                return false;

            actualStatus = rs.getString("status");
            toBeAdded = -rs.getDouble("pricePerUnit") * rs.getInt("quantity");
        } catch (SQLException e) {
            return false;
        }

        if(actualStatus.equals("PAYED"))
            // order not in ISSUED state
            return false;

        // change status to PAYED
        String sql3 = "UPDATE 'order' SET status=? WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql3);

            st.setString(1, "PAYED");
            st.setInt(2, orderId);
            int updatedRows = st.executeUpdate();

            if(updatedRows == 0)
                return false;

            // record order on balance
            recordBalanceUpdate(toBeAdded);
            isOrderListUpdated = false;
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // TODO da checkare
    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        // check role of the user (only administrator, cashier and shopManager)
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && (!loggedUser.getRole().equals("ShopManager"))))
            throw new UnauthorizedException();

        // orderId not null, not <=0
        if(orderId == null || orderId <= 0){
            throw new InvalidOrderIdException();
        }

        String sql = "SELECT quantity, productCode, status FROM 'order' WHERE id=?";
        int quantity;
        String productCode;
        ProductType product;
        String actualStatus;
        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, orderId);
            ResultSet rs = st.executeQuery();

            if(!rs.next())
                // no orderId found
                return false;

            quantity = rs.getInt("quantity");
            productCode = rs.getString("productCode");
            actualStatus = rs.getString("status");
            product = this.getProductTypeByBarCode(productCode);
        } catch (Exception e) {
            return false;
        }

        if(product.getLocation() == null){
            throw new InvalidLocationException();
        }
        if(actualStatus.equals("COMPLETED"))
            return false;

        try {
            this.updateQuantity(product.getId(), quantity);
        }catch(Exception e){
            return false;
        }

        String sql3="UPDATE 'order' SET status=? WHERE id=?" ;
        try {
            PreparedStatement st = conn.prepareStatement(sql3);

            st.setString(1,"COMPLETED");
            st.setInt(2,orderId);
            int updatedRows = st.executeUpdate();

            if(updatedRows == 0)
                return false;

            isOrderListUpdated = false;
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

        String sql3="SELECT * FROM 'order'" ;
        try {
            PreparedStatement st = conn.prepareStatement(sql3);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                this.orderList.add( new it.polito.ezshop.model.Order(
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
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else if (customerName==null || customerName.isEmpty())
            throw new InvalidCustomerNameException();
        else
        {
            String sql = "INSERT INTO customer(customerName, loyaltyCardId) VALUES (?, ?)";
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                st.setString(1,customerName);
                st.setString(2,"");
                if(st.executeUpdate()>0) {
                    this.isCustomerListUpdated = false;
                    return st.getGeneratedKeys().getInt(1);
                }
                else
                    return -1;
            } catch (SQLException e) {
                return -1;
            }
        }
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        else if (newCustomerName==null || newCustomerName.isEmpty())
            throw new InvalidCustomerNameException();
        else if (newCustomerCard!=null && !newCustomerCard.isEmpty() && !newCustomerCard.matches("[0-9]+") && newCustomerCard.length()!=10 ) {
            throw new InvalidCustomerCardException(); //Only when customer card is full but not a string of 10 digits, bypassed empty and null cases otherwise exception conflicts
        }
        else if ( id== null || id<=0) {
            throw new InvalidCustomerIdException();
        }
        else {
            try{

                if(newCustomerCard==null)
                {
                    String sql = "UPDATE customer SET customerName = ? WHERE id=?";
                    PreparedStatement st = conn.prepareStatement(sql);
                    st.setString(1,newCustomerName);
                    st.setInt(2,id);
                    if(st.executeUpdate()>0)
                    {
                        this.isCustomerListUpdated = false;
                        return true;
                    }
                }
                else if(newCustomerCard.isEmpty())
                {
                    String sql = "UPDATE customer SET loyaltyCardId=NULL, customerName = ? WHERE id=?";
                    PreparedStatement st = conn.prepareStatement(sql);
                    st.setString(1,newCustomerName);
                    st.setInt(2,id);
                    if(st.executeUpdate()>0)
                    {
                        this.isCustomerListUpdated = false;
                        return true;
                    }
                    
                }
                else
                {
                    String sql1 = "SELECT * FROM customer WHERE loyaltyCardId=?";
                    PreparedStatement st1 = conn.prepareStatement(sql1);
                    st1.setString(1, newCustomerCard);
                    st1.executeQuery();
                    conn.commit();
                    ResultSet rs1 = st1.getResultSet();
                    if(!rs1.next())
                    {
                        String sql = "UPDATE customer SET loyaltyCardId=?, customerName = ? WHERE id=?";
                        PreparedStatement st = conn.prepareStatement(sql);
                        st.setString(1,newCustomerCard);
                        st.setString(2,newCustomerName);
                        st.setInt(3,id);
                        if(st.executeUpdate()>0)
                        {
                            this.isCustomerListUpdated = false;
                            return true;
                        }
                    }
                }

                return false;
                
            } catch (SQLException e){
                return false;
            }

        }
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
                return (st.executeUpdate()>0);
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
            try {
                String sql = "SELECT * FROM customer INNER JOIN loyaltyCard ON loyaltyCard.id=customer.loyaltyCardId WHERE customer.id=?";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1,id);
                ResultSet rs = st.executeQuery();
                if(rs.next())
                    return new it.polito.ezshop.model.Customer(
                        rs.getInt("customerId"),
                        rs.getString("customerName"),
                        rs.getString("loyaltyCardId"),
                        rs.getInt("points")
                    );
                else
                    return null;
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
            try {
                String sql = "SELECT * FROM customer INNER JOIN loyaltyCard ON loyaltyCard.id=customer.loyaltyCardId";
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery();
                this.customerList.clear();
                while(rs.next()){
                    customerList.add(new it.polito.ezshop.model.Customer(
                            rs.getInt("customerId"),
                            rs.getString("customerName"),
                            rs.getString("loyaltyCardId"),
                            rs.getInt("points")
                    ));
                }
                this.isCustomerListUpdated=true;

            } catch (SQLException ignored) {

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
                return "";
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
            try{
                String sql1 = "SELECT * FROM customer WHERE loyaltyCardId=?";
                PreparedStatement st1 = conn.prepareStatement(sql1);
                st1.setString(1,customerCard);
                st1.executeQuery();
                conn.commit();
                ResultSet rs1 = st1.getResultSet();
                if(rs1.next())
                    return false;

                String sql2 = "SELECT * FROM customer WHERE id=?";
                PreparedStatement st2 = conn.prepareStatement(sql2);
                st2.setInt(1,customerId);
                st2.executeQuery();
                conn.commit();
                ResultSet rs2 = st2.getResultSet();
                if(rs2.next())
                    return false;

                this.isCustomerListUpdated=false;
                String sql3 = "UPDATE customer SET loyaltyCardId=? WHERE id=?";
                PreparedStatement st = conn.prepareStatement(sql3);
                st.setString(1,customerCard);
                st.setInt(2,customerId);
                st.executeUpdate();
                conn.commit();
                return true;
            } catch(SQLException e)
            {
                return false;
            }
        }
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
                String sql1 = "SELECT * FROM loyaltyCard WHERE id=?";
                PreparedStatement st1 = conn.prepareStatement(sql1);
                st1.setString(1, customerCard);
                st1.executeQuery();
                conn.commit();
                ResultSet rs1 = st1.getResultSet();
                if(rs1.next())
                {
                    if(pointsToBeAdded<0 && rs1.getInt("points")+pointsToBeAdded<0) {
                        return false;
                    }
                }
                else
                    return false;

                String sql2 = "UPDATE loyaltyCard SET points = points + ? WHERE id=? ";
                PreparedStatement st2 = conn.prepareStatement(sql2);
                st2.setInt(1,pointsToBeAdded);
                st2.setString(2,customerCard);
                st2.executeUpdate();
                conn.commit();
                return true;
            } catch(SQLException e)
            {
                return false;
            }
        }

    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        // check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        int res;
        String sql = "INSERT INTO saleTransaction (DiscountRate, balanceId, total, status) VALUES (0.0,'',0.0,'OPEN')";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.executeUpdate();
            res= st.getGeneratedKeys().getInt(1);
        }catch(SQLException e){

            return -1;
        }

        return res;

    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();
        //check amount
        if(amount<=0)
            throw new InvalidQuantityException();

        // productCode not null, not empty
        if(productCode == null|| productCode.equals("") )
            throw new InvalidProductCodeException();

        // check if productCode is valid
        if(!validateProductCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        ProductType product;

        //check id
        String sql = "SELECT id from SaleTransaction WHERE id=? ";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs=st.executeQuery();
            if(rs.getInt("id")==transactionId){
                return false;
            }

        }catch(SQLException e){
            return false;
        }

        //check availability and the presence of the product;
        try {
            product = this.getProductTypeByBarCode(productCode);
        }catch(Exception e) {
            return false;
        }

        try{
            this.updateQuantity(product.getId(),-amount);
        }catch(Exception e){
            return false;
        }

        String sql2 = "INSERT INTO productEntry (transactionId, barcode, amount) VALUES (?,?,?) ";
        try {
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setInt(1,transactionId);
            st.setString(2,productCode);
            st.setInt(3,amount);
            st.executeUpdate();

        }catch(SQLException e){
            try{
            this.updateQuantity(product.getId(),amount);
            }catch(Exception e2){
                throw new InvalidQuantityException();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();
        //check amount
        if(amount<=0)
            throw new InvalidQuantityException();

        // productCode not null, not empty
        if(productCode == null|| productCode.equals("") )
            throw new InvalidProductCodeException();

        // check if productCode is valid
        if(!validateProductCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        //check id
        String sql = "SELECT id from SaleTransaction WHERE id=? ";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,transactionId);
            ResultSet rs=st.executeQuery();
            if(rs.getInt("id")!=transactionId){
                return false;
            }

        }catch(SQLException e){
            return false;
        }


        //update quantity
        String sql2 = "UPDATE productEntry SET amount=amount-? WHERE transactionId=? AND barcode=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setInt(1,amount);
            st.setInt(2,transactionId);
            st.setString(3,productCode);

            st.executeUpdate();
            try {
                ProductType product = this.getProductTypeByBarCode(productCode);
                this.updateQuantity(product.getId(), amount);
            }catch (Exception e2) {
                return false;
            }
        }catch(SQLException e){
            return false;
        }
        return true;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();
        //check discountRate
        if(discountRate>=0.0 && discountRate<=1.0)
            throw new InvalidDiscountRateException();

        // productCode not null, not empty
        if(productCode == null|| productCode.equals("") )
            throw new InvalidProductCodeException();

        // check if productCode is valid
        if(!validateProductCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        //check status
        String sql = "SELECT status FROM SaleTransaction WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, transactionId);

            ResultSet rs = st.executeQuery();
            if(!rs.getString("status").equals("OPEN"))
                return false;
        }catch (SQLException e) {
            return false;
        }

        //check productCode
        String sql1 = "SELECT barcode from productEntry WHERE transactionId=? AND barcode=? ";
        try {
            PreparedStatement st = conn.prepareStatement(sql1);
            st.setInt(1,transactionId);
            st.setString(2,productCode);
            ResultSet rs=st.executeQuery();
            if(!rs.getString("barcode").equals(productCode)){
                return false;
            }

        }catch(SQLException e){
            return false;
        }

        String sql2 ="UPDATE productEntry SET discountRate=? WHERE transactionId=? AND barcode=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setDouble(1,discountRate);
            st.setInt(2,transactionId);
            st.setString(3,productCode);
            st.executeUpdate();
            return true;
        }catch(SQLException e){
            return false;
        }

    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();
        //check discountRate
        if(discountRate>=0.0 && discountRate<=1.0)
            throw new InvalidDiscountRateException();

        //check status
        String sql = "SELECT status FROM saleTransaction WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, transactionId);

            ResultSet rs = st.executeQuery();
            if(rs.getString("status").equals("PAYED"))
                return false;
        }catch (SQLException e) {
            return false;
        }

        String sql2 ="UPDATE saleTransaction SET discountRate=? WHERE id=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setDouble(1,discountRate);
            st.setInt(2,transactionId);
            st.executeUpdate();
            return true;
        }catch(SQLException e){
            return false;
        }

    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();
        int points;
        String sql="SELECT PE.amount, PE.discountRate, PT.pricePerUnit, ST.discountRate FROM productEntry PE,saleTransaction ST, productType PT WHERE ST.id=PE.transactionId AND ST.id=? AND PE.barcode=PT.productCode ";
        try {
            double priceWithoutSaleDiscount=0.0;
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,transactionId);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                priceWithoutSaleDiscount+=rs.getInt("PE.amount")*rs.getDouble("PT.pricePerUnit")
                        -(rs.getInt("PE.amount")*rs.getDouble("PT.pricePerUnit"))*rs.getDouble("PE.discountRate");
            }
            double finalPrice =priceWithoutSaleDiscount-priceWithoutSaleDiscount*rs.getDouble("ST.discountRate");
            points = (int) (finalPrice/10);
            return points;
        }catch(SQLException e){
            return -1;
        }

    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        //check authorization 
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();
        
        //compute total price
        String sql="SELECT PE.amount, PE.discountRate, PT.pricePerUnit, ST.discountRate, ST.status FROM productEntry PE,saleTransaction ST, productType PT WHERE ST.id=PE.transactionId AND ST.id=? AND PE.barcode=PT.productCode ";
        double total;
        try {
            double priceWithoutSaleDiscount=0.0;
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,transactionId);
            ResultSet rs = st.executeQuery();
            if(rs.getString("status").equals("PAYED"))
                return false;
            while(rs.next()){
                priceWithoutSaleDiscount+=rs.getInt("PE.amount")*rs.getDouble("PT.pricePerUnit")
                        -(rs.getInt("PE.amount")*rs.getDouble("PT.pricePerUnit"))*rs.getDouble("PE.discountRate");
            }
            total =priceWithoutSaleDiscount-priceWithoutSaleDiscount*rs.getDouble("ST.discountRate");

        }catch(SQLException e){
            return false;
        }
        
        // update transaction by setting the status and its total
        String sql2 = "UPDATE saleTransaction SET status='CLOSED', total=? WHERE id=?";
        try{
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setDouble(1,total);
            st.setInt(2,transactionId);
            return true;
        }catch(SQLException e){
            return false;
        }
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(saleNumber==null||saleNumber<=0)
            throw new InvalidTransactionIdException();

        //check status
        String sql="SELECT status FROM saleTransaction WHERE id=? ";

        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,saleNumber);
            ResultSet rs = st.executeQuery();
            if(rs.getString("status").equals("PAYED"))
                return false;
            
        }catch(SQLException e){
            return false;
        }

        String sql2="DELETE FROM saleTransaction WHERE id=?";
        try {

            PreparedStatement st = conn.prepareStatement(sql2);
            st.setInt(1,saleNumber);
            st.executeUpdate();

        }catch(SQLException e){
            return false;
        }

        String sql3 = "DELETE FROM productEntry WHERE transactionId=?";
        try {

            PreparedStatement st = conn.prepareStatement(sql3);
            st.setInt(1,saleNumber);
            st.executeUpdate();

        }catch(SQLException e){
            return false;
        }
        
        return true;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();

        //check status
        String sql="SELECT ST.id, ST.balanceId, ST.discountRate, ST.total, PE.barcode, PE.amount, PE.discountRate, PT.description, PT.pricePerUnit FROM saleTransaction ST, productEntry PE, productType PT WHERE PE.transactionId=ST.id AND ST.id=? AND PE.barcode=PT.productCode";
        List<TicketEntry> entries;
        try {
            entries = new ArrayList<>();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,transactionId);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                entries.add(new it.polito.ezshop.model.TicketEntry(
                                                rs.getString("PE.barcode"),
                                                rs.getString("PT.productDescription"),
                                                rs.getInt("PE.amount"),
                                                rs.getDouble("PT.pricePerUnit"),
                                                rs.getDouble("PE.discountRate")
                                                                                ));
            }

            return new it.polito.ezshop.model.SaleTransaction(
                    rs.getInt("ST.id"),
                    entries,
                    rs.getDouble("ST.discountRate"),
                    rs.getDouble("ST.total"));
        }catch(SQLException e){
            return null;
        }
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(saleNumber==null||saleNumber<=0)
            throw new InvalidTransactionIdException();
        int res;

        String sql = "INSERT INTO returnTransaction (quantity,saleTransactionId,discountRate,returnedPrice,total,status) VALUES (0,?,0.0,0.0,0.0,'OPEN')";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,saleNumber);
            st.executeUpdate();
            res= st.getGeneratedKeys().getInt(1);
        }catch(SQLException e){
            return -1;
        }

        return res;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException{
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(returnId==null||returnId<=0)
            throw new InvalidTransactionIdException();
        //check quantity
        if(amount<=0)
            throw new InvalidQuantityException();

        // productCode not null, not empty
        if(productCode == null|| productCode.equals("") )
            throw new InvalidProductCodeException();

        // check if productCode is valid
        if(!validateProductCode(productCode)) {
            throw new InvalidProductCodeException();
        }

        String sql2 = "INSERT INTO productEntry (transactionId, barcode, amount) VALUES (?,?,?) ";
        try {
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setInt(1,returnId);
            st.setString(2,productCode);
            st.setInt(3,amount);

            st.executeUpdate();

        }catch(SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        if(!commit){
            this.deleteReturnTransaction(returnId);
        }
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(returnId==null||returnId<=0)
            throw new InvalidTransactionIdException();

        //check status
        String sql="SELECT status, barcode, amount FROM returnTransaction, productEntry WHERE id=? ";
        String productCode;

        int amount;
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,returnId);
            ResultSet rs = st.executeQuery();
            productCode= rs.getString("barcode");
            amount = rs.getInt("amount");
            if(!rs.getString("status").equals("OPEN"))
                return false;
        }catch(SQLException e){
            return false;
        }

        ProductType product;
        try {
            product = this.getProductTypeByBarCode(productCode);
        }catch(Exception e) {
            return false;
        }

        try{
            this.updateQuantity(product.getId(),amount);
        }catch(Exception e) {
            return false;
        }

        String sql3="SELECT PE.amount, PT.pricePerUnit FROM productEntry PE, productType PT WHERE PE.barcode=PT.productCode AND PE.transactionId=?";
        double total=0;
        try{
            PreparedStatement st = conn.prepareStatement(sql3);
            st.setInt(1,returnId);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                total+=rs.getInt("PE.amount")*rs.getDouble("PT.pricePerUnit");
            }
        }catch(SQLException e){
            return false;
        }

        // update transaction by setting the status
        String sql2 = "UPDATE returnTransaction SET status='CLOSED', total=?  WHERE id=?";
        try{
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setDouble(1, total);
            st.setInt(2,returnId);

            return true;
        }catch(SQLException e){
            try{
                this.updateQuantity(product.getId(),-amount);
                return false;
            }catch(Exception ee){
                return false;
            }
        }

    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(returnId==null||returnId<=0)
            throw new InvalidTransactionIdException();

        //check status
        String sql="SELECT status FROM returnTransaction WHERE id=? ";

        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,returnId);
            ResultSet rs = st.executeQuery();
            if(rs.getString("status").equals("PAYED"))
                return false;

        }catch(SQLException e){
            return false;
        }

        String sql2="DELETE FROM productEntry WHERE transactionId=?";
        try {

            PreparedStatement st = conn.prepareStatement(sql2);
            st.setInt(1,returnId);
            st.executeUpdate();

        }catch(SQLException e){
            return false;
        }

        String sql3="DELETE FROM returnTransaction WHERE id=?";
        try {

            PreparedStatement st = conn.prepareStatement(sql3);
            st.setInt(1,returnId);
            st.executeUpdate();

        }catch(SQLException e){
            return false;
        }

        return true;

    }

    @Override
    public double receiveCashPayment(Integer transactionId, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();
        //check cash
        if(cash <= 0)
            throw new InvalidPaymentException();

        String sql="SELECT total FROM saleTransaction WHERE id=? AND status='CLOSE' ";
        double total;
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,transactionId);
            ResultSet rs = st.executeQuery();
            total = rs.getDouble("total");

        }catch(SQLException e){
            return -1.0;
        }

        if(total>cash){
            return -1.0;
        }else{
            this.recordBalanceUpdate(total);
            // update transaction by setting the status
            String sql2 = "UPDATE saleTransaction SET status='PAYED' WHERE id=?";
            try{
                PreparedStatement st = conn.prepareStatement(sql2);
                st.setInt(1,transactionId);

            }catch(SQLException e) {
                return -1.0;
            }
                return cash-total;
        }
    }

    @Override
    public boolean receiveCreditCardPayment(Integer transactionId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(transactionId==null||transactionId<=0)
            throw new InvalidTransactionIdException();
        if(creditCard==null || creditCard.isEmpty() || !CreditCard.validateWithLuhn(creditCard))
            throw new InvalidCreditCardException();

        String sql="SELECT total FROM saleTransaction WHERE id=? AND status='CLOSE' ";
        double total;
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,transactionId);
            ResultSet rs = st.executeQuery();
            total = rs.getDouble("total");

        }catch(SQLException e){
            return false;
        }

        String sql2="SELECT balance FROM creditCard WHERE cardNumber=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setString(1,creditCard);
            ResultSet rs = st.executeQuery();
            if(rs.getDouble("balance")<total)
                return false;
        }catch(SQLException e){
            return false;
        }

        String sql3 ="UPDATE creditCard SET balance=balance-? WHERE cardNumber=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql3);
            st.setDouble(1,total);
            st.setString(2,creditCard);
            st.executeUpdate();

        }catch(SQLException e){
            return false;
        }

        String sql4 = "UPDATE saleTransaction SET status='PAYED' WHERE id=?";
        try{
            PreparedStatement st = conn.prepareStatement(sql4);
            st.setInt(1,transactionId);
        }catch(SQLException e) {
            return false;
        }

        this.recordBalanceUpdate(total);

        return true;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        //check authorization
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(returnId==null||returnId<=0)
            throw new InvalidTransactionIdException();


        String sql="SELECT total FROM returnTransaction WHERE id=? AND status='CLOSE'";
        double total;
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,returnId);
            ResultSet rs = st.executeQuery();
            total = rs.getDouble("total");

        }catch(SQLException e){
            return -1.0;
        }

        this.recordBalanceUpdate(-total);

        // update transaction by setting the status
        String sql2 = "UPDATE returnTransaction SET status='PAYED' WHERE id=?";
        try{
            PreparedStatement st = conn.prepareStatement(sql2);
            st.setInt(1,returnId);

        }catch(SQLException e) {
            return -1.0;
        }


        return total;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager") && !loggedUser.getRole().equals("Cashier")))
            throw new UnauthorizedException();
        //check id
        if(returnId==null||returnId<=0)
            throw new InvalidTransactionIdException();
        if(creditCard==null || creditCard.isEmpty() || !CreditCard.validateWithLuhn(creditCard))
            throw new InvalidCreditCardException();

        String sql="SELECT total FROM returnTransaction WHERE id=? AND status='CLOSE' ";
        double total;
        try {

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,returnId);
            ResultSet rs = st.executeQuery();
            total = rs.getDouble("total");

        }catch(SQLException e){
            return -1.0;
        }

        String sql3 ="UPDATE creditCard SET balance=balance+? WHERE cardNumber=?";
        try {
            PreparedStatement st = conn.prepareStatement(sql3);
            st.setDouble(1,total);
            st.setString(2,creditCard);
            st.executeUpdate();

        }catch(SQLException e){
            return -1.0;
        }

        String sql4 = "UPDATE returnTransaction SET status='PAYED' WHERE id=?";
        try{
            PreparedStatement st = conn.prepareStatement(sql4);
            st.setInt(1,returnId);
        }catch(SQLException e) {
            return -1.0;
        }

        this.recordBalanceUpdate(-total);

        return total;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        String type;
        double sum = 0.0;
        if(toBeAdded<0)
            type="DEBIT";
        else
            type ="CREDIT";
        try{
            String sql = "SELECT * FROM balanceOperation";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next())
            {
                sum+=rs.getDouble("money");
            }
            if (sum+toBeAdded<0)
                return false;

            String sql2 = "INSERT INTO balanceOperation(date,money,type) VALUES (?,?,?) ";
            PreparedStatement st2 = conn.prepareStatement(sql2);
            st2.setDate(1, java.sql.Date.valueOf((new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            st2.setDouble(2,toBeAdded);
            st2.setString(3,type);
            st2.executeUpdate();

        }catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        List<BalanceOperation> l = new ArrayList<>();
        ResultSet rs;
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (to!=null && from!=null)
        {
            LocalDate realFrom=from, realTo=to;
            if (to.isBefore(from))
            {
                realFrom = to;
                realTo=from;
            }

            try {
                String sql = "SELECT * FROM balanceOperation WHERE date > ? AND date < ? ";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setDate(1, java.sql.Date.valueOf(realFrom));
                st.setDate(2, java.sql.Date.valueOf(realTo));
                st.executeQuery();
                rs = st.getResultSet();
                while (rs.next())
                {
                    l.add(
                            new it.polito.ezshop.model.BalanceOperation(
                                    rs.getInt("id"),
                                    Instant.ofEpochMilli(rs.getDate("date").getTime())
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate(),
                                    rs.getDouble("money"),
                                    rs.getString("type")
                            )
                    );
                }
            } catch (SQLException ignored) {

            }
        }
        else if(from==null && to!=null) {
            try {
                String sql = "SELECT * FROM balanceOperation WHERE date < ? ";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setDate(1,java.sql.Date.valueOf(to));
                st.executeQuery();
                rs = st.getResultSet();
                while (rs.next())
                {
                    l.add(
                            new it.polito.ezshop.model.BalanceOperation(
                                    rs.getInt("id"),
                                    Instant.ofEpochMilli(rs.getDate("date").getTime())
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate(),
                                    rs.getDouble("money"),
                                    rs.getString("type")
                            )
                    );
                }
            } catch (SQLException ignored) {

            }
        }
        else if(from!=null)
        {
            try {
                String sql = "SELECT * FROM balanceOperation WHERE date > ? ";

                PreparedStatement st = conn.prepareStatement(sql);
                st.setDate(1, java.sql.Date.valueOf(from));
                st.executeQuery();
                rs = st.getResultSet();
                while (rs.next())
                {
                    l.add(
                            new it.polito.ezshop.model.BalanceOperation(
                                    rs.getInt("id"),
                                    Instant.ofEpochMilli(rs.getDate("date").getTime())
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate(),
                                    rs.getDouble("money"),
                                    rs.getString("type")
                            )
                    );
                }
            } catch (SQLException ignored) {

            }
        }
        return l;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        if(loggedUser == null || (!loggedUser.getRole().equals("Administrator") && !loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        else
        {
            double sum = 0.0;
            try
            {
                String sql = "SELECT money FROM balanceOperation";
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery();
                while (rs.next())
                {
                    sum+=rs.getInt("money");
                }
                return sum;
            } catch (SQLException e)
            {
                return 0.0;
            }
        }
    }


//    public void close()
//    {
//        try {
//            conn.close();
//        } catch (SQLException ignored) {
//
//        }
//    }
}
