package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.services.SpecialtyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String office) {
        return ResponseEntity.ok(specialtyService.findAll(name, office));
    }

    @GetMapping("/specialties/{id}")
    public ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(specialtyService.findById(id));
    }

    @PostMapping("/specialties")
    public ResponseEntity<SpecialtyDTO> create(@RequestBody SpecialtyDTO specialty) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specialtyService.create(specialty));
    }

    @PutMapping("/specialties/{id}")
    public ResponseEntity<SpecialtyDTO> update(@RequestBody SpecialtyDTO specialty, @PathVariable Integer id) {
        return ResponseEntity.ok(specialtyService.update(id, specialty));
    }

    @DeleteMapping("/specialties/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        specialtyService.delete(id);
        return ResponseEntity.ok(" Delete ID :" + id);
    }
}