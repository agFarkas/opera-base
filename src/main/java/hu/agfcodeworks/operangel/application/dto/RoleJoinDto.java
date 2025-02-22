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
public class RoleJoinDto {

    @EqualsAndHashCode.Include
    private final UUID naturalId;
}
