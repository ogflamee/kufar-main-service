package com.sia.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.config.TestSecurityConfig;
import com.sia.dto.AdDTO;
import com.sia.entity.AdStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class MainServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void createAd_shouldSaveToDb() throws Exception {
        AdDTO request = AdDTO.builder()
                .title("Laptop")
                .description("Good")
                .price(new BigDecimal("1000"))
                .city("Minsk")
                .status(AdStatus.ACTIVE)
                .userId(100)
                .categoryId(100)
                .build();

        mockMvc.perform(post("/api/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Laptop"));
    }

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void getAdById_shouldReturnAd() throws Exception {
        mockMvc.perform(get("/api/ads/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void getAllAds_shouldReturnPage() throws Exception {
        mockMvc.perform(get("/api/ads?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void updateAd_shouldUpdate() throws Exception {
        AdDTO request = AdDTO.builder()
                .title("Updated")
                .description("Updated desc")
                .price(new BigDecimal("2000"))
                .city("Minsk")
                .status(AdStatus.ACTIVE)
                .userId(100)
                .categoryId(100)
                .build();

        mockMvc.perform(put("/api/ads/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void deleteAd_shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/ads/110"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/ads/110"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void createAd_shouldReturn400_whenInvalid() throws Exception {
        AdDTO request = AdDTO.builder()
                .title("")
                .price(new BigDecimal("-1"))
                .build();

        mockMvc.perform(post("/api/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }
}