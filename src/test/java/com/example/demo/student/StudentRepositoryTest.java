package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    //We don't need to test all the Spring Data JPA methods.
    //We only want to test the methods that we write in our Repository interface

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {

        underTest.deleteAll();

    }
//
//    @BeforeEach //keyboard shortcut - Generate option popup by alt+insert and select Setup
//    void setUp() {
//
//    }

    @Test
    void ShouldCheckIfStudentEmailExists(){

        //given
        String email = "Adam@123.com";
        Student student = new Student(
                "Adam",
                email,
                Gender.MALE
        );
        underTest.save(student);

        //when
        Boolean expected= underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isTrue();

    }

    @Test
    void ShouldCheckIfStudentEmailDoesNotExist(){

        //given
        String email = "Adam@123.com";

        //when
        Boolean expected= underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isFalse();

    }


}