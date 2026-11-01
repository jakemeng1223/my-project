package com.jakemeng.lifedb.record;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class DailyRecordControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void saveThenGet() throws Exception {
        mvc.perform(put("/api/records/2026-11-01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sleepTime":"05:40","wakeTime":"14:00","mood":6,"activities":"搭项目框架"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recordDate").value("2026-11-01"))
                .andExpect(jsonPath("$.mood").value(6));

        mvc.perform(get("/api/records/2026-11-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activities").value("搭项目框架"));
    }

    @Test
    void savingSameDateTwiceUpdatesInsteadOfDuplicating() throws Exception {
        for (int mood : new int[] {4, 8}) {
            mvc.perform(put("/api/records/2026-11-02")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"mood\":" + mood + "}"))
                    .andExpect(status().isOk());
        }

        mvc.perform(get("/api/records"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].mood").value(8));
    }

    @Test
    void rejectsMoodOutOfRange() throws Exception {
        mvc.perform(put("/api/records/2026-11-01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mood\":11}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("情绪最高 10 分"));
    }

    @Test
    void missingDateReturns404() throws Exception {
        mvc.perform(get("/api/records/2020-01-01"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRemovesRecord() throws Exception {
        mvc.perform(put("/api/records/2026-11-03")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mood\":5}"))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/records/2026-11-03"))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/records/2026-11-03"))
                .andExpect(status().isNotFound());
    }
}
