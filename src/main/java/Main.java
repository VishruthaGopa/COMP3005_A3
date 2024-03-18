import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        // Database connection
        // Update the database connection details here (url, username, and password) match your PostgreSQL configuration.
        String url = "jdbc:postgresql://localhost:5432/A3_Q1";
        String user = "postgres";
        String password = "postgres";
        Scanner scanner = new Scanner(System.in); // Scanner

        try {
            // Establishing database connection
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database.");

            // Check if the students table exists, if not, create and populate it
            if (!tableExists("students")) {
                createAndPopulateTable();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display menu loop
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. View all students");
            System.out.println("2. Add a student");
            System.out.println("3. Update a student's email");
            System.out.println("4. Delete a student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // View all students
                    getAllStudents();
                    break;
                case 2:
                    // Adding a new student
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter enrollment date (YYYY-MM-DD): ");
                    String enrollmentDate = scanner.nextLine();
                    addStudent(firstName, lastName, email, enrollmentDate);
                    break;
                case 3:
                    // Updating a student's email
                    System.out.print("Enter student ID: ");
                    int studentId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    updateStudentEmail(studentId, newEmail);
                    break;
                case 4:
                    // Deleting a student
                    System.out.print("Enter student ID to delete: ");
                    int idToDelete = scanner.nextInt();
                    deleteStudent(idToDelete);
                    break;
                case 5:
                    // Exiting the program
                    System.out.println("Exiting program...");
                    return;
                default:
                    // User didn't pick a valid choice from menu.
                    System.out.println("Invalid choice. Please enter a valid one.");
            }
        }
    }

    // Method to retrieve and display all students
    public static void getAllStudents() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                // Print each student's information
                System.out.print(resultSet.getInt("student_id") + " \t");
                System.out.print(resultSet.getString("first_name") + " \t");
                System.out.print(resultSet.getString("last_name") + " \t");
                System.out.print(resultSet.getString("email") + " \t");
                System.out.println(resultSet.getDate("enrollment_date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Method to add a new student
    public static void addStudent(String firstName, String lastName, String email, String enrollmentDate) {
        String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)){
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            Date sqlEnrollmentDate = Date.valueOf(enrollmentDate);
            pstmt.setDate(4, sqlEnrollmentDate);

            pstmt.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a student's email
    public static void updateStudentEmail(int studentId, String newEmail){
        String updateSQL = "UPDATE students SET email = ? WHERE student_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)){
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, studentId);
            pstmt.executeUpdate();
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student email updated successfully.");
            } else {
                System.out.println("No student found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Method to delete a student
    public static void deleteStudent(int idToDelete){
        String deleteSQL = "DELETE FROM students WHERE student_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)){
            pstmt.setInt(1, idToDelete);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student with ID " + idToDelete + " deleted successfully.");
            } else {
                System.out.println("No student found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Method to check if a table exists in the database
    public static boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);
        return resultSet.next();
    }

    // Method to create the students table and insert initial data
    public static void createAndPopulateTable() {
        try (Statement statement = connection.createStatement()) {
            // Check if the students table exists
            boolean tableExists = tableExists("students");

            if (!tableExists) {
                // Create the students table
                String createTableSQL = "CREATE TABLE students (" +
                        "student_id SERIAL PRIMARY KEY, " +
                        "first_name TEXT NOT NULL, " +
                        "last_name TEXT NOT NULL, " +
                        "email TEXT NOT NULL UNIQUE, " +
                        "enrollment_date DATE)";

                statement.executeUpdate(createTableSQL);
                System.out.println("Table 'students' created successfully.");
            }

            // Insert initial data into the students table
            String insertDataSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES " +
                    "('John', 'Doe', 'john.doe@example.com', '2023-09-01'), " +
                    "('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'), " +
                    "('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02')";

            statement.executeUpdate(insertDataSQL);
            System.out.println("Initial data inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}