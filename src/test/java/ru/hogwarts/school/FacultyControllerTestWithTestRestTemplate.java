package ru.hogwarts.school;







import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestWithTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate; // Тут мы получаем зависимость через Autowired

    @Test
    void testGetFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/1", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Гриффиндор"); // Уточнить по данным
    }

    @Test
    void testCreateFaculty() {
        Faculty newFaculty = new Faculty(null, "Слизерин", "#00A5E0");
        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", newFaculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Слизерин");
    }

    @Test
    void testEditFaculty() {
        Faculty updatedFaculty = new Faculty(1L, "Хаффлпафф", "#FFFF00");
        ResponseEntity<Faculty> response = restTemplate.exchange("/faculty", org.springframework.http.HttpMethod.PUT, null, Faculty.class, updatedFaculty);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Хаффлпафф");
    }

    @Test
    void testSearchFaculties() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("/faculty/search?q=Грифф", Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThan(0); // Ожидание наличия результата
    }
}