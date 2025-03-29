package hu.agfcodeworks.operangel.application.ui.editor;

import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.function.BiConsumer;

import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.EMPTY_STRING;
import static hu.agfcodeworks.operangel.application.ui.constants.UiConstants.dateFormatter;

public class DateEditor extends DefaultCellEditor {

    public DateEditor(@NonNull BiConsumer<LocalDate, LocalDate> finishedEditingHandler) {
        super(new JTextField());

        this.delegate = new EditorDelegate() {

            @Override
            public Object getCellEditorValue() {
                var textField = (JTextField) editorComponent;
                var text = textField.getText();

                if (!StringUtils.hasText(text)) {
                    return null;
                }

                if (Objects.isNull(value)) {
                    var trimmedText = text.trim();
                    var localDate = parseLocalDate(trimmedText);

                    finishedEditingHandler.accept(null, localDate);
                    return localDate;
                }

                if (value instanceof LocalDate originalLocalDate) {
                    var trimmedText = text.trim();

                    if (!Objects.equals(dateFormatter.format(originalLocalDate), trimmedText)) {
                        var newLocalDate = parseLocalDate(trimmedText);

                        finishedEditingHandler.accept(originalLocalDate, newLocalDate);
                        return newLocalDate;
                    }
                }

                return value;
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

            private LocalDate parseLocalDate(String trimmedText) {
                try {
                    return LocalDate.parse(trimmedText, dateFormatter);
                } catch (DateTimeParseException ex) {
                    return (LocalDate) value;
                }
            }
        };
    }
}
