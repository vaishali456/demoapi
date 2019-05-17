package com.tatacommunications.cf.demoservice.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tatacommunications.cf.demoservice.model.Course;

// This will be AUTO IMPLEMENTED by Spring into a Bean called courseRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

	@Query("select c from Course c where c.name = ?1")
    List<Course> findCourseByName(String cName);
}