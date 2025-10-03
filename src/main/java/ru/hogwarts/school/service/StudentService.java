package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

@Service


public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class); // Добавляем логгер
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Long countTotalStudents() {
        logger.debug("Invoked method countTotalStudents"); // Debug-level log
        return studentRepository.countTotalStudents();
    }

    public Double averageStudentAge() {
        logger.debug("Invoked method averageStudentAge"); // Debug-level log
        return studentRepository.averageStudentAge();
    }

    public List<Student> lastFiveStudents() {
        logger.debug("Invoked method lastFiveStudents"); // Debug-level log
        return studentRepository.findLastFiveStudents();
    }

    public Student addStudent(Student student) {
        logger.info("Invoked method addStudent"); // Info-level log
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.debug("Invoked method findStudent with id={}", id); // Debug-level log
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        logger.info("Invoked method editStudent"); // Info-level log
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.warn("Deleting student with id={}", id); // Warning-level log
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        logger.debug("Invoked method findByAge with age={}", age); // Debug-level log
        return studentRepository.findByAge(age);
    }

    public List<Student> getAllStudents() {
        logger.debug("Invoked method getAllStudents"); // Debug-level log
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeRange(int min, int max) {
        logger.debug("Invoked method findByAgeRange with range {}-{}", min, max); // Debug-level log
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyForStudent(Long studentId) {
        logger.debug("Invoked method getFacultyForStudent with studentId={}", studentId); // Debug-level log

        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            logger.error("No student found with id={}", studentId); // Error-level log
            return new EntityNotFoundException("Студенческий профиль не найден.");
        });
        return student.getFaculty();
    }


    public void printStudentsInParallel() {
        var students = studentRepository.findAll();
        checkStudentsSize(students);

        // Первую пару студентов выведем сразу в основном потоке
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        // Вывод третьей пары студентов в отдельном потоке
        CompletableFuture.runAsync(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        // Четвертый поток выводит ещё одну пару студентов
        CompletableFuture.runAsync(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });
    }


    public void printStudentsSynchronized() {
        var students = studentRepository.findAll();
        checkStudentsSize(students);

        // Блокировка для предотвращения конфликтов при доступе к ресурсам
        var lock = new Object();

        // Первая пара студентов выводится в основном потоке
        synchronized (lock) {
            System.out.println(students.get(0).getName());
            System.out.println(students.get(1).getName());
        }

        // Во втором потоке вторая пара студентов выводится синхронизированно
        CompletableFuture.runAsync(() -> {
            synchronized (lock) {
                System.out.println(students.get(2).getName());
                System.out.println(students.get(3).getName());
            }
        });

        // Третий поток выводит третью группу студентов
        CompletableFuture.runAsync(() -> {
            synchronized (lock) {
                System.out.println(students.get(4).getName());
                System.out.println(students.get(5).getName());
            }
        });
    }


    private void checkStudentsSize(List<Student> students) {
        if (students.size() < 6) {
            throw new IllegalArgumentException("Необходимо минимум 6 студентов для отображения.");
        }
    }
}
