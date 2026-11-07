package com.tecsup.petclinic.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SpecialtyDTO {

	private Integer id;

	private String name;

    private String office;

    @JsonProperty("h_open")
    private Integer openingHour;

    @JsonProperty("h_close")
    private Integer closingHour;

}
