package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.RoleSimpleDto;
import hu.agfcodeworks.operangel.application.model.Role;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleJoinDtoMapper extends AbstractDtoMapper<Role, RoleSimpleDto> {

    @Override
    public RoleSimpleDto entityToDto(@NonNull Role role) {
        return RoleSimpleDto.builder()
                .withNaturalId(role.getNaturalId())
                .build();
    }
}
