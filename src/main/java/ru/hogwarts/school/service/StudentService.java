package ru.hogwarts.school.service;


import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service

public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // 1. Поиск студентов по возрасту
    public Collection<Student> findByAge(Integer age) {
        return studentRepository.findByAge(age);
    }

    // 2. Поиск студентов по диапазону возраста
    public Collection<Student> findByAgeRange(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    // 3. Получение среднего возраста студентов
    public Double getAverageAge() {
        return studentRepository.findAll()
                .stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(Double.NaN);
    }

    // 4. Получение общего количества студентов
    public Long countTotalStudents() {
        return studentRepository.count();
    }

    // 5. Получение последних пяти студентов
    public List<Student> lastFiveStudents() {
        return studentRepository.findTop5ByOrderByIdDesc();
    }

    // 6. Поиск студентов, чьи имена начинаются с буквы A
    public List<String> getNamesStartingWithA() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    // 7. Получение студента по ID
    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // 8. Добавление нового студента
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // 9. Редактирование студента
    public Student editStudent(Student updatedStudent) {
        return studentRepository.save(updatedStudent);
    }

    // 10. Поиск факультета по студенту
    public Faculty getFacultyForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
        return student.getFaculty();
    }

    // Дополнительно: параллельное и синхронизированное представление студентов
    public void printStudentsInParallel() {
        List<Student> students = studentRepository.findAll();
        if (students.size() < 6) {
            throw new IllegalStateException("Нужно минимум 6 студентов для демонстрации.");
        }

        // Параллельная печать первых двух студентов
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        // Параллельность через CompletableFuture
        CompletableFuture.runAsync(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        CompletableFuture.runAsync(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });
    }

    public void printStudentsSynchronized() {
        List<Student> students = studentRepository.findAll();
        if (students.size() < 6) {
            throw new IllegalStateException("Нужно минимум 6 студентов для демонстрации.");
        }

        // Общие объекты для синхронизации
        Object lock = new Object();

        // Синхронизированная печать первой пары студентов
        synchronized (lock) {
            System.out.println(students.get(0).getName());
            System.out.println(students.get(1).getName());
        }

        // Синхронизированные потоки для вывода следующей пары студентов
        CompletableFuture.runAsync(() -> {
            synchronized (lock) {
                System.out.println(students.get(2).getName());
                System.out.println(students.get(3).getName());
            }
        });

        CompletableFuture.runAsync(() -> {
            synchronized (lock) {
                System.out.println(students.get(4).getName());
                System.out.println(students.get(5).getName());
            }
        });
    }
}