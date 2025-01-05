package hu.agfcodeworks.operangel.application.ui.design;

import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Objects;

import static javax.swing.plaf.basic.BasicGraphicsUtils.drawString;

public class TabbedPaneUi extends BasicTabbedPaneUI {

    private static final String SCROLL_BAR_THUMB_SHADOW_KEY = "ScrollBar.thumbShadow";

    private final Color color1 = Color.WHITE;

    private final Color selectedBgColor = new Color(0, 0, 50, 10);

    private final Color selectedBorderColor;

    private final Color deSelectedBgColor = new Color(0, 0, 50, 55);

    private final Color deselectedBorderColor;

    private final Color disabledBgColor = new Color(0, 0, 50, 28);

    private final Color disabledBorderColor;

    private final Color disabledCaptionColor = new Color(100, 100, 100, 110);

    private final Color enabledCaptionColor = Color.BLACK;

    private final int inclTab = 1;

    private final int tabPadding = 20;

    private Polygon shape;

    public TabbedPaneUi() {
        var scrollBarThumbShadowColor = UIManager.getColor(SCROLL_BAR_THUMB_SHADOW_KEY);

        this.selectedBorderColor = new Color(
                scrollBarThumbShadowColor.getRed(),
                scrollBarThumbShadowColor.getGreen(),
                scrollBarThumbShadowColor.getBlue()
        );

        this.deselectedBorderColor = new Color(
                scrollBarThumbShadowColor.getRed(),
                scrollBarThumbShadowColor.getGreen(),
                scrollBarThumbShadowColor.getBlue(),
                70
        );

        this.disabledBorderColor = new Color(
                scrollBarThumbShadowColor.getRed(),
                scrollBarThumbShadowColor.getGreen(),
                scrollBarThumbShadowColor.getBlue(),
                40
        );
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        tabAreaInsets.right = 18;
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        if (runCount > 1) {
            var lines = new int[runCount];

            for (var i = 0; i < runCount; i++) {
                lines[i] = rects[tabRuns[i]].y + (tabPlacement == TOP ? maxTabHeight : 0);
            }

            Arrays.sort(lines);
            var fila = runCount;

            for (var i = 0; i < lines.length - 1; i++, fila--) {
                var carp = new Polygon();
                var actLine = lines[i];

                carp.addPoint(0, actLine);

                carp.addPoint(tabPane.getWidth() - 2 * fila - 2, actLine);
                carp.addPoint(tabPane.getWidth() - 2 * fila, actLine + 3);

                if (i < lines.length - 2) {
                    var nextLine = lines[i + 1];

                    carp.addPoint(tabPane.getWidth() - 2 * fila, nextLine);
                    carp.addPoint(0, nextLine);
                } else {
                    carp.addPoint(tabPane.getWidth() - 2 * fila, actLine + rects[selectedIndex].height);
                    carp.addPoint(0, actLine + rects[selectedIndex].height);
                }

                carp.addPoint(0, actLine);

                g.fillPolygon(carp);
                g.setColor(darkShadow.darker());
                g.drawPolygon(carp);
            }
        }

        super.paintTabArea(g, tabPlacement, selectedIndex);
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        var g2D = (Graphics2D) g;

        var xp = new int[]{x, x, x + 3, x + w - inclTab - 6, x + w - inclTab - 2, x + w - inclTab, x + w - inclTab, x};
        var yp = new int[]{y + h, y + 3, y, y, y + 1, y + 3, y + h, y + h};

        shape = new Polygon(xp, yp, xp.length);

        var gradient = calculateTabGradient(y, h, tabIndex, isSelected);

        g2D.setPaint(gradient);
        g2D.fill(shape);
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
        g.setFont(font.deriveFont(isSelected ? Font.BOLD : Font.PLAIN));
        var v = getTextViewForTab(tabIndex);

        if (Objects.nonNull(v)) {
            v.paint(g, textRect);
        } else {
            var mnemonicIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);

            g.setColor(calculateTabCaptionColor(tabIndex));
            drawString(g, title, mnemonicIndex, textRect.x, textRect.y + metrics.getAscent());
        }
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        return tabPadding + inclTab + super.calculateTabWidth(tabPlacement, tabIndex, metrics);
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        if (tabPlacement == LEFT || tabPlacement == RIGHT) {
            return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
        }

        int anchoFocoH = 4;
        return anchoFocoH + super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        drawBorder(g, calculateBorderColor(tabIndex));
    }

    private Color calculateBorderColor(int tabIndex) {
        if (isEnabled(tabIndex)) {
            return deselectedBorderColor;
        }

        return disabledBorderColor;
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        if (tabPane.hasFocus() && isSelected) {
            drawBorder(g, selectedBorderColor);
        }
    }

    private GradientPaint calculateTabGradient(int y, int h, int tabIndex, boolean isSelected) {
        if (!isEnabled(tabIndex)) {
            return new GradientPaint(0, 0, color1, 0, y + h / 2, disabledBgColor);
        }

        if (isSelected) {
            return new GradientPaint(0, 0, color1, 0, y + h / 2, selectedBgColor);
        }

        return new GradientPaint(0, 0, color1, 0, y + h / 2, deSelectedBgColor);
    }

    private Color calculateTabCaptionColor(int tabIndex) {
        if (isEnabled(tabIndex)) {
            return enabledCaptionColor;
        }

        return disabledCaptionColor;
    }

    private boolean isEnabled(int tabIndex) {
        return tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex);
    }

    private void drawBorder(Graphics g, Color borderColor) {
        g.setColor(borderColor);
        g.drawPolygon(shape);
    }

}
