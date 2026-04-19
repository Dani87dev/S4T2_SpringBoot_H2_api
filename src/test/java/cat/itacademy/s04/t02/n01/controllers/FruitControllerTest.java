package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.dto.FruitDTO;
import cat.itacademy.s04.t02.n01.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n01.services.FruitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FruitController.class)
public class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FruitService fruitService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createFruit_shouldReturn201() throws Exception {
        FruitDTO response = new FruitDTO(1L, "Apple", 5);
        when(fruitService.createFruit(any(FruitDTO.class))).thenReturn(response);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FruitDTO(null, "Apple", 5))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(5));
    }

    @Test
    void createFruit_shouldReturn400WhenNameBlank() throws Exception {
        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FruitDTO(null, "", 5))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFruit_shouldReturn400WhenWeightInvalid() throws Exception {
        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FruitDTO(null, "Apple", -1))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFruitById_shouldReturn200() throws Exception {
        FruitDTO response = new FruitDTO(1L, "Apple", 5);
        when(fruitService.getFruitById(1L)).thenReturn(response);

        mockMvc.perform(get("/fruits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void getFruitById_shouldReturn404() throws Exception {
        when(fruitService.getFruitById(99L)).thenThrow(new FruitNotFoundException("Fruit not found with id: 99"));

        mockMvc.perform(get("/fruits/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllFruits_shouldReturn200WithList() throws Exception {
        when(fruitService.getAllFruits()).thenReturn(Arrays.asList(
                new FruitDTO(1L, "Apple", 5),
                new FruitDTO(2L, "Banana", 3)
        ));

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllFruits_shouldReturn200WithEmptyList() throws Exception {
        when(fruitService.getAllFruits()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void updateFruit_shouldReturn200() throws Exception {
        FruitDTO response = new FruitDTO(1L, "Banana", 3);
        when(fruitService.updateFruit(eq(1L), any(FruitDTO.class))).thenReturn(response);

        mockMvc.perform(put("/fruits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FruitDTO(null, "Banana", 3))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Banana"));
    }

    @Test
    void updateFruit_shouldReturn404() throws Exception {
        when(fruitService.updateFruit(eq(99L), any(FruitDTO.class)))
                .thenThrow(new FruitNotFoundException("Fruit not found with id: 99"));

        mockMvc.perform(put("/fruits/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FruitDTO(null, "Banana", 3))))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFruit_shouldReturn204() throws Exception {
        doNothing().when(fruitService).deleteFruit(1L);

        mockMvc.perform(delete("/fruits/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteFruit_shouldReturn404() throws Exception {
        doThrow(new FruitNotFoundException("Fruit not found with id: 99"))
                .when(fruitService).deleteFruit(99L);

        mockMvc.perform(delete("/fruits/99"))
                .andExpect(status().isNotFound());
    }
}