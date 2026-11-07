package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.repositories.VetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VetController {

	private final VetRepository vetRepository;

	public VetController(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	private VetDTO toDto(Vet vet) {
		return VetDTO.builder()
				.id(vet.getId())
				.firstName(vet.getFirstName())
				.lastName(vet.getLastName())
				.build();
	}

	@GetMapping(value = "/vets")
	public ResponseEntity<List<VetDTO>> findAll() {
		List<VetDTO> vets = vetRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(vets);
	}

	@GetMapping(value = "/vets/{id}")
	public ResponseEntity<VetDTO> findById(@PathVariable Integer id) {
		return vetRepository.findById(id)
				.map(vet -> ResponseEntity.ok(toDto(vet)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/vets")
	public ResponseEntity<VetDTO> create(@RequestBody VetDTO vetDTO) {
		Vet vet = new Vet();
		vet.setFirstName(vetDTO.getFirstName());
		vet.setLastName(vetDTO.getLastName());
		return ResponseEntity.status(HttpStatus.CREATED).body(toDto(vetRepository.save(vet)));
	}

	@PutMapping(value = "/vets/{id}")
	public ResponseEntity<VetDTO> update(@RequestBody VetDTO vetDTO, @PathVariable Integer id) {
		return vetRepository.findById(id).map(vet -> {
			vet.setFirstName(vetDTO.getFirstName());
			vet.setLastName(vetDTO.getLastName());
			return ResponseEntity.ok(toDto(vetRepository.save(vet)));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/vets/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		if (!vetRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		vetRepository.deleteById(id);
		return ResponseEntity.ok(" Delete ID :" + id);
	}

}
