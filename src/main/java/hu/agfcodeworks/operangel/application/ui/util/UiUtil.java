package hu.agfcodeworks.operangel.application.ui.util;

import hu.agfcodeworks.operangel.application.ui.dto.DbConnectionStatus;
import lombok.experimental.UtilityClass;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.EMPTY_STRING;

@UtilityClass
public class UiUtil {

    private final String DB_CONNECTION_STATUS_ICON_FILENAME_PATTERN = "icons/db-connection-%s.png";

    private static final String MENU_PREFIX = "Menu.";

    private static final String MENU_ITEM_PREFIX = "MenuItem.";
    private static final String CAPTION_PATTERN = "%s%s";
    private static final String TRIPLE_DOT = "...";

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
//                .filter(e -> e.getKey().toString().startsWith("Button"))
                .forEach(System.out::println);
    }

    private static Font makeFont(Font font) {
        return font.deriveFont(Font.PLAIN);
    }

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

    public Icon loadDbConnectionIconFromPresource(DbConnectionStatus dbConnectionStatus) {
        var fileName = DB_CONNECTION_STATUS_ICON_FILENAME_PATTERN.formatted(dbConnectionStatus.name().toLowerCase());

        try (var inputStream = UiUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            return new ImageIcon(inputStream.readAllBytes());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public Border getBorder(String key) {
        return UIManager.getDefaults()
                .getBorder(key);
    }
}
