package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.RoleJoinDto;
import hu.agfcodeworks.operangel.application.model.Role;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleJoinDtoMapper extends AbstractDtoMapper<Role, RoleJoinDto> {

    @Override
    public RoleJoinDto entityToDto(@NonNull Role role) {
        return RoleJoinDto.builder()
                .withNaturalId(role.getNaturalId())
                .build();
    }
}
