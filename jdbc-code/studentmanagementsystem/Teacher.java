package studentmanagementsystem;

/**
 * Teacher model class representing a teacher entity in the database
 */
public class Teacher {
    private String teacherId;
    private String name;
    private String sex;
    private String title;
    private String department;
    private String email;

    // Default constructor
    public Teacher() {
    }

    // Parameterized constructor
    public Teacher(String teacherId, String name, String sex, String title, String department, String email) {
        this.teacherId = teacherId;
        this.name = name;
        this.sex = sex;
        this.title = title;
        this.department = department;
        this.email = email;
    }

    // Getters and setters
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", title='" + title + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}