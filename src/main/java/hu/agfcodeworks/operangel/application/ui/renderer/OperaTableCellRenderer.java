package hu.agfcodeworks.operangel.application.ui.renderer;

import hu.agfcodeworks.operangel.application.dto.ArtistListDto;
import hu.agfcodeworks.operangel.application.dto.ComposerDto;
import hu.agfcodeworks.operangel.application.dto.LocationDto;
import hu.agfcodeworks.operangel.application.dto.RoleDto;
import hu.agfcodeworks.operangel.application.ui.text.TextProviders;
import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.util.Objects;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.COLUMN_ROLE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_DATE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_LOCATION;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.FONT_STYLE_ROLE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_CONDUCTOR;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_DATE;
import static hu.agfcodeworks.operangel.application.ui.constants.OperaTableConstants.ROW_LOCATION;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.dateFormatter;

@AllArgsConstructor
public class OperaTableCellRenderer extends DefaultTableCellRenderer {

    private static final Color GENERAL_BASE_COLOR = Color.WHITE;

    @Setter
    private int lastConductorRow;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (row == lastConductorRow) {
            setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 1, 0, GENERAL_BASE_COLOR),
                    new MatteBorder(0, 0, 1, 0, Color.BLACK)
            ));
        }

        setText(obtainText(value));
        setFont(obtainFont(row, column));
        setBackground(calculateBackground(row));

        return this;
    }

    private Color calculateBackground(int row) {
        if (row > lastConductorRow) {
            return row % 2 == 1 ? GENERAL_BASE_COLOR : subtract(GENERAL_BASE_COLOR, 20);
        }

        return GENERAL_BASE_COLOR;
    }

    private static Color subtract(Color color, int valueToSubtract) {
        return new Color(color.getRed() - valueToSubtract, color.getGreen() - valueToSubtract, color.getBlue() - valueToSubtract);
    }

    private String obtainText(Object value) {
        if (Objects.isNull(value)) {
            return EMPTY_TEXT;
        }

        if (value instanceof LocalDate localDate) {
            return dateFormatter.format(localDate);
        }

        if (value instanceof LocationDto locationDto) {
            return TextProviders.locationTextProvider.apply(locationDto);
        }

        if (value instanceof RoleDto roleDto) {
            return TextProviders.roleTextProvider.apply(roleDto);
        }

        if (value instanceof ComposerDto composerDto) {
            return TextProviders.composerTextProvider.apply(composerDto);
        }

        if (value instanceof ArtistListDto artistListDto) {
            return TextProviders.artistTextProvider.apply(artistListDto);
        }

        return value.toString();
    }

    private Font obtainFont(int row, int column) {
        var font = getFont();

        if (isRoleColumn(column)) {
            if (row > lastConductorRow) {
                return font.deriveFont(FONT_STYLE_ROLE);
            }
        } else {
            if (isDateRow(row)) {
                return font.deriveFont(FONT_STYLE_DATE);
            }

            if (isLocationRow(row)) {
                return font.deriveFont(FONT_STYLE_LOCATION);
            }

            if (isConductorRow(row)) {
                return font.deriveFont(FONT_STYLE_CONDUCTOR);
            }
        }

        return getFont();
    }

    private static boolean isRoleColumn(int column) {
        return column == COLUMN_ROLE;
    }

    private static boolean isDateRow(int row) {
        return row == ROW_DATE;
    }

    private static boolean isLocationRow(int row) {
        return row == ROW_LOCATION;
    }

    private boolean isConductorRow(int row) {
        return row >= ROW_CONDUCTOR && row <= lastConductorRow;
    }
}
