package hu.agfcodeworks.operangel.application.util;

import hu.agfcodeworks.operangel.application.configuration.Config;
import hu.agfcodeworks.operangel.application.dto.StatusCategory;
import hu.agfcodeworks.operangel.application.dto.StatusDto;
import hu.agfcodeworks.operangel.application.event.ContextEvent;
import hu.agfcodeworks.operangel.application.event.listener.ContextEventListener;
import hu.agfcodeworks.operangel.application.event.values.ContextStatus;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContextException;
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
                .withCategory(StatusCategory.ERROR)
                .build();
    }

    public StatusDto startContext(@NonNull ContextEventListener contextEventListener) {
        try {
            contextEventListener.statusChanged(
                    ContextEvent.builder()
                            .withStatus(ContextStatus.AWAITING)
                            .build()
            );
            conditionallyInitContext();
            context.start();

            contextEventListener.statusChanged(
                    ContextEvent.builder()
                            .withStatus(ContextStatus.ESTABLISHED)
                            .build()
            );

            return StatusDto.builder()
                    .withCategory(StatusCategory.OK)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();

            contextEventListener.statusChanged(
                    ContextEvent.builder()
                            .withStatus(ContextStatus.REFUSED)
                            .build()
            );

            return StatusDto.builder()
                    .withCategory(StatusCategory.ERROR)
                    .build();
        }
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

    public <T> T getBean(Class<T> clazz) {
        if (isContextInactive()) {
            throw new ApplicationContextException("Context is inactive.");
        }

        return context.getBean(clazz);
    }

    private static boolean isContextInactive() {
        return Objects.isNull(context) || !context.isRunning() || context.isClosed() || !context.isActive();
    }
}
