package hu.agfcodeworks.operangel.application.ui.util;

import hu.agfcodeworks.operangel.application.configuration.Config;
import hu.agfcodeworks.operangel.application.dto.StatusCategory;
import hu.agfcodeworks.operangel.application.dto.StatusDto;
import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Objects;

@UtilityClass
public class ContextUtil {

    private AnnotationConfigApplicationContext context;

    private void initContext() {
        context = new AnnotationConfigApplicationContext(Config.class);
    }

    public StatusDto startContext() {
        conditionallyInitContext();
        context.start();

        return StatusDto.builder()
                .withCategory(StatusCategory.OK)
                .build();
    }

    public StatusDto stopContext() {
        conditionallyInitContext();
        context.stop();

        return StatusDto.builder()
                .withCategory(StatusCategory.OK)
                .build();
    }

    public StatusDto restartContext() {
        conditionallyInitContext();

        context.stop();
        context.start();

        return StatusDto.builder()
                .withCategory(StatusCategory.OK)
                .build();
    }

    private void conditionallyInitContext() {
        if (Objects.isNull(context)) {
            initContext();
        }
    }
}
