# Design Document

Authors: Roberto Alessi (290180), Michelangelo Bartolomucci (292422), Gianvito Marzo (281761), Roberto Torta (290184)

Date: 29/04/2021

Version: 0.6


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design
<discuss architectural styles used, if any>
The model used is MVC (Model-View-Controller) since is a standalone application working on each cash register separately. The façade class is Shop.

```plantuml
@startuml
top to bottom direction
allow_mixing

package it.polito.ezshop {
    package it.polito.ezshop.gui {
    }
    
    package it.polito.ezshop.exception {}
    
    package it.polito.ezshop.model {
        package it.polito.ezshop.model.AccountBook
        package it.polito.ezshop.model.LoyalityCard
        package it.polito.ezshop.model.UserList
        package it.polito.ezshop.model.Inventory
        package it.polito.ezshop.model.LoyaltyCardList
        package it.polito.ezshop.model.CustomerList
        package it.polito.ezshop.model.AccountBook
    }
    package it.polito.ezshop.data {
        interface it.polito.ezshop.data.EZShopInterface
        class it.polito.ezshop.data.Shop
    }
}

it.polito.ezshop.data.Shop .|> it.polito.ezshop.data.EZShopInterface
it.polito.ezshop.model <- it.polito.ezshop.data.Shop
it.polito.ezshop.data.Shop <- it.polito.ezshop.gui
it.polito.ezshop.exception <- it.polito.ezshop.data.Shop
@enduml
```

# Low level design

```plantuml
@startuml
top to bottom direction
scale 1024 width 
scale 768 height 
note as Persistent
  Persistent classes: 
  UserList, Inventory, 
  CustomerList, AccountBook, 
  OrderList, LoyaltyCardList.
end note

package it.polito.ezshop.model{

    class User{
        - id : Integer
        - username : String
        - password : String
        - role : enum{"Administrator"|"Cashier"|"ShopManager"}
        - status : Boolean
    }

    class UserList{
        - List : ArrayList<User>
        
        + createUser()
        + deleteUser()
        + getAllUsers()
        + getUser()
        + updateUserRights()
        + login()
        + logout()
        - savePersistent()
    }

    class ProductType{
        - id : Integer
        - productCode : String
        - description : String
        - pricePerUnit : double
        - quantity : Integer
        - discountRate : Integer
        - notes : String
        - position : ArrayList<Position>
    }

    class Inventory{
        - list : ArrayList<ProductType>
        
        + createProductType()
        + updateProduct()®
        + deleteProductType()
        + getAllProductTypes()
        + getProductTypeByBarCode()
        + getProductTypesByDescription()
        + updateQuantity()
        + updatePosition()
        + removePosition()
        - savePersistent()
    }

    class Order{
        - orderId : Integer
        - productId : Integer
        - supplier: String
        - pricePerUnit : double
        - quantity: int
        - status: enum{ISSUED|ORDERED|COMPLETED}
    }

    class OrderList{    
        - orderList : ArrayList<Order>

        + issueOrder()
        + payOrderFor()
        + payOrder()
        + getAllOrders()
        + recordOrderArrival()
        - savePersistent()
        
    }

    class Position{
        + aisleID : Integer
        + rackID : Integer
        + levelID : Integer
    }

    class Quantity{
        - quantity : HashMap<Integer,Integer>
    }

    class LoyaltyCard{
        - cardId : String
        - points : Integer
        - customerId : String
    }

    class LoyaltyCardList{
        - LoyaltyCardList: ArrayList<LoyaltyCard>

        + createCard()
        + modifyPointsOnCard()
        - savePersistant()
    }

    class Customer{
        - customerId : Integer
        - loyaltyCardId : ArrayList<String>
        - name : String
        - surname : String
    }

    class CustomerList{
        - customersList : ArrayList<Customer>

        + defineCustomer()
        + deleteCustomer()
        + searchCustomer()
        + modifyCustomer()
        + getCustomer()
        + getAllCustomers()
        + attachCardToCustomer()
        - savePersistent()
    }

    class FinancialTransaction {
        - description : String
        - amount : Double
        - date : Date
        - type : enum{DEBIT|CREDIT}
    }


    class SaleTransaction{
        - transactionElements : Map<ProductType,Integer>
        - id : Integer
        - points : Integer
        - date : Date
        - time : DateTime
        - cost : Double
        - paymentType : String
        - discountRate : Integer
        - returnedProducts : ArrayList<ReturnTransaction>
    }

    class ReturnTransaction {
        - quantity : Integer 
        - returnedValue : ProductType
        - idSaleTransaction : Integer
    }

    class AccountBook{
        - listTransaction : List<FinancialTransaction>

        + startReturnTransaction()
        + returnProduct()
        + endReturnTransaction()
        + deleteReturnTransaction()
        + startSaleTransaction()
        + deleteSaleTransaction()
        + endSaleTransaction()
        + getSaleTransaction()
        + receivePaymentCash()
        + receivePaymentcreditCard()
        + returnPaymentCash()
        + returnPaymentCreditCard()
        + recordBalanceUpdate()
        + getCreditsAndDebits()
        + computeBalance()
        + computePointsForSale()
        + addProductToSale()
        + deleteProductFromSale()
        + applyDiscountRateToProduct()
        + applyDiscountRateToSale()
        - savePersistent()
        

    }
}

package it.polito.ezshop.data{
    class Shop{
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
        + removePosition()
        + issueOrder()
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
        + deleteSaleTransactionId()
        + endSaleTransaction()
        + getSaleTransaction()
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
}

package it.polito.ezshop.exception {
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
}



SaleTransaction "*" -- "*" ProductType
FinancialTransaction <-- Order
LoyaltyCardList "1"--> "*" LoyaltyCard
Inventory "1" --> "*" ProductType
Position "*"-- "1"ProductType
User "*" <-- "1" UserList
LoyaltyCard "*" -- " 1" Customer
Customer "*" <-- "1" CustomerList
Order "*" --> ProductType
OrderList "1" --> "*" Order
FinancialTransaction"1" ---> "*"AccountBook
Exception "*"<-right-"1" Shop
ReturnTransaction -|> FinancialTransaction
SaleTransaction -|> FinancialTransaction
Shop --> it.polito.ezshop.model
(SaleTransaction, ProductType)  .. Quantity
it.polito.ezshop.model -- Persistent
@enduml
```

# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>

|FR |Customer|CustomerList|LoyaltyCard|LoyaltyCardList|Inventory|ProductType|Position |SaleTransaction|ReturnTransaction|AccountBook|Exception|Quantity|Order|User|UserList|Shop|
|:-:|:------:|:----------:|:---------:|:-------------:|:-------:|:---------:|:-------:|:-------------:|:---------------:|:---------:|:-------:|:------:|:---:|:--:|:------:|:--:|
|FR1|        |            |           |               |         |           |         |               |                 |           |        x|        |     |   x|       x|   x| 
|FR3|        |            |           |               |        x|          x|         |               |                 |           |        x|        |    x|    |        |   x| 
|FR4|        |            |           |               |        x|          x|        x|              x|                x|          x|        x|        |    x|    |        |   x| 
|FR5|       x|           x|          x|              x|         |           |         |              x|                x|           |        x|        |     |    |        |   x| 
|FR6|       x|           x|          x|              x|        x|          x|         |              x|                x|          x|        x|       x|     |    |        |   x| 
|FR7|       x|           x|          x|              x|         |           |         |              x|                x|          x|        x|       x|     |    |        |   x| 
|FR8|        |            |           |               |         |           |         |              x|                x|          x|        x|        |     |    |        |   x|


# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

## Use Case 1
### Scenario 1-1
```plantuml
@startuml
autonumber
EZShopGui -> Shop
Shop -> Inventory : createProductType()
Inventory -> ProductType : createProductType()
Inventory <-- ProductType : id
Inventory -> ProductType : updatePosition()
ProductType -> Position : getPosition()
ProductType <-- Position : Position
ProductType -> Position : Position.add()
Inventory <-- ProductType : true
Shop <-- Inventory : true
@enduml
```

## Use Case 2
### Scenario 2-1
```plantuml
@startuml
autonumber
EZShopGui -> Shop 
Shop -> UserList : createUser()
UserList -> User : createUser()
UserList <-- User : User
Shop <-- UserList : id
Shop -> UserList : updateUserRights()
UserList -> User : setRole()
Shop <-- UserList : true
@enduml
```

## Use Case 3
### Scenario 3-1
```plantuml
@startuml
autonumber
EZShopGui -> Shop
Shop -> OrderList : issueOrder()
OrderList -> Order : issueOrder()
OrderList <-- Order : Order
OrderList -> Order : setState()
Shop <- OrderList : id
@enduml
```

## Use Case 4
### Scenario 4-1 
```plantuml
@startuml
autonumber
EZShopGui -> Shop
Shop -> CustomerList : defineCustomer()
CustomerList -> Customer: defineCustomer()
CustomerList <-- Customer: Customer
Shop <-- CustomerList : id
@enduml
```

### Scenario 4-2 
```plantuml
@startuml
autonumber
EZShopGui -> Shop
Shop -> LoyaltyCardList : createCard()
LoyaltyCardList -> LoyaltyCard: createCard()
LoyaltyCardList <-- LoyaltyCard: LoyaltyCard
Shop <-- LoyaltyCardList : cardId
Shop -> CustomerList : attachCardToCustomer()
CustomerList -> Customer : getLoyaltyCardId()
CustomerList <-- Customer : LoyaltyCardId
CustomerList -> Customer : loyaltyCardId.add()
Shop <-- CustomerList : true
@enduml
```

## Use Cases 5
### Scenario 5-1
```plantuml
@startuml
autonumber
EZShopGui -> Shop 
Shop -> UserList : login()
Shop <- UserList : User
Shop -> User : getRole()
Shop <-- User : Role
EZShopGui <- Shop
@enduml
```

## Use Cases 6
### Scenario 6-1
```plantuml
@startuml
autonumber
EZShopGui -> Shop
Shop -> AccountBook : startSaleTransaction()
Shop <-- AccountBook : id
Shop -> Inventory : getProductTypeByBarCode()
Shop <-- Inventory : ProductType
Shop -> AccountBook : addProductToSale()
Shop -> Inventory : updateQuantity()
Inventory -> ProductType : setQuantity()
Shop <- Inventory : true
Shop <- AccountBook : true
Shop -> AccountBook : endSaleTransaction()
Shop -> AccountBook : UC7
Shop <-- AccountBook : true
Shop -> AccountBook : recordBalanceUpdate()
Shop <-- AccountBook : true
@enduml
```

### Scenario 6-2
```plantuml
@startuml
autonumber
EZShopGui -> Shop
Shop -> AccountBook : startSaleTransaction()
Shop <-- AccountBook : id
Shop -> Inventory : getProductTypeByBarCode()
Shop <-- Inventory : ProductType
Shop -> AccountBook : addProductToSale()
Shop -> Inventory : updateQuantity()
Inventory -> ProductType : setQuantity()
Shop <-- Inventory : true
EZShopGui <-- Shop : getDiscountRate()
EZShopGui -> Shop : discountRate
Shop -> AccountBook: applyDiscountRateToProduct()
Shop <-- AccountBook: true
Shop <-- AccountBook : true
Shop -> AccountBook : endSaleTransaction()
Shop -> AccountBook : UC7
Shop <-- AccountBook : true
Shop -> AccountBook : recordBalanceUpdate()
Shop <-- AccountBook : true
@enduml
```

## Use Case 8
### Scenario 8-1
```plantuml
@startuml
autonumber

EZShopGui -> Shop
Shop -> AccountBook : startReturnTransaction()
Shop <-- AccountBook : id
Shop -> Inventory : getProductTypeByBarCode()
Shop <-- Inventory : ProductType
Shop -> AccountBook : addProductToSale()
Shop -> AccountBook : returnProduct()
Shop <-- AccountBook : true
Shop -> Inventory : updateQuantity()
Inventory -> ProductType : setQuantity()
Shop <-- Inventory : true
Shop <-- AccountBook : true
Shop -> AccountBook : endReturnTransaction()
Shop <-- AccountBook : true
Shop -> AccountBook : UC10
Shop <-- AccountBook : true
Shop -> AccountBook : recordBalanceUpdate()
Shop <-- AccountBook : true
@enduml
```

## Use Case 9
### Scenario 9-1
```plantuml
@startuml
autonumber
EZShopGui -> Shop
EZShopGui <- Shop : getFromDate()
EZShopGui --> Shop : fromDate
EZShopGui <- Shop : getToDate()
EZShopGui --> Shop : toDate
Shop -> Accounting :getCreditsAndDebits()
Shop <-- Accounting :balanceOperations
EZShopGui <-- Shop  : balanceOperations
@enduml
```


