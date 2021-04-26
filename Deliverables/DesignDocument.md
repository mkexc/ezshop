# Design Document 

Authors: Roberto Alessi (290180), Michelangelo Bartolomucci (292422), Gianvito Marzo (281761), Roberto Torta (290184)

Date: 26/04/2021

Version: 0.1


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design
<discuss architectural styles used, if any>
The model used is MVC since is a standalone application working on each cash register separately.
MODEL: it.ezshop.model.*
VIEW: it.ezshop.gui.*
CONTROLLER : it.ezshop.data.* (EZSHOPInterface implementation inside data) 

<report package diagram>

```plantuml
@startuml
left to right direction
allow_mixing

package it.ezshop {
    package it.ezshop.gui {
        package it.ezshop.gui.input {
            package it.ezshop.gui.input.handler
        }
        package it.ezshop.gui.graphics {
            package it.ezshop.gui.graphics.drawer
        }
    }
    package it.ezshop.exception {
        
    }
    package it.ezshop.model {
        package it.ezshop.model.AccountBook
        package it.ezshop.model.LoyalityCard
        package it.ezshop.model.Product
    }
    package it.ezshop.data {
        interface it.ezshop.data.EZShopInterface
        class it.ezshop.data.EZShopController
    }
    package it.ezshop.transactionManagement {
        package it.ezshop.transactionManagement.SaleTransaction
        package it.ezshop.transactionManagement.FinancialTransaction
        package it.ezshop.transactionManagement.Order
    }
}

it.ezshop.data.EZShopController .|> it.ezshop.data.EZShopInterface
it.ezshop.gui -- it.ezshop.data.EZShopController
it.ezshop.model <- it.ezshop.data.EZShopController
it.ezshop.model <- it.ezshop.gui

@enduml
```

# Low level design

```plantuml
@startuml
left to right direction

class Product{
    + name : String
    + destrinction : String
    + type : Integer
    + priceToBuy : Double
    + priceToSell : Double
    + barCode : Integer
    + getProductName()
    + getProductDescription()
    + getProductBarCode()
    + getProductType()
    + getPriceToBuy()
    + getPriceToSell()
} 
class ManageProduct{
    + manageProductCatalog()
    + defineNewProductType()
    + modifyExistingProductType()
    + deleteProductType()
    + getListProductsTypes()
    + searchProductTypeByBarCode()
    + searchProductTypeByDescription()
}

class Catalog{
    + productList : ArrayList<Product>
    + name : String
    + getProductList()
    + getCatalogName()
}
class Inventory{
    + productQuantity : HashMap<Product,Integer>
    + productPosition : HashMap<Product,Position> 
    + getProductMapQuantity()
    + getProductMapPosition()
    + modifyQuantityForProductType()
    + modifyPositionForProductType()
    + issueReorderWarningForProductType()
    + sendAndPayOrderForProducType()
    + payIssuedReorderWarning()
    + recordOrderArrival()
    + getListOrdersByStatus()
}
class Order{
    + price : Double
    + productBarCode : Integer
    + state : String
    + quantity : Integer
    + getProductBarCode()
    + getQuantity()
    + getState()
    + getPrice()
}
class Position{
    + shelf : Integer
    + row : Integer
    + getPosition()
    + setPosition()
}
class FidelityCard{
    + points : Integer
    + id : Integer 
    + getPoints()
    + getId()
}
class Customer{
    + fidelityCardId : Integer
    + name : String
    + surname : String   
    + getFidelityCard()
    + getName()
    + getSurname()
}
class ManageCostumerAndCard{
    + defineCustomer()
    + modifyCustomer()
    + deleteCustomer()
    + searchCustomer()
    + getCustomersList()
    + createCard()
    + attachCardToCustomer()
    + modifyPointsOnCard()
}
class Transaction{
    + transactionElements : Map<Product,Integer>
    + id : Integer
    + points : Integer
    + getTransactionId()
    + addPoints()
}
class ReturnTransaction{}
class SaleTransaction{}
class DiscountRate{
    + rate : Double
    + ProductBarCode : Integer
    + getProductBarCode()
    + getRate()
}
class ManageSaleTransaction{
    + startSale()
    + addProductToSale()
    + deleteProductFromSale()
    + applyDiscountRateToSale()
    + applyDiscountRateToProductType()
    + computePointsForSale()
    + readBarCodeProduct()
    + printSaleTicket()
    + getSaleTicketFromTicketNumber()
    + closeSaleTransaction()
    + rollbackSaleTransaction()
    + commitSaleTransaction()
    + startReturnTransaction()
    + returnProductListedInSaleTicket()
    + closeReturnTransaction()
    + rollbackReturnTransaction()
    + commitReturnTransaction()
}

class ManagePayment{
    + receivePaymentCash()
    + receivePaymentcreditCard()
    + returnPaymentCash()
    + returnPaymentCreditCard()
}

class Debit{
    + debit : Double
    + getDebit()
}
class Credit{
    + credit : Double
    + getCredit()
}
class Accounting{
    + listTransaction : List<Transaction>
    + recordDebit()
    + recordCredit()
    + showMovimentsOverTimePeriod()
    + computeBalance()
}

class Administrator{
    + privilegeLevel: Integer
    + getLevelPrivileges()
}
class ShopManager{
    + privilegeLevel: Integer
    + getLevelPrivileges()
}
class Cashier{
    + privilegeLevel: Integer
    + getLevelPrivileges()
}
class User{
    - username : String
    - password : String
    - role : String
    + getUsername()
    + getRole()
    ~ manageRole()
    ~ managePassword()
    ~ deleteUser()
}

class UserList{
    - List : ArrayList<User>
    + addUser()
    + removeUser()
    + listAll()
    + searchUser()
    + changeUserRights()
}

interface EZShopInterface{
    + reset()
    + createUser()
    + deleteUser()
    + getAllUsers()
    + getUser()
    + updateUserRights()
    + login()
    + logout()
    + createProductType()
    + updateProduct()
    + deleteProductType()
    + getAllProductTypes()
    + getProductTypeByBarCode()
    + getProductTypesByDescription()
    + updateQuantity()
    + updatePosition()
    + issueReorder()
    + payOrderFor()
    + payOrder()
    + recordOrderArrival()
    + getAllOrders()
    + defineCustomer()
    + modifyCustomer()
    + deleteCustomer()
    + getCustomer()
    + getAllCustomers()
    + createCard()
    + attachCardToCustomer()
    + modifyPointsOnCard()
    + startSaleTransaction()
    + addProductToSale()
    + deleteProductFromSale()
    + applyDiscountRateToProduct()
    + applyDiscountRateToSale()
    + computePointsForSale()
    + deleteSaleTicket()
    + getSaleTicket()
    + getTicketByNumber()
    + startReturnTransaction()
    + returnProduct()
    + endReturnTransaction()
    + deleteReturnTransaction()
    + receiveCashPayment()
    + receiveCreditCardPayment()
    + returnCashPayment()
    + returnCreditCardPayment()
    + recordBalanceUpdate()
    + getCreditsAndDebits()
    + computeBalance()
}

class EZShopController {
    
}

class Exception{
    + InvalidUsernameException()
    + InvalidPasswordException()
    + InvalidRoleException()
    + InvalidUserIdException()
    + UnauthorizedException()
    + InvalidProductIdException()
    + InvalidProductDescriptionException()
    + InvalidProductCodeException()
    + InvalidPricePerUnitException()
    + InvalidLocationException()
    + InvalidTicketNumberException()
    + InvalidQuantityException()
    + InvalidTransactionIdException()
    + InvalidPaymentException()
    + InvalidCreditCardException()
    + InvalidOrderIdException()
    + InvalidCustomerNameException()
    + InvalidCustomerCardException()
    + InvalidCustomerIdException()
    + InvalidDiscountRateException()    
}

Product --> ManageProduct
DiscountRate -- Transaction
FidelityCard -- Transaction
Catalog -- Inventory
FidelityCard --> ManageCostumerAndCard
Customer --> ManageCostumerAndCard
SaleTransaction --|> Transaction
ReturnTransaction --|> Transaction
Transaction --> ManageSaleTransaction
Product -- Transaction
Quantity --Transaction
Position -- Inventory
Debit -- Order
Credit <-- Transaction
User --> UserList
Administrator --|> User
Cashier --|> User
ShopManager --|> User
Debit --> Accounting
Credit --> Accounting
Transaction --> ManagePayment
Product -- Inventory
Quantity -- Inventory
Order -- Inventory
EZShopInterface --|> EZShopController
@enduml
```


<for each package, report class diagram>









# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

