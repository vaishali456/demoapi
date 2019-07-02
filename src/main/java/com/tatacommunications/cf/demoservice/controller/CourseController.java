package com.tatacommunications.cf.demoservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tatacommunications.cf.demoservice.component.CourseService;
import com.tatacommunications.cf.demoservice.data.CourseRepository;
import com.tatacommunications.cf.demoservice.exception.ResourceNotFoundException;
import com.tatacommunications.cf.demoservice.model.Course;

@RestController
@RequestMapping(path = "/pocapp/courses")
public class CourseController {

	Logger logger = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	private CourseService courseService;

	@Autowired
	private CourseRepository courseRepository;

	// Get All courses
	@CrossOrigin(origins = "http://demouiapp1")
	@GetMapping
	public ResponseEntity<?> getAllCourses() {
		logger.info("GET Request started for /pocapp/courses to get all courses");
		List<Course> courses = courseRepository.findAll();
		if (courses == null || courses.isEmpty()) {
			logger.error("No Course registered in DB");
			throw new ResourceNotFoundException("No Course exists in the system");
		}
		return ResponseEntity.ok(courses);
	}

	// Get course by id
	@GetMapping("/{cId}")
	public ResponseEntity<?> getCourseById(@PathVariable(value = "cId") String cId) {
		logger.info("GET Request started for /pocapp/courses/{} to get course by ID", cId);
		Course course = courseRepository.findById(cId).orElse(null);
		if (course == null) {
			logger.error("Course not found for id: {}", cId);
			throw new ResourceNotFoundException("Course not found for id: " + cId);
		}
		return ResponseEntity.ok(course);
	}

	@RequestMapping(method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> createCourse(@Valid @RequestBody Course course) {
		logger.info("POST Request started for /pocapp/courses to craete new course: {}", course);
		if (courseService.isAlreadyExists(course)) {
			logger.error("Course already exists with course name: {}", course.getName());
			throw new ResourceNotFoundException("Course already exists with course name: " + course.getName());
		}
		Course courseCreated = courseRepository.save(course);
		logger.info("New Course created successfully. new course Id: {}", courseCreated.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(courseCreated);
	}

	@PutMapping(value = "/{cId}")
	public ResponseEntity<?> updateCourse(@PathVariable(value = "cId") String cId, @Valid @RequestBody Course course) {
		logger.info("PUT Request started for /pocapp/courses/{} to update course: {}", cId, course);
		Course cExisting = courseRepository.findById(cId).orElse(null);
		if (cExisting == null) {
			logger.error("Course not found for course Id: {}", cId);
			throw new ResourceNotFoundException("Course not found for id: " + cId);
		}
		cExisting.setName(course.getName());
		cExisting.setDescription(course.getDescription());

		Course updatedCourse = courseRepository.save(cExisting);
		logger.info("Course updated successfully. course Id: {}", updatedCourse.getId());
		return ResponseEntity.ok(updatedCourse);
	}

	@DeleteMapping(value = "/{cId}")
	public ResponseEntity<?> deleteCourse(@PathVariable(value = "cId") String cId) {
		logger.info("DELETE Request started for /pocapp/courses/{} to delete course", cId);
		Course cExisting = courseRepository.findById(cId).orElse(null);
		if (cExisting == null) {
			logger.error("Course not found for course Id: {}", cId);
			throw new ResourceNotFoundException("Course not found for id: " + cId);
		}
		if (!cExisting.getStudents().isEmpty()) {
			logger.error("Course is assigned to one or more students & can't be delted. Remove this course from all students to delete it. course Id: {}", cId);
			throw new ResourceNotFoundException("Course is assigned to one or more students & can't be delted. Remove this course from all students to delete it. id: " + cId);
		}
		courseRepository.delete(cExisting);
		logger.info("Course deleted successfully. course Id: {}", cId);
		return ResponseEntity.ok().build();
	}

}
