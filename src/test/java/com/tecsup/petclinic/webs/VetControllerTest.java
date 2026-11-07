package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VetDTO;
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
 * Pruebas de integracion de la entidad Vet (controlador, repositorio y base H2)
 */
@AutoConfigureMockMvc
@SpringBootTest
public class VetControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllVets() throws Exception {

		final int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get("/vets"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}

	@Test
	public void testFindVetOK() throws Exception {

		String FIRST_NAME = "James";
		String LAST_NAME = "Carter";

		this.mockMvc.perform(get("/vets/1"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(LAST_NAME)));
	}

	@Test
	public void testFindVetKO() throws Exception {

		mockMvc.perform(get("/vets/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateVet() throws Exception {

		String FIRST_NAME = "Ana";
		String LAST_NAME = "Torres";

		VetDTO newVet = VetDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.build();

		this.mockMvc.perform(post("/vets")
						.content(om.writeValueAsString(newVet))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(LAST_NAME)));
	}

	@Test
	public void testDeleteVet() throws Exception {

		VetDTO newVet = VetDTO.builder()
				.firstName("Luis")
				.lastName("Mendoza")
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/vets")
						.content(om.writeValueAsString(newVet))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/vets/" + id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/vets/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteVetKO() throws Exception {

		mockMvc.perform(delete("/vets/1000"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateVet() throws Exception {

		String UP_FIRST_NAME = "Carla";
		String UP_LAST_NAME = "Rojas";

		VetDTO newVet = VetDTO.builder()
				.firstName("Pedro")
				.lastName("Salas")
				.build();

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/vets")
						.content(om.writeValueAsString(newVet))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE
		VetDTO upVet = VetDTO.builder()
				.id(id)
				.firstName(UP_FIRST_NAME)
				.lastName(UP_LAST_NAME)
				.build();

		mockMvc.perform(put("/vets/" + id)
						.content(om.writeValueAsString(upVet))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		// FIND
		mockMvc.perform(get("/vets/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)));

		// DELETE
		mockMvc.perform(delete("/vets/" + id))
				.andExpect(status().isOk());
	}

}
