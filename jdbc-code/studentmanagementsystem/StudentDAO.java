package studentmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student entity
 * Provides methods to perform CRUD operations on the students table
 */
public class StudentDAO {
    
    // SQL statements
    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE IF NOT EXISTS students (" +
        "student_id VARCHAR(20) PRIMARY KEY, " +
        "name VARCHAR(50) NOT NULL, " +
        "sex VARCHAR(10), " +
        "birthday DATE, " +
        "major VARCHAR(50), " +
        "college VARCHAR(50)" +
        ")";
    
    private static final String INSERT_SQL = 
        "INSERT INTO students (student_id, name, sex, birthday, major, college) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_SQL = 
        "UPDATE students SET name = ?, sex = ?, birthday = ?, major = ?, college = ? " +
        "WHERE student_id = ?";
    
    private static final String DELETE_SQL = 
        "DELETE FROM students WHERE student_id = ?";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT * FROM students WHERE student_id = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT * FROM students";
    
    /**
     * Initialize the database by creating the students table if it doesn't exist
     * @throws SQLException if a database access error occurs
     */
    public void initializeDatabase() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
            System.out.println("Students table created or already exists");
        }
    }
    
    /**
     * Add a new student to the database
     * @param student the student to add
     * @return true if the student was added successfully, false otherwise
     */
    public boolean addStudent(Student student) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getSex());
            pstmt.setString(4, student.getBirthday());
            pstmt.setString(5, student.getMajor());
            pstmt.setString(6, student.getCollege());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing student in the database
     * @param student the student to update
     * @return true if the student was updated successfully, false otherwise
     */
    public boolean updateStudent(Student student) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getSex());
            pstmt.setString(3, student.getBirthday());
            pstmt.setString(4, student.getMajor());
            pstmt.setString(5, student.getCollege());
            pstmt.setString(6, student.getStudentId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a student from the database
     * @param studentId the ID of the student to delete
     * @return true if the student was deleted successfully, false otherwise
     */
    public boolean deleteStudent(String studentId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setString(1, studentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a student by ID
     * @param studentId the ID of the student to get
     * @return the student, or null if not found
     */
    public Student getStudentById(String studentId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setString(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractStudentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting student by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all students from the database
     * @return a list of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                Student student = extractStudentFromResultSet(rs);
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
        }
        
        return students;
    }
    
    /**
     * Extract a Student object from a ResultSet
     * @param rs the ResultSet containing student data
     * @return a Student object
     * @throws SQLException if a database access error occurs
     */
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setName(rs.getString("name"));
        student.setSex(rs.getString("sex"));
        student.setBirthday(rs.getString("birthday"));
        student.setMajor(rs.getString("major"));
        student.setCollege(rs.getString("college"));
        return student;
    }
}
