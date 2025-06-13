package studentmanagementsystem;

/**
 * Student model class representing a student entity in the database
 */
public class Student {
    private String studentId;
    private String name;
    private String sex;
    private String birthday;
    private String major;
    private String college;

    // Default constructor
    public Student() {
    }

    // Parameterized constructor
    public Student(String studentId, String name, String sex, String birthday, String major, String college) {
        this.studentId = studentId;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.major = major;
        this.college = college;
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", major='" + major + '\'' +
                ", college='" + college + '\'' +
                '}';
    }
}
