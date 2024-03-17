# COMP 3005 Assignment 3 Question 1
#### Vishrutha Gopa 101267228

This Java application helps manage student records using a PostgreSQL database. It supports CRUD (Create, Read, Update, Delete) operations on student information. Users can view all student details, add new students, update student email addresses, and delete existing student records from the database.

## Prerequisites
Before running the application, ensure you have the following installed:
- PostgreSQL
- JDK (Java Development Kit)

## Setup Instructions
1. **Database Setup**:
    - Create a database named `A3_Q1` in your PostgreSQL server.
    - Execute the SQL commands provided in the students_ddl.sql file to create the necessary table and insert initial data.
    - Update the database connection details (url, username, and password) in the Main.java file to match your PostgreSQL configuration.

2. **Compile the Application**:
    - Navigate to the directory containing the `Main.java` file.
    - Compile the Java application using the following command:
      ```
      javac Main.java
      ```

3. **Run the Application**:
    - After compiling, run the application using the following command:
      ```
      java Main
      ```
    - Follow the menu to interact with the application.

## Video Demonstration
