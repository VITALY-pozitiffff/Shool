package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;

import java.util.*;

import ru.hogwarts.school.model.Faculty;import ru.hogwarts.school.model.Student;import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class); // добавляем логгер

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Invoked method addFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.debug("Invoked method findFaculty");
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Invoked method editFaculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.warn("Deleting faculty with ID {}", id); // предупреждение о удалении факультета
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findByColor(String color) {
        logger.debug("Invoked method findByColor");
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> getAllFaculties() {
        logger.debug("Invoked method getAllFaculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> searchFaculties(String keyword) {
        logger.debug("Invoked method searchFaculties");
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(keyword, keyword);
    }

    public List<Student> getStudentsFromFaculty(Long facultyId) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(facultyId);
        if (!optionalFaculty.isPresent()) {
            logger.error("Faculty not found with ID {}", facultyId); // ошибка о факультете
            throw new EntityNotFoundException("Факультет не найден.");
        }
        return new ArrayList<>(optionalFaculty.get().getStudents());
    }
}