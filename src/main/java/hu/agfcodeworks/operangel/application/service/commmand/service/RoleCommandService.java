package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.command.RoleCommand;
import hu.agfcodeworks.operangel.application.dto.command.RoleDeleteCommand;
import hu.agfcodeworks.operangel.application.mapper.RoleDtoMapper;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.model.Role;
import hu.agfcodeworks.operangel.application.repository.RoleRepository;
import hu.agfcodeworks.operangel.application.service.query.service.PlayQueryService;
import hu.agfcodeworks.operangel.application.service.query.service.RoleQueryService;
import hu.agfcodeworks.operangel.application.util.ThreadCacheUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Transactional
@Service
@AllArgsConstructor
public class RoleCommandService {

    private final RoleQueryService roleQueryService;

    private final RoleDtoMapper roleDtoMapper;

    private final RoleRepository roleRepository;

    private final PlayQueryService playQueryService;


    public RoleDto save(@NonNull RoleCommand roleCommand) {
        return updateOrCreate(roleCommand);
    }

    private RoleDto updateOrCreate(RoleCommand roleCommand) {
        return roleQueryService.findByNaturalId(roleCommand.getNaturalId())
                .map(role -> update(roleCommand, role))
                .orElseGet((() -> createNew(roleCommand)));
    }

    private RoleDto update(RoleCommand roleCommand, Role role) {
        role.setDescription(roleCommand.getDescription());

        roleRepository.save(role);
        return roleDtoMapper.entityToDto(role);
    }

    private RoleDto createNew(RoleCommand roleCommand) {
        var entity = Role.builder()
                .withNaturalId(roleCommand.getNaturalId())
                .withDescription(roleCommand.getDescription())
                .withPlay(obtainPlay(roleCommand))
                .build();

        roleRepository.save(entity);

        ThreadCacheUtil.storeRole(entity);

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
