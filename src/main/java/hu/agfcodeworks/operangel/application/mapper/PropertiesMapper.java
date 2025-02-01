package hu.agfcodeworks.operangel.application.mapper;

import java.util.Properties;

public abstract class PropertiesMapper<DTO> {

    public abstract Properties dtoToProperties(DTO dto);

    public abstract DTO propertiesToDto(Properties properties);
}
