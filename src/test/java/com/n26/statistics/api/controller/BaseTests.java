package com.n26.statistics.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class BaseTests {

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected MockMvc mvc;

    protected double randomValue(double min, double max) {
        return min + (Math.random() * (max - min));
    }
}
