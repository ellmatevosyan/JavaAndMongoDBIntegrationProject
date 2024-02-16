package com.picsart.java;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("SchoolDB");

        for (String s : mongoClient.listDatabaseNames()) {
            System.out.println(s);
        }

        database.createCollection("Students");
        database.createCollection("Courses");

        database.listCollectionNames().forEach(System.out::println);

        CoursesDAO coursesDAO = new CoursesDAO(database);
        coursesDAO.createIndex();
        coursesDAO.addCourse("Mathematics", 1);
        coursesDAO.addCourse("Physics", 2);
        coursesDAO.addCourse("Chemistry", 3);
        coursesDAO.addCourse("Biology", 4);
        coursesDAO.addCourse("Computer Science", 5);
        coursesDAO.addCourse("History", 6);

        StudentsDAO studentsDAO = new StudentsDAO(database);


        boolean exit = false;
        Scanner scanner = new Scanner(System.in);

        while(!exit){
            System.out.println("You need to choose from this options:");
            System.out.println("1. Create new Student.");
            System.out.println("2. Find all courses according to student ID.");
            System.out.println("3. Update the courses in which the student is enrolled.");
            System.out.println("4. Delete the student.");
            System.out.println("5. Exit.");
            System.out.println("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1 -> studentsDAO.addStudent();
                case 2 -> studentsDAO.findCourseByStudent();
                case 3 -> studentsDAO.updateStudentByCourse();
                case 4 -> studentsDAO.deleteStudent();
                case 5 -> exit = true;
                default -> System.out.println("Invalid choice. You can select the numbers from 1 to 11. Please try again!");
            }


        }





    }
}