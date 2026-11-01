package com.jakemeng.lifedb.matrix;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class LifeItemControllerTest {

    @Autowired
    private MockMvc mvc;

    private long create(String json) throws Exception {
        String body = mvc.perform(post("/api/items").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$.id")).longValue();
    }

    @Test
    void createThenList() throws Exception {
        create("""
                {"domain":"BODY","name":"睡眠","status":"POOR","priority":"HIGH","nextStep":"每天 11 点起床"}
                """);

        mvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].domain").value("BODY"))
                .andExpect(jsonPath("$[0].status").value("POOR"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }

    @Test
    void statusAndPriorityAreOptional() throws Exception {
        create("{\"domain\":\"FINANCE\",\"name\":\"存款\"}");

        mvc.perform(get("/api/items"))
                .andExpect(jsonPath("$[0].status").doesNotExist())
                .andExpect(jsonPath("$[0].priority").doesNotExist());
    }

    @Test
    void updateChangesFields() throws Exception {
        long id = create("{\"domain\":\"STUDY\",\"name\":\"Java\",\"status\":\"FAIR\"}");

        mvc.perform(put("/api/items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"domain\":\"STUDY\",\"name\":\"Java\",\"status\":\"GOOD\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("GOOD"));
    }

    @Test
    void rejectsBlankName() throws Exception {
        mvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"domain\":\"BODY\",\"name\":\"  \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("名称不能为空"));
    }

    @Test
    void rejectsMissingDomain() throws Exception {
        mvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"睡眠\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("要选一个方面"));
    }

    @Test
    void deleteThenUpdateReturns404() throws Exception {
        long id = create("{\"domain\":\"SOCIAL\",\"name\":\"朋友\"}");

        mvc.perform(delete("/api/items/" + id)).andExpect(status().isNoContent());
        mvc.perform(put("/api/items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"domain\":\"SOCIAL\",\"name\":\"朋友\"}"))
                .andExpect(status().isNotFound());
    }
}
