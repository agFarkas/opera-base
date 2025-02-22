package hu.agfcodeworks.operangel.application.service.queryservice;

import hu.agfcodeworks.operangel.application.dto.PlayDetailedDto;
import hu.agfcodeworks.operangel.application.dto.PlayDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.mapper.PlayDetailedDtoMapper;
import hu.agfcodeworks.operangel.application.mapper.PlayDtoMapper;
import hu.agfcodeworks.operangel.application.mapper.RoleDtoMapper;
import hu.agfcodeworks.operangel.application.model.enums.PlayType;
import hu.agfcodeworks.operangel.application.repository.PlayRepository;
import hu.agfcodeworks.operangel.application.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
}
