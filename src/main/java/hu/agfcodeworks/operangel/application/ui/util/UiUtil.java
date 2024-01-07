package hu.agfcodeworks.operangel.application.ui.util;

import lombok.experimental.UtilityClass;

import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

@UtilityClass
public class UiUtil {

    private static final String CAPTION_PATTERN = "%s%s";
    private static final String TRIPLE_DOT = "...";
    private static final String EMPTY_STRING = "";

    public JMenuItem makeMenuItem(String caption, ActionListener actionListener, boolean dialogOpening) {
        var menuItem = new JMenuItem(CAPTION_PATTERN.formatted(caption, dialogOpening? TRIPLE_DOT : EMPTY_STRING));
        menuItem.addActionListener(actionListener);

        return menuItem;
    }

}
