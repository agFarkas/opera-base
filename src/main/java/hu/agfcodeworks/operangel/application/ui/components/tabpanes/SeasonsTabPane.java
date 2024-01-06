package hu.agfcodeworks.operangel.application.ui.components.tabpanes;

import org.springframework.stereotype.Component;

import java.awt.FlowLayout;

@Component
public class SeasonsTabPane extends AbstractTabPane {

    public SeasonsTabPane() {
        super(new FlowLayout());
    }
}
