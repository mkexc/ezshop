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
|   Stakeholder x..     |             |

# Context Diagram and interfaces

## Context Diagram
\<Define here Context diagram using UML use case diagram>

\<actors are a subset of stakeholders>

```plantuml
@startuml
left to right direction
package context_diagram {
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

## Interfaces
\<describe here each interface in the context diagram>

\<GUIs will be described graphically in a separate document>

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   Actor x..     |  |  |

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
|  FR1     |  |
|  FR2     |   |
| FRx..  | |

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     |   |  | |
|  NFR2     | |  | |
|  NFR3     | | | |
| NFRx .. | | | |


# Use case diagram and use cases


## Use case diagram
\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>

```plantuml
@startuml
left to right direction
package use_cases{
    actor cashier as CSH 
    actor POS
    actor Warehouse_worker as WW
    actor Manager as MNG
    actor Owner as OWN

    package app {
        usecase "FR1: Manage sales" as FR1
        usecase "FR1.1: Transaction for a customer purchase" as FR1_1
        usecase "FR1.2: Display total purchase cost" as FR1_2
        usecase "FR2: Manage inventory" as FR2
        usecase "FR2.1: Refill warehouse" as FR2_1
        usecase "FR2.2: Check items in inventory" as FR2_2
        usecase "FR3: Manage costumer" as FR3
        usecase "FR3.1: Add a new subscribed customer" as FR3_1
        usecase "FR3.2: Manage a fidelity card" as FR3_2
        usecase "FR3.3: Edit/Update an existing subscriber" as FR3_3
        usecase "FR3.4: Delete a customer from subscribers" as FR3_4
        usecase "FR4: Support accounting" as FR4
        usecase "FR4.1: Calculate taxes" as FR4_1
        usecase "FR4.2: Calculate daily income" as FR4_2
        usecase "FR4.3: Analyze annual income" as FR4_3
        usecase "FR4.4: Confro if the number of sold products is equal to the number of the warehouse decrease" as FR4_4
        usecase "FR5: Manage employees" as FR5
        usecase "FR5.1: Hire a new employee" as FR5_1
        usecase "FR5.2: Fire an employee" as FR5_2
        usecase "FR5.3: Edit employee's documents" as FR5_3
        usecase "FR5.4: Bind employee witha purchase" as FR5_4
        usecase "FR5.5: Pay employee" as FR5_5
        usecase "FR5.6: Manage employee salary" as FR5_6

        CSH --> FR1
        CSH --> FR3
        POS ---> FR1
        WW  --> FR2
        MNG --> FR5
        MNG --> FR4
        OWN --|> MNG
        OWN --> FR5
        OWN --> FR4

        FR1 --> FR3
        FR1 ..> FR1_1
        FR1 ..> FR1_2
        FR1_1 --> FR2
        FR1_1 --> FR4_2

        FR2 ..> FR2_1
        FR2 ..> FR2_2

        FR3 .> FR3_1
        FR3 ..> FR3_2
        FR3 ..> FR3_3
        FR3 ..> FR3_4

        FR4 ..> FR4_1
        FR4 ..> FR4_2
        FR4 ..> FR4_3
        FR4 ..> FR4_4

        FR5 ..> FR5_1
        FR5 ..> FR5_2
        FR5 ..> FR5_3
        FR5 ..> FR5_4
        FR5 ..> FR5_5
        FR5 ..> FR5_6
        FR5_5 --> FR4

    }
}

@enduml
```

>>>>>>> docs: added use case diagram

\<next describe here each use case in the UCD>
### Use case 1, UC1
| Actors Involved        |  |
| ------------- |:-------------:|
|  Precondition     | \<Boolean expression, must evaluate to true before the UC can start> |
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

