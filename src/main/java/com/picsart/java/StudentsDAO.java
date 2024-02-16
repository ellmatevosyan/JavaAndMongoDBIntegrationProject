package com.picsart.java;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class StudentsDAO {
    private final MongoCollection<Document> studentsCollection;
    private final MongoCollection<Document> coursesCollection;

    public StudentsDAO(MongoDatabase database) {
        this.studentsCollection = database.getCollection("Students");
        this.coursesCollection = database.getCollection("Courses");
    }

    //Create operation
    public void addStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the student");
        String name = scanner.nextLine();
        System.out.println("Enter the age of the student");
        int age = scanner.nextInt();
        scanner.nextLine();

        //Fetch available courses from the "Courses" collection
        List<Document> courses = coursesCollection.find().into(new ArrayList<>());

        System.out.println("Available courses: ");
        for (Document course : courses) {
            System.out.println(course.get("_id") + ": " + course.get("name"));
        }

        //Prompt user to enter enrolled course IDs
        List<ObjectId> enrolledCourses = new ArrayList<>();
        boolean exit = false;
        while (!exit) {
            System.out.println("Enter the ID of the course to enroll in (enter 'done' to finish )");
            String courseIdInput = scanner.nextLine();
            if (courseIdInput.equalsIgnoreCase("done")) {
                exit = true;
            } else {
                ObjectId courseId = new ObjectId(courseIdInput);
                if (coursesCollection.find(new Document("_id", courseId)).first() != null) {
                    enrolledCourses.add(courseId);
                } else {
                    System.out.println("Course with ID " + courseId + " not found. Please enter a valid course ID.");
                }
            }
        }

        //Insert the student document with enrolled courses
        Document studentDocument = new Document();
        studentDocument.put("name", name);
        studentDocument.put("age", age);
        studentDocument.put("enrolledCourses", enrolledCourses);
        studentsCollection.insertOne(studentDocument);
        System.out.println("Student added successfully!");
    }


    public List<Document> findCourseByStudent() {
        Scanner scanner = new Scanner(System.in);
//        FindIterable<Document> students = studentsCollection.find().projection(new Document("_id", 1).append("name", 1));
//        System.out.println("Student IDs and Names:");
//        for (Document st : students) {
//            String studentId = st.getString("_id");
//            String studentName = st.getString("name");
//            System.out.println("ID: " + studentId + ", Name: " + studentName);
//        }
            System.out.println("Enter student ID to see in which courses he is enrolled");
            String studentIdInput = scanner.nextLine();
            List<Document> studentCourses = new ArrayList<>();

            Document student = studentsCollection.find(new Document("_id", studentIdInput)).first();
            if (student != null) {
                List<ObjectId> enrolledCourseIds = student.getList("enrolledCourses", ObjectId.class);
                for (ObjectId courseId : enrolledCourseIds) {
                    Document course = coursesCollection.find(new Document("_id", courseId)).first();
                    if (course != null) {
                        studentCourses.add(course);
                    }
                }
            } else {
                System.out.println("Student with ID " + studentIdInput + " not found.");
            }
            return studentCourses;
        }
    public void updateStudentByCourse() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Enter the student ID you want to update:");
            String studentId = scanner.nextLine();
            Document student = studentsCollection.find(new Document("id", studentId)).first();

            if (student != null) {
                List<ObjectId> enrolledCourses = (List<ObjectId>) student.get("enrolledCourses");
                if (enrolledCourses != null) {
                    System.out.println("Courses in which the student is enrolled: " + enrolledCourses);
                }

                // Prompt user to add a new course
                System.out.println("Enter the ID of the course you want to add:");
                int newCourseId = scanner.nextInt();

                // Add the new course to the enrolled courses list
                if (enrolledCourses == null) {
                    enrolledCourses = new ArrayList<>();
                }
                enrolledCourses.add(new ObjectId(String.valueOf(newCourseId)));

                // Update the student document in the database with the new enrolled courses list
                studentsCollection.updateOne(new Document("id", studentId), new Document("$set", new Document("enrolledCourses", enrolledCourses)));

                System.out.println("New course added to the student's enrolled courses.");
            } else {
                System.out.println("Student with ID " + studentId + " not found.");
            }

            System.out.println("Do you want to update another student? (yes/no)");
            String choice = scanner.next().toLowerCase();
            if (!choice.equals("yes")) {
                exit = true;
            }
        }
    }

    public void deleteStudent(){
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit){
            System.out.println("Enter the student id you want to delete.");
            String studentId = scanner.nextLine();
            Document student = studentsCollection.find(new Document("_id",studentId)).first();

            if(student != null){
                studentsCollection.deleteOne(new Document("_id",studentId));
                System.out.println("Student with ID " + studentId + " has been deleted.");
            }else {
                System.out.println("Student with ID " + studentId + " not found.");
            }
            System.out.println("Do you want to delete another student? (yes/no)");
            String choice = scanner.nextLine().toLowerCase();
            if (!choice.equals("yes")) {
                exit = true;
            }

        }
    }



}
