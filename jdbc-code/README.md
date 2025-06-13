Step 1: Initialize the mysql database.

create a  database named 'studentdb' on localhost, change the root password in file 'DatabaseConnection.java'

Step 2: Compile java file

javac studentmanagementsystem/*.java

Step 3: Run main file

java 1.8

java -classpath "mysql-connector-j-9.3.0.jar;." studentmanagementsystem.StudentManagementSystem

java 11+
java -p "mysql-connector-j-9.3.0.jar" studentmanagementsystem.StudentManagementSystem