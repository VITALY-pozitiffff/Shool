package ru.hogwarts.school.controller;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/student")
public class StudentController {


    private final StudentService studentService;


    @Autowired
    private StudentRepository studentRepository;

    private final Object lock = new Object();


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/print-parallel")
    public void printParallel() {
        studentService.printStudentsInParallel();
    }

    private void printSynchronized(String name) {
        synchronized (lock) {
            System.out.println(name);
        }
    }
    @GetMapping("/print-synchronized")
    public void printSynchronized() {
        studentService.printStudentsSynchronized();
    }





    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> lastFiveStudents() {
        List<Student> students = studentService.lastFiveStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> averageStudentAge() {
        Double avgAge = studentService.averageStudentAge();
        return ResponseEntity.ok(avgAge);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> totalStudentCount() {
        Long count = studentService.countTotalStudents();
        return ResponseEntity.ok(count);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/names-start-with-a")
    public ResponseEntity<List<String>> getNamesStartingWithA() {
        List<String> names = studentRepository.findAll()
                .stream()
                .map(Student::getName)          // Получаем имена студентов
                .filter(name -> name.toUpperCase().startsWith("A")) // Фильтруем имена
                .sorted()                       // Сортируем по алфавиту
                .collect(Collectors.toList());  // Собираем в список

        return ResponseEntity.ok(names);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        double averageAge = studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)      // Преобразовываем потоки в числа (возрасты)
                .average()                      // Вычисляем среднее
                .orElse(Double.NaN);           // Или выдаём NaN, если пусто

        return ResponseEntity.ok(averageAge);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@Valid @RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) Integer age) {
        if (age != null && age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/by-age-range")
    public ResponseEntity<Collection<Student>> findStudentsByAgeRange(
            @RequestParam("min") int min,
            @RequestParam("max") int max) {

        if (min >= max) { // Проверяем корректность границ
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        return ResponseEntity.ok(studentService.findByAgeRange(min, max));
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<?> getFacultyForStudent(@PathVariable Long id) {
        try {
            Faculty faculty = studentService.getFacultyForStudent(id);
            return ResponseEntity.ok(faculty);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}

