package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.PlayDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.model.Role;
import hu.agfcodeworks.operangel.application.util.TextUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleDtoMapper extends AbstractDtoMapper<Role, RoleDto> {

    @Override
    public RoleDto entityToDto(@NonNull Role role) {
        return RoleDto.builder()
                .withPlayId(role.getPlay().getNaturalId())
                .withNaturalId(role.getNaturalId())
                .withDescription(role.getDescription())
                .withDescriptionUnified(TextUtil.unify(role.getDescription()))
                .build();
    }
}
