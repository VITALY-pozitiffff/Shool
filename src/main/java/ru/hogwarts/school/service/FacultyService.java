package ru.hogwarts.school.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;


    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }


    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }


    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }


    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColor(color);
    }


    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
    public Collection<Faculty> searchFaculties(String keyword) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(keyword, keyword);
    }
    public List<Student> getStudentsFromFaculty(Long facultyId) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(facultyId);
        if (!optionalFaculty.isPresent()) {
            throw new EntityNotFoundException("Факультет не найден.");
        }
        return new ArrayList<>(optionalFaculty.get().getStudents()); // преобразуем Set в List
    }
}