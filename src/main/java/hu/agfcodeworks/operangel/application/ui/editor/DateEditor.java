package hu.agfcodeworks.operangel.application.ui.editor;

import hu.agfcodeworks.operangel.application.ui.editor.listener.CellEditingListener;
import org.springframework.util.StringUtils;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.EMPTY_STRING;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.dateFormatter;

public class DateEditor extends DefaultCellEditor {

    private final int row;
    private final int column;
    private final CellEditingListener editingListener;

    public DateEditor(int row, int column, CellEditingListener editingListener) {
        super(new JTextField());

        this.row = row;
        this.column = column;
        this.editingListener = editingListener;

        this.delegate = new EditorDelegate() {

            @Override
            public Object getCellEditorValue() {
                var textField = (JTextField) editorComponent;
                var text = textField.getText();

                if (!StringUtils.hasText(text)) {
                    if (Objects.nonNull(value)) {
                        return value;
                    }

                    return null;
                }

                if (Objects.isNull(value)) {
                    return parseText(text);
                }

                if (value instanceof LocalDate localDate) {
                    var trimmedText = text.trim();

                    if (!Objects.equals(dateFormatter.format(localDate), trimmedText)) {
                        return parseLocalDate(trimmedText);
                    }
                }

                return value;
            }

            private LocalDate parseLocalDate(String trimmedText) {
                try {
                    return LocalDate.parse(trimmedText, dateFormatter);
                } catch (DateTimeParseException ex) {
                    return (LocalDate) value;
                }
            }


            @Override
            public void setValue(Object value) {
                super.setValue(value);

                var textField = (JTextField) editorComponent;

                if (Objects.isNull(value)) {
                    textField.setText(EMPTY_STRING);
                } else if (value instanceof LocalDate localDate) {
                    textField.setText(dateFormatter.format(localDate));
                }
            }
        };
    }

    private static LocalDate parseText(String text) {
        try {
            return LocalDate.parse(text.trim(), dateFormatter);
        } catch (Exception ex) {
            return null;
        }
    }
}
