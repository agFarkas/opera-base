package hu.agfcodeworks.operangel.application.service.query.service;

import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.mapper.RoleDtoMapper;
import hu.agfcodeworks.operangel.application.model.Role;
import hu.agfcodeworks.operangel.application.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleQueryService {

    private final RoleRepository roleRepository;

    private final RoleDtoMapper roleDtoMapper;

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAllRoles()
                .stream()
                .map(roleDtoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Optional<Role> findByNaturalId(UUID naturalId) {
        return roleRepository.findByNaturalId(naturalId);
    }

    public Set<Role> findBySimpleDtos(Set<UUID> naturalIds) {
        return roleRepository.findByNaturalIds(naturalIds);
    }
}
