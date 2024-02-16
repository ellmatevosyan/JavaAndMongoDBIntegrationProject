package com.picsart.java;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

public class CoursesDAO {
    private final MongoCollection<Document> coursesCollection;

    public CoursesDAO(MongoDatabase database){
        this.coursesCollection = database.getCollection("Courses");
    }

    public void addCourse(String courseName,int courseId){
        Document existingCourse = coursesCollection.find(new Document("id",courseId)).first();
        if(existingCourse == null){
            Document courseDocument = new Document();
            courseDocument.put("name",courseName);
            courseDocument.put("id",courseId);
            coursesCollection.insertOne(courseDocument);
            System.out.println("Course added successfully.");
        }else {
            System.out.println("Course with Id " + courseId + " already exists." );
        }

    }
    public void createIndex(){
        coursesCollection.createIndex(new Document("_id",1),new IndexOptions());

    }
}
