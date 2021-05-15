# Unit Testing Documentation

Authors: Roberto Alessi (290180), Michelangelo Bartolomucci (292422), Gianvito Marzo (281761), Roberto Torta (290184)

Date: 15/05/2021

Version: 0.2

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)

- [White Box Unit Tests](#white-box-unit-tests)

# Black Box Unit Tests - EZShop

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

 ### **Class *it.polito.ezshop.model.ProductType* - method *validateProductCode***

**Criteria for method *validateProductCode*:**
	
 - Validity of productCode

**Predicates for method *validateProductCode*:**

| Criteria                | Predicate      |
| ----------------------  | -------------- |
| Validity of productCode |      true      |
|                         |      false     |

**Boundaries**:

| Criteria                | Boundary values |
| ----------------------- | --------------- |
| Validity of productCode | none            |

**Combination of predicates**:
| Validity of productCode | Valid / Invalid | Description of the test case   | JUnit test case           |
|-------------------------|-----------------|--------------------------------|---------------------------|
|  Valid(14)              |   Valid         | T1("11234567890125")   ->true  | testValidationProductCode |
|  Invalid(14)            |   Invalid       | T2("12345678901234")   ->false | testValidationProductCode |
|  Valid(13)              |   Valid         | T3("1234567890128")    ->true  | testValidationProductCode |
|  Valid(12)              |   Valid         | T4("123456789012")     ->true  | testValidationProductCode |
|  Invalid(9)             |   Invalid       | T5("123456789")        ->false | testValidationProductCode |
|  Invalid(17)            |   Invalid       | T6("12345678901111112")->false | testValidationProductCode |
|  Valid(14)              |   Valid         | T7("11234567890200")   ->true  | testValidationProductCode |


### **Class *it.polito.ezshop.model.CreditCard* - method *validateWithLuhn***

**Criteria for method *validateWithLuhn*:**
	
 - Validity of cardNumber

**Predicates for method *validateWithLuhn*:**

| Criteria                | Predicate      |
| ----------------------  | -------------- |
| Validity of cardNumber  |      true      |
|                         |      false     |

**Boundaries**:

| Criteria               | Boundary values |
| ---------------------- | --------------- |
| Validity of cardNumber | none            |

**Combination of predicates**:
| Validity of cardNumber  | Valid / Invalid | Description of the test case | JUnit test case           |
|-------------------------|-----------------|------------------------------|---------------------------|
|  Valid                  |   Valid         | T1("4556737586899855")->true | testValidationWithLuhn    |
|  Invalid                |   Invalid       | T2("4324332424")      ->false| testValidationWithLuhn    |

# White Box Unit Tests - EZShop

 ### **Class *it.polito.ezshop.model.ProductType* - method *validateProductCode***

There's no need to execute White Box tests for this method because with BlackBox testing we can reach the 100% coverage of the method's code.

### **Class *it.polito.ezshop.model.CreditCard* - method *validateWithLuhn***

There's no need to execute White Box tests for this method because with BlackBox testing we can reach the 100% coverage of the method's code.