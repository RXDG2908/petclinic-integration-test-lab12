package com.tecsup.petclinic.helpers;

import com.tecsup.petclinic.dtos.SpecialtyDTO;

public final class SpecialtyObjectCreator {
    private SpecialtyObjectCreator() {
    }

    public static SpecialtyDTO specialty(Integer id, String name, String office) {
        return SpecialtyDTO.builder().id(id).name(name).office(office)
                .openingHour(8).closingHour(18).build();
    }
}