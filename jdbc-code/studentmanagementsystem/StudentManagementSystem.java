package studentmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Student Management System
 * A Swing-based application for managing student data in a MySQL database
 */
public class StudentManagementSystem extends JFrame {
    
    // UI Components
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, sexField, birthdayField, majorField, collegeField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JTextField searchField;
    private JButton searchButton;
    
    // Data Access Object
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    
    /**
     * Constructor
     */
    public StudentManagementSystem() {
        // Initialize the DAO
        studentDAO = new StudentDAO();
        teacherDAO = new TeacherDAO();
        
        // Initialize the database
        try {
            studentDAO.initializeDatabase();
            teacherDAO.initializeDatabase();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Failed to initialize database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Set up the UI
        initializeUI();
        
        // Load student data
        loadStudentData();
    }
    
    /**
     * Initialize the user interface
     */
    private void initializeUI() {
        // Set frame properties
        setTitle("Academic Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create tab pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Create student panel
        JPanel studentPanel = new JPanel(new BorderLayout(10, 10));
        studentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add components to student panel
        JPanel studentFormPanel = createFormPanel();
        studentPanel.add(studentFormPanel, BorderLayout.NORTH);
        
        JPanel studentTablePanel = createTablePanel();
        studentPanel.add(studentTablePanel, BorderLayout.CENTER);
        
        JPanel studentButtonPanel = createButtonPanel();
        studentPanel.add(studentButtonPanel, BorderLayout.SOUTH);
        
        // Create teacher panel
        JPanel teacherPanel = createTeacherPanel();
        
        // Add tabs
        tabbedPane.addTab("Students", studentPanel);
        tabbedPane.addTab("Teachers", teacherPanel);
        
        // Create the menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Add the tabbed pane to the frame
        add(tabbedPane);
    }
    
    /**
     * Create the form panel for student information
     * @return the form panel
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        
        // Student ID
        panel.add(new JLabel("Student ID:"));
        idField = new JTextField(20);
        panel.add(idField);
        
        // Name
        panel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        panel.add(nameField);
        
        // Sex
        panel.add(new JLabel("Sex:"));
        sexField = new JTextField(20);
        panel.add(sexField);
        
        // Birthday
        panel.add(new JLabel("Birthday (YYYY-MM-DD):"));
        birthdayField = new JTextField(20);
        panel.add(birthdayField);
        
        // Major
        panel.add(new JLabel("Major:"));
        majorField = new JTextField(20);
        panel.add(majorField);
        
        // College
        panel.add(new JLabel("College:"));
        collegeField = new JTextField(20);
        panel.add(collegeField);
        
        return panel;
    }
    
    /**
     * Create the table panel for displaying student data
     * @return the table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Student List"));
        
        // Create the search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by ID:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudent());
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Create the table
        String[] columnNames = {"Student ID", "Name", "Sex", "Birthday", "Major", "College"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        
        // Add selection listener to the table
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    displayStudentData(selectedRow);
                }
            }
        });
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the button panel for CRUD operations
     * @return the button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Add button
        addButton = new JButton("Add");
        addButton.addActionListener(e -> addStudent());
        panel.add(addButton);
        
        // Update button
        updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateStudent());
        panel.add(updateButton);
        
        // Delete button
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteStudent());
        panel.add(deleteButton);
        
        // Clear button
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());
        panel.add(clearButton);
        
        return panel;
    }
    
    /**
     * Create the menu bar
     * @return the menu bar
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        // Refresh menu item
        JMenuItem refreshMenuItem = new JMenuItem("Refresh");
        refreshMenuItem.addActionListener(e -> loadStudentData());
        fileMenu.add(refreshMenuItem);
        
        // Exit menu item
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> {
            DatabaseConnection.closeConnection();
            System.exit(0);
        });
        fileMenu.add(exitMenuItem);
        
        menuBar.add(fileMenu);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        
        // About menu item
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Student Management System\nVersion 1.0\n\nA simple application for managing student data.",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutMenuItem);
        
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    /**
     * Load student data from the database and display it in the table
     */
    private void loadStudentData() {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all students from the database
        List<Student> students = studentDAO.getAllStudents();
        
        // Add each student to the table
        for (Student student : students) {
            Object[] rowData = {
                student.getStudentId(),
                student.getName(),
                student.getSex(),
                student.getBirthday(),
                student.getMajor(),
                student.getCollege()
            };
            tableModel.addRow(rowData);
        }
        
        // Clear the form
        clearForm();
    }
    
    /**
     * Display the selected student's data in the form
     * @param row the selected row in the table
     */
    private void displayStudentData(int row) {
        idField.setText((String) tableModel.getValueAt(row, 0));
        nameField.setText((String) tableModel.getValueAt(row, 1));
        sexField.setText((String) tableModel.getValueAt(row, 2));
        birthdayField.setText((String) tableModel.getValueAt(row, 3));
        majorField.setText((String) tableModel.getValueAt(row, 4));
        collegeField.setText((String) tableModel.getValueAt(row, 5));
        
        // Disable the ID field when updating
        idField.setEditable(false);
    }
    
    /**
     * Clear the form fields
     */
    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        sexField.setText("");
        birthdayField.setText("");
        majorField.setText("");
        collegeField.setText("");
        
        // Enable the ID field for adding new students
        idField.setEditable(true);
        
        // Clear the table selection
        studentTable.clearSelection();
    }
    
    /**
     * Add a new student to the database
     */
    private void addStudent() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Create a new student object
        Student student = new Student(
            idField.getText(),
            nameField.getText(),
            sexField.getText(),
            birthdayField.getText(),
            majorField.getText(),
            collegeField.getText()
        );
        
        // Add the student to the database
        boolean success = studentDAO.addStudent(student);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Student added successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Reload the student data
            loadStudentData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to add student. The student ID may already exist.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update an existing student in the database
     */
    private void updateStudent() {
        // Check if a student is selected
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a student to update.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Create a student object with the updated data
        Student student = new Student(
            idField.getText(),
            nameField.getText(),
            sexField.getText(),
            birthdayField.getText(),
            majorField.getText(),
            collegeField.getText()
        );
        
        // Update the student in the database
        boolean success = studentDAO.updateStudent(student);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Student updated successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Reload the student data
            loadStudentData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to update student.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete a student from the database
     */
    private void deleteStudent() {
        // Check if a student is selected
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a student to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the student ID
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this student?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Delete the student from the database
            boolean success = studentDAO.deleteStudent(studentId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Student deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload the student data
                loadStudentData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete student.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Search for a student by ID
     */
    private void searchStudent() {
        String studentId = searchField.getText().trim();
        
        if (studentId.isEmpty()) {
            // If the search field is empty, load all students
            loadStudentData();
            return;
        }
        
        // Get the student from the database
        Student student = studentDAO.getStudentById(studentId);
        
        if (student != null) {
            // Clear the table
            tableModel.setRowCount(0);
            
            // Add the student to the table
            Object[] rowData = {
                student.getStudentId(),
                student.getName(),
                student.getSex(),
                student.getBirthday(),
                student.getMajor(),
                student.getCollege()
            };
            tableModel.addRow(rowData);
            
            // Select the row
            studentTable.setRowSelectionInterval(0, 0);
            displayStudentData(0);
        } else {
            JOptionPane.showMessageDialog(this,
                "Student not found.",
                "Not Found",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear the form
            clearForm();
        }
    }
    
    /**
     * Validate the input fields
     * @return true if all required fields are valid, false otherwise
     */
    private boolean validateInput() {
        // Check if the ID field is empty
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Student ID is required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            idField.requestFocus();
            return false;
        }
        
        // Check if the name field is empty
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Name is required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and display the application
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem app = new StudentManagementSystem();
            app.setVisible(true);
        });
    }

    /**
     * Add new method for creating teacher panel
     */
    private JPanel createTeacherPanel() {
        TeacherManagementPanel teacherPanel = new TeacherManagementPanel();
        return teacherPanel;
    }
}
