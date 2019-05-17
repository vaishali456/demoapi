package com.tatacommunications.cf.demoservice.component;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatacommunications.cf.demoservice.data.StudentRepository;
import com.tatacommunications.cf.demoservice.model.Student;

@Service
public class StudentService {

	Logger logger = LoggerFactory.getLogger(CourseService.class);

	@Autowired
	private StudentRepository studentRepository;

	public boolean isAlreadyExists(Student student) {
		List<Student> studentsByName = (List<Student>) studentRepository.findStudentByName(student.getName());
		logger.info("Student with name: {} already exists: ", student.getName(), !(studentsByName == null || studentsByName.isEmpty()));
		return !(studentsByName == null || studentsByName.isEmpty());
	}
}