# Smart Delivery Planner - Project Documentation

## 1. Title Page
**Project Title**: Smart Delivery Planner
**Objective**: To enable the management of deliveries efficiently for companies like Zepto, Blinkit (similar to the Traveling Salesman Problem).
**Tech Stack**: Java, JDBC, SQLite, JavaFX
**Architecture**: Layered Architecture (Presentation Layer, Business Logic Layer, DAO Layer, Database Layer).

## 2. Problem Statement
The objective is to design, develop, and implement a real-world software application in Java demonstrating the effective use of Object-Oriented Programming (OOP) concepts along with Database Connectivity. The application must store, retrieve, update, and manage data using a relational database (SQLite). This planner acts as a smart delivery system to assist quick commerce delivery routing.

## 3. Objectives
1. **User Management**: Add, View, Update, Delete, and Search records.
2. **Database Operations (CRUD)**: Create, Read, Update, Delete using JDBC and SQLite.
3. **OOP Design**: Implement Classes/Objects, Encapsulation (Getters/Setters), Inheritance, Polymorphism (Overloading/Overriding), and Abstraction (Abstract class/Interface). Minimum 5 classes.
4. **Database Requirements**: Create a relational database with appropriate tables, primary keys, and relationships.
5. **System Architecture**: Adopt a 4-layer architecture (Presentation, Business Logic, DAO, Database).
6. **Thread Integration**: Apply threading to simulate or manage concurrent delivery tracking or processes.

## 4. System Design
### Layered Architecture
* **Presentation Layer (JavaFX)**: The GUI handling user input, record listing, and management panes.
* **Business Logic Layer**: Core classes such as `Delivery`, `Vehicle`, `Driver`, `RoutePlanner`, etc. Implements application logic, routing (TSP simulation), threading, validations.
* **Data Access Layer (DAO)**: Interfaces and Impl classes (`DeliveryDAO`, `DriverDAO`) to interact securely and cleanly with the database.
* **Database Layer**: SQLite handling persistent data storage locally.

## 5. Class Diagram (Proposed)
Classes to be implemented (minimum 5):
1. **Person (Abstract Class)**
   * Attributes: `id`, `name`, `contactNumber`
   * Abstract Methods: `getRoleDetails()`
2. **Driver (inherits Person)**
   * Attributes: `licenseNumber`, `vehicleId`
   * Methods: Overrides `getRoleDetails()`, getters/setters.
3. **Delivery**
   * Attributes: `deliveryId`, `address`, `status`, `driverId`
   * Methods: CRUD-related data bindings.
4. **BaseDAO (Interface)**
   * Methods: `create()`, `read()`, `update()`, `delete()`, `search()`
5. **DeliveryDAOImpl (implements BaseDAO)**
   * Methods: Executes SQL queries on `Delivery` records.
6. **DeliveryManager (Business Logic)**
   * Methods: Methods utilizing collections (ArrayList/HashMap) to manipulate data before fetching/sending from/to DAO. Includes Threading usage.

## 6. Database Schema
* **Table `drivers`**
  * `id` INTEGER PRIMARY KEY AUTOINCREMENT
  * `name` TEXT NOT NULL
  * `contact` TEXT
  * `license_number` TEXT NOT NULL

* **Table `deliveries`**
  * `id` INTEGER PRIMARY KEY AUTOINCREMENT
  * `address` TEXT NOT NULL
  * `status` TEXT NOT NULL (Pending, In-Transit, Delivered)
  * `driver_id` INTEGER (FOREIGN KEY referencing `drivers.id`)

## 7. Explanation of OOP Concepts Used
* **Encapsulation**: Using `private` fields with public getters/setters in classes like `Delivery` and `Driver`.
* **Inheritance**: `Driver` extending an abstract `Person` class to inherit base attributes.
* **Polymorphism**: 
  * *Method Overriding*: Overriding `.toString()` or abstract methods from the base class.
  * *Method Overloading*: `searchDelivery(int id)` vs `searchDelivery(String status)`.
* **Abstraction**: Defining contracts using the `BaseDAO` interface and abstract class `Person`.
* **Threading**: Implementing `Runnable` for concurrent background tasks.

## 8. Development Checklist
- [ ] Database Schema & SQL Scripts mapped.
- [ ] Model Classes defined.
- [ ] DAO Layer functioning with JDBC (SQLite/MySQL).
- [ ] Business Logic correctly coupling Models and DAO.
- [ ] JavaFX GUI integrated for Presentation Layer.
- [ ] Runnable threads integrated to simulate deliveries.

## 9. Conclusion
The Smart Delivery Planner efficiently uses Object-Oriented principles and Java constructs alongside Database persistency, providing a modern architecture and manageable codebase.
