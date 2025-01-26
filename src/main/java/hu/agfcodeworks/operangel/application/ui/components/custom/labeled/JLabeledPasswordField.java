package hu.agfcodeworks.operangel.application.ui.components.custom.labeled;

import lombok.NonNull;

import javax.swing.JPasswordField;

import static hu.agfcodeworks.operangel.application.constants.StringConstants.EMPTY_TEXT;

public class JLabeledPasswordField extends JAbstractedLabeledTextField<JPasswordField> {

    public JLabeledPasswordField(@NonNull String labelText, int columns) {
        super(labelText, new JPasswordField(EMPTY_TEXT, columns));
    }
}
