package com.tatacommunications.cf.demoservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tatacommunications.cf.demoservice.component.StudentService;
import com.tatacommunications.cf.demoservice.data.CourseRepository;
import com.tatacommunications.cf.demoservice.data.StudentRepository;
import com.tatacommunications.cf.demoservice.exception.ResourceNotFoundException;
import com.tatacommunications.cf.demoservice.model.Course;
import com.tatacommunications.cf.demoservice.model.Student;

@RestController
@RequestMapping(path = "/pocapp/students")
public class StudentController {

	Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	// Get All students
	@GetMapping
	public ResponseEntity<?> getAllStudents() {
		logger.info("GET Request started for /pocapp/students to get all students");
		List<Student> students = studentRepository.findAll();
		if (students == null || students.isEmpty()) {
			logger.error("No Student registered in DB");
			throw new ResourceNotFoundException("No Student exists in the system");
		}
		return ResponseEntity.ok(students);
	}

	// Get student by id
	@GetMapping("/{sId}")
	public ResponseEntity<?> getStudentById(@PathVariable(value = "sId") String sId) {
		logger.info("GET Request started for /pocapp/students/{} to get student by ID", sId);
		Student student = studentRepository.findById(sId).orElse(null);
		if (student == null) {
			logger.error("Student not found for id: {}", sId);
			throw new ResourceNotFoundException("Student not found for id: " + sId);
		}
		return ResponseEntity.ok(student);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {
		logger.info("POST Request started for /pocapp/students to craete new student: {}", student);
		if (studentService.isAlreadyExists(student)) {
			logger.error("Student already exists with student name: {}", student.getName());
			throw new ResourceNotFoundException("Student already exists with student name: " + student.getName());
		}
		List<String> courseIds = student.getCourses().stream().map(c -> c.getId()).collect(Collectors.toList());
		student.getCourses().clear();
		courseIds.forEach(cId -> {
			Course c = courseRepository.findById(cId).orElse(null);
			if (c == null) {
				logger.error("Course not found for id: {}", cId);
				throw new ResourceNotFoundException("Course not found for id: " + cId);
			}
			student.getCourses().add(c);
		});
		Student studentCreated = studentRepository.save(student);
		logger.info("New Student created successfully. new student Id: {}", studentCreated.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(studentCreated);
	}

	@PutMapping(value = "/{sId}")
	public ResponseEntity<?> updateStudent(@PathVariable(value = "sId") String sId,
			@Valid @RequestBody Student student) {
		logger.info("PUT Request started for /pocapp/students/{} to update student: {}", sId, student);
		Student sExisting = studentRepository.findById(sId).orElse(null);
		if (sExisting == null) {
			logger.error("Student not found for student Id: {}", sId);
			throw new ResourceNotFoundException("Student not found for id: " + sId);
		}
		sExisting.setName(student.getName());
		sExisting.setDescription(student.getDescription());

		List<String> courseIds = student.getCourses().stream().map(c -> c.getId()).collect(Collectors.toList());
		sExisting.getCourses().clear();
		courseIds.forEach(cId -> {
			Course c = courseRepository.findById(cId).orElse(null);
			if (c == null) {
				logger.error("Course not found for id: {}", cId);
				throw new ResourceNotFoundException("Course not found for id: " + cId);
			}
			sExisting.getCourses().add(c);
		});
		
		Student updatedStudent = studentRepository.save(sExisting);
		logger.info("Student updated successfully. student Id: {}", updatedStudent.getId());
		return ResponseEntity.ok(updatedStudent);
	}

	@DeleteMapping(value = "/{sId}")
	public ResponseEntity<?> deleteStudent(@PathVariable(value = "sId") String sId) {
		logger.info("DELETE Request started for /pocapp/students/{} to delete student", sId);
		Student cExisting = studentRepository.findById(sId).orElse(null);
		if (cExisting == null) {
			logger.error("Student not found for student Id: {}", sId);
			throw new ResourceNotFoundException("Student not found for id: " + sId);
		}
		studentRepository.delete(cExisting);
		logger.info("Student deleted successfully. student Id: {}", sId);
		return ResponseEntity.ok().build();
	}

}
