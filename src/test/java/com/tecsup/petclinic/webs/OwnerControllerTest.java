package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.OwnerDTO;
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
 * Pruebas de integracion de la entidad Owner (controlador, repositorio y base H2)
 */
@AutoConfigureMockMvc
@SpringBootTest
public class OwnerControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllOwners() throws Exception {

		final int ID_FIRST_RECORD = 1;

		this.mockMvc.perform(get("/owners"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
	}

	@Test
	public void testFindOwnerOK() throws Exception {

		String FIRST_NAME = "George";
		String LAST_NAME = "Franklin";
		String CITY = "Madison";

		this.mockMvc.perform(get("/owners/1"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(LAST_NAME)))
				.andExpect(jsonPath("$.city", is(CITY)));
	}

	@Test
	public void testFindOwnerKO() throws Exception {

		mockMvc.perform(get("/owners/666"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateOwner() throws Exception {

		String FIRST_NAME = "Marta";
		String LAST_NAME = "Quispe";
		String ADDRESS = "Av. Lima 123";
		String CITY = "Arequipa";
		String TELEPHONE = "987654321";

		OwnerDTO newOwner = OwnerDTO.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.address(ADDRESS)
				.city(CITY)
				.telephone(TELEPHONE)
				.build();

		this.mockMvc.perform(post("/owners")
						.content(om.writeValueAsString(newOwner))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(LAST_NAME)))
				.andExpect(jsonPath("$.address", is(ADDRESS)))
				.andExpect(jsonPath("$.city", is(CITY)))
				.andExpect(jsonPath("$.telephone", is(TELEPHONE)));
	}

	@Test
	public void testDeleteOwner() throws Exception {

		OwnerDTO newOwner = OwnerDTO.builder()
				.firstName("Jorge")
				.lastName("Paredes")
				.address("Calle Sol 45")
				.city("Cusco")
				.telephone("912345678")
				.build();

		ResultActions mvcActions = mockMvc.perform(post("/owners")
						.content(om.writeValueAsString(newOwner))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		mockMvc.perform(delete("/owners/" + id))
				.andExpect(status().isOk());

		mockMvc.perform(get("/owners/" + id))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteOwnerKO() throws Exception {

		mockMvc.perform(delete("/owners/1000"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateOwner() throws Exception {

		String UP_FIRST_NAME = "Rosa";
		String UP_LAST_NAME = "Vega";
		String UP_ADDRESS = "Jr. Union 678";
		String UP_CITY = "Trujillo";
		String UP_TELEPHONE = "999111222";

		OwnerDTO newOwner = OwnerDTO.builder()
				.firstName("Mario")
				.lastName("Diaz")
				.address("Av. Brasil 10")
				.city("Lima")
				.telephone("900000000")
				.build();

		// CREATE
		ResultActions mvcActions = mockMvc.perform(post("/owners")
						.content(om.writeValueAsString(newOwner))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String response = mvcActions.andReturn().getResponse().getContentAsString();
		Integer id = JsonPath.parse(response).read("$.id");

		// UPDATE
		OwnerDTO upOwner = OwnerDTO.builder()
				.id(id.longValue())
				.firstName(UP_FIRST_NAME)
				.lastName(UP_LAST_NAME)
				.address(UP_ADDRESS)
				.city(UP_CITY)
				.telephone(UP_TELEPHONE)
				.build();

		mockMvc.perform(put("/owners/" + id)
						.content(om.writeValueAsString(upOwner))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		// FIND
		mockMvc.perform(get("/owners/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
				.andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)))
				.andExpect(jsonPath("$.address", is(UP_ADDRESS)))
				.andExpect(jsonPath("$.city", is(UP_CITY)))
				.andExpect(jsonPath("$.telephone", is(UP_TELEPHONE)));

		// DELETE
		mockMvc.perform(delete("/owners/" + id))
				.andExpect(status().isOk());
	}

}
