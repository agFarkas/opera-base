package hu.agfcodeworks.operangel.application.ui.components.tabpanes;

import org.springframework.stereotype.Component;

import java.awt.FlowLayout;

@Component
public class CalendarTabPane extends AbstractTabPane {

    public CalendarTabPane() {
        super(new FlowLayout());

    }
}
