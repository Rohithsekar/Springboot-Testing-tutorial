package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.junit.MockitoJUnitRunner;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    //    private AutoCloseable autoCloseable;
//    @InjectMocks
    private StudentService underTest;
//
//    @Captor
//    private ArgumentCaptor<Student> studentArgumentCaptor;


    private Student Student;

    @BeforeEach
        //runs before each test
    void setUp() {
//     autoCloseable = MockitoAnnotations.openMocks(this);
        MockitoAnnotations.openMocks(this);
        underTest = new StudentService(repository);
//        // given
//        String email = "Emily@123.com";
//        Student student = new Student(
//                "Emily",
//                email,
//                Gender.FEMALE
//        );
//        //when
//        repository.save(student);

    }


//    @AfterEach
//    void tearDown() throws Exception{
//        autoCloseable.close();
//    }

    @Test
    void ShouldGetAllStudents() {

        //when
        underTest.getAllStudents();

        //then
        verify(repository).findAll();

    }

    @Test
    void shouldAddStudent() {

        //given
        String email = "Adam@123.com";
        Student student = new Student(
                "Adam",
                email,
                Gender.MALE
        );

        //when
        underTest.addStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(repository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);


    }

    @Test
    void shouldThrowExceptionWhenEmailTaken() {

        //given
        String email = "Adam@123.com";
        Student student = new Student(
                "Adam",
                email,
                Gender.MALE
        );

        given(repository.selectExistsEmail(anyString()))
                .willReturn(true);

        //when
        //then - we expect the method should throw an exception when the
        //provided email already exists
        assertThatThrownBy(() ->
                underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        //verifies that once the exception is thrown by the method, the object is never persisted in the table

        verify(repository, never()).save(any());

    }


    @Test
//    @Disabled
    void shouldDeleteStudentWhenStudentExists() {
        // given
        Long studentId = 1L;

        given(repository.existsById(studentId)).willReturn(true);

        // when
        underTest.deleteStudent(studentId);

        // then
        verify(repository).existsById(studentId);
        verify(repository).deleteById(studentId);
    }

    @Test
//    @Disabled
    void shouldThrowExceptionWhenStudentDoesNotExist() {
        // given
        Long studentId = 1L;

        given(repository.existsById(studentId)).willReturn(false);

        // when
        assertThatThrownBy(()->
                underTest.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exist");

        // then
        verify(repository,never()).deleteById(studentId);

    }



}
