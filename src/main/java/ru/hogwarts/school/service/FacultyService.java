package ru.hogwarts.school.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    // Создание преподавателя
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    // Поиск преподавателя по id
    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    // Редактирование преподавателя
    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    // Удаление преподавателя
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    // Поиск преподавателей по цвету
    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    // Получение всех преподавателей
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
}