package ru.hogwarts.school.service;

import java.util.Collection;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;import org.springframework.stereotype.Service;import ru.hogwarts.school.model.Faculty;import ru.hogwarts.school.model.Student;import ru.hogwarts.school.repository.StudentRepository;

@Service
public class StudentService {


    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeRange(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyForStudent(Long studentId) {


        Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("Студенческий профиль не найден."));
        return student.getFaculty();
    }}