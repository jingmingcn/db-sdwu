import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class Student {
    String studentNum;
    String name;
    String sex;
    String birthday;
    String major;
    String college;

    public Student(String studentNum, String name, String sex, String birthday, String major, String college) {
        this.studentNum = studentNum;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.major = major;
        this.college = college;
    }

    public void displayInfo() {
        System.out.println("Student Number: " + studentNum);
        System.out.println("Name: " + name);
        System.out.println("Sex: " + sex);
        System.out.println("Birthday: " + birthday);
        System.out.println("Major: " + major);
        System.out.println("College: " + college);
        System.out.println("---------------------------");
    }
}

public class StudentManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Student");
            System.out.println("2. Search Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    searchStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    displayAllStudents();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Student Number: ");
        String studentNum = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Sex: ");
        String sex = scanner.nextLine();
        System.out.print("Birthday (YYYY-MM-DD): ");
        String birthday = scanner.nextLine();
        System.out.print("Major: ");
        String major = scanner.nextLine();
        System.out.print("College: ");
        String college = scanner.nextLine();

        students.add(new Student(studentNum, name, sex, birthday, major, college));
        System.out.println("Student added successfully.");
    }

    private static void searchStudent() {
        System.out.print("Enter Student Number to search: ");
        String studentNum = scanner.nextLine();
        for (Student student : students) {
            if (student.studentNum.equals(studentNum)) {
                student.displayInfo();
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void deleteStudent() {
        System.out.print("Enter Student Number to delete: ");
        String studentNum = scanner.nextLine();
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student student = iterator.next();
            if (student.studentNum.equals(studentNum)) {
                iterator.remove();
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("\nStudent Information:");
            for (Student student : students) {
                student.displayInfo();
            }
        }
    }
}