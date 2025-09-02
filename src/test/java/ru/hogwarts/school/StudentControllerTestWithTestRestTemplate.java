package ru.hogwarts.school;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestWithTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetStudentInfo() {
        ResponseEntity<Student> response = restTemplate.getForEntity("/student/1", Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Гарри Поттер"); // Уточнить по данным
    }

    @Test
    void testCreateStudent() {
        Student newStudent = new Student(null, "Гермиона Грейнджер", 17);
        ResponseEntity<Student> response = restTemplate.postForEntity("/student", newStudent, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Гермиона Грейнджер");
    }

    @Test
    void testEditStudent() {
        Student updatedStudent = new Student(1L, "Рон Уизли", 19);
        ResponseEntity<Student> response = restTemplate.exchange("/student", org.springframework.http.HttpMethod.PUT, null, Student.class, updatedStudent);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Рон Уизли");
    }

    @Test
    void testFindStudentsByAge() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student?age=18", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThan(0); // Должны быть ученики возраста 18 лет
    }

    @Test
    void testFindStudentsByAgeRange() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student/by-age-range?min=17&max=19", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThan(0); // Должны быть ученики возраста от 17 до 19 лет
    }

    @Test
    void testGetFacultyForStudent() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/student/1/faculty", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString()).contains("Гриффиндор"); // Учитель Гарри принадлежит Гриффиндору
    }
}