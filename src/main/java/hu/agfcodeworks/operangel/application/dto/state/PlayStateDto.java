package hu.agfcodeworks.operangel.application.dto.state;


import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.ui.util.TextUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder(setterPrefix = "with")
@AllArgsConstructor
public class PlayStateDto {

    @Getter
    private final UUID playNaturalId;

    private final List<RoleDto> roles;

    @Getter
    private final List<PerformanceStateDto> performanceStateDtos;

    public List<RoleDto> getRoles() {
        return new ArrayList<>(roles);
    }

    public RoleDto store(@NonNull RoleDto roleDto) {
        if (Objects.isNull(roleDto.getNaturalId())) {
            return add(roleDto);
        }

        return updateRole(roleDto);
    }

    public void delete(@NonNull RoleDto roleDto) {
        roles.remove(roleDto);
    }

    public void clearRoles() {
        roles.clear();
    }

    private RoleDto add(RoleDto roleDto) {
        var roleDtoOpt = roles.stream()
                .filter(r -> Objects.equals(r.getDescription(), roleDto.getDescription()))
                .findFirst();

        if (roleDtoOpt.isPresent()) {
            return roleDtoOpt.get();
        }

        var cachedRoleDto = makeRoleDto(roleDto);

        roles.add(cachedRoleDto);
        return cachedRoleDto;
    }

    private RoleDto updateRole(@NonNull RoleDto roleDto) {
        roles.remove(roleDto);
        roles.add(roleDto);

        return roleDto;
    }

    private RoleDto makeRoleDto(RoleDto roleDto) {
        return RoleDto.builder()
                .withPlayNaturalId(playNaturalId)
                .withNaturalId(obtainNaturalId(roleDto))
                .withDescription(roleDto.getDescription())
                .withDescriptionUnified(TextUtil.unify(roleDto.getDescription()))
                .build();
    }

    private UUID obtainNaturalId(RoleDto roleDto) {
        if (Objects.nonNull(roleDto.getNaturalId())) {
            return roleDto.getNaturalId();
        }

        return UUID.randomUUID();
    }

}
