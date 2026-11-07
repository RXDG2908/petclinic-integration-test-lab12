package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VisitDTO;
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
 * Pruebas de integracion de la entidad Visit (controlador, repositorio y base H2)
 */
@AutoConfigureMockMvc
@SpringBootTest
public class VisitControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllVisits() throws Exception {

		final int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get("/visits"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}

	@Test
	public void testFindVisitOK() throws Exception {

		int PET_ID = 7;
		String VISIT_DATE = "2010-03-04";
		String DESCRIPTION = "rabies shot";

		this.mockMvc.perform(get("/visits/1"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.petId", is(PET_ID)))
				.andExpect(jsonPath("$.visitDate", is(VISIT_DATE)))
				.andExpect(jsonPath("$.description", is(DESCRIPTION)));
	}

	@Test
	public void testFindVisitKO() throws Exception {

		mockMvc.perform(get("/visits/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateVisit() throws Exception {

		int PET_ID = 1;
		String VISIT_DATE = "2026-11-06";
		String DESCRIPTION = "vaccination";

		VisitDTO newVisit = VisitDTO.builder()
				.petId(PET_ID)
				.visitDate(VISIT_DATE)
				.description(DESCRIPTION)
				.build();

		this.mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisit))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.petId", is(PET_ID)))
				.andExpect(jsonPath("$.visitDate", is(VISIT_DATE)))
				.andExpect(jsonPath("$.description", is(DESCRIPTION)));
	}

	@Test
	public void testCreateVisitPetNotFound() throws Exception {

		VisitDTO newVisit = VisitDTO.builder()
				.petId(9999)
				.visitDate("2026-11-06")
				.description("checkup")
				.build();

		this.mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisit))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteVisit() throws Exception {

		VisitDTO newVisit = VisitDTO.builder()
				.petId(1)
				.visitDate("2026-11-07")
				.description("checkup")
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisit))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/visits/" + id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/visits/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteVisitKO() throws Exception {

		mockMvc.perform(delete("/visits/1000"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateVisit() throws Exception {

		String UP_VISIT_DATE = "2026-12-01";
		String UP_DESCRIPTION = "surgery follow-up";

		VisitDTO newVisit = VisitDTO.builder()
				.petId(1)
				.visitDate("2026-11-08")
				.description("first visit")
				.build();

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/visits")
						.content(om.writeValueAsString(newVisit))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE
		VisitDTO upVisit = VisitDTO.builder()
				.id(id.longValue())
				.petId(1)
				.visitDate(UP_VISIT_DATE)
				.description(UP_DESCRIPTION)
				.build();

		mockMvc.perform(put("/visits/" + id)
						.content(om.writeValueAsString(upVisit))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		// FIND
		mockMvc.perform(get("/visits/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.visitDate", is(UP_VISIT_DATE)))
				.andExpect(jsonPath("$.description", is(UP_DESCRIPTION)));

		// DELETE
		mockMvc.perform(delete("/visits/" + id))
				.andExpect(status().isOk());
	}

}
