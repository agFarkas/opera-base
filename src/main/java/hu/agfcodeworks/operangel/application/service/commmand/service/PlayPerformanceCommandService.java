package hu.agfcodeworks.operangel.application.service.commmand.service;

import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.command.PlayPerformanceChangeCommand;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
public class PlayPerformanceCommandService {

    public void save(@NonNull PlayPerformanceChangeCommand playPerformanceChangeCommand) {

    }
}
