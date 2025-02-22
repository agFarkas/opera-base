package hu.agfcodeworks.operangel.application.mapper;

import lombok.NonNull;

public abstract class AbstractDtoMapper<ENTITY, DTO> {

    public abstract DTO entityToDto(@NonNull ENTITY entity);
}
