package ru.hogwarts.school;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FacultyControllerMvcTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    @Transactional
    void clearDatabase() {
        facultyRepository.deleteAll(); // Чистка перед каждым тестом
    }

    @Test
    void testGetFaculty() throws Exception {
        // Создадим факультет для теста
        Faculty gryffindor = new Faculty(null, "Гриффиндор", "#FF0000");
        facultyRepository.save(gryffindor);

        ResultActions result = mvc.perform(get("/faculty/" + gryffindor.getId()).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"));
    }

    @Test
    void testCreateFaculty() throws Exception {
        ResultActions result = mvc.perform(post("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Слизерин\", \"color\":\"#00A5E0\"}"));
        result.andExpect(status().isCreated());
    }

    @Test
    void testEditFaculty() throws Exception {
        ResultActions result = mvc.perform(put("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1, \"name\":\"Хаффлпафф\", \"color\":\"#FFFF00\"}"));
        result.andExpect(status().isOk());
    }

    @Test
    void testSearchFaculties() throws Exception {
        ResultActions result = mvc.perform(get("/faculty/search?q=Грифф").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").exists()); // Ожидание массива факультетов
    }
}