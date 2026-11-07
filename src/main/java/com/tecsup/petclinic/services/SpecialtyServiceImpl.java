package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository repository;

    public SpecialtyServiceImpl(SpecialtyRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialtyDTO> findAll(String name, String office) {
        List<Specialty> specialties;
        if (name != null && office != null) {
            specialties = repository.findByNameAndOfficeOrderById(name, office);
        } else if (name != null) {
            specialties = repository.findByNameOrderById(name);
        } else if (office != null) {
            specialties = repository.findByOfficeOrderById(office);
        } else {
            specialties = repository.findAll(Sort.by("id"));
        }
        return specialties.stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialtyDTO findById(Integer id) {
        return toDto(findEntity(id));
    }

    @Override
    public SpecialtyDTO create(SpecialtyDTO dto) {
        validateSchedule(dto);
        Specialty specialty = new Specialty();
        apply(dto, specialty);
        return toDto(repository.save(specialty));
    }

    @Override
    public SpecialtyDTO update(Integer id, SpecialtyDTO dto) {
        Specialty specialty = findEntity(id);
        validateSchedule(dto);
        specialty.setName(dto.getName());
        // Preserve existing details for legacy requests containing only a name.
        if (dto.getOffice() != null) {
            specialty.setOffice(dto.getOffice());
        }
        if (dto.getOpeningHour() != null) {
            specialty.setOpeningHour(dto.getOpeningHour());
            specialty.setClosingHour(dto.getClosingHour());
        }
        return toDto(repository.save(specialty));
    }

    @Override
    public void delete(Integer id) {
        repository.delete(findEntity(id));
    }

    private Specialty findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Specialty not found"));
    }

    private void validateSchedule(SpecialtyDTO dto) {
        Integer opening = dto.getOpeningHour();
        Integer closing = dto.getClosingHour();
        if (opening == null && closing == null) {
            return;
        }
        if (opening == null || closing == null || opening < 0 || opening > 23
                || closing < 0 || closing > 23 || opening >= closing) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid specialty schedule");
        }
    }

    private void apply(SpecialtyDTO dto, Specialty specialty) {
        specialty.setName(dto.getName());
        specialty.setOffice(dto.getOffice());
        specialty.setOpeningHour(dto.getOpeningHour());
        specialty.setClosingHour(dto.getClosingHour());
    }

    private SpecialtyDTO toDto(Specialty specialty) {
        return SpecialtyDTO.builder().id(specialty.getId()).name(specialty.getName())
                .office(specialty.getOffice()).openingHour(specialty.getOpeningHour())
                .closingHour(specialty.getClosingHour()).build();
    }
}