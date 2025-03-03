package hu.agfcodeworks.operangel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Builder(setterPrefix = "with")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Getter
public class RoleDto {

    private UUID playNaturalId;

    @EqualsAndHashCode.Include
    private final UUID naturalId;

    private final String description;

    private  final String descriptionUnified;
}
