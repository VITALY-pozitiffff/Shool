package ru.hogwarts.school.controller;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {


    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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

