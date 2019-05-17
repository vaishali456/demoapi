package com.tatacommunications.cf.demoservice.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tatacommunications.cf.demoservice.model.Student;

// This will be AUTO IMPLEMENTED by Spring into a Bean called studentRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

	@Query("select s from Student s where s.name = ?1")
	List<Student> findStudentByName(String sName);
}