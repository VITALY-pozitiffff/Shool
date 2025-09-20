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
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private StudentService studentService;

    @BeforeEach
    void setup() {
        // Базовые настройки для сервиса
        given(studentService.findStudent((long) anyInt())).willReturn(new Student(1L, "Гарри Поттер", 18));
    }

    @Test
    void testGetStudentInfo() throws Exception {
        ResultActions result = mvc.perform(get("/student/1")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void testCreateStudent() throws Exception {
        Student inputStudent = new Student(null, "Гермиона Грейнджер", 17);
        Student outputStudent = new Student(1L, "Гермиона Грейнджер", 17);

        given(studentService.addStudent(inputStudent)).willReturn(outputStudent);

        ResultActions result = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Гермиона Грейнджер\", \"age\":17}"));

        result.andExpect(status().isCreated());
    }

    @Test
    void testEditStudent() throws Exception {
        Student inputStudent = new Student(1L, "Рон Уизли", 19);
        Student outputStudent = new Student(1L, "Рон Уизли", 19);

        given(studentService.editStudent(inputStudent)).willReturn(outputStudent);

        ResultActions result = mvc.perform(put("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1, \"name\":\"Рон Уизли\", \"age\":19}"));

        result.andExpect(status().isOk());
    }

    @Test
    void testFindStudentsByAge() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Студент 18 лет", 18));
        students.add(new Student(2L, "Другой студент 18 лет", 18));

        given(studentService.findByAge(18)).willReturn(students);

        ResultActions result = mvc.perform(get("/student?age=18")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[*].name").exists());
    }

    @Test
    void testFindStudentsByAgeRange() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Студент 17 лет", 17));
        students.add(new Student(2L, "Студент 18 лет", 18));
        students.add(new Student(3L, "Студент 19 лет", 19));

        given(studentService.findByAgeRange(17, 19)).willReturn(students);

        ResultActions result = mvc.perform(get("/student/by-age-range?min=17&max=19")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[*].name").exists());
    }

    @Test
    void testGetFacultyForStudent() throws Exception {
        // Пока не реализуем, поскольку это требует дополнительной связи между Students и Faculties
        // Нужно добавить соответствующие моки и настройки, если понадобится этот тест
    }
}