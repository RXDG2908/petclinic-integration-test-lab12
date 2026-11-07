package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integracion de la entidad Specialty (controlador, repositorio y base H2)
 */
@AutoConfigureMockMvc
@SpringBootTest
public class SpecialtyControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllSpecialties() throws Exception {

		final int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get("/specialties"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}

	@Test
	public void testFindSpecialtyOK() throws Exception {

		String NAME = "radiology";

		this.mockMvc.perform(get("/specialties/1"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is(NAME)));
	}

	@Test
	public void testFindSpecialtyKO() throws Exception {

		mockMvc.perform(get("/specialties/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateSpecialty() throws Exception {

		String NAME = "cardiology";

		SpecialtyDTO newSpecialty = SpecialtyDTO.builder()
				.name(NAME)
				.build();

		this.mockMvc.perform(post("/specialties")
						.content(om.writeValueAsString(newSpecialty))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is(NAME)));
	}

	@Test
	public void testDeleteSpecialty() throws Exception {

		SpecialtyDTO newSpecialty = SpecialtyDTO.builder()
				.name("dermatology")
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/specialties")
						.content(om.writeValueAsString(newSpecialty))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/specialties/" + id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/specialties/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteSpecialtyKO() throws Exception {

		mockMvc.perform(delete("/specialties/1000"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateSpecialty() throws Exception {

		String UP_NAME = "neurology";

		SpecialtyDTO newSpecialty = SpecialtyDTO.builder()
				.name("oncology")
				.build();

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/specialties")
						.content(om.writeValueAsString(newSpecialty))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE
		SpecialtyDTO upSpecialty = SpecialtyDTO.builder()
				.id(id)
				.name(UP_NAME)
				.build();

		mockMvc.perform(put("/specialties/" + id)
						.content(om.writeValueAsString(upSpecialty))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		// FIND
		mockMvc.perform(get("/specialties/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.name", is(UP_NAME)));

		// DELETE
		mockMvc.perform(delete("/specialties/" + id))
				.andExpect(status().isOk());
	}

}
