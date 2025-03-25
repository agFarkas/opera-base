package hu.agfcodeworks.operangel.application.mapper;

import lombok.NonNull;

public abstract class AbstractMapper<SOURCE, TARGET> {

    public abstract TARGET entityToDto(@NonNull SOURCE source);
}
