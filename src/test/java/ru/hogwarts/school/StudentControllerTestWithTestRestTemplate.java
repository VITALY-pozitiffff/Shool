package ru.hogwarts.school;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestWithTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Гермиона Грейнджер");
    }

    @Test
    void testEditStudent() {
        Student ronWeasley = new Student(1L, "Рон Уизли", 19);
        ResponseEntity<Student> response = restTemplate.exchange("/student", org.springframework.http.HttpMethod.PUT, null, Student.class, ronWeasley);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Рон Уизли");
    }

    @Test
    void testFindStudentsByAge() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student?age=18", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThan(0); // Ждём наличие учеников
    }

    @Test
    void testFindStudentsByAgeRange() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student/by-age-range?min=17&max=19", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThan(0); // Ждём наличие учеников
    }

    @Test
    void testGetFacultyForStudent() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/student/1/faculty", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString()).contains("Гриффиндор"); // Название факультета Гарри
    }
}