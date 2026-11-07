package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    List<Specialty> findByNameOrderById(String name);
    List<Specialty> findByOfficeOrderById(String office);
    List<Specialty> findByNameAndOfficeOrderById(String name, String office);
}
