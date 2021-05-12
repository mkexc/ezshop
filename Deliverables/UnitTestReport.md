# Unit Testing Documentation

Authors: Roberto Alessi (290180), Michelangelo Bartolomucci (292422), Gianvito Marzo (281761), Roberto Torta (290184)

Date: 12/05/2021

Version: 0.1

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

 ### **Class *ezshop.java* - method *createUser***



**Criteria for method *createUser*:**
	

 - Validity of username
 - Validity of password
 - Role




**Predicates for method *createUser*:**

| Criteria             | Predicate      |
| -------------------- | -------------- |
| Validity of username |      valid     |
|                      |       null     |
| Validity of password |      valid     |
|                      |       null     |
| Role                 |      valid     |
|                      |       null     |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Validity of username | Validity of password | Role  | Valid / Invalid | Description of the test case | JUnit test case |
|----------------------|----------------------|------ |-----------------|------------------------------|-----------------|
|  Invalid             |                    * |     * |   Invalid       |                              |                 |
|                     *|          Invalid     |     * |   Invalid       |                              |                 |
|                     *|                     *|Invalid|   Invalid       |                              |                 |
|  Valid               |          Valid       |Valid  |   Valid         |                              |                 |
|                      |                      |       |                 |                              |                 |




# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|-----------|-----------------|
|           |                 |
|           |                 |
|           |                 |

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|----------|-----------|----------------------|-----------------|
|          |           |                      |                 |
|          |           |                      |                 |
|          |           |                      |                 |



