package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.VisitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VisitController {

	private final VisitRepository visitRepository;

	private final PetRepository petRepository;

	public VisitController(VisitRepository visitRepository, PetRepository petRepository) {
		this.visitRepository = visitRepository;
		this.petRepository = petRepository;
	}

	private VisitDTO toDto(Visit visit) {
		return VisitDTO.builder()
				.id(visit.getId())
				.petId(visit.getPet().getId())
				.visitDate(visit.getVisitDate().toString())
				.description(visit.getDescription())
				.build();
	}

	@GetMapping(value = "/visits")
	public ResponseEntity<List<VisitDTO>> findAll() {
		List<VisitDTO> visits = visitRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(visits);
	}

	@GetMapping(value = "/visits/{id}")
	public ResponseEntity<VisitDTO> findById(@PathVariable Long id) {
		return visitRepository.findById(id)
				.map(visit -> ResponseEntity.ok(toDto(visit)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/visits")
	public ResponseEntity<VisitDTO> create(@RequestBody VisitDTO visitDTO) {
		return petRepository.findById(visitDTO.getPetId()).map(pet -> {
			Visit visit = new Visit();
			visit.setPet(pet);
			visit.setVisitDate(LocalDate.parse(visitDTO.getVisitDate()));
			visit.setDescription(visitDTO.getDescription());
			return ResponseEntity.status(HttpStatus.CREATED).body(toDto(visitRepository.save(visit)));
		}).orElse(ResponseEntity.badRequest().build());
	}

	@PutMapping(value = "/visits/{id}")
	public ResponseEntity<VisitDTO> update(@RequestBody VisitDTO visitDTO, @PathVariable Long id) {
		return visitRepository.findById(id).map(visit -> {
			visit.setDescription(visitDTO.getDescription());
			visit.setVisitDate(LocalDate.parse(visitDTO.getVisitDate()));
			return ResponseEntity.ok(toDto(visitRepository.save(visit)));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/visits/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		if (!visitRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		visitRepository.deleteById(id);
		return ResponseEntity.ok(" Delete ID :" + id);
	}

}
