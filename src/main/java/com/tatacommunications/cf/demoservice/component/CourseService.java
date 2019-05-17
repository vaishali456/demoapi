package com.tatacommunications.cf.demoservice.component;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatacommunications.cf.demoservice.data.CourseRepository;
import com.tatacommunications.cf.demoservice.model.Course;

@Service
public class CourseService {

	Logger logger = LoggerFactory.getLogger(CourseService.class);

	@Autowired
	private CourseRepository courseRepository;

	public boolean isAlreadyExists(Course course) {
		logger.debug("Checking if course with name: {} already exists in system", course.getName());
		List<Course> coursesByName = (List<Course>) courseRepository.findCourseByName(course.getName());
		logger.info("Course with name: {} already exists: {}", course.getName(), !(coursesByName == null || coursesByName.isEmpty()));
		return !(coursesByName == null || coursesByName.isEmpty());
	}
}