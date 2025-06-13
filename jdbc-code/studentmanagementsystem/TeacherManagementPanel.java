package studentmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherManagementPanel extends JPanel {
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, sexField, titleField, departmentField, emailField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JTextField searchField;
    private JButton searchButton;
    
    private TeacherDAO teacherDAO;
    
    public TeacherManagementPanel() {
        teacherDAO = new TeacherDAO();
        try {
            teacherDAO.initializeDatabase();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Failed to initialize teacher database: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        
        loadTeacherData();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Teacher Information"));
        
        // Teacher ID
        panel.add(new JLabel("Teacher ID:"));
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
        
        // Title
        panel.add(new JLabel("Title:"));
        titleField = new JTextField(20);
        panel.add(titleField);
        
        // Department
        panel.add(new JLabel("Department:"));
        departmentField = new JTextField(20);
        panel.add(departmentField);
        
        // Email
        panel.add(new JLabel("Email:"));
        emailField = new JTextField(20);
        panel.add(emailField);
        
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Teacher List"));
        
        // Create the search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by ID:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchTeacher());
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Create the table
        String[] columnNames = {"Teacher ID", "Name", "Sex", "Title", "Department", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };
        teacherTable = new JTable(tableModel);
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teacherTable.getTableHeader().setReorderingAllowed(false);
        
        // Add selection listener to the table
        teacherTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = teacherTable.getSelectedRow();
                if (selectedRow != -1) {
                    displayTeacherData(selectedRow);
                }
            }
        });
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Add button
        addButton = new JButton("Add");
        addButton.addActionListener(e -> addTeacher());
        panel.add(addButton);
        
        // Update button
        updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateTeacher());
        panel.add(updateButton);
        
        // Delete button
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteTeacher());
        panel.add(deleteButton);
        
        // Clear button
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());
        panel.add(clearButton);
        
        return panel;
    }

    private void loadTeacherData() {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all teachers from the database
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        
        // Add each teacher to the table
        for (Teacher teacher : teachers) {
            Object[] rowData = {
                teacher.getTeacherId(),
                teacher.getName(),
                teacher.getSex(),
                teacher.getTitle(),
                teacher.getDepartment(),
                teacher.getEmail()
            };
            tableModel.addRow(rowData);
        }
        
        // Clear the form
        clearForm();
    }

    // Helper methods
    private void displayTeacherData(int row) {
        idField.setText((String) tableModel.getValueAt(row, 0));
        nameField.setText((String) tableModel.getValueAt(row, 1));
        sexField.setText((String) tableModel.getValueAt(row, 2));
        titleField.setText((String) tableModel.getValueAt(row, 3));
        departmentField.setText((String) tableModel.getValueAt(row, 4));
        emailField.setText((String) tableModel.getValueAt(row, 5));
        
        // Disable the ID field when updating
        idField.setEditable(false);
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        sexField.setText("");
        titleField.setText("");
        departmentField.setText("");
        emailField.setText("");
        
        // Enable the ID field for adding new teachers
        idField.setEditable(true);
        
        // Clear the table selection
        teacherTable.clearSelection();
    }

    private void addTeacher() {
        if (!validateInput()) {
            return;
        }
        
        Teacher teacher = new Teacher(
            idField.getText(),
            nameField.getText(),
            sexField.getText(),
            titleField.getText(),
            departmentField.getText(),
            emailField.getText()
        );
        
        boolean success = teacherDAO.addTeacher(teacher);
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Teacher added successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            loadTeacherData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to add teacher. The teacher ID may already exist.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a teacher to update.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) {
            return;
        }
        
        Teacher teacher = new Teacher(
            idField.getText(),
            nameField.getText(),
            sexField.getText(),
            titleField.getText(),
            departmentField.getText(),
            emailField.getText()
        );
        
        boolean success = teacherDAO.updateTeacher(teacher);
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Teacher updated successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            loadTeacherData();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to update teacher.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a teacher to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String teacherId = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this teacher?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = teacherDAO.deleteTeacher(teacherId);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Teacher deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadTeacherData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete teacher.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchTeacher() {
        String teacherId = searchField.getText().trim();
        
        if (teacherId.isEmpty()) {
            loadTeacherData();
            return;
        }
        
        Teacher teacher = teacherDAO.getTeacherById(teacherId);
        if (teacher != null) {
            tableModel.setRowCount(0);
            Object[] rowData = {
                teacher.getTeacherId(),
                teacher.getName(),
                teacher.getSex(),
                teacher.getTitle(),
                teacher.getDepartment(),
                teacher.getEmail()
            };
            tableModel.addRow(rowData);
            teacherTable.setRowSelectionInterval(0, 0);
            displayTeacherData(0);
        } else {
            JOptionPane.showMessageDialog(this,
                "Teacher not found.",
                "Not Found",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        }
    }

    private boolean validateInput() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Teacher ID is required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            idField.requestFocus();
            return false;
        }
        
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
}