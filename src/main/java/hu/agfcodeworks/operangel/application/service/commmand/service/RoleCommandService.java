package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.command.RoleCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleDeleteCommand;
import hu.agfcodeworks.operangel.application.mapper.RoleDtoMapper;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.model.Role;
import hu.agfcodeworks.operangel.application.repository.RoleRepository;
import hu.agfcodeworks.operangel.application.service.query.service.PlayQueryService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class RoleCommandService {

    private final RoleRepository roleRepository;

    private final RoleDtoMapper roleDtoMapper;

    private final PlayQueryService playQueryService;


    public RoleDto save(@NonNull RoleCommand roleCommand) {
        return updateOrCreate(roleCommand);
    }

    private RoleDto updateOrCreate(RoleCommand roleCommand) {
        return roleRepository.findByNaturalId(roleCommand.getNaturalId())
                .map(role -> {
                    role.setDescription(roleCommand.getDescription());

                    roleRepository.save(role);
                    return roleDtoMapper.entityToDto(role);
                }).orElseGet((() -> createNew(roleCommand)));
    }

    private RoleDto createNew(RoleCommand roleCommand) {
        var entity = Role.builder()
                .withNaturalId(roleCommand.getNaturalId())
                .withDescription(roleCommand.getDescription())
                .withPlay(obtainPlay(roleCommand))
                .build();

        roleRepository.save(entity);

        return roleDtoMapper.entityToDto(entity);
    }

    private Play obtainPlay(RoleCommand roleCommand) {
        return playQueryService.findByNaturalId(roleCommand.getPlayNaturalId())
                .orElseThrow(() -> new RuntimeException("Play Not Found"));
    }

    public void delete(@NonNull RoleDeleteCommand roleCommand) {
        roleRepository.deleteByNaturalId(roleCommand.getNaturalId());
    }

}
