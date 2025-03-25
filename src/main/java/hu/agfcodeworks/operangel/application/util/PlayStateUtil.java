package hu.agfcodeworks.operangel.application.util;

import hu.agfcodeworks.operangel.application.dto.state.PlayStateDto;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;

@UtilityClass
public class PlayStateUtil {

    public PlayStateDto clone(PlayStateDto playState) {
        return PlayStateDto.builder()
                .withRoles(new LinkedList<>(playState.getRoles()))
                .withPerformanceStateDtos(new LinkedList<>(playState.getPerformanceStateDtos()))
                .build();
    }
}
