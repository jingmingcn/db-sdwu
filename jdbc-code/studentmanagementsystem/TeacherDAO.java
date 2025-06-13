package studentmanagementsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Teacher entity
 * Provides methods to perform CRUD operations on the teachers table
 */
public class TeacherDAO {
    
    // SQL statements
    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE IF NOT EXISTS teachers (" +
        "teacher_id VARCHAR(20) PRIMARY KEY, " +
        "name VARCHAR(50) NOT NULL, " +
        "sex VARCHAR(10), " +
        "title VARCHAR(50), " +
        "department VARCHAR(50), " +
        "email VARCHAR(100)" +
        ")";
    
    private static final String INSERT_SQL = 
        "INSERT INTO teachers (teacher_id, name, sex, title, department, email) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_SQL = 
        "UPDATE teachers SET name = ?, sex = ?, title = ?, department = ?, email = ? " +
        "WHERE teacher_id = ?";
    
    private static final String DELETE_SQL = 
        "DELETE FROM teachers WHERE teacher_id = ?";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT * FROM teachers WHERE teacher_id = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT * FROM teachers";
    
    /**
     * Initialize the database by creating the teachers table if it doesn't exist
     */
    public void initializeDatabase() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        }
    }
    
    /**
     * Add a new teacher to the database
     */
    public boolean addTeacher(Teacher teacher) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            
            pstmt.setString(1, teacher.getTeacherId());
            pstmt.setString(2, teacher.getName());
            pstmt.setString(3, teacher.getSex());
            pstmt.setString(4, teacher.getTitle());
            pstmt.setString(5, teacher.getDepartment());
            pstmt.setString(6, teacher.getEmail());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding teacher: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing teacher in the database
     */
    public boolean updateTeacher(Teacher teacher) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, teacher.getName());
            pstmt.setString(2, teacher.getSex());
            pstmt.setString(3, teacher.getTitle());
            pstmt.setString(4, teacher.getDepartment());
            pstmt.setString(5, teacher.getEmail());
            pstmt.setString(6, teacher.getTeacherId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating teacher: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a teacher from the database
     */
    public boolean deleteTeacher(String teacherId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setString(1, teacherId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting teacher: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a teacher by ID
     */
    public Teacher getTeacherById(String teacherId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setString(1, teacherId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractTeacherFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting teacher by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all teachers from the database
     */
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                Teacher teacher = extractTeacherFromResultSet(rs);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all teachers: " + e.getMessage());
        }
        
        return teachers;
    }
    
    /**
     * Extract a Teacher object from a ResultSet
     */
    private Teacher extractTeacherFromResultSet(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(rs.getString("teacher_id"));
        teacher.setName(rs.getString("name"));
        teacher.setSex(rs.getString("sex"));
        teacher.setTitle(rs.getString("title"));
        teacher.setDepartment(rs.getString("department"));
        teacher.setEmail(rs.getString("email"));
        return teacher;
    }
}