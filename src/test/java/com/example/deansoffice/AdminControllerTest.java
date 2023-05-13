package com.example.deansoffice;

import com.example.deansoffice.controller.AdminController;
import com.example.deansoffice.dao.WorkerDAO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.service.AdminService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WorkerDAO workerDAO;

    private int adminId;

    private List<Worker> workers = new ArrayList<>();


    @Autowired
    private Environment env;

    private void executeSQL(String[] sqlStatements) {
        String dbUrl = env.getProperty("spring.datasource.url");
        String dbUsername = env.getProperty("spring.datasource.username");
        String dbPassword = env.getProperty("spring.datasource.password");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false); // Disable auto-commit

            for (String sql : sqlStatements) {
                stmt.addBatch(sql); // Add each SQL statement to batch
            }

            stmt.executeBatch(); // Execute all SQL statements in batch
            conn.commit(); // Commit changes
        } catch (SQLException ex) {
            System.out.println("Error executing SQL: " + ex.getMessage());
        }
    }


    @BeforeAll
    public void setUp() {
        // Set up test data
        adminId = 1;
        Worker workerFirst = Worker.builder()
                .name("John")
                .surname("Doe")
                .phoneNumber("123456789")
                .email("johndoe@example.com")
                .room("101")
                .build();

        Worker workerSecond = Worker.builder()
                .name("Jane")
                .surname("Smith")
                .phoneNumber("123456781")
                .email("janesmith@example.com")
                .room("102")
                .build();

        workerDAO.save(workerFirst);
        workerDAO.save(workerSecond);

        workers.add(workerFirst);
        workers.add(workerSecond);
    }

    @AfterAll
    public void tearDown() {
        executeSQL(new String[] {
                "SET FOREIGN_KEY_CHECKS = 0",
                "TRUNCATE TABLE worker",
                "SET FOREIGN_KEY_CHECKS = 1"
        });
    }

    @Test
    public void testGetWorkers() throws Exception {
        adminId = 1;

        // Make GET request to endpoint
        MvcResult result = mockMvc.perform(get("/admin/" + adminId + "/worker")
                .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZXIiLCJpYXQiOjE2ODM5ODY2NjMsImV4cCI6MTY4Mzk4ODEwM30.LH8ucCOIb1dCqB3e-kQKy2Xx78FtgGjpGV9kQPoWQNM"))
                .andExpect(status().isOk())
                .andReturn();

        // Verify response body matches expected DTO list
        List<WorkerDTO> expectedDTOs = workers.stream()
                .map(WorkerDTO::new)
                .collect(Collectors.toList());
        String responseBody = result.getResponse().getContentAsString();
        List<WorkerDTO> actualDTOs = objectMapper.readValue(responseBody, new TypeReference<List<WorkerDTO>>() {});
        assertEquals(expectedDTOs, actualDTOs);
    }
}