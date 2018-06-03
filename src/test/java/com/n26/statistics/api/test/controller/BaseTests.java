package com.n26.statistics.api.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/snippets")
@SpringBootTest
public class BaseTests {

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected MockMvc mvc;

}
