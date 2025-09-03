package ru.hogwarts.school;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestWithTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    @Transactional
    void clearDatabase() {
        facultyRepository.deleteAll();
    }

    @Test
    void testGetFaculty() {
        // Создадим факультет для теста
        Faculty gryffindor = new Faculty(null, "Гриффиндор", "#FF0000");
        facultyRepository.save(gryffindor);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + gryffindor.getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Гриффиндор");
    }

    @Test
    void testCreateFaculty() {
        Faculty slytherin = new Faculty(null, "Слизерин", "#00A5E0");
        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", slytherin, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Слизерин");
    }

    @Test
    void testEditFaculty() {
        Faculty hufflepuff = new Faculty(1L, "Хаффлпафф", "#FFFF00");
        ResponseEntity<Faculty> response = restTemplate.exchange("/faculty", org.springframework.http.HttpMethod.PUT, null, Faculty.class, hufflepuff);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Хаффлпафф");
    }

    @Test
    void testSearchFaculties() {
        // Отправляем запрос и ждем ответ в виде списка
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/search?q=Грифф",
                Faculty.class
        );
    }
}