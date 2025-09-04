package ru.hogwarts.school;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerMvcTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    @Transactional
    void clearDatabase() {
        studentRepository.deleteAll();
    }

    @Test
    void testGetStudentInfo() throws Exception {
        // Создадим студента для теста
        Student harryPotter = new Student(null, "Гарри Поттер", 18);
        studentRepository.save(harryPotter);

        ResultActions result = mvc.
                perform(get("/student/" + harryPotter.getId()).
                accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гарри Поттер"));
    }

    @Test
    void testCreateStudent() throws Exception {
        // Мокируем метод сервиса
        BDDMockito.given(StudentService.addStudent(Mockito.any(Student.class))).willReturn(new Student(1L, "Гермиона Грейнджер", 17));

        // Отправляем POST-запрос
        ResultActions result = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Гермиона Грейнджер\", \"age\":17}"));

        // Проверяем статус ответа
        result.andExpect(status().isOk()); // Проверяем, что статус равен 200 OK
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
        ResultActions result = mvc.
                perform(get("/student?age=18").
                accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").exists()); // Ожидание массива учеников
    }

    @Test
    void testFindStudentsByAgeRange() throws Exception {
        ResultActions result = mvc.
                perform(get("/student/by-age-range?min=17&max=19").
                accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").exists()); // Ожидание массива учеников
    }

    @Test
    void testGetFacultyForStudent() throws Exception {
        ResultActions result = mvc.
                perform(get("/student/1/faculty").
                accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор")); // Название факультета Гарри
    }
}