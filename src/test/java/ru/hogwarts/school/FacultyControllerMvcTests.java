package ru.hogwarts.school;




import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

    @Test
    void testGetFaculty() throws Exception {
        ResultActions result = mvc.perform(get("/faculty/1").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор")); // Уточнить по данным
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
                .andExpect(jsonPath("$[*].name").exists()); // Ожидание наличия массива факультетов
    }
}