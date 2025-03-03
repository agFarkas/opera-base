package hu.agfcodeworks.operangel.application.service.commmandservice;

import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.dto.command.RoleCommand;
import hu.agfcodeworks.operangel.application.mapper.RoleDtoMapper;
import hu.agfcodeworks.operangel.application.model.Play;
import hu.agfcodeworks.operangel.application.model.Role;
import hu.agfcodeworks.operangel.application.repository.RoleRepository;
import hu.agfcodeworks.operangel.application.service.cache.RoleCache;
import hu.agfcodeworks.operangel.application.service.queryservice.PlayQueryService;
import hu.agfcodeworks.operangel.application.util.TextUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
public class RoleCommandService {

    private final RoleRepository roleRepository;

    private final RoleDtoMapper roleDtoMapper;

    private final PlayQueryService playQueryService;

    private final RoleCache roleCache;

    public RoleDto save(@NonNull RoleCommand roleCommand) {
        if (Objects.nonNull(roleCommand.getNaturalId())) {
            return update(roleCommand);
        }

        return create(roleCommand);
    }

    private RoleDto create(RoleCommand roleCommand) {
        var roleDto = createNew(roleCommand);
        refreshInCache(roleDto);

        return roleDto;
    }

    private RoleDto update(RoleCommand roleCommand) {
        var newRoleDto = findAndUpdate(roleCommand);
        refreshInCache(newRoleDto);

        return newRoleDto;
    }

    private void refreshInCache(RoleDto roleDto) {
        var cacheList = roleCache.get(roleDto.getPlayNaturalId());

        if (cacheList.contains(roleDto)) {
            cacheList.remove(roleDto);
        }

        cacheList.add(roleDto);
    }


    private RoleDto findAndUpdate(RoleCommand roleCommand) {
        return roleRepository.findByNaturalId(roleCommand.getNaturalId())
                .map(role -> {
                    role.setDescription(roleCommand.getDescription());

                    roleRepository.save(role);
                    return roleDtoMapper.entityToDto(role);
                }).orElseGet((() -> makeRoleDtoByCommand(roleCommand)));
    }

    private RoleDto makeRoleDtoByCommand(RoleCommand roleCommand) {
        return RoleDto.builder()
                .withNaturalId(roleCommand.getNaturalId())
                .withDescription(roleCommand.getDescription())
                .withDescriptionUnified(TextUtil.unify(roleCommand.getDescription()))
                .build();
    }

    private RoleDto createNew(RoleCommand roleCommand) {
        var entity = Role.builder()
                .withNaturalId(UUID.randomUUID())
                .withDescription(roleCommand.getDescription())
                .withPlay(obtainPlay(roleCommand))
                .build();

        roleRepository.save(entity);

        return roleDtoMapper.entityToDto(entity);
    }

    private Play obtainPlay(RoleCommand roleCommand) {
        return playQueryService.getPlayByNaturalId(roleCommand.getPlayNaturalId())
                .orElseThrow(() -> new RuntimeException("Play Not Found"));
    }

    public void delete(@NonNull RoleCommand roleCommand) {
        roleRepository.deleteByNaturalId(roleCommand.getNaturalId());
        removeFromCache(roleCommand);
    }

    private void removeFromCache(RoleCommand roleCommand) {
        var  cacheList = roleCache.get(roleCommand.getPlayNaturalId());
        var roleDtoOpt = cacheList.stream()
                .filter(r -> Objects.equals(r.getNaturalId(), roleCommand.getNaturalId()))
                .findFirst();

        roleDtoOpt.ifPresent(cacheList::remove);
    }
}
