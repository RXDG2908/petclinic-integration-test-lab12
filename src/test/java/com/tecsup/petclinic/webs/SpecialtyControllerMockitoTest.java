package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.helpers.SpecialtyObjectCreator;
import com.tecsup.petclinic.services.SpecialtyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SpecialtyControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SpecialtyService specialtyService;

    @Test
    public void testFindAllSpecialties() throws Exception {
        when(specialtyService.findAll(null, null)).thenReturn(List.of(
                SpecialtyObjectCreator.specialty(1, "radiology", "Farewell")));
        mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1))).andExpect(jsonPath("$[0].id", is(1)));
        verify(specialtyService).findAll(null, null);
    }

    @Test
    public void testFindSpecialtyOK() throws Exception {
        when(specialtyService.findById(1)).thenReturn(
                SpecialtyObjectCreator.specialty(1, "radiology", "Farewell"));
        mockMvc.perform(get("/specialties/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("radiology")));
        verify(specialtyService).findById(1);
    }

    @Test
    public void testFindSpecialtyKO() throws Exception {
        when(specialtyService.findById(666)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/specialties/666")).andExpect(status().isNotFound());
        verify(specialtyService).findById(666);
    }

    @Test
    public void testCreateSpecialty() throws Exception {
        SpecialtyDTO input = SpecialtyObjectCreator.specialty(null, "cardiology", "Lima");
        when(specialtyService.create(input)).thenReturn(
                SpecialtyObjectCreator.specialty(10, "cardiology", "Lima"));
        mockMvc.perform(post("/specialties").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("cardiology")))
                .andExpect(jsonPath("$.office", is("Lima")))
                .andExpect(jsonPath("$.h_open", is(8))).andExpect(jsonPath("$.h_close", is(18)));
        verify(specialtyService).create(input);
    }

    @Test
    public void testFindByName() throws Exception {
        when(specialtyService.findAll("surgery", null)).thenReturn(List.of(
                SpecialtyObjectCreator.specialty(2, "surgery", "Maryland")));
        mockMvc.perform(get("/specialties").param("name", "surgery"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("surgery")));
        verify(specialtyService).findAll("surgery", null);
    }

    @Test
    public void testFindByOffice() throws Exception {
        when(specialtyService.findAll(null, "Farewell")).thenReturn(List.of(
                SpecialtyObjectCreator.specialty(1, "radiology", "Farewell")));
        mockMvc.perform(get("/specialties").param("office", "Farewell"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("radiology")))
                .andExpect(jsonPath("$[0].office", is("Farewell")));
        verify(specialtyService).findAll(null, "Farewell");
    }

    @Test
    public void testCreateSpecialty_InvalidSchedule() throws Exception {
        SpecialtyDTO input = SpecialtyObjectCreator.specialty(null, "invalid", "Lima");
        input.setClosingHour(8);
        when(specialtyService.create(input)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mockMvc.perform(post("/specialties").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
        verify(specialtyService).create(input);
    }

    @Test
    public void testCreateSpecialty_InvalidHourRange() throws Exception {
        SpecialtyDTO input = SpecialtyObjectCreator.specialty(null, "invalid", "Lima");
        input.setClosingHour(24);
        when(specialtyService.create(input)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mockMvc.perform(post("/specialties").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
        verify(specialtyService).create(input);
    }

    @Test
    public void testDeleteSpecialty() throws Exception {
        SpecialtyDTO input = SpecialtyObjectCreator.specialty(null, "temporary", "Lima");
        when(specialtyService.create(input)).thenReturn(
                SpecialtyObjectCreator.specialty(10, "temporary", "Lima"));
        doNothing().when(specialtyService).delete(10);
        String response = mockMvc.perform(post("/specialties").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        int id = objectMapper.readTree(response).get("id").asInt();
        mockMvc.perform(delete("/specialties/" + id)).andExpect(status().isOk());
        verify(specialtyService).create(input);
        verify(specialtyService).delete(10);
    }
}