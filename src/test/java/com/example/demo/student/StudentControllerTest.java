package com.example.demo.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentService)).build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void ShouldGetAllStudents() throws Exception {
        // Prepare test data
        List<Student> students = new ArrayList<>();
        students.add(new Student("john", "john@example.com",Gender.MALE ));
        students.add(new Student("Mary","Mary@example.com",Gender.FEMALE));

        // Mock the behavior of the studentService.getAllStudents() method
        when(studentService.getAllStudents()).thenReturn(students);

        // Perform the GET request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/students"))
                .andExpect(status().isOk())
                .andReturn();

        // Perform assertions on the result
        // For example, assert the response content, JSON structure, etc.
    }

    @Test
    void ShouldAddStudent() throws Exception {
        // Prepare test data
        Student student = new Student("john", "john@example.com",Gender.MALE);

        // Convert the student object to JSON
        String jsonStudent = objectMapper.writeValueAsString(student);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudent))
                .andExpect(status().isOk());

        // Verify that the studentService.addStudent() method is called
        verify(studentService).addStudent(student);
    }

    @Test
    void ShouldDeleteStudent() throws Exception {
        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/{studentId}", 1L))
                .andExpect(status().isOk());

        // Verify that the studentService.deleteStudent() method is called with the correct studentId
        verify(studentService).deleteStudent(1L);
    }
}