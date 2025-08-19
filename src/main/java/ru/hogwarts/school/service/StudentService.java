package ru.hogwarts.school.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Создание студента
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // Поиск студента по id
    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Редактирование студента
    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    // Удаление студента
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Поиск студентов по возрасту
    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    // Получение всех студентов
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}