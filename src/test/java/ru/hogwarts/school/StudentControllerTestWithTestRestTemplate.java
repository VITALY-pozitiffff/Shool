package ru.hogwarts.school;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestWithTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    @Transactional
    void clearDatabase() {
        studentRepository.deleteAll();
    }

    @Test
    void testGetStudentInfo() {
        // Создадим студента для теста
        Student harryPotter = new Student(null, "Гарри Поттер", 18);
        studentRepository.save(harryPotter);

        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + harryPotter.getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Гарри Поттер");
    }

    @Test
    void testCreateStudent() {
        Student hermioneGranger = new Student(null, "Гермиона Грейнджер", 17);
        ResponseEntity<Student> response = restTemplate.postForEntity("/student", hermioneGranger, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // Изменено на OK
        assertThat(response.getBody().getName()).isEqualTo("Гермиона Грейнджер");
    }

    @Test
    void testEditStudent() {
        // Создаем и сохраняем студента
        Student ronWeasley = new Student(null, "Рон Уизли", 19);
        studentRepository.save(ronWeasley);

        // Обновляем данные студента
        Student updatedRonWeasley = new Student(ronWeasley.getId(), "Рон Уизли", 20);
        ResponseEntity<Student> response = restTemplate.exchange("/student",
                HttpMethod.PUT,
                new HttpEntity<>(updatedRonWeasley),
                Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Рон Уизли");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    void testFindStudentsByAge() {
        // Добавляем студентов в базу данных
        Student student1 = new Student(null, "Студент 18 лет", 18);
        Student student2 = new Student(null, "Другой студент 18 лет", 18);
        studentRepository.save(student1);
        studentRepository.save(student2);

        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student?age=18", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThan(0); // Проверяем, что есть студенты
    }
    @Test
    void testFindStudentsByAgeRange() {
        // Добавляем студентов в базу данных
        Student student1 = new Student(null, "Студент 17 лет", 17);
        Student student2 = new Student(null, "Студент 18 лет", 18);
        Student student3 = new Student(null, "Студент 19 лет", 19);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);

        // Получаем студентов в возрастном диапазоне
        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student/by-age-range?min=17&max=19", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThan(0); // Проверяем, что есть студенты
    }

    @Test
    void testGetFacultyForStudent() {
        // Создаем и сохраняем факультет
        Faculty gryffindor = new Faculty(null, "Гриффиндор", "#FF0000");
        facultyRepository.save(gryffindor);

        // Создаем и сохраняем студента с факультетом
        Student harryPotter = new Student(null, "Гарри Поттер", 18);
        harryPotter.setFaculty(gryffindor);
        studentRepository.save(harryPotter);

        // Получаем факультет для студента
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/student/" + harryPotter.getId() + "/faculty", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Гриффиндор");
    }
}