package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SpecialtyController {

	private final SpecialtyRepository specialtyRepository;

	public SpecialtyController(SpecialtyRepository specialtyRepository) {
		this.specialtyRepository = specialtyRepository;
	}

	private SpecialtyDTO toDto(Specialty specialty) {
		return SpecialtyDTO.builder()
				.id(specialty.getId())
				.name(specialty.getName())
				.build();
	}

	@GetMapping(value = "/specialties")
	public ResponseEntity<List<SpecialtyDTO>> findAll() {
		List<SpecialtyDTO> list = specialtyRepository.findAll(org.springframework.data.domain.Sort.by("id")).stream().map(this::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@GetMapping(value = "/specialties/{id}")
	public ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer id) {
		return specialtyRepository.findById(id)
				.map(specialty -> ResponseEntity.ok(toDto(specialty)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/specialties")
	public ResponseEntity<SpecialtyDTO> create(@RequestBody SpecialtyDTO specialtyDTO) {
		Specialty specialty = new Specialty();
		specialty.setName(specialtyDTO.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(toDto(specialtyRepository.save(specialty)));
	}

	@PutMapping(value = "/specialties/{id}")
	public ResponseEntity<SpecialtyDTO> update(@RequestBody SpecialtyDTO specialtyDTO, @PathVariable Integer id) {
		return specialtyRepository.findById(id).map(specialty -> {
			specialty.setName(specialtyDTO.getName());
			return ResponseEntity.ok(toDto(specialtyRepository.save(specialty)));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/specialties/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		if (!specialtyRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		specialtyRepository.deleteById(id);
		return ResponseEntity.ok(" Delete ID :" + id);
	}

}
