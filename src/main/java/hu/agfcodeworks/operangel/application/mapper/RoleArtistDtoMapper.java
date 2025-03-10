package hu.agfcodeworks.operangel.application.mapper;

import hu.agfcodeworks.operangel.application.dto.ArtistPerformanceDto;
import hu.agfcodeworks.operangel.application.model.ArtistPerformanceRoleJoin;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleArtistDtoMapper extends AbstractDtoMapper<ArtistPerformanceRoleJoin, ArtistPerformanceDto> {

    private final ArtistListDtoMapper artistListDtoMapper;

    private final RoleJoinDtoMapper roleJoinDtoMapper;

    @Override
    public ArtistPerformanceDto entityToDto(@NonNull ArtistPerformanceRoleJoin join) {
        var joinId = join.getId();

        return ArtistPerformanceDto.builder()
                .withPerformanceId(joinId.getPerformance().getNaturalId())
                .withArtistListDto(artistListDtoMapper.entityToDto(joinId.getArtist()))
                .withRoleSimpleDto(roleJoinDtoMapper.entityToDto(joinId.getRole()))
                .build();
    }
}
