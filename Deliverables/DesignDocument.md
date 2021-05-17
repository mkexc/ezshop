# Design Document

Authors: Roberto Alessi (290180), Michelangelo Bartolomucci (292422), Gianvito Marzo (281761), Roberto Torta (290184)

Date: 17/05/2021

Version: 2.0


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)


# High level design

EZShop is standalone application working indipendently on a single cash register. In the implementation, the façade class is EZShop.

```plantuml
@startuml
scale 1024 width
top to bottom direction
allow_mixing

package it.polito.ezshop {
    package it.polito.ezshop.gui {}
    
    package it.polito.ezshop.exceptions {}
    
    package it.polito.ezshop.model {
        class it.polito.ezshop.model.BalanceOperation
        class it.polito.ezshop.model.CreditCard
        class it.polito.ezshop.model.Customer
        class it.polito.ezshop.model.Order
        class it.polito.ezshop.model.ProductType
        class it.polito.ezshop.model.SaleTransaction
        class it.polito.ezshop.model.TicketEntry
        class it.polito.ezshop.model.User
    }

    package it.polito.ezshop.data {
        interface it.polito.ezshop.data.EZShopInterface
        interface it.polito.ezshop.data.BalanceOperation
        interface it.polito.ezshop.data.Customer
        interface it.polito.ezshop.data.Order
        interface it.polito.ezshop.data.ProductType
        interface it.polito.ezshop.data.SaleTransaction
        interface it.polito.ezshop.data.TicketEntry
        interface it.polito.ezshop.data.User
        class it.polito.ezshop.data.EZShop
    }

    note as modelImpl
        The classes implements all the interface
        inside package it.polito.ezshop.data,
        respectively.
    end note
}


it.polito.ezshop.data.EZShop .|> it.polito.ezshop.data.EZShopInterface
it.polito.ezshop.model .|> it.polito.ezshop.data
it.polito.ezshop.model .up. modelImpl
it.polito.ezshop.model.BalanceOperation <.. "<<extends>>" it.polito.ezshop.model.SaleTransaction
it.polito.ezshop.data.EZShop <-up- it.polito.ezshop.gui
it.polito.ezshop.exceptions <-- it.polito.ezshop.data.EZShop
it.polito.ezshop.model <-- it.polito.ezshop.data.EZShop

@enduml
```

# Low level design

```plantuml
@startuml
top to bottom direction
scale 1024 width 
scale 768 height

package it.polito.ezshop.model{

    class User <<Persistent>> {
        - id: Integer
        - username: String
        - password: String
        - role : enum{"Administrator"|"Cashier"|"ShopManager"}
    }

    class ProductType <<Persistent>> {
        - id: Integer
        - productCode: String
        - description: String
        - pricePerUnit: double
        - quantity: Integer
        - notes: String
        - location: String
        
        + validateProductCode()
    }

    class Order <<Persistent>> {
        - orderId: Integer
        - productCode: String
        - pricePerUnit: double
        - quantity: Integer
        - status: enum{ISSUED|ORDERED|COMPLETED}
    }

    class Customer <<Persistent>> {
        - customerId: Integer
        - loyaltyCardId: String
        - customerName: String
        - points: Integer
    }

    class BalanceOperation <<Persistent>> {
        - balanceId: Integer
        - date: LocalDate
        - money: double
        - type: enum{DEBIT|CREDIT} 
    }

    class SaleTransaction {
        - transactionId: Integer
        - entries: List<TicketEntry>
        - discountRate: double
        - price: double
    }

    class CreditCard <<Persistent>> {
        - cardNumber: String
        - balance: double
        
        + validateWithLuhn()
    }

    class TicketEntry {
        - barCode: String
        - productDescription: String
        - amount: int
        - pricePerUnit: double
        - discountRate: double
    }
}

package it.polito.ezshop.data{
    class EZShop{
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
        + deleteSaleTransaction()
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

SaleTransaction --> TicketEntry
TicketEntry --> "*" ProductType
Order "*" --> ProductType
SaleTransaction --|> BalanceOperation
EZShop -right-> it.polito.ezshop.model

@enduml
```

# Verification traceability matrix

| FR  | Customer | ProductType | SaleTransaction | Exception | Order | User | TicketEntry | BalanceOperation | CreditCard | EZShop |
| --- | :------: | :---------: | :-------------: | :-------: | :---: | :--: | :---------: | :--------------: | :--------: | :----: |
| FR1 |          |             |                 |         x |       |    x |             |                  |            |      x | 
| FR3 |          |           x |                 |         x |     x |      |             |                  |            |      x | 
| FR4 |          |           x |               x |         x |     x |      |             |                  |            |      x | 
| FR5 |        x |             |               x |         x |       |      |             |                  |            |      x | 
| FR6 |        x |           x |               x |         x |       |      |             |                  |            |      x | 
| FR7 |        x |             |               x |         x |       |      |             |                  |            |      x | 
| FR8 |          |             |               x |         x |       |      |             |                  |            |      x |


# Verification sequence diagrams

## Use Case 1

### Scenario 1-1

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> Inventory : createProductType()
Inventory -> ProductType : createProductType()
Inventory <-- ProductType : id
Inventory -> ProductType : updatePosition()
ProductType -> Position : getPosition()
ProductType <-- Position : Position
ProductType -> Position : Position.add()
Inventory <-- ProductType : true
EZShop <-- Inventory : true
@enduml
```

### Scenario 1-3

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> Inventory : getProductTypeByBarcode()
Inventory --> EZShop : ProductType
Inventory -> ProductType : updateProduct()
Inventory <-- ProductType : true
@enduml
```

## Use Case 2

### Scenario 2-1

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop 
EZShop -> UserList : createUser()
UserList -> User : createUser()
UserList <-- User : User
EZShop <-- UserList : id
EZShop -> UserList : updateUserRights()
UserList -> User : setRole()
EZShop <-- UserList : true
@enduml
```

### Scenario 2-2

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop 
EZShop -> UserList : getUser()
EZShop <-- UserList : id
UserList -> EZShop : User
EZShop -> UserList : deleteUser()
EZShop <-- UserList : true
@enduml
```

## Use Case 3

### Scenario 3-1

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> OrderList : issueOrder()
OrderList -> Order : issueOrder()
OrderList <-- Order : Order
OrderList -> Order : setState()
EZShop <- OrderList : id
@enduml
```

### Scenario 3-2

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> OrderList : getAllOrders()
EZShop <-- OrderList : orderList
EZShop --> EZShopGUI : orderList
EZShop <-- EZShopGUI : Order
EZShop -> Order : payOrder()
Order -> Order : setState()
EZShop <-- Order : true
@enduml
```

### Scenario 3-3

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop

EZShop -> OrderList : getAllOrders()
EZShop <-- OrderList : orderList
EZShop --> EZShopGUI : orderList
EZShop <-- EZShopGUI : Order
EZShop -> OrderList : recordOrderArrival()
OrderList -> Order : recordOrderArrival()
Order -> Order : setState()
OrderList <-- Order : true
EZShop <-- OrderList : true
EZShop -> Inventory : getProductType()
EZShop <-- Inventory : ProductType
EZShop -> Inventory : updateQuantity()
Inventory -> ProductType : updateQuantity()
ProductType -> ProductType : setQuantity()
Inventory <-- ProductType : true
@enduml
```

## Use Case 4

### Scenario 4-1 

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> CustomerList : defineCustomer()
CustomerList -> Customer: defineCustomer()
CustomerList <-- Customer: Customer
EZShop <-- CustomerList : id
@enduml
```

### Scenario 4-2

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> LoyaltyCardList : createCard()
LoyaltyCardList -> LoyaltyCard: createCard()
LoyaltyCardList <-- LoyaltyCard: LoyaltyCard
EZShop <-- LoyaltyCardList : cardId
EZShop -> CustomerList : attachCardToCustomer()
CustomerList -> Customer : getLoyaltyCardId()
CustomerList <-- Customer : LoyaltyCardId
CustomerList -> Customer : loyaltyCardId.add()
EZShop <-- CustomerList : true
@enduml
```

### Scenario 4-4

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> CustomerList : getAllCustomers()
EZShop <-- CustomerList : customerList
EZShop --> EZShopGUI : customerList
EZShop <-- EZShopGUI : Customer
EZShop -> Customer: modifyCustomer()
EZShop <-- Customer: true
@enduml
```

## Use Cases 5

### Scenario 5-1

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop 
EZShop -> UserList : login()
EZShop <- UserList : User
EZShop -> User : getRole()
EZShop <-- User : Role
EZShopGUI <- EZShop
@enduml
```

### Scenario 5-2

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop 
EZShop -> UserList : getUser()
EZShop <- UserList : User
EZShop -> User : logout()
EZShop <-- User : true
EZShopGUI <- EZShop
@enduml
```

## Use Cases 6

### Scenario 6-1

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> AccountBook : startSaleTransaction()
EZShop <-- AccountBook : id
EZShop -> Inventory : getProductTypeByBarCode()
EZShop <-- Inventory : ProductType
EZShop -> AccountBook : addProductToSale()
EZShop -> Inventory : updateQuantity()
Inventory -> ProductType : updateQuantity()
ProductType -> ProductType : setQuantity()
Inventory <-- ProductType : true
EZShop <-- Inventory : true
EZShop <-- AccountBook : true
EZShop -> AccountBook : endSaleTransaction()
EZShop -> AccountBook : UC7
EZShop <-- AccountBook : change>=0
EZShop -> AccountBook : recordBalanceUpdate()
EZShop <-- AccountBook : true
@enduml
```

### Scenario 6-2

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> AccountBook : startSaleTransaction()
EZShop <-- AccountBook : id
EZShop -> Inventory : getProductTypeByBarCode()
EZShop <-- Inventory : ProductType
EZShop -> AccountBook : addProductToSale()
EZShop -> Inventory : updateQuantity()
Inventory -> ProductType : updateQuantity()
ProductType -> ProductType : setQuantity()
Inventory <-- ProductType : true
EZShop <-- Inventory : true
EZShopGUI <- EZShop : getDiscountRate()
EZShopGUI --> EZShop : discountRate
EZShop -> AccountBook: applyDiscountRateToProduct()
EZShop <-- AccountBook: true
EZShop <-- AccountBook : true
EZShop -> AccountBook : endSaleTransaction()
EZShop -> AccountBook : UC7
EZShop <-- AccountBook : change>=0
EZShop -> AccountBook : recordBalanceUpdate()
EZShop <-- AccountBook : true
@enduml
```

### Scenario 6-4

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> AccountBook : startSaleTransaction()
EZShop <-- AccountBook : id
EZShop -> Inventory : getProductTypeByBarCode()
EZShop <-- Inventory : ProductType
EZShop -> AccountBook : addProductToSale()
EZShop -> Inventory : updateQuantity()
Inventory -> ProductType : updateQuantity()
ProductType -> ProductType : setQuantity()
Inventory <-- ProductType :true
EZShop <-- Inventory : true
EZShop <-- AccountBook : true
EZShop -> AccountBook : endSaleTransaction()
EZShop -> AccountBook : UC7
EZShop <-- AccountBook : change>=0
EZShop -> AccountBook : recordBalanceUpdate()
EZShop <-- AccountBook : true
EZShop -> AccountBook : computePointsForSale()
EZShop <-- AccountBook : points
EZShop -> EZShopGUI : getCardId()
EZShop <-- EZShopGUI : cardId
EZShop -> LoyaltyCardList : modifyPointsOnCard()
LoyaltyCardList -> LoyaltyCard : modifyPointsOnCard()
LoyaltyCard -> LoyaltyCard : setPoints()
LoyaltyCardList <-- LoyaltyCard : true
EZShop <-- LoyaltyCardList : true
@enduml
```

### Scenario 6-5

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> AccountBook : startSaleTransaction()
EZShop <-- AccountBook : id
EZShop -> Inventory : getProductTypeByBarCode()
EZShop <-- Inventory : ProductType
EZShop -> AccountBook : addProductToSale()
EZShop -> Inventory : updateQuantity()
Inventory -> ProductType : updateQuantity()
ProductType -> ProductType : setQuantity()
Inventory <-- ProductType : true
EZShop <- Inventory : true
EZShop <- AccountBook : true
EZShop -> AccountBook : endSaleTransaction()
EZShop -> AccountBook : UC7
EZShop <-- AccountBook : change=-1
EZShop -> AccountBook : deleteSaleTransaction()
EZShop <-- AccountBook : true
@enduml
```

## Use Case 7

### Scenario 7-4

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop : receiveCashPayment()
EZShop -> AccountBook : receiveCashPayment()
EZShop <-- AccountBook : change
EZShop --> EZShopGUI : change
@enduml
```

## Use Case 8

### Scenario 8-1

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShop -> AccountBook : startReturnTransaction()
EZShop <-- AccountBook : id
EZShop -> Inventory : getProductTypeByBarCode()
EZShop <-- Inventory : ProductType
EZShop -> AccountBook : addProductToSale()
EZShop -> AccountBook : returnProduct()
EZShop <-- AccountBook : true
EZShop -> Inventory : updateQuantity()
Inventory -> ProductType : updateQuantity()
ProductType -> ProductType : setQuantity()
Inventory <-- ProductType : true
EZShop <-- Inventory : true
EZShop <-- AccountBook : true
EZShop -> AccountBook : UC10
EZShop <-- AccountBook : return>=0
EZShop -> AccountBook : endReturnTransaction()
EZShop <-- AccountBook : true
EZShop -> AccountBook : recordBalanceUpdate()
EZShop <-- AccountBook : true
@enduml
```

## Use Case 9

### Scenario 9-1

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop
EZShopGUI <- EZShop : getFromDate()
EZShopGUI --> EZShop : fromDate
EZShopGUI <- EZShop : getToDate()
EZShopGUI --> EZShop : toDate
EZShop -> Accounting :getCreditsAndDebits()
EZShop <-- Accounting :balanceOperations
EZShopGUI <-- EZShop  : balanceOperations
@enduml
```

## Use Case 10

### Scenario 10-2

```plantuml
@startuml
autonumber
EZShopGUI -> EZShop : returnCashPayment()
EZShop -> AccountBook : returnCashPayment()
EZShop <-- AccountBook : return
EZShop --> EZShopGUI : return
@enduml
```
