DROP TABLE IF EXISTS courses;
CREATE TABLE courses (
  id VARCHAR(45) NOT NULL,
  name VARCHAR(45) DEFAULT NULL,
  description VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (id));
  
DROP TABLE IF EXISTS students;
CREATE TABLE students (
  id varchar(45) NOT NULL,
  name varchar(45) DEFAULT NULL,
  description varchar(45) DEFAULT NULL,
  PRIMARY KEY (id));

DROP TABLE IF EXISTS 
CREATE TABLE courses_students (
  course_id varchar(45) NOT NULL,
  students_id varchar(45) NOT NULL
) 