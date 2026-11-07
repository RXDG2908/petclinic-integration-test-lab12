package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import java.util.List;

public interface SpecialtyService {
    List<SpecialtyDTO> findAll(String name, String office);
    SpecialtyDTO findById(Integer id);
    SpecialtyDTO create(SpecialtyDTO specialty);
    SpecialtyDTO update(Integer id, SpecialtyDTO specialty);
    void delete(Integer id);
}