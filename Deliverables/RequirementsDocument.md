# Requirements Document 

Authors:

Date:

Version:

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders


| Stakeholder name  | Description | 
| ----------------- |:-----------:|
|   End user   	    | warehouse workers, cashiers, cash registers, managers, owners |
| Company that develops | software eng, mecha engineers, electrical engineers, marketing people, safety engineers |
| Maintainers | the same company that develops the app |
| Certification authority |  |
| Generic Device |  |
| Suppliers, customers |  |
| Item |  |


# Context Diagram and interfaces

## Context Diagram
\<Define here Context diagram using UML use case diagram>

\<actors are a subset of stakeholders>

## Interfaces
\<describe here each interface in the context diagram>

\<GUIs will be described graphically in a separate document>

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
| Cashier     | On/off button | GUI |
|             | scan item command | Barcode scanner |
|             | onClick command | Touchscreen |
| Generic Device | 220V 50hz | Cash Register |
| | Pos | Internet connection |
| Warehouse worker | GUI | On/off button |
| | | Touchscreen |
| Manager/Owner | GUI | On/off button |
| | | Touchscreen |

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>


# Functional and non functional requirements

## Functional Requirements

\<In the form DO SOMETHING, or VERB NOUN, describe high level capabilities of the system>

\<they match to high level use cases>

| ID        | Description  |
| ------------- |:-------------:| 
| FR1 Manage sales |  |
| FR1.1 Transaction for a customer purchase  |   |
| FR1.1.1 Check the register number  | |
| FR1.1.1.1 Electronic transaction | |
| FR1.1.1.1.1 Check for fidelity card | |
| FR1.1.1.1.2 Make the transaction through the POS | |
| FR1.1.1.2 Cash transaction | |
| FR1.1.1.2.1 Check for fidelity card | |
| FR1.2 Display total purchase cost | |
| FR2 Manage inventory | |
| FR2.1 Refill warehouse | |
| FR2.1.1 warehouse receives the supply from the suppliers | |
| FR2.1.2 warehouse increase the items supply level | |
| FR2.2 Check items in inventory | |
| FR2.2.1 Check only by name | |
| FR2.2.2 Check quantity | |
| FR2.2.2.1 Order new supply if the level is below a threshold | |
| FR2.2.2.1.1 Make an order from suppliers | |
| FR2.2.3 Make dispute to the supplier for bad/damaged products | |
| FR3 Manage customers | |
| FR3.1 Add a new subscribed customer | |
| FR3.2 Manage a fidelity card | |
| FR3.2.1 Check fidelity points | |
| FR3.2.2 Give gifts to subscriber after threshold | |
| FR3.3 Edit/Update an existing subscriber | |
| FR3.4 Delete a customer from subscribers | |
| FR4 Support accounting | |
| FR4.1 Calculate taxes | |
| FR4.2 Calculate daily income | |
| FR4.3 Analyze annual income | |
| FR4.4.1 Analyze and display total income | |
| FR4.4.2 Analyze and display net income | |
| FR4.4 Check numbers of sold products if equal to inventory warehouse decrement | |
| FR5 Manage employees | |
| FR5.1 Hire a new Employee | |
| FR5.2 Fire an Employee | |
| FR5.3 Edit employee’s documents | |
| FR5.4 Bind employee with a purchase (for statistic’s sake) | |
| FR5.5 Pay employee | |
| FR5.6 Manage employee salary | |
| FR | |
| FR | |
| FR | |
| FR | |
| FR | |


## Non Functional Requirements

\<Describe constraints on functional requirements>
speed
size
reliabilty
ease of use
robustness
portability
efficiency
availability

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
| NFR1 | size | manage a maximum of 2500 item types | |
| NFR2 | size | manage a maximum of 50 employees | |
| NFR3 | efficiency | be able to complete actions in less than 2 clicks | |
| NFR4 | portability | deployable on different operating systems (Java) | | 
| NFR5 | speed | low latency (1 seconds for action) | | 
| NFR6 | ease of use | time to learn how to use for non engineer < 15minutes | | 
| NFR7 | efficiency | response time to any (virtual) button pressed <1sec | | 
| NFR8 | robustness | availability at least 99% | | 
| NFR9 | | | | 
| NFR10 | | | | 
| NFR | | | | 
| NFR | | | | 
| NFR | | | | 
| NFR | | | | 
| NFR | | | | 



# Use case diagram and use cases


## Use case diagram
\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>

```plantuml

@startuml
left to right direction
package uc {
  actor Warehouse_worker as WW
  actor Manager as MNG
  actor Owner as OWN
  actor Cashier as CSH
  actor Customer as CST
  actor POS 
  package Generic_Device {
    usecase "APP" as APP
    
  }
  WW --> APP
  MNG --> APP
  OWN --> APP
  CSH -- Generic_Device
  CST -- CSH
  POS -- APP
}

@enduml
```


\<next describe here each use case in the UCD>
### Use case 1, UC1
| Actors Involved        |  |
| ------------- |:-------------:| 
|  Precondition     | Cash register is ON
| | Barcode scanner is ready |
| | Item’s barcode is valid |
| | POS is connected |


|  Post condition     | \<Boolean expression, must evaluate to true after UC is finished> |
|  Nominal Scenario     | \<Textual description of actions executed by the UC> |
|  Variants     | \<other executions, ex in case of errors> |

##### Scenario 1.1 

\<describe here scenarios instances of UC1>

\<a scenario is a sequence of steps that corresponds to a particular execution of one use case>

\<a scenario is a more formal description of a story>

\<only relevant scenarios should be described>

| Scenario 1.1 | |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the scenario can start> |
|  Post condition     | \<Boolean expression, must evaluate to true after scenario is finished> |
| Step#        | Description  |
|  1     |  |  
|  2     |  |
|  ...     |  |

##### Scenario 1.2

##### Scenario 1.x

### Use case 2, UC2
..

### Use case x, UCx
..



# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships> 

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

# System Design
\<describe here system design>

\<must be consistent with Context diagram>

# Deployment Diagram 

\<describe here deployment diagram >

