package hu.agfcodeworks.operangel.application.ui.components.tabpanes;

import org.springframework.stereotype.Component;

import java.awt.FlowLayout;

@Component
public class OperasTabPane extends AbstractTabPane {

    public OperasTabPane() {
        super(new FlowLayout());
    }
}
