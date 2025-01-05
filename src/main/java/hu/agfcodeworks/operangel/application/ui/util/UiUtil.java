package hu.agfcodeworks.operangel.application.ui.util;

import lombok.experimental.UtilityClass;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Map;

@UtilityClass
public class UiUtil {

    private static final String MENU_PREFIX = "Menu.";

    private static final String MENU_ITEM_PREFIX = "MenuItem.";

    static {
        Map.of(
                "background", new Color(238, 238, 238, 60),
                "selectionBackground", new Color(163, 184, 204, 60),
                "acceleratorForeground", new Color(99, 130, 191, 60),
                "acceleratorSelectionForeground", new Color(99, 130, 191, 60),
                "font", makeFont((Font) UIManager.get("Menu.font"))
        ).forEach((k, v) -> {
            UIManager.put(MENU_PREFIX + k, v);
            UIManager.put(MENU_ITEM_PREFIX + k, v);
        });

        UIManager.getDefaults()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().toString().startsWith("Button"))
                .forEach(System.out::println);
    }

    private static Object makeFont(Font font) {
        return font.deriveFont(Font.PLAIN);
    }

    private static final String CAPTION_PATTERN = "%s%s";
    private static final String TRIPLE_DOT = "...";
    private static final String EMPTY_STRING = "";

    public JMenuItem makeMenuItem(String caption, ActionListener actionListener, boolean dialogOpening) {
        var menuItem = new JMenuItem(CAPTION_PATTERN.formatted(caption, dialogOpening ? TRIPLE_DOT : EMPTY_STRING));

        menuItem.addActionListener(actionListener);
        menuItem.setOpaque(false);

        return menuItem;
    }

    public JMenu makeMenu(String caption) {
        var menuItem = new JMenu(caption);
        menuItem.setOpaque(false);

        return menuItem;
    }
}
