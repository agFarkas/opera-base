package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.RoleArtistDto;
import hu.agfcodeworks.operangel.application.model.ArtistPerformanceRoleJoin;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleArtistDtoMapper extends AbstractDtoMapper<ArtistPerformanceRoleJoin, RoleArtistDto> {

    private final ArtistListDtoMapper artistListDtoMapper;

    private final RoleJoinDtoMapper roleJoinDtoMapper;

    @Override
    public RoleArtistDto entityToDto(@NonNull ArtistPerformanceRoleJoin join) {
        var joinId = join.getId();

        return RoleArtistDto.builder()
                .withPerformanceId(joinId.getPerformance().getNaturalId())
                .withArtistListDto(artistListDtoMapper.entityToDto(joinId.getArtist()))
                .withRoleJoinDto(roleJoinDtoMapper.entityToDto(joinId.getRole()))
                .build();
    }
}
