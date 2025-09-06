
package ru.hogwarts.school;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
class FacultyControllerMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FacultyService facultyService;

    @BeforeEach
    void setup() {
        // Базовые настройки для сервиса
        given(facultyService.findFaculty(anyLong())).willReturn(new Faculty(1L, "Гриффиндор", "#FF0000"));
    }

    @Test
    void testGetFaculty() throws Exception {
        ResultActions result = mvc.perform(get("/faculty/1")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("#FF0000"));
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty inputFaculty = new Faculty(null, "Слизерин", "#00A5E0");
        Faculty outputFaculty = new Faculty(1L, "Слизерин", "#00A5E0");

        given(facultyService.addFaculty(inputFaculty)).willReturn(outputFaculty);

        ResultActions result = mvc.perform(post("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Слизерин\", \"color\":\"#00A5E0\"}"));

        result.andExpect(status().isOk());
    }

    @Test
    void testEditFaculty() throws Exception {
        Faculty inputFaculty = new Faculty(1L, "Хаффлпафф", "#FFFF00");
        Faculty outputFaculty = new Faculty(1L, "Хаффлпафф", "#FFFF00");

        given(facultyService.editFaculty(inputFaculty)).willReturn(outputFaculty);

        ResultActions result = mvc.perform(put("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1, \"name\":\"Хаффлпафф\", \"color\":\"#FFFF00\"}"));

        result.andExpect(status().isOk());
    }

    @Test
    void testSearchFaculties() throws Exception {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(1L, "Гриффиндор", "#FF0000"));
        faculties.add(new Faculty(2L, "Слизерин", "#00A5E0"));

        given(facultyService.searchFaculties("ri")).willReturn(faculties);

        ResultActions result = mvc.perform(get("/faculty/search?q=ri")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}