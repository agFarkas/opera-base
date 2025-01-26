package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.swing.JTextField;
import java.util.Objects;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;

public class JLabeledNumberField extends JAbstractedLabeledTextField<JTextField> {

    public JLabeledNumberField(@NonNull String labelText, int columns) {
        super(labelText, new JTextField(EMPTY_TEXT, columns));

        setRegex("[0-9]{1,}");
    }

    public Integer getNumber() {
        var text = getText();

        if (!StringUtils.hasText(text)) {
            return null;
        }

        return Integer.parseInt(text);
    }

    public void setNumber(Integer number) {
        if (Objects.isNull(number)) {
            setText(EMPTY_TEXT);
        } else {
            setText(Integer.toString(number));
        }
    }
}
