package hu.agfcodeworks.operangel.application.util;

import hu.agfcodeworks.operangel.application.service.DbSettingsService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OutContextUtilTest {
    @Test
    void getComponentTest() {
        var component = OutContextUtil.getComponent(DbSettingsService.class);

        assertThat(component)
                .isInstanceOf(DbSettingsService.class);
    }
}
