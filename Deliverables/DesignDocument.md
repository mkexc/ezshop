# Design Document

Authors: Roberto Alessi (290180), Michelangelo Bartolomucci (292422), Gianvito Marzo (281761), Roberto Torta (290184)

Date: 28/04/2021

Version: 0.5


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
left to right direction

package ShopPackage{

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
    }

    class ProductType{
        - id : Integer
        - productCode : String
        - description : String
        - pricePerUnit : double
        - quantity : Integer
        - discountRate : Integer
        - notes : String
    }

    class Inventory{
        - list : HashMap<ProductType,Position>

        + updateProduct()
        + deleteProductType()
        + getAllProductTypes()
        + getProductTypeByBarCode()
        + getProductTypesByDescription()
        + updateQuantity()
        + updatePosition()
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
        - points : Integer
        - id : String()
    }

    class LoyaltyCardList{
        - LoyaltyCardList: ArrayList<LoyaltyCard>

        + createCard()
        + attachCardToCustomer()
        + modifyPointsOnCard()
    }

    class Customer{
        - customerId : Integer
        - loyaltyCardId : String
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

        + addProductToSale()
        + deleteProductFromSale()
        + applyDiscountRateToProduct()
        + applyDiscountRateToSale()
        + computePointsForSale()
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
    }
}

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
    + SaleTransaction()
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

SaleTransaction -- ProductType
LoyaltyCard -- SaleTransaction
Quantity -- SaleTransaction
FinancialTransaction -- Order
LoyaltyCardList -- LoyaltyCard
Inventory --> ProductType
Position -- Inventory
User --> UserList
LoyaltyCard --> Customer
Customer --> CustomerList
Order -- ProductType
OrderList -- Order
FinancialTransaction --> AccountBook
Exception <-- Shop
ReturnTransaction -> FinancialTransaction
SaleTransaction -> FinancialTransaction
Shop --> ShopPackage

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
actor User
User -> EZShopGui
EZShopGui -> EZShopController
EZShopController -> Inventory
Inventory -> ProductType : defineNewProductType()
Inventory -> Position : setPosition()
@enduml
```

## Use Case 2
### Scenario 2-1
```plantuml
@startuml
autonumber
ShopManager ->EZShopGui
EZShopGui -> EZShopController
EZShopController -> User : createUser()
EZShopController -> User : updateUserRights()
@enduml
```

## Use Case 3
### Scenario 3-1
```plantuml
@startuml
autonumber
actor Manager

Manager -> EZShopGui
EZShopGui -> EZShopController
EZShopController -> ShopManager : getUserRight()
ShopManager --> EZShopController : UserRight
EZShopController -> Order : setProductToOrder()
EZShopController -> Order : setQuantity()
EZShopController -> Order : issueReorder()
EZShopController -> Catalog : getPrice()
EZShopController <-- Catalog : price
EZShopController -> Order : setPrice()
EZShopController -> Order : setState()
EZShopController -> Order : sendAndPayOrderForProducType()

@enduml
```

## Use Case 4
### Scenario 4-1
```plantuml
@startuml
autonumber

User ->EZShopGui
EZShopGui -> EZShopController
EZShopController -> Customer : defineCustomer()


@enduml
```

## Use Cases 5
### Scenario 5-1
```plantuml
@startuml
autonumber

User -> EZShopGui
EZShopGui -> EZShopController
EZShopController -> User : login()
EZShopController -> User : getUserRight()
EZShopController <-- User : UserRight
EZShopGui <- EZShopController
User <- EZShopGui

@enduml
```

## Use Cases 6
### Scenario 6-1
```plantuml
@startuml
autonumber
Cashier -> EZShopGui
EZShopGui -> EZShopController
EZShopController -> SaleTransaction : startSaleTransaction()
SaleTransaction -> Inventory : getProductTypeByBarCode()
SaleTransaction <-- Inventory : ProductType
EZShopController -> SaleTransaction : addProductToSale()
SaleTransaction -> Inventory : updateQuantity()
Cashier --> SaleTransaction : endSaleTransaction()
EZShopController -> SaleTransaction : receiveCashPayment()/receiveCreditCardPayment()
SaleTransaction -> Accounting : recordBalanceUpdate()

@enduml
```


## Use Cases 7
### Scenario 7-1
```plantuml
@startuml

@enduml
```

## Use Case 8
### Scenario 8-1
```plantuml
@startuml
autonumber
Cashier -> EZShopGui
EZShopGui -> EZShopController
EZShopController -> Cashier :getSaleTransaction()
EZShopController <-- Cashier :SaleTransactionId
EZShopController -> ReturnTransaction : startReturnTransaction()
ReturnTransaction -> Inventory : getProductTypeByBarCode()
ReturnTransaction <-- Inventory : ProductType
EZShopController -> ReturnTransaction : returnProduct()
ReturnTransaction -> Inventory : updateQuantity()
Cashier -> ReturnTransaction : endReturnTransaction()
EZShopController -> ReturnTransaction : returnCreditCardPayment()
ReturnTransaction -> Accounting : recordBalanceUpdate()



@enduml
```

## Use Case 9
### Scenario 9-1
```plantuml
@startuml
autonumber
ShopManager -> EZShopGui
EZShopGui -> EZShopController
EZShopController -> Accounting :getCreditsAndDebits()
EZShopController <-- Accounting :ListCreditsAndDebits
EZShopGui <- EZShopController

@enduml
```
## Use Case 10
### Scenario 10-1
```plantuml
@startuml
autonumber
Cashier  -> EZShopGui
EZShopGui -> EZShopController

@enduml
```


