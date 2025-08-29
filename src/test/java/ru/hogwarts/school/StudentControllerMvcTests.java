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
class StudentControllerMvcTests{

    @Autowired
    private MockMvc mvc;

    @Test
    void testGetStudentInfo() throws Exception {
        ResultActions result = mvc.perform(get("/student/1").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гарри Поттер")); // Уточнить по данным
    }

    @Test
    void testCreateStudent() throws Exception {
        ResultActions result = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Гермиона Грейнджер\", \"age\":17}"));
        result.andExpect(status().isCreated());
    }

    @Test
    void testEditStudent() throws Exception {
        ResultActions result = mvc.perform(put("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1, \"name\":\"Рон Уизли\", \"age\":19}"));
        result.andExpect(status().isOk());
    }

    @Test
    void testFindStudentsByAge() throws Exception {
        ResultActions result = mvc.perform(get("/student?age=18").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").exists()); // Ожидание наличия массива учеников
    }

    @Test
    void testFindStudentsByAgeRange() throws Exception {
        ResultActions result = mvc.perform(get("/student/by-age-range?min=17&max=19").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").exists()); // Ожидание наличия массива учеников
    }

    @Test
    void testGetFacultyForStudent() throws Exception {
        ResultActions result = mvc.perform(get("/student/1/faculty").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор")); // Уточнить по данным
    }
}