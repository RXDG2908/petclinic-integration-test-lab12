package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.repositories.OwnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OwnerController {

	private final OwnerRepository ownerRepository;

	public OwnerController(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	private OwnerDTO toDto(Owner owner) {
		return OwnerDTO.builder()
				.id(owner.getId())
				.firstName(owner.getFirstName())
				.lastName(owner.getLastName())
				.address(owner.getAddress())
				.city(owner.getCity())
				.telephone(owner.getTelephone())
				.build();
	}

	private void copy(OwnerDTO dto, Owner owner) {
		owner.setFirstName(dto.getFirstName());
		owner.setLastName(dto.getLastName());
		owner.setAddress(dto.getAddress());
		owner.setCity(dto.getCity());
		owner.setTelephone(dto.getTelephone());
	}

	@GetMapping(value = "/owners")
	public ResponseEntity<List<OwnerDTO>> findAll() {
		List<OwnerDTO> owners = ownerRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(owners);
	}

	@GetMapping(value = "/owners/{id}")
	public ResponseEntity<OwnerDTO> findById(@PathVariable Long id) {
		return ownerRepository.findById(id)
				.map(owner -> ResponseEntity.ok(toDto(owner)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/owners")
	public ResponseEntity<OwnerDTO> create(@RequestBody OwnerDTO ownerDTO) {
		Owner owner = new Owner();
		copy(ownerDTO, owner);
		return ResponseEntity.status(HttpStatus.CREATED).body(toDto(ownerRepository.save(owner)));
	}

	@PutMapping(value = "/owners/{id}")
	public ResponseEntity<OwnerDTO> update(@RequestBody OwnerDTO ownerDTO, @PathVariable Long id) {
		return ownerRepository.findById(id).map(owner -> {
			copy(ownerDTO, owner);
			return ResponseEntity.ok(toDto(ownerRepository.save(owner)));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/owners/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		if (!ownerRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		ownerRepository.deleteById(id);
		return ResponseEntity.ok(" Delete ID :" + id);
	}

}
